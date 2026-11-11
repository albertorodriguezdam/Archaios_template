package helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
     * Añade una entrada con fecha y hora a un archivo de registro, conservando su contenido anterior. 
     * Crea el archivo y las carpetas necesarias si no existen.
     *
     * @param filePath La ruta del archivo de registro.
     * @param message El mensaje que se añade al registro.
     * @throws IOException Si falla la creación de las carpetas o la apertura, escritura o cierre del archivo.
     */
    private static void appendLog(String filePath, String message) throws IOException {

        File logFile = new File(filePath);
        File parentDirectory = logFile.getParentFile();

        if ((parentDirectory != null) && (!parentDirectory.exists())) {
            boolean directoryCreated = parentDirectory.mkdirs();

            if (!directoryCreated) {
                throw new IOException("No se ha podido crear la carpeta: " + parentDirectory);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write("[" + LogHelper.getTimestamp() + "] " + message);
            writer.newLine();
        }
    }

    /**
     * Añade una entrada de inicio con fecha y hora al registro de la partida.
     * Crea el archivo en log/register y las carpetas necesarias si no existen.
     *
     * @param gameName El nombre de la partida que identifica el archivo de registro.
     * @throws IOException Si falla la preparación, escritura o cierre del registro.
     */
    public static void createGameLog(String gameName) throws IOException {
        
        String filePath = "log/register/" + gameName + ".log";
        LogHelper.appendLog(filePath, "Inicio de la partida.");
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
        LogHelper.appendLog(filePath, treasureCode);
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

        LogHelper.appendLog(filePath, errorText.toString());
    }
}