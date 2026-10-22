package helpers;

import java.nio.charset.Charset;
import java.util.Scanner;


/**
 * Componente encargado de ser un único lector para menús, nombres y coordenadas.
 * @author Constantin Laurentiu
 */
public class InputValidator {
    
    /**Sistema único utilizado como lector de datos de entrada. */
    private static final Scanner scanner = new Scanner(System.in, Charset.defaultCharset());

    /**
     * Solicita una línea de texto mediante la consola nativa o,
     * si no está disponible, mediante el Scanner compartido.
     * @param message Mensaje que se muestra antes de leer.
     * @return Línea introducida por el usuario, sin el salto de línea.
     */
    public static String readLine(String message) {
        if (System.console() != null) return System.console().readLine("%s", message);
       
        System.out.print(message);
        return InputValidator.scanner.nextLine();
    }

    /**
     * Solicita una opción numérica dentro de un intervalo.
     * Repite la petición si la entrada no es un entero o está fuera del intervalo.
     * @param min Valor mínimo permitido, incluido.
     * @param max Valor máximo permitido, incluido.
     * @param message Mensaje para solicitar la opción.
     * @param error Mensaje que se muestra cuando la entrada no es válida.
     * @return Opción válida comprendida entre min y max.
     */
    public static int inputOptionQuest(int min, int max, String message, String error) {
        while (true) {
            try {
                int option = Integer.parseInt(readLine(message));
                if (option >= min && option <= max) return option;
            
            } catch (NumberFormatException e) {
                
            }
            System.out.println(error);
        }
    }

    /**
     * Solicita una opción numérica dentro de un intervalo,
     * utilizando el mensaje de error predeterminado.
     * @param min Valor mínimo permitido, incluido.
     * @param max Valor máximo permitido, incluido.
     * @param message Mensaje para solicitar la opción.
     * @return Opción válida comprendida entre min y max.
     */
    public static int inputOptionQuest(int min, int max, String message) {
        return inputOptionQuest(min, max, message,
            "Introduce un valor entre " + min + " y " + max + ".");
    }

    /**
     * Solicita una opción del menú principal comprendida entre 0 y 7.
     * @param message Mensaje para solicitar la opción.
     * @param error Mensaje que se muestra cuando la entrada no es válida.
     * @return Opción válida del menú principal; 0 para salir.
     */
    public static int inputMenu(String message, String error) {
        return inputOptionQuest(0, 7, message, error);
    }

    /**
     * Solicita un texto adicional, como el nombre de un arqueólogo.
     * @param message Mensaje para solicitar el texto.
     * @return Texto introducido por el usuario, sin validar ni modificar.
     */
    public static String inputExtra(String message) {
        return readLine(message);
    }

    /**
     * Solicita una línea de coordenadas de excavación sin validar su formato.
     * @param message Mensaje para solicitar las coordenadas.
     * @param error Parámetro reservado; actualmente no se utiliza.
     * @return Texto introducido con las coordenadas.
     */
    public static String inputDigPosition(String message, String error) {
        return readLine(message);
    }

   /**
     * Solicita una contraseña ocultando la entrada cuando hay consola nativa.
     * Si no está disponible, la lee como texto visible.
     * @param message Mensaje para solicitar la contraseña.
     * @return Caracteres de la contraseña introducida.
     */
    public static char[] readPassword(String message) {
        if (System.console() != null) return System.console().readPassword(message);
        return readLine(message).toCharArray();
    }
}
