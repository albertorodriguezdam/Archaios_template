package commons;
/* para pruebas usar 

New-Item -ItemType Directory -Force bin | Out-Null
javac -encoding UTF-8 -sourcepath src -d bin src\commons\Archaios.java
if ($LASTEXITCODE -ne 0) { throw "No se compila" }

java -cp bin commons.Archaios --pruebas

*/

import commons.repositories.RegionRepository;
import commons.repositories.SkillRepository;
import commons.repositories.TreasureRepository;
import excavation.ETerrain;
import excavation.ETerrainSize;
import excavation.Excavation;
import helpers.InputValidator;
import helpers.MenuHelper;
import java.util.ArrayList;
import java.util.Collections;
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
        regions.add(new Sumeria()); */
        regions.sort(java.util.Comparator.comparing(Region::getCode));
        for (Region region : regions) {
            RegionRepository.registerRegion(region);
            TreasureRepository.registerRegionTreasures(region);
        }
    }

    public static void main(String[] args) {
        Archaios game = new Archaios();
        if (args.length > 0 && "--pruebas".equals(args[0])) {
            game.archeologists = DatosPrueba.createArcheologists();
            game.stats = DatosPrueba.createStats();
            game.points = DatosPrueba.INITIAL_POINTS;
        } else {
            game.init();
        }
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
        MenuHelper.header("Excavación");
        while (true) {
            Archeologist selected = selectArcheologist("Arqueólogo que realiza la excavación: ");
            if (selected == null) return;
            List<Region> available = new ArrayList<>();
            List<String> owned = selected.getLicenses();
            for (Region region : regions) {
                if (!List.of("EGP", "PTR", "SUM").contains(region.getCode()) || owned.contains(region.getCode())) available.add(region);
            }
            while (true) {
                MenuHelper.subHeader("Regiones");
                for (int i = 0; i < available.size(); i++) {
                    System.out.println((i + 1) + ".- " + available.get(i).getClass().getSimpleName());
                }
                System.out.println("0.- Volver");
                int regionNumber = InputValidator.inputOptionQuest(0, available.size(), "Región en la que realizar la excavación: ");
                if (regionNumber == 0) return;
                Region region = available.get(regionNumber - 1);
                List<ETerrain> terrains = Excavation.getAvailableTerrains(selected);
                List<ETerrainSize> sizes = new ArrayList<>();
                for (ETerrain terrain : terrains) {
                    if (!sizes.contains(terrain.getSize())) sizes.add(terrain.getSize());
                }
                MenuHelper.subHeader("Terreno");
                for (int i = 0; i < sizes.size(); i++) System.out.println((i + 1) + ".- " + capitalise(sizes.get(i).toString()));
                System.out.println("0.- Volver");
                int sizeNumber = InputValidator.inputOptionQuest(0, sizes.size(), "Tamaño del terreno a excavar: ");
                if (sizeNumber == 0) continue;
                List<ETerrain> candidates = new ArrayList<>();
                for (ETerrain terrain : terrains) {
                    if (terrain.getSize() == sizes.get(sizeNumber - 1)) candidates.add(terrain);
                }
                ETerrain terrain = candidates.get(random.nextInt(candidates.size()));
                finishExcavation(new Excavation(terrain, region, selected), region, selected);
                return;
            }
        }
    }

    private void showStats() {
        while (true) {
            MenuHelper.header("Estadísticas");
            System.out.println("1.- Ver regiones");
            System.out.println("2.- Ver tesoros");
            System.out.println("0.- Volver");
            int option = InputValidator.inputOptionQuest(0, 2, "");
            if (option == 0) return;
            if (option == 1) {
                stats.getStatsPlaces();
            } else {
                MenuHelper.header("Estadísticas de tesoros");
                System.out.println("1.- Por región");
                System.out.println("2.- Por tipo");
                System.out.println("0.- Volver");
                int type = InputValidator.inputOptionQuest(0, 2, "");
                if (type == 1) stats.showTreasuresByRegion();
                if (type == 2) stats.showTreasuresByType();
            }
        }
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


    private void finishExcavation(Excavation excavation, Region region, Archeologist selected) {
        MenuHelper.header("Excavación en " + region.getClass().getSimpleName());
        excavation.excavate();
        System.out.println("::::::::::::::::::::::::::::::::::::::::");
        List<String> foundCodes = excavation.finish();
        List<Treasure> found = excavation.getFoundTreasures();
        stats.registerVisit(region.getCode());
        MenuHelper.subHeader("Tesoros encontrados");
        for (int i = 0; i < foundCodes.size(); i++) {
            if (i > 0) System.out.println("--------------------");
            stats.registerTreasure(foundCodes.get(i));
            Treasure treasure = found.get(i);
            System.out.println(treasure.getTxt());
            System.out.println(treasure.getDescription());
            System.out.println("Código: " + foundCodes.get(i));
            System.out.println(capitalise(Atlas.typeAndRarity(treasure)));
        }
        System.out.println("----------------------------------------");
        int earnedPoints = excavation.getEarnedPoints();
        points += earnedPoints;
        System.out.println("Puntos conseguidos: " + earnedPoints);
        int earnedXp = excavation.getEarnedXp();
        System.out.println("El arqueólogo " + selected.getName() + " ha conseguido " + earnedXp + " puntos de experiencia.");
        int levelsGained = selected.addXp(earnedXp);
        for (int i = 0; i < levelsGained; i++) {
            int level = selected.getLevel() - levelsGained + i + 1;
            chooseLevelSkill(selected, level);
        }
        if (levelsGained > 0 && selected.getLevel() == selected.getMAX_LEVEL()) {
            MenuHelper.subHeader("Especialización");
            System.out.println("El arqueólogo " + selected.getName() + " ha llegado al nivel máximo y se especializa en una región.");
            for (int i = 0; i < regions.size(); i++) {
                System.out.println((i + 1) + ".- " + regions.get(i).getClass().getSimpleName());
            }
            int choice = InputValidator.inputOptionQuest(1, regions.size(), "Región en la que especializarse: ");
            selected.setSpecialization(regions.get(choice - 1).getCode());
        }
    }


    private void chooseLevelSkill(Archeologist selected, int level) {
        List<Skill> available = SkillRepository.getLearnable(selected);
        if (available.isEmpty()) {
            System.out.println("No quedan habilidades disponibles para aprender.");
            return;
        }
        Collections.shuffle(available, random);
        List<Skill> choices = new ArrayList<>();
        for (Skill skill : available) {
            if (!skill.getDependencies().isEmpty()) {
                choices.add(skill);
                break;
            }
        }
        for (Skill skill : available) {
            if (choices.size() == 3) break;
            if (!choices.contains(skill)) choices.add(skill);
        }
        System.out.println("::::::::::::::::::::");
        System.out.println("El arqueólogo " + selected.getName() + " ha subido al nivel " + level + ".");
        System.out.println("Puede aprender una de estas tres habilidades:");
        for (int i = 0; i < choices.size(); i++) {
            System.out.println((i + 1) + ".- " + choices.get(i).getName() + " | " + choices.get(i).getDescription());
        }
        int option = InputValidator.inputOptionQuest(1, choices.size(), "Habilidad a aprender: ");
        selected.learnSkill(choices.get(option - 1));
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
