package commons;

import commons.repositories.RegionRepository;
import commons.repositories.TreasureRepository;
import excavation.ETerrain;
import excavation.Excavation;
import helpers.InputValidator;
import java.util.ArrayList;
import java.util.List;
import people.Archeologist;
import regions.Grecia;
import regions.Region;
import skills.Skill;
import skills.SkillExcavation;
import treasures.Treasure;

public class Archaios {
    private List<Archeologist> archeologists = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private Stats stats = new Stats();
    private int points = 0;
    private boolean firstArcheologistCreated = false;

    public Archaios() {
        Region greece = new Grecia();
        regions.add(greece);
        RegionRepository.registerRegion(greece);
        TreasureRepository.registerRegionTreasures(greece);
    }

    public static void main(String[] args) {
        Archaios game = new Archaios();
        game.menu();
    }

    public void menu() {
        int option;

        do {
            System.out.println("\n========== Archaios ==========");
            System.out.println("1. Arqueólogos");
            System.out.println("2. Habilidades");
            System.out.println("3. Licencias");
            System.out.println("4. Atlas");
            System.out.println("5. Catálogo de tesoros");
            System.out.println("6. Excavar");
            System.out.println("7. Estadísticas");
            System.out.println("0. Salir");

            option = InputValidator.inputOptionQuest(
                0, 7, "Elige una opción: ", "Escribe un número del 0 al 7."
            );

            switch (option) {
                case 1:
                    archeologistsMenu();
                    break;
                case 2:
                    showSkills();
                    break;
                case 3:
                    licenseArcheologist();
                    break;
                case 4:
                    new Atlas(regions).show();
                    break;
                case 5:
                    new Atlas(regions).showCatalog();
                    break;
                case 6:
                    excavateMenu();
                    break;
                case 7:
                    showStats();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
            }
        } while (option != 0);
    }

    private void archeologistsMenu() {
        int option;

        do {
            System.out.println("\n--- Arqueólogos ---");
            System.out.println("1. Crear arqueólogo");
            System.out.println("2. Ver arqueólogos");
            System.out.println("0. Volver");

            option = InputValidator.inputOptionQuest(
                0, 2, "Elige una opción: ", "Escribe 0, 1 o 2."
            );

            switch (option) {
                case 1:
                    String name = InputValidator.inputExtra("Nombre: ");
                    Archeologist archeologist = new Archeologist(name);

                    if (!firstArcheologistCreated) {
                        // El primero empieza con una habilidad.
                        archeologist.learnSkill(SkillExcavation.createExcavation(
                            "Excavación I", "Cinco extracciones extra", 5
                        ));
                        firstArcheologistCreated = true;
                    }

                    archeologists.add(archeologist);
                    System.out.println("Arqueólogo " + name + " añadido.");
                    break;
                case 2:
                    showArcheologists();
                    break;
            }
        } while (option != 0);
    }

    private void showArcheologists() {
        if (archeologists.isEmpty()) {
            System.out.println("Todavía no hay arqueólogos.");
            return;
        }

        for (int i = 0; i < archeologists.size(); i++) {
            System.out.println((i + 1) + ". " + archeologists.get(i));
        }
        System.out.println();
    }

    private void showSkills() {
        if (archeologists.isEmpty()) {
            System.out.println("Primero crea un arqueólogo desde la opción 1.");
            return;
        }

        showArcheologists();

        int number = InputValidator.inputOptionQuest(
            0, archeologists.size(),
            "Arqueólogo (0 para volver): ",
            "Número incorrecto."
        );

        if (number == 0) {
            return;
        }

        List<Skill> skills = archeologists.get(number - 1).getSkills();

        if (skills.isEmpty()) {
            System.out.println("Este arqueólogo todavía no tiene habilidades.");
        } else {
            for (Skill skill : skills) {
                System.out.println(skill);
            }
        }
        System.out.println();
    }

    private void licenseArcheologist() {
        if (archeologists.isEmpty()) {
            System.out.println("No hay arqueólogos que licenciar.");
            return;
        }

        showArcheologists();

        int number = InputValidator.inputOptionQuest(
            0, archeologists.size(),
            "Arqueólogo (0 para volver): ",
            "Número incorrecto."
        );

        if (number == 0) {
            return;
        }

        Archeologist chosen = archeologists.get(number - 1);
        int earned = chosen.getLicensingPoints();

        System.out.println(
            "Licenciar a " + chosen.getName() + " da " + earned + " puntos."
        );

        int confirm = InputValidator.inputOptionQuest(
            0, 1, "¿Confirmar? 1 sí, 0 no: ", "Escribe 0 o 1."
        );

        if (confirm == 1) {
            archeologists.remove(number - 1);
            points += earned;
            System.out.println("Arqueólogo licenciado. Puntos: " + points);
        }
        System.out.println();
    }

    private void excavateMenu() {
        if (archeologists.isEmpty()) {
            System.out.println("Primero crea un arqueólogo.");
            return;
        }

        System.out.println("\n--- Regiones ---");
        for (int i = 0; i < regions.size(); i++) {
            System.out.println((i + 1) + ". " + regions.get(i).getClass().getSimpleName());
        }

        int regionNumber = InputValidator.inputOptionQuest(
            0, regions.size(), "Región (0 para volver): ", "Número incorrecto."
        );
        if (regionNumber == 0) {
            return;
        }
        Region region = regions.get(regionNumber - 1);

        System.out.println("\n--- Arqueólogos ---");
        for (int i = 0; i < archeologists.size(); i++) {
            Archeologist a = archeologists.get(i);
            System.out.println((i + 1) + ". " + a.getName() + " (nivel " + a.getLevel() + ")");
        }

        int archeologistNumber = InputValidator.inputOptionQuest(
            0, archeologists.size(), "Arqueólogo (0 para volver): ", "Número incorrecto."
        );
        if (archeologistNumber == 0) {
            return;
        }
        Archeologist chosen = archeologists.get(archeologistNumber - 1);

        List<ETerrain> terrains = Excavation.getAvailableTerrains(chosen);
        if (terrains.isEmpty()) {
            System.out.println("Este arqueólogo no tiene terrenos disponibles.");
            return;
        }

        System.out.println("\n--- Terrenos disponibles ---");
        for (int i = 0; i < terrains.size(); i++) {
            System.out.println((i + 1) + ". " + terrains.get(i));
        }

        int terrainNumber = InputValidator.inputOptionQuest(
            0, terrains.size(), "Terreno (0 para volver): ", "Número incorrecto."
        );
        if (terrainNumber == 0) {
            return;
        }

        Excavation excavation = new Excavation(terrains.get(terrainNumber - 1), region, chosen);
        excavation.excavate();
        List<String> foundCodes = excavation.finish();

        stats.registerVisit(region.getCode());
        for (String code : foundCodes) {
            stats.registerTreasure(code);
        }

        List<Treasure> found = excavation.getFoundTreasures();
        for (int i = 0; i < found.size(); i++) {
            System.out.println("\n" + found.get(i).getTxt() + " (" + foundCodes.get(i) + ")");
            System.out.println(found.get(i).getDescription());
        }

        int earnedPoints = excavation.getEarnedPoints();
        int earnedXp = excavation.getEarnedXp();
        points += earnedPoints;
        int levelsGained = chosen.addXp(earnedXp);

        System.out.println("\nPuntos ganados: " + earnedPoints + ". Total: " + points);
        System.out.println("Experiencia ganada: " + earnedXp);
        if (levelsGained > 0) {
            System.out.println(chosen.getName() + " ha subido " + levelsGained + " nivel(es).");
        }
    }

    private void showStats() {
        int option;

        do {
            System.out.println("\n--- Estadísticas ---");
            System.out.println("1. Lugares visitados");
            System.out.println("2. Tesoros por región");
            System.out.println("3. Tesoros por tipo");
            System.out.println("0. Volver");

            option = InputValidator.inputOptionQuest(
                0, 3, "Elige una opción: ", "Escribe un número del 0 al 3."
            );

            switch (option) {
                case 1:
                    stats.getStatsPlaces();
                    break;
                case 2:
                    stats.showTreasuresByRegion();
                    break;
                case 3:
                    stats.showTreasuresByType();
                    break;
            }

            if (option != 0) {
                System.out.println();
            }
        } while (option != 0);
    }
}