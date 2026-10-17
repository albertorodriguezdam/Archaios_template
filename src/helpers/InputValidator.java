package helpers;

import java.nio.charset.Charset;
import java.util.Scanner;

/** Un único lector para menús, nombres y coordenadas. */
public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in, Charset.defaultCharset());

    public static String readLine(String message) {
        if (System.console() != null) return System.console().readLine("%s", message);
        System.out.print(message);
        return scanner.nextLine();
    }

    public static int inputOptionQuest(int min, int max, String message, String error) {
        while (true) {
            try {
                int option = Integer.parseInt(readLine(message));
                if (option >= min && option <= max) return option;
            } catch (NumberFormatException e) {
                // Se repite la petición con el mensaje de la referencia.
            }
            System.out.println(error);
        }
    }

    public static int inputOptionQuest(int min, int max, String message) {
        return inputOptionQuest(min, max, message,
            "Introduce un valor entre " + min + " y " + max + ".");
    }

    public static int inputMenu(String message, String error) {
        return inputOptionQuest(0, 7, message, error);
    }

    public static String inputExtra(String message) {
        return readLine(message);
    }

    public static String inputDigPosition(String message, String error) {
        return readLine(message);
    }

    public static char[] readPassword(String message) {
        if (System.console() != null) return System.console().readPassword(message);
        return readLine(message).toCharArray();
    }
}
