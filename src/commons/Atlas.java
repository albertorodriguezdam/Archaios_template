package commons;

import helpers.InputValidator;
import java.util.List;
import regions.Region;
import treasures.Treasure;

public class Atlas {
    private List<Region> regions;

    public Atlas(List<Region> regions) {
        this.regions = regions;
    }

    public void show() {
        int option;

        do {
            option = chooseRegion("Atlas de regiones");

            if (option != 0) {
                Region region = regions.get(option - 1);
                System.out.println(
                    "\n--- " + region.getClass().getSimpleName() + " ---"
                );

                for (Treasure treasure : region.getTreasures()) {
                    System.out.println(
                        treasure.getTxt() + " | "
                        + treasure.getType() + " | "
                        + treasure.getRarity()
                    );
                }

                int again = InputValidator.inputOptionQuest(
                    0, 1, "¿Volver al atlas? 1 sí, 0 menú: ", "Escribe 0 o 1."
                );
                if (again == 0) {
                    return;
                }
            }
        } while (option != 0);
    }

    public void showCatalog() {
        int option;

        do {
            option = chooseRegion("Catálogo de tesoros");

            if (option != 0) {
                Region region = regions.get(option - 1);
                List<Treasure> treasures = region.getTreasures();

                if (treasures.isEmpty()) {
                    System.out.println("No hay tesoros en esta región.");
                    return;
                }

                int again = 1;
                while (again == 1) {
                    System.out.println("\n--- Tesoros de " + region.getClass().getSimpleName() + " ---");

                    for (int i = 0; i < treasures.size(); i++) {
                        System.out.println((i + 1) + ". " + treasures.get(i).getTxt());
                    }

                    int number = InputValidator.inputOptionQuest(
                        1, treasures.size(), "Elige un tesoro: ", "Número incorrecto."
                    );

                    System.out.println("\n" + treasures.get(number - 1) + "\n");
                    again = InputValidator.inputOptionQuest(
                        0, 1, "¿Quieres ver otro tesoro de esta región? 1 sí, 0 menú: ",
                        "Escribe 0 o 1."
                    );
                }
                return;
            }
        } while (option != 0);
    }

    private int chooseRegion(String title) {
        System.out.println("\n--- " + title + " ---");

        for (int i = 0; i < regions.size(); i++) {
            Region region = regions.get(i);

            System.out.println(
                (i + 1) + ". "
                + region.getClass().getSimpleName()
                + " (" + region.getCode() + ")"
            );
        }

        System.out.println("0. Volver");

        return InputValidator.inputOptionQuest(
            0, regions.size(),
            "Elige una región: ",
            "Número incorrecto."
        );
    }
}