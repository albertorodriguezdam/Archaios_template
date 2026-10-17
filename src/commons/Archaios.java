package commons;

import commons.repositories.RegionRepository;
import commons.repositories.SkillRepository;
import commons.repositories.TreasureRepository;
import excavation.ETerrain;
import excavation.Excavation;
import helpers.InputValidator;
import helpers.MenuHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import people.Archeologist;
import regions.Grecia;
import regions.Region;
import skills.ESkillType;
import skills.Skill;
import treasures.Treasure;

public class Archaios {
    private List<Archeologist> archeologists = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private Stats stats = new Stats();
    private int points = 0;
    private final Random random = new Random();

    public Archaios() {
        SkillRepository.register();
        regions.add(new Grecia());
        /*regions.add(new Iberia());
        regions.add(new Roma());
        regions.add(new Egipto());
        regions.add(new Petra());
        regions.add(new Sumeria());*/
        regions.sort(java.util.Comparator.comparing(Region::getCode));
        for (Region region : regions) {
            RegionRepository.registerRegion(region);
            TreasureRepository.registerRegionTreasures(region);
        }
    }

    public static void main(String[] args) {
        Archaios game = new Archaios();
        game.init();
        game.menu();
    }

    public void menu() {
        int option;

        do {
            MenuHelper.mainHeader();
            System.out.println("1.- Arqueólogos");
            System.out.println("2.- Habilidades");
            System.out.println("3.- Licencias");
            System.out.println("4.- Atlas");
            System.out.println("5.- Catálogo de tesoros");
            System.out.println("6.- Excavación");
            System.out.println("7.- Estadísticas");
            System.out.println("0.- Salir");

            option = InputValidator.inputOptionQuest(
                0, 7, ""
            );

            switch (option) {
                case 1:
                    archeologistsMenu();
                    break;
                case 2:
                    showSkills();
                    break;
                case 3:
                    buyLicenses();
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
                    break;
            }
        } while (option != 0);
    }

    private void archeologistsMenu() {
        int option;
        do {
            MenuHelper.header("Arqueólogos");
            System.out.println("1.- Ver arqueólogos");
            System.out.println("2.- Contratar");
            System.out.println("3.- Licenciar");
            System.out.println("0.- Volver");
            option = InputValidator.inputOptionQuest(0, 3, "");
            switch (option) {
                case 1:
                    viewArcheologist();
                    break;
                case 2:
                    hireArcheologist();
                    break;
                case 3:
                    licenseArcheologist();
                    break;
            }
        } while (option != 0);
    }

    private void showArcheologists() {
        MenuHelper.subHeader("Arqueólogos");
        for (int i = 0; i < archeologists.size(); i++) {
            System.out.println((i + 1) + ".- " + archeologists.get(i).getName());
        }
        System.out.println("0.- Volver");
    }

    private void showSkills() {
        ESkillType[] types = ESkillType.values();
        while (true) {
            MenuHelper.header("Habilidades");
            for (int i = 0; i < types.length; i++) {
                System.out.println((i + 1) + ".- " + capitalise(types[i].toString()));
            }
            System.out.println("0.- Volver");
            int type = InputValidator.inputOptionQuest(0, types.length, "Tipo de habilidad: ");
            if (type == 0) return;
            List<Skill> skills = SkillRepository.getByType(types[type - 1]);
            while (true) {
                MenuHelper.subHeader(capitalise(types[type - 1].toString()));
                for (int i = 0; i < skills.size(); i++) {
                    System.out.println((i + 1) + ".- " + skills.get(i).getName());
                }
                System.out.println("0.- Volver");
                int number = InputValidator.inputOptionQuest(0, skills.size(), "Habilidad a consultar: ");
                if (number == 0) break;
                Skill selected = skills.get(number - 1);
                MenuHelper.subHeader(selected.getName());
                System.out.println(selected.getDescription());
                System.out.println("Tipo: " + selected.getType());
                showSkillRelations("Dependencias", selected.getDependencies());
                showSkillRelations("Incompatibilidades", selected.getIncompatibilities());
                if (MenuHelper.returnMenu("Consultar otra habilidad") == 0) return;
            }
        }
    }

    private void licenseArcheologist() {
        System.out.println("Al licenciar un arqueólogo te deshaces de él otorgando puntos en compensación.");
        if (archeologists.size() <= 1) {
            System.out.println("No puedes licenciar tu último arqueólogo.");
            return;
        }
        while (true) {
            Archeologist selected = selectArcheologist("Arqueólogo a licenciar: ");
            if (selected == null) return;
            int earned = selected.getLicensingPoints();
            System.out.println("Licenciar a " + selected.getName() + " otorga " + earned + " puntos.");
            if (MenuHelper.confirm("¿Quieres licenciarlo?")) {
                archeologists.remove(selected);
                points += earned;
                return;
            }
        }
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

    /** La primera contratación se realiza al crear la partida, sin coste ni habilidades. */
    private void init() {
        String name = InputValidator.inputExtra("Introduce el nombre del primer arqueólogo: ");
        archeologists.add(new Archeologist(name));
    }


    private String capitalise(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private void showSkillRelations(String title, List<Skill> skills) {
        if (skills.isEmpty()) return;
        System.out.println("--------------------");
        System.out.println(title + ":");
        for (Skill skill : skills) System.out.println("-" + skill.getName());
    }


    private void viewArcheologist() {
        int again = 1;
        while (again == 1) {
            Archeologist selected = selectArcheologist("Arqueólogo a consultar: ");
            if (selected == null) return;
            MenuHelper.subHeader(selected.getName());
            System.out.println("Nivel: " + selected.getLevel());
            if (selected.getLevel() == selected.getMAX_LEVEL()) {
                System.out.println("Especialización: " + specializationReference(selected.getSpecialization()));
            }
            String experience = Integer.toString(selected.getXp());
            if (selected.getLevel() < selected.getMAX_LEVEL()) {
                experience += "/" + selected.getXpToNextLevel();
            }
            System.out.println("Exp: " + experience);
            System.out.println("Terrenos:");
            for (String terrain : selected.getTerrainsSize()) {
                System.out.println(" -" + terrain);
            }
            if (!selected.getLicenses().isEmpty()) {
                System.out.println("Licencias:");
                for (String code : selected.getLicenses()) {
                    System.out.println(" -" + RegionRepository.getRegion(code).getClass().getSimpleName());
                }
            }
            if (!selected.getSkills().isEmpty()) {
                System.out.println("Habilidades:");
                for (Skill skill : selected.getSkills()) {
                    System.out.println(" -" + skill.getName() + ":");
                    System.out.println("  " + skill.getDescription());
                }
            }
            again = MenuHelper.returnMenu("Consultar otro arqueólogo");
        }
    }


    private Archeologist selectArcheologist(String question) {
        if (archeologists.isEmpty()) {
            System.out.println("No hay arqueólogos.");
            return null;
        }
        showArcheologists();
        int number = InputValidator.inputOptionQuest(0, archeologists.size(), question);
        return number == 0 ? null : archeologists.get(number - 1);
    }


    private void hireArcheologist() {
        System.out.println("Puntos disponibles: " + points);
        System.out.println("El coste de contratar a un nuevo arqueólogo es de 50 puntos");
        if (points < 50) {
            System.out.println("No tienes puntos suficientes para contratar un nuevo arqueólogo.");
            return;
        }
        if (!MenuHelper.confirm("¿Quieres contratar un nuevo arqueólogo?")) {
            System.out.println("No tienes puntos suficientes para contratar un nuevo arqueólogo.");
            return;
        }
        int roll = random.nextInt(100);
        int skillCount = roll < 70 ? 0 : roll < 90 ? 1 : 2;
        String rarity = skillCount == 0 ? "común" : skillCount == 1 ? "infrecuente" : "raro";
        Archeologist generated = new Archeologist("");
        for (int i = 0; i < skillCount; i++) {
            List<Skill> possible = SkillRepository.getLearnable(generated);
            generated.learnSkill(possible.get(random.nextInt(possible.size())));
        }
        System.out.println("----------------------------------------");
        System.out.println("Arqueólogo " + rarity);
        if (!generated.getSkills().isEmpty()) {
            MenuHelper.subHeader("Habilidades");
            for (Skill skill : generated.getSkills()) {
                System.out.println(skill.getName());
                System.out.println("  " + skill.getDescription());
            }
        }
        String name = InputValidator.inputExtra("Introduce el nombre del arqueólogo: ");
        Archeologist hired = new Archeologist(name);
        for (Skill skill : generated.getSkills()) hired.learnSkill(skill);
        archeologists.add(hired);
        points -= 50;
    }


    private void buyLicenses() {
        MenuHelper.header("Licencias");
        while (true) {
            Archeologist selected = selectArcheologist("Arqueólogo para el que comprar licencias: ");
            if (selected == null) return;
            List<String> owned = selected.getLicenses();
            List<Region> available = new ArrayList<>();
            for (Region region : regions) {
                if (List.of("EGP", "PTR", "SUM").contains(region.getCode()) && !owned.contains(region.getCode())) available.add(region);
            }
            if (available.isEmpty()) {
                System.out.println(selected.getName() + " ya dispone de todas las licencias.");
                continue;
            }
            int cost = 20 + 10 * owned.size();
            MenuHelper.subHeader(selected.getName());
            System.out.println("Puntos disponibles: " + points);
            System.out.println("Comprar una licencia para " + selected.getName() + " cuesta " + cost + " puntos.");
            if (points < cost) {
                System.out.println("No dispones de puntos suficientes para comprarle una licencia a " + selected.getName());
                continue;
            }
            for (int i = 0; i < available.size(); i++) {
                System.out.println((i + 1) + ".- " + available.get(i).getClass().getSimpleName());
            }
            System.out.println("0.- Volver");
            int option = InputValidator.inputOptionQuest(0, available.size(), "Comprar licencia para la región: ");
            if (option == 0) continue;
            selected.addLicense(available.get(option - 1).getCode());
            points -= cost;
            System.out.println("Licencia comprada.");
            return;
        }
    }


    /** Conserva la etiqueta del tipo que imprime la referencia; el identificador varía en cada ejecución. */
    private String specializationReference(String code) {
        Region region = RegionRepository.getRegion(code);
        if (region == null) return "null";
        String type;
        switch (code) {
            case "GRC": type = "Greece"; break;
            /*case "IBR": type = "Iberia"; break;
            case "ROM": type = "Rome"; break;
            case "EGP": type = "Egypt"; break;
            case "PTR": type = "Petra"; break;
            case "SUM": type = "Sumer"; break;*/
            default: return region.toString();
        }
        return "regions.places." + type + "@" + Integer.toHexString(System.identityHashCode(region));
    }

}
