package helpers;

/** Formato compartido de las pantallas del programa de referencia. */
public final class MenuHelper {
    private MenuHelper() { }

    public static void mainHeader() {
        System.out.println("==================== Archaios ====================");
    }

    public static void header(String title) {
        System.out.println(":::::::::::::::::::: " + title + " ::::::::::::::::::::");
    }

    public static void subHeader(String title) {
        System.out.println("-------------------- " + title + " --------------------");
    }

    public static int returnMenu(String text) {
        System.out.println("----------------------------------------");
        System.out.println("1.- " + text);
        System.out.println("0.- Volver");
        return InputValidator.inputOptionQuest(0, 1, "");
    }

    public static boolean confirm(String text) {
        System.out.println(text);
        System.out.println("0.- No");
        System.out.println("1.- Si");
        return InputValidator.inputOptionQuest(0, 1, "") == 1;
    }
}
