package helpers;
import java.io.Console;
/**
 * Clase auxiliar para validar entradas por consola.
 */
public class InputValidator {
/**
 * Consola usada para leer datos del usuario.
 */
    private static Console console = System.console();
    /**
     * Pide una opción numérica dentro de un rango.
     * 
     * @param min valor mínimo permitido    
     * @param max valor máximo permitido 
     * @param msg mensaje que se muestra al usuario
     * @param err mensaje de error
     * @return opción válida introducida por el usuario
     */
    public static int inputOptionQuest(int min, int max, String msg, String err) {

        int option = Integer.MIN_VALUE;
        
        while (option == Integer.MIN_VALUE) {

            try {
                option = Integer.parseInt(console.readLine(msg));

                if (option < min || option > max){
                    System.out.println(err);
                    option = Integer.MIN_VALUE;
                }
            } catch (NumberFormatException ex) {
                System.out.println(err);
            }
        }

        return option;
    }
    /* Falta acabar los metodos que vimos en el InputValidator del profesor */
    public static int inputMenu(String msg, String err) {
        // TODO
        return 0;
    }

    public static String inputExtra(String msg) {
        // TODO
        return null;
    }

    public static String inputDigPosition(String msg, String err){
        // TODO
        return null;
    }

    public static char[] readPassword(String msg) {
        // TODO
        return null;
    }

}
