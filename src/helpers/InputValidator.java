package helpers;

import java.nio.charset.Charset;
import java.util.Scanner;

/** Un único lector para menús, nombres y coordenadas. */
public class InputValidator {
    // Se reutiliza el mismo Scanner para todas las lecturas que lo necesiten.
    private static final Scanner scanner = new Scanner(System.in, Charset.defaultCharset());

    // Lee una línea usando la consola nativa cuando está disponible.
    public static String readLine(String message) {
        if (System.console() != null) return System.console().readLine("%s", message);
        // Si no hay consola nativa, utiliza el Scanner compartido.
        System.out.print(message);
        return scanner.nextLine();
    }

    // Repite la pregunta hasta recibir un número dentro del intervalo permitido.
    public static int inputOptionQuest(int min, int max, String message, String error) {
        while (true) {
            try {
                int option = Integer.parseInt(readLine(message));
                if (option >= min && option <= max) return option;
            // Una entrada que no se puede convertir en número vuelve a solicitarse.
            } catch (NumberFormatException e) {
                // Se repite la petición con el mensaje de la referencia.
            }
            System.out.println(error);
        }
    }

    // Usa el mensaje de error común cuando no se indica uno propio.
    public static int inputOptionQuest(int min, int max, String message) {
        return inputOptionQuest(min, max, message,
            "Introduce un valor entre " + min + " y " + max + ".");
    }

    // Atajo para leer una opción del menú principal, entre 0 y 7
    public static int inputMenu(String message, String error) {
        return inputOptionQuest(0, 7, message, error);
    }

    // Lee el texto tal como lo escribe el usuario, por ejemplo el nombre.
    public static String inputExtra(String message) {
        return readLine(message);
    }

    // Solo lee las coordenadas como texto no las valida en este método.
    public static String inputDigPosition(String message, String error) {
        return readLine(message);
    }

    // Usa la lectura oculta si hay consola nativ, en otro caso lee texto normal.
    public static char[] readPassword(String message) {
        if (System.console() != null) return System.console().readPassword(message);
        return readLine(message).toCharArray();
    }
}
