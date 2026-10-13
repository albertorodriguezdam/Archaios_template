package commons;

import java.util.List;

import helpers.InputValidator;
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
            option = chooseRegion();

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
            }
        } while (option != 0);
    }

    public void showCatalog() {
        int option;

        do {
            option = chooseRegion();

            if (option != 0) {
                Region region = regions.get(option - 1);
                List<Treasure> treasures = region.getTreasures();

                for (int i = 0; i < treasures.size(); i++) {
                    System.out.println(
                        (i + 1) + ". " + treasures.get(i).getTxt()
                    );
                }

                int number = InputValidator.inputOptionQuest(
                    0, treasures.size(),
                    "Tesoro (0 para volver): ",
                    "Número incorrecto."
                );

                if (number != 0) {
                    System.out.println(treasures.get(number - 1));
                }
            }
        } while (option != 0);
    }

    private int chooseRegion() {
        System.out.println("\n--- Regiones ---");

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