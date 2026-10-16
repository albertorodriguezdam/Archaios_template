package helpers;

import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static int inputOptionQuest(int min, int max, String msg, String err) {
        while (true) {
            System.out.print(msg);
            String text = scanner.nextLine();

            try {
                int option = Integer.parseInt(text.trim());
                if (option >= min && option <= max) {
                    return option;
                }
            } catch (NumberFormatException e) {
                // Si no es un número, se vuelve a pedir.
            }

            System.out.println(err);
        }
    }

    public static int inputMenu(String msg, String err) {
        return inputOptionQuest(0, 7, msg, err);
    }

    public static String inputExtra(String msg) {
        while (true) {
            System.out.print(msg);
            String text = scanner.nextLine().trim();

            if (!text.isEmpty()) {
                return text;
            }

            System.out.println("Escribe un texto.");
        }
    }

    public static String inputDigPosition(String msg, String err) {
        return inputExtra(msg);
    }

    public static char[] readPassword(String msg) {
        return inputExtra(msg).toCharArray();
    }
}