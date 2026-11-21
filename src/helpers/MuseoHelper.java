package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import messages.EMsgCodes;
import messages.ResponseWrapper;
import zeraf.ESistema;
import zeraf.Zeraf;
import zeraf.ZerafFactory;

/**
 * Envía los hallazgos con Terra y conserva los envíos que aún no se han
 * aceptado.
 */
public final class MuseoHelper {
    /**
     * Ruta exigida por ZerafFactory en Terra 1.0.0; URL y Group se leen del
     * archivo.
     */
    private static final Path CONFIG_FILE = Path.of("config", "Zeraf.conf");
    /** Conversor JSON utilizado para los respaldos locales del Museo. */
    private static final Gson JSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    /**
     * Configuración del grupo que realiza el envío.
     *
     * @param group Identificador del grupo.
     */
    private record Configuration(String group) {
    }

    /**
     * Lote de códigos pendiente de aceptación por el Museo.
     *
     * @param group Grupo del envío; null si todavía no había configuración.
     * @param codes Códigos completos, conservando el orden y las repeticiones.
     */
    private record PendingSend(String group, List<String> codes) {
    }

    /**
     * Contenido del respaldo local de envíos pendientes.
     *
     * @param uid     Identificador de la partida.
     * @param batches Lotes que todavía no han sido aceptados.
     */
    private record PendingData(String uid, List<PendingSend> batches) {
    }

    /** Impide crear instancias de esta clase de utilidades estáticas. */
    private MuseoHelper() {
    }

    /**
     * Conserva los códigos completos antes de intentar el envío. Los pendientes del
     * mismo UID y grupo se incluyen en el siguiente intento, respetando
     * repeticiones.
     * Este método no recibe estadísticas ni cambia el archivo de la partida.
     *
     * @param uid              Identificador de la partida activa.
     * @param codes            Códigos completos de esta excavación, sin recortar ni
     *                         deduplicar.
     * @param pendingDirectory Carpeta independiente del sistema de guardado.
     */
    public static void send(String uid, List<String> codes, Path pendingDirectory) {
        Path pendingFile = null;
        boolean stored = false;
        try {
            if (uid == null || uid.isBlank()) {
                throw new IOException("Falta el UID para enviar los hallazgos.");
            }
            List<String> currentCodes = List.copyOf(codes);
            if (currentCodes.isEmpty()) {
                System.out.println("Museo: no hay tesoros nuevos que enviar.");
                return;
            }
            pendingFile = pendingDirectory.resolve(MuseoHelper.fileKey(uid) + ".json");
            List<PendingSend> pending = MuseoHelper.readPending(pendingFile, uid);

            Configuration configuration = null;
            Exception preparationError = null;
            Path terraBackup = null;
            boolean previousTerraBackup = false;
            try {
                configuration = MuseoHelper.readConfiguration();
                terraBackup = MuseoHelper.terraBackupPath(uid, configuration.group());
                previousTerraBackup = Files.exists(terraBackup);
                if (previousTerraBackup) {
                    List<String> previousCodes = MuseoHelper.readTerraBackup(terraBackup, uid, configuration.group());
                    if (!MuseoHelper.containsBackup(pending, configuration.group(), previousCodes)) {
                        pending.add(new PendingSend(configuration.group(), previousCodes));
                    }
                }
            } catch (IOException | RuntimeException exception) {
                preparationError = exception;
            }

            String group = configuration == null ? null : configuration.group();
            pending.add(new PendingSend(group, currentCodes));
            // Un envío creado sin configuración queda ligado al grupo en su primer intento.
            if (preparationError == null) {
                for (int i = 0; i < pending.size(); i++) {
                    PendingSend batch = pending.get(i);
                    if (batch.group() == null) {
                        pending.set(i, new PendingSend(group, batch.codes()));
                    }
                }
            }
            MuseoHelper.writePending(pendingFile, new PendingData(uid, pending));
            stored = true;
            if (preparationError != null) {
                throw new IOException("No se pudo preparar el envío: " + preparationError.getMessage(),
                        preparationError);
            }
            if (previousTerraBackup) {
                // Ya está conservado en la cola. Evita que Terra lo reenvíe por su cuenta.
                Files.delete(terraBackup);
            }

            List<String> allCodes = new ArrayList<>();
            for (PendingSend batch : pending) {
                if (group.equals(batch.group())) {
                    allCodes.addAll(batch.codes());
                }
            }
            // La factoría lee URL y Group del archivo. El protocolo lo implementa Terra.
            Zeraf zeraf = ZerafFactory.createZeraf(ESistema.MUSEO, uid, group);
            ResponseWrapper<String> response = zeraf.sendData(allCodes);
            if (response == null || response.getCode() == null) {
                throw new IOException("El Museo no devolvió una respuesta válida.");
            }
            System.out.println("Museo: " + response.getCode().getMessage());
            if (response.getContent() != null && !response.getContent().isBlank()) {
                System.out.println(response.getContent());
            }
            if (response.getCode() != EMsgCodes.CORRECT) {
                MuseoHelper.logStatus(response.getCode());
            }

            if (Files.exists(terraBackup)) {
                List<String> backedUpCodes = MuseoHelper.readTerraBackup(terraBackup, uid, group);
                if (!backedUpCodes.equals(allCodes)) {
                    throw new IOException("El respaldo de Terra no coincide con el envío; se conserva para revisarlo.");
                }
                // La cola ya contiene estos mismos códigos y no pierde los intentos anteriores.
                Files.delete(terraBackup);
            }
            if (response.getCode() == EMsgCodes.CORRECT) {
                pending.removeIf(batch -> group.equals(batch.group()));
                MuseoHelper.writePending(pendingFile, new PendingData(uid, pending));
            } else {
                System.out.println("Los códigos completos siguen pendientes en " + pendingFile + ".");
            }
        } catch (IOException | RuntimeException exception) {
            System.err.println("Museo: " + exception.getMessage());
            if (stored) {
                System.out.println("Los códigos completos se conservan en " + pendingFile + ".");
            } else {
                System.err.println(
                        "No se pudo actualizar el respaldo del Museo. Los hallazgos siguen en la partida guardada.");
            }
            try {
                LogHelper.logError(exception);
            } catch (IOException | RuntimeException logException) {
                System.err.println("No se pudo registrar el error del Museo: " + logException.getMessage());
            }
        }
    }

    /**
     * Registra un estado de error devuelto por Terra.
     * Si falla el registro, muestra el problema sin interrumpir el envío.
     *
     * @param code Estado de error devuelto por el Museo.
     */
    private static void logStatus(EMsgCodes code) {
        IOException status = new IOException("Estado devuelto por Museo [" + code.name()
                + "]: " + code.getMessage());
        try {
            LogHelper.logError(status);
        } catch (IOException | RuntimeException exception) {
            System.err.println("No se pudo registrar el resultado del Museo: " + exception.getMessage());
        }
    }

    /**
     * Lee el grupo y comprueba la URL antes de utilizar la factoría de Terra.
     *
     * @return Configuración con el identificador del grupo.
     * @throws IOException si no se puede leer el archivo o la URL no es
     *                     una dirección HTTP o HTTPS completa y válida.
     */
    private static Configuration readConfiguration() throws IOException {
        JsonObject object = JsonParser.parseString(Files.readString(MuseoHelper.CONFIG_FILE, StandardCharsets.UTF_8))
                .getAsJsonObject();
        String url = MuseoHelper.requiredText(object.get("URL"));
        String group = MuseoHelper.requiredText(object.get("Group"));
        URI destination;
        try {
            destination = URI.create(url);
        } catch (IllegalArgumentException exception) {
            throw new IOException("La URL de " + MuseoHelper.CONFIG_FILE + " no es válida.", exception);
        }
        if (!("http".equalsIgnoreCase(destination.getScheme()) || "https".equalsIgnoreCase(destination.getScheme()))
                || destination.getHost() == null) {
            throw new IOException(
                    "La URL de " + MuseoHelper.CONFIG_FILE + " debe ser una dirección HTTP o HTTPS completa.");
        }

        return new Configuration(group);
    }

    /**
     * Lee los lotes del respaldo local y comprueba que pertenecen a la partida.
     * Este archivo utiliza un formato interno, independiente del mensaje del Museo.
     *
     * @param file Ruta del respaldo local.
     * @param uid  Identificador esperado de la partida.
     * @return Lista modificable de lotes; vacía si el archivo no existe.
     * @throws IOException si no se puede leer el respaldo, su formato no es válido
     *                     o el identificador no coincide.
     */
    private static List<PendingSend> readPending(Path file, String uid) throws IOException {
        if (!Files.exists(file))
            return new ArrayList<>();
        try {
            JsonObject object = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8))
                    .getAsJsonObject();
            if (!uid.equals(MuseoHelper.requiredText(object.get("uid")))) {
                throw new IOException("El UID del respaldo del Museo no coincide.");
            }
            List<PendingSend> batches = new ArrayList<>();
            for (JsonElement element : object.getAsJsonArray("batches")) {
                JsonObject batch = element.getAsJsonObject();
                JsonElement group = batch.get("group");
                batches.add(
                        new PendingSend(group == null || group.isJsonNull() ? null : MuseoHelper.requiredText(group),
                                MuseoHelper.readCodes(batch.getAsJsonArray("codes"))));
            }
            return batches;
        } catch (RuntimeException exception) {
            throw new IOException("El respaldo del Museo no es válido y se conserva sin sobrescribir.", exception);
        }
    }

    /**
     * Escribe el respaldo local mediante un archivo temporal.
     * Utiliza una sustitución atómica cuando el sistema de archivos la admite.
     *
     * @param file Ruta del respaldo que se va a actualizar.
     * @param data Datos pendientes que se van a conservar.
     * @throws IOException si falla la creación, escritura o sustitución del
     *                     archivo.
     */
    private static void writePending(Path file, PendingData data) throws IOException {
        Files.createDirectories(file.getParent());
        Path temporary = Files.createTempFile(file.getParent(), "museo-", ".tmp");
        try {
            Files.writeString(temporary, MuseoHelper.JSON.toJson(data), StandardCharsets.UTF_8);
            try {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /**
     * Lee el respaldo Base64 generado por Terra y comprueba su UID y grupo.
     *
     * @param file  Ruta del respaldo de Terra.
     * @param uid   Identificador esperado de la partida.
     * @param group Identificador esperado del grupo.
     * @return Códigos completos recuperados del respaldo.
     * @throws IOException si no se puede leer o interpretar el respaldo,
     *                     o pertenece a otra partida o grupo.
     */
    private static List<String> readTerraBackup(Path file, String uid, String group) throws IOException {
        try {
            byte[] decoded = Base64.getDecoder().decode(Files.readString(file, StandardCharsets.UTF_8));
            JsonObject message = JsonParser.parseString(new String(decoded, StandardCharsets.UTF_8)).getAsJsonObject();
            if (!uid.equals(MuseoHelper.requiredText(message.get("uid")))
                    || !group.equals(MuseoHelper.requiredText(message.get("group")))) {
                throw new IOException("El respaldo de Terra pertenece a otro usuario o grupo.");
            }
            return MuseoHelper.readCodes(message.getAsJsonArray("data"));
        } catch (RuntimeException exception) {
            throw new IOException("No se pudo leer el respaldo de Terra; se conserva sin sobrescribir.", exception);
        }
    }

    /**
     * Comprueba si los códigos coinciden con un lote del grupo o con la
     * concatenación de todos sus lotes, respetando el orden y las repeticiones.
     *
     * @param pending Lotes pendientes de envío.
     * @param group   Grupo cuyos lotes se comparan.
     * @param codes   Códigos recuperados del respaldo de Terra.
     * @return true si existe alguna de esas coincidencias; false en otro caso.
     */
    private static boolean containsBackup(List<PendingSend> pending, String group, List<String> codes) {
        List<String> combined = new ArrayList<>();
        for (PendingSend batch : pending) {
            if (group.equals(batch.group())) {
                if (batch.codes().equals(codes))
                    return true;
                combined.addAll(batch.codes());
            }
        }
        return combined.equals(codes);
    }

    /**
     * Extrae los códigos de un array JSON sin modificar su orden ni sus
     * repeticiones.
     *
     * @param array Array JSON que contiene los códigos.
     * @return Lista no modificable con los códigos completos.
     * @throws IllegalArgumentException si algún elemento no es un texto válido
     *                                  o está vacío o formado únicamente por
     *                                  espacios.
     */
    private static List<String> readCodes(JsonArray array) {
        List<String> codes = new ArrayList<>();
        for (JsonElement element : array)
            codes.add(MuseoHelper.requiredText(element));
        return List.copyOf(codes);
    }

    /**
     * Obtiene un texto obligatorio de un elemento JSON sin modificar su contenido.
     *
     * @param value Elemento JSON que se va a comprobar.
     * @return Texto original del elemento.
     * @throws IllegalArgumentException si el elemento es nulo, no es una cadena
     *                                  o está vacío o formado únicamente por
     *                                  espacios.
     */
    private static String requiredText(JsonElement value) {
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()
                || value.getAsString().isBlank()) {
            throw new IllegalArgumentException("Falta un texto obligatorio o su formato es incorrecto.");
        }
        return value.getAsString();
    }

    /**
     * Construye la ruta del respaldo de Terra utilizando el grupo y el UID.
     *
     * @param uid   Identificador de la partida.
     * @param group Identificador del grupo.
     * @return Ruta del archivo de respaldo dentro de la carpeta backup.
     * @throws IOException si los identificadores contienen caracteres rechazados
     *                     para el nombre del archivo.
     */
    private static Path terraBackupPath(String uid, String group) throws IOException {
        String name = group + "_" + uid + ".bkp";
        if (name.chars().anyMatch(character -> character < 32 || "<>:\"/\\|?*".indexOf(character) >= 0)) {
            throw new IOException("UID o Group contienen caracteres incompatibles con el respaldo de Terra.");
        }
        return Path.of("backup", name);
    }

    /**
     * Genera una clave SHA-256 para identificar el respaldo local de una partida.
     *
     * @param uid Identificador de la partida.
     * @return Resumen SHA-256 expresado en hexadecimal.
     * @throws IllegalStateException si el algoritmo SHA-256 no está disponible.
     */
    private static String fileKey(String uid) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(uid.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("No está disponible SHA-256.", exception);
        }
    }
}
