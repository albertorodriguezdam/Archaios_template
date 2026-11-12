package helpers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Agrupa las utilidades de escritura de los registros de partidas
 * y errores, incorporando fecha y hora a sus entradas.
 *
 * @author Javier A. Goldar Picón
 */
public class LogHelper {
    
    /**
     * Obtiene la fecha y hora actuales del equipo y las convierte
     * en texto con el formato yyyy-MM-dd HH:mm:ss.
     *
     * @return La fecha y hora formateadas como texto.
     */
    private static String getTimestamp() {

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    /**
     * Escribe una entrada con fecha y hora y crea las carpetas necesarias.
     *
     * @param filePath La ruta del archivo de registro.
     * @param message El mensaje que se añade al registro.
     * @param options Indica si se crea un registro nuevo o se añade al existente.
     * @throws IOException Si falla la creación de las carpetas o la apertura, escritura o cierre del archivo.
     */
    private static void writeLog(String filePath, String message, StandardOpenOption... options) throws IOException {
        Path logFile = Path.of(filePath);
        try {
            Files.createDirectories(logFile.getParent());
        } catch (FileAlreadyExistsException exception) {
            throw new IOException("La ruta " + logFile.getParent() + " no es una carpeta.", exception);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8, options)) {
            writer.write("[" + LogHelper.getTimestamp() + "] " + message);
            writer.newLine();
        }
    }

    /**
     * Añade una entrada de inicio con fecha y hora al registro de la partida.
     * Crea un archivo nuevo en log/register. Si ya existe, informa al llamador.
     *
     * @param gameName El nombre de la partida que identifica el archivo de registro.
     * @throws IOException Si falla la preparación, escritura o cierre del registro.
     */
    public static void createGameLog(String gameName) throws IOException {
        
        String filePath = "log/register/" + gameName + ".log";
        LogHelper.writeLog(filePath, "Inicio de la partida.",
            StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
    }

    /**
     * Añade el código completo de una pieza encontrada al registro
     * de la partida, junto con la fecha y hora actuales.
     *
     * @param gameName El nombre de la partida que identifica el archivo de registro.
     * @param treasureCode El código completo de la pieza encontrada.
     * @throws IOException Si falla la preparación, escritura o cierre del registro.
     */
    public static void logTreasure(String gameName, String treasureCode) throws IOException {
        
        String filePath = "log/register/" + gameName + ".log";
        LogHelper.writeLog(filePath, treasureCode, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Añade al registro global log/errors.log la descripción y los elementos de la traza 
     * de una excepción, incluyendo la fecha y hora actuales y conservando las entradas anteriores.
     *
     * @param exception La excepción cuya información se registra.
     * @throws IOException Si falla la preparación, escritura o cierre del registro.
     */
    public static void logError(Exception exception) throws IOException {
        
        String filePath = "log/errors.log";
        String description = exception.toString();

        StringBuilder errorText = new StringBuilder(description);
        errorText.append("\n");

        for (StackTraceElement traceElement : exception.getStackTrace()) {
            errorText.append(" at ")
            .append(traceElement.toString())
            .append("\n");
        }

        LogHelper.writeLog(filePath, errorText.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
