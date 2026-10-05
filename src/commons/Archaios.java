package commons;

import helpers.InputValidator;

public class Archaios {

    private String userID;
    private String groupCode;
    private String gameName;
  //private Stats stats;


    public void preInit() {

    }

    public void init() {
        System.out.println("Hay partidas guardadas, ¿quieres cargar alguna?");
        System.out.println("0.- No");
        System.out.println("1.- Sí");

        int selectedOption = InputValidator.inputOptionQuest(
            0,
            1,
            "",
            "Introduce un valor entre 0 y 1."
        );
    }

    public void end() {

    }

    public void save() {

    }    
    /**
     * TODO
     */
    public void menu() {
        System.out.println("==================== Archaios ====================");
        System.out.println("1.- Arqueólogos");
        System.out.println("2.- Habilidades");
        System.out.println("3.- Licencias");
        System.out.println("4.- Atlas");
        System.out.println("5.- Catálogo de tesoros");
        System.out.println("6.- Excavación");
        System.out.println("7.- Estadísticas");
        System.out.println("0.- Salir");
        int selectedOption = InputValidator.inputOptionQuest(
            0,
            7,
            "",
            "Introduce un valor entre 0 y 7."
        );
    }


}

