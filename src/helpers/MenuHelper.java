package helpers;

/**
 * Utilidades para mostrar cabeceras, opciones de retorno y confirmaciones.
 * @author Constantin Laurentiu
 */
public final class MenuHelper {

    /**
     * Impide crear instancias de esta clase de utilidades.
     */
    private MenuHelper() { }

    /**
     * Muestra la cabecera del menú principal de Archaios.
     */
    public static void mainHeader() {
        System.out.println("==================== Archaios ====================");
    }

    /**
     * Muestra la cabecera de una sección.
     * @param title Título de la sección.
     */
    public static void header(String title) {
        System.out.println(":::::::::::::::::::: " + title + " ::::::::::::::::::::");
    }

    /**
     * Muestra la cabecera de una ficha o una lista.
     * @param title Título de la ficha o lista.
     */
    public static void subHeader(String title) {
        System.out.println("-------------------- " + title + " --------------------");
    }

    /**
     * Muestra una opción para continuar la consulta y otra para volver.
     *
     * @param text Texto de la opción para continuar.
     * @return 1 para continuar la consulta; 0 para volver.
     */
    public static int returnMenu(String text) {
        System.out.println("----------------------------------------");
        System.out.println("1.- " + text);
        System.out.println("0.- Volver");
        return InputValidator.inputOptionQuest(0, 1, "");
    }

    /**
     * Solicita una confirmación mediante las opciones 0 y 1.
     *
     * @param text Pregunta que se muestra al usuario.
     * @return true si selecciona 1 para confirmar; false si selecciona 0.
     */
    public static boolean confirm(String text) {
        System.out.println(text);
        System.out.println("0.- No");
        System.out.println("1.- Si");
        return InputValidator.inputOptionQuest(0, 1, "") == 1;
    }
}
