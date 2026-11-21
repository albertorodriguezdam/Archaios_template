package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import commons.Stats;
import commons.repositories.RegionRepository;
import commons.repositories.SkillRepository;
import commons.repositories.TreasureRepository;
import excavation.ETerrain;
import excavation.Excavation;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import people.Archeologist;
import regions.IDifficultRegion;
import skills.Skill;
import treasures.Treasure;

/**
 * Gestiona la lectura, validación y escritura de partidas en archivos JSON.
 */
public final class SaveManager {
    /**
     * Directorio donde se conservan las partidas guardadas.
     */
    private static final Path SAVE_DIRECTORY = Path.of("saves");
    /**
     * Extensión de los archivos de partida.
     */
    private static final String EXTENSION = ".save";
    /**
     * Conversor JSON estricto utilizado para leer y escribir las partidas.
     */
    private static final Gson GSON = new GsonBuilder().setStrictness(Strictness.STRICT)
            .setPrettyPrinting().create();
    /**
     * Identificador utilizado en el guardado para el terreno cuadrado pequeño.
     */
    private static final String SAVED_SMALL_SQUARE = "S_SQUARE";
    /**
     * Nombre utilizado en el guardado para la habilidad de desierto.
     */
    private static final String SAVED_DESERT_SKILL = "Especialista en desierto";
    /**
     * Nombre de la habilidad de desierto en el catálogo del sistema.
     */
    private static final String DESERT_SKILL = "Especialización en desierto";

    /**
     * Impide crear instancias de esta clase de utilidades estáticas.
     */
    private SaveManager() {
    }

    /**
     * Agrupa el estado de una partida para intercambiarlo entre el gestor de
     * guardado y la aplicación.
     *
     * @param uid           Identificador del usuario.
     * @param name          Nombre de la partida.
     * @param archeologists Arqueólogos de la partida.
     * @param stats         Estadísticas de visitas y hallazgos.
     * @param points        Saldo de puntos.
     */
    public record SavedGame(String uid, String name, List<Archeologist> archeologists,
            Stats stats, int points) {
    }

    /**
     * Obtiene los archivos de partida disponibles, ordenados por nombre.
     *
     * @return Lista de archivos regulares de partida; vacía si el directorio no
     *         existe.
     * @throws IOException si no se puede abrir o recorrer el directorio de
     *                     partidas.
     */
    public static List<Path> listSaves() throws IOException {
        List<Path> saves = new ArrayList<>();
        if (Files.notExists(SaveManager.SAVE_DIRECTORY))
            return saves;

        try (DirectoryStream<Path> files = Files.newDirectoryStream(SaveManager.SAVE_DIRECTORY)) {
            for (Path file : files) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (name.endsWith(SaveManager.EXTENSION)
                        && Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
                    saves.add(file);
                }
            }
        }
        saves.sort(Comparator.comparing(Path::getFileName));
        return saves;
    }

    /**
     * Comprueba que el nombre de partida pueda utilizarse como nombre de archivo.
     *
     * @param name Nombre que se quiere comprobar.
     * @return {@code true} si supera las comprobaciones de nombre; {@code false} en
     *         otro caso.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank() || name.equals(".") || name.equals(".."))
            return false;
        for (char character : name.toCharArray()) {
            if (Character.isISOControl(character) || "\\/:*?\"<>|".indexOf(character) >= 0)
                return false;
        }
        String base = name.split("\\.", 2)[0].stripTrailing().toUpperCase(Locale.ROOT);
        return !base.matches("CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9]");
    }

    /**
     * Construye la ruta de guardado a partir del nombre de la partida.
     *
     * @param name Nombre válido de la partida.
     * @return Ruta del archivo dentro del directorio de partidas.
     * @throws IllegalArgumentException si el nombre no es válido.
     */
    public static Path pathFor(String name) {
        if (!SaveManager.isValidName(name)) {
            throw new IllegalArgumentException("El nombre de partida no es válido.");
        }
        return SaveManager.SAVE_DIRECTORY.resolve(name + SaveManager.EXTENSION);
    }

    /**
     * Lee una partida en UTF-8, valida su contenido y reconstruye su estado.
     *
     * @param file Archivo de partida que se va a leer.
     * @return Estado de la partida validado y reconstruido.
     * @throws IOException si falla la lectura o los datos no representan una
     *                     partida válida para el programa.
     */
    public static SavedGame read(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonElement document = SaveManager.GSON.fromJson(reader, JsonElement.class);
            return SaveManager.decode(document);
        } catch (JsonParseException | IllegalArgumentException exception) {
            throw new IOException("El archivo no contiene una partida compatible. "
                    + exception.getMessage(), exception);
        }
    }

    /**
     * Valida y escribe una partida completa mediante un archivo temporal. Las
     * actualizaciones requieren sustitución atómica.
     *
     * @param file    Ruta del archivo de destino.
     * @param game    Estado de la partida que se quiere guardar.
     * @param newGame Indica si se crea un archivo nuevo sin sobrescribir uno
     *                existente.
     * @throws IOException si los datos no superan la validación o falla la
     *                     preparación, escritura, sustitución o limpieza del
     *                     archivo.
     */
    public static void write(Path file, SavedGame game, boolean newGame) throws IOException {
        JsonObject document = SaveManager.encode(game);
        try {
            SaveManager.decode(document);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Los datos de la partida no se pueden guardar. "
                    + exception.getMessage(), exception);
        }

        Path directory = file.getParent();
        try {
            Files.createDirectories(directory);
        } catch (IOException exception) {
            throw new IOException("No se pudo preparar la carpeta " + directory + ".", exception);
        }

        Path temporary = Files.createTempFile(directory, "partida-", ".tmp");
        try {
            Files.writeString(temporary, SaveManager.GSON.toJson(document), StandardCharsets.UTF_8);
            if (newGame) {
                Files.move(temporary, file);
            } else {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (AtomicMoveNotSupportedException exception) {
            throw new IOException("No se puede sustituir el archivo de forma segura. Se conserva el guardado anterior.",
                    exception);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /**
     * Construye el documento JSON correspondiente al estado de una partida.
     *
     * @param game Estado de la partida que se transforma.
     * @return Objeto JSON con los campos del formato de guardado.
     */
    private static JsonObject encode(SavedGame game) {
        JsonObject result = new JsonObject();
        result.addProperty("UID", game.uid());
        result.addProperty("Partida", game.name());
        JsonArray people = new JsonArray();
        for (Archeologist person : game.archeologists()) {
            JsonObject data = new JsonObject();
            data.addProperty("Nombre", person.getName());
            data.addProperty("Nv", person.getLevel());
            data.addProperty("Exp", person.getXp());
            JsonArray terrains = new JsonArray();
            for (ETerrain terrain : Excavation.getAvailableTerrains(person)) {
                terrains.add(terrain == ETerrain.SMALL_SQUARE ? SaveManager.SAVED_SMALL_SQUARE : terrain.name());
            }
            data.add("Terrenos", terrains);
            JsonArray skills = new JsonArray();
            for (Skill skill : person.getSkills()) {
                String name = skill.getName();
                skills.add(SaveManager.DESERT_SKILL.equals(name) ? SaveManager.SAVED_DESERT_SKILL : name);
            }
            data.add("Habilidades", skills);
            data.addProperty("Especialización", person.getSpecialization());
            data.add("Licencias", SaveManager.GSON.toJsonTree(person.getLicenses()));
            people.add(data);
        }
        result.add("Arqueólogos", people);
        JsonObject statistics = new JsonObject();
        statistics.add("Tesoros", SaveManager.GSON.toJsonTree(new TreeMap<>(game.stats().getTreasureCounts())));
        statistics.add("Lugares", SaveManager.GSON.toJsonTree(new TreeMap<>(game.stats().getVisitCounts())));
        result.add("Estadísticas", statistics);
        result.addProperty("Puntos", game.points());
        return result;
    }

    /**
     * Valida el documento JSON y reconstruye los datos de una partida.
     *
     * @param document Documento JSON leído o generado para la partida.
     * @return Estado de la partida reconstruido.
     * @throws IllegalArgumentException si los campos, sus tipos o su contenido no
     *                                  representan una partida válida.
     */
    private static SavedGame decode(JsonElement document) {
        JsonObject data = SaveManager.objectWithFields(document, "la partida", "UID", "Partida",
                "Arqueólogos", "Estadísticas", "Puntos");
        String uid = SaveManager.readRequiredText(data.get("UID"), "UID");
        String name = SaveManager.readText(data.get("Partida"), "Partida");
        if (!SaveManager.isValidName(name)) {
            throw new IllegalArgumentException("El nombre de partida no es válido.");
        }
        List<Archeologist> people = new ArrayList<>();
        for (JsonElement value : SaveManager.readArray(data.get("Arqueólogos"), "Arqueólogos")) {
            people.add(SaveManager.readArcheologist(value));
        }
        if (people.isEmpty())
            throw new IllegalArgumentException("La partida debe tener un arqueólogo.");

        Stats stats = SaveManager.readStats(data.get("Estadísticas"));
        int points = SaveManager.readInt(data.get("Puntos"), "Puntos");
        return new SavedGame(uid, name, people, stats, points);
    }

    /**
     * Reconstruye un arqueólogo y valida su progresión, habilidades, datos
     * regionales y terrenos.
     *
     * @param value Objeto JSON del arqueólogo.
     * @return Arqueólogo reconstruido con las habilidades del catálogo.
     * @throws IllegalArgumentException si los datos del arqueólogo son inválidos o
     *                                  incoherentes.
     */
    private static Archeologist readArcheologist(JsonElement value) {
        JsonObject data = SaveManager.objectWithFields(value, "un arqueólogo", "Nombre", "Nv", "Exp",
                "Terrenos", "Habilidades", "Especialización", "Licencias");
        String name = SaveManager.readRequiredText(data.get("Nombre"), "Nombre");
        int level = SaveManager.readInt(data.get("Nv"), "Nv");
        int xp = SaveManager.readInt(data.get("Exp"), "Exp");
        Archeologist person = new Archeologist(name, level, xp);

        List<Skill> skills = new ArrayList<>();
        for (String skillName : SaveManager.readTexts(data.get("Habilidades"), "Habilidades")) {
            String catalogName = SaveManager.SAVED_DESERT_SKILL.equals(skillName) ? SaveManager.DESERT_SKILL
                    : skillName;
            Skill skill = SkillRepository.get(catalogName);
            if (skill == null)
                throw new IllegalArgumentException("Habilidad desconocida. " + skillName);
            skills.add(skill);
        }
        person.restoreSkills(skills);

        List<String> licenses = SaveManager.readTexts(data.get("Licencias"), "Licencias");
        for (String code : licenses) {
            if (!(RegionRepository.getRegion(code) instanceof IDifficultRegion)) {
                throw new IllegalArgumentException("Licencia regional no válida. " + code);
            }
        }
        String specialization = SaveManager.readText(data.get("Especialización"), "Especialización");
        if (!specialization.isEmpty() && RegionRepository.getRegion(specialization) == null) {
            throw new IllegalArgumentException("Región de especialización desconocida. " + specialization);
        }
        person.restoreRegionalData(licenses, specialization);
        SaveManager.checkTerrains(data.get("Terrenos"), person);
        return person;
    }

    /**
     * Comprueba que los terrenos guardados coinciden con los permitidos por el
     * nivel y las habilidades del arqueólogo.
     *
     * @param value  Lista JSON de identificadores de terreno.
     * @param person Arqueólogo reconstruido cuyos permisos se comprueban.
     * @throws IllegalArgumentException si hay identificadores desconocidos,
     *                                  terrenos repetidos o permisos que no
     *                                  coinciden.
     */
    private static void checkTerrains(JsonElement value, Archeologist person) {
        Set<ETerrain> terrains = new HashSet<>();
        for (String name : SaveManager.readTexts(value, "Terrenos")) {
            ETerrain terrain = SaveManager.SAVED_SMALL_SQUARE.equals(name) ? ETerrain.SMALL_SQUARE
                    : ETerrain.valueOf(name);
            if (!terrains.add(terrain))
                throw new IllegalArgumentException("Terreno repetido. " + name);
        }
        if (!terrains.equals(new HashSet<>(Excavation.getAvailableTerrains(person)))) {
            throw new IllegalArgumentException("Los terrenos de " + person.getName()
                    + " no corresponden a su nivel y habilidades.");
        }
    }

    /**
     * Reconstruye las estadísticas comprobando los códigos de piezas, las regiones
     * y sus contadores.
     *
     * @param value Objeto JSON con las estadísticas.
     * @return Estadísticas reconstruidas.
     * @throws IllegalArgumentException si los códigos o los contadores no son
     *                                  válidos.
     */
    private static Stats readStats(JsonElement value) {
        JsonObject data = SaveManager.objectWithFields(value, "Estadísticas", "Tesoros", "Lugares");
        Map<String, Integer> treasures = SaveManager.readCounts(data.get("Tesoros"), "Tesoros");
        for (String code : treasures.keySet()) {
            if (!code.matches("[A-Z]{3}_[A-Z]{3}_[0-9]{3}_[0-9]{4}")) {
                throw new IllegalArgumentException("Código de pieza no válido. " + code);
            }
            Treasure treasure = TreasureRepository.getTreasure(code);
            int part = Integer.parseInt(CodeHelper.getTreasurePart(code));
            if (treasure == null || part < 1 || part > treasure.getParts()) {
                throw new IllegalArgumentException("Pieza desconocida. " + code);
            }
        }
        Map<String, Integer> visits = SaveManager.readCounts(data.get("Lugares"), "Lugares");
        for (String code : visits.keySet()) {
            if (RegionRepository.getRegion(code) == null) {
                throw new IllegalArgumentException("Región desconocida en las estadísticas. " + code);
            }
        }
        Stats stats = new Stats();
        stats.restoreCounts(treasures, visits);
        return stats;
    }

    /**
     * Lee un objeto JSON cuyos valores son contadores enteros no negativos.
     *
     * @param value Objeto JSON de contadores.
     * @param field Nombre utilizado para identificar el campo en los errores.
     * @return Mapa que asocia cada clave con su contador.
     * @throws IllegalArgumentException si el valor no es un objeto o alguno de sus
     *                                  contadores no es válido.
     */
    private static Map<String, Integer> readCounts(JsonElement value, String field) {
        JsonObject object = SaveManager.readObject(value, field);
        Map<String, Integer> counts = new TreeMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            counts.put(entry.getKey(), SaveManager.readInt(entry.getValue(), field + " / " + entry.getKey()));
        }
        return counts;
    }

    /**
     * Obtiene un objeto JSON y comprueba que contiene exactamente los campos
     * indicados.
     *
     * @param value  Valor JSON que se comprueba.
     * @param label  Nombre descriptivo del objeto para los mensajes de error.
     * @param fields Campos que debe contener el objeto.
     * @return Objeto JSON comprobado.
     * @throws IllegalArgumentException si el valor no es un objeto o hay campos
     *                                  ausentes o desconocidos.
     */
    private static JsonObject objectWithFields(JsonElement value, String label, String... fields) {
        JsonObject object = SaveManager.readObject(value, label);
        if (!object.keySet().equals(Set.of(fields))) {
            throw new IllegalArgumentException("Hay campos ausentes o desconocidos en " + label + ".");
        }
        return object;
    }

    /**
     * Comprueba que un valor JSON es un objeto y lo obtiene.
     *
     * @param value Valor JSON que se comprueba.
     * @param field Nombre del campo para los mensajes de error.
     * @return Objeto JSON recibido.
     * @throws IllegalArgumentException si el valor es nulo o no es un objeto JSON.
     */
    private static JsonObject readObject(JsonElement value, String field) {
        if (value == null || !value.isJsonObject()) {
            throw new IllegalArgumentException("Se esperaba un objeto en " + field + ".");
        }
        return value.getAsJsonObject();
    }

    /**
     * Comprueba que un valor JSON es una lista y la obtiene.
     *
     * @param value Valor JSON que se comprueba.
     * @param field Nombre del campo para los mensajes de error.
     * @return Lista JSON recibida.
     * @throws IllegalArgumentException si el valor es nulo o no es una lista JSON.
     */
    private static JsonArray readArray(JsonElement value, String field) {
        if (value == null || !value.isJsonArray()) {
            throw new IllegalArgumentException("Se esperaba una lista en " + field + ".");
        }
        return value.getAsJsonArray();
    }

    /**
     * Lee una lista JSON de cadenas que no pueden estar vacías ni contener solo
     * espacios.
     *
     * @param value Lista JSON que se transforma.
     * @param field Nombre del campo para los mensajes de error.
     * @return Nueva lista con las cadenas leídas.
     * @throws IllegalArgumentException si el valor no es una lista o alguno de sus
     *                                  elementos no es un texto válido.
     */
    private static List<String> readTexts(JsonElement value, String field) {
        List<String> texts = new ArrayList<>();
        for (JsonElement item : SaveManager.readArray(value, field)) {
            texts.add(SaveManager.readRequiredText(item, field));
        }
        return texts;
    }

    /**
     * Lee una cadena JSON y comprueba que no está vacía ni contiene solo espacios.
     *
     * @param value Valor JSON que se comprueba.
     * @param field Nombre del campo para los mensajes de error.
     * @return Cadena recibida, sin modificar su contenido.
     * @throws IllegalArgumentException si el valor no es una cadena o no contiene
     *                                  texto.
     */
    private static String readRequiredText(JsonElement value, String field) {
        String text = SaveManager.readText(value, field);
        if (text.isBlank())
            throw new IllegalArgumentException(field + " no puede estar vacío.");
        return text;
    }

    /**
     * Comprueba que el valor JSON es una cadena y obtiene su contenido.
     *
     * @param value Valor JSON que se comprueba.
     * @param field Nombre del campo para los mensajes de error.
     * @return Cadena recibida, que puede estar vacía.
     * @throws IllegalArgumentException si el valor es nulo o no es una cadena JSON.
     */
    private static String readText(JsonElement value, String field) {
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Se esperaba texto en " + field + ".");
        }
        return value.getAsString();
    }

    /**
     * Lee un número JSON cuyo valor debe ser un entero no negativo dentro del rango
     * de int.
     *
     * @param value Valor JSON que se comprueba.
     * @param field Nombre del campo para los mensajes de error.
     * @return Entero no negativo leído.
     * @throws IllegalArgumentException si el valor no es numérico, es negativo,
     *                                  tiene parte fraccionaria o queda fuera del
     *                                  rango de int.
     */
    private static int readInt(JsonElement value, String field) {
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException("Se esperaba un número entero en " + field + ".");
        }
        try {
            int number = value.getAsBigDecimal().intValueExact();
            if (number < 0)
                throw new IllegalArgumentException(field + " no puede ser negativo.");
            return number;
        } catch (ArithmeticException | NumberFormatException exception) {
            throw new IllegalArgumentException(field + " debe ser un entero válido.", exception);
        }
    }
}
