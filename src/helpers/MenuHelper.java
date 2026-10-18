package helpers;
public final class MenuHelper {
    // Solo contiene métodos estáticos,no hace falta crear un objeto.
    private MenuHelper() { }

    // Imprime el título del menú principal.
    public static void mainHeader() {
        System.out.println("==================== Archaios ====================");
    }

    // Imprime el título de una sección con el formato común.
    public static void header(String title) {
        System.out.println(":::::::::::::::::::: " + title + " ::::::::::::::::::::");
    }

    // Imprime el título de una ficha o de una lista.
    public static void subHeader(String title) {
        System.out.println("-------------------- " + title + " --------------------");
    }

    // Devuelve 1 para repetir la consulta o 0 para volver.
    public static int returnMenu(String text) {
        System.out.println("----------------------------------------");
        System.out.println("1.- " + text);
        System.out.println("0.- Volver");
        return InputValidator.inputOptionQuest(0, 1, "");
    }

    // Devuelve true solo cuando se confirma con la opción 1.
    public static boolean confirm(String text) {
        System.out.println(text);
        System.out.println("0.- No");
        System.out.println("1.- Si");
        return InputValidator.inputOptionQuest(0, 1, "") == 1;
    }
}
