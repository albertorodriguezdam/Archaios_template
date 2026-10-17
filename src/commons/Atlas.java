package commons;

import helpers.InputValidator;
import helpers.MenuHelper;
import java.util.ArrayList;
import java.util.List;
import regions.Region;
import treasures.ETreasureRarity;
import treasures.ETreasureType;
import treasures.Treasure;

public class Atlas {
    private List<Region> regions;

    public Atlas(List<Region> regions) {
        this.regions = new ArrayList<>(regions);
        this.regions.sort(java.util.Comparator.comparing(Region::getCode));
    }

    public void show() {
        while (true) {
            int option = chooseRegion("Atlas", "Consultar región: ");
            if (option == 0) return;
            Region region = regions.get(option - 1);
            MenuHelper.subHeader(region.getClass().getSimpleName());
            if (List.of("EGP", "PTR", "SUM").contains(region.getCode())) System.out.println("Región difícil. " + "Imperio de las arenas");
            for (Treasure treasure : getTreasures(region)) {
                System.out.println("-" + treasure.getTxt() + " - " + typeAndRarity(treasure));
            }
            if (MenuHelper.returnMenu("Consultar otra región") == 0) return;
        }
    }

    public void showCatalog() {
        while (true) {
            int option = chooseRegion("Catálogo de tesoros", "Consultar tesoros de la región: ");
            if (option == 0) return;
            Region region = regions.get(option - 1);
            List<Treasure> treasures = getTreasures(region);
            if (treasures.isEmpty()) {
                System.out.println("No hay tesoros en esta región.");
                continue;
            }
            while (true) {
                MenuHelper.subHeader(region.getClass().getSimpleName());
                for (int i = 0; i < treasures.size(); i++) {
                    System.out.println((i + 1) + ".- " + treasures.get(i).getTxt());
                }
                System.out.println("0.- Volver");
                int number = InputValidator.inputOptionQuest(0, treasures.size(), "Consultar tesoro: ");
                if (number == 0) break;
                showTreasure(treasures.get(number - 1), treasures.get(number - 1).getCode());
                if (MenuHelper.returnMenu("Consultar otro tesoro") == 0) return;
            }
        }
    }

    private int chooseRegion(String title, String question) {
        MenuHelper.header(title);
        for (int i = 0; i < regions.size(); i++) {
            System.out.println((i + 1) + ".- " + regions.get(i).getClass().getSimpleName());
        }
        System.out.println("0.- Volver");
        return InputValidator.inputOptionQuest(0, regions.size(), question);
    }

    private List<Treasure> getTreasures(Region region) {
        List<Treasure> result = new ArrayList<>(region.getTreasures());
        List<ETreasureType> order = List.of(ETreasureType.COIN, ETreasureType.BEAD,
            ETreasureType.JEWELRY, ETreasureType.CERAMIC, ETreasureType.EQUIP,
            ETreasureType.STELE, ETreasureType.FRAGMENT, ETreasureType.TILE);
        result.sort((a, b) -> {
            int type = Integer.compare(order.indexOf(a.getType()), order.indexOf(b.getType()));
            return type != 0 ? type : a.getCode().compareTo(b.getCode());
        });
        return result;
    }

    private static boolean isFeminine(Treasure treasure) {
        return treasure.getType() != ETreasureType.EQUIP && treasure.getType() != ETreasureType.FRAGMENT;
    }

    public static String typeAndRarity(Treasure treasure) {
        String rarity = treasure.getRarity().toString();
        if (treasure.getRarity() == ETreasureRarity.RARE && isFeminine(treasure)) rarity = "rara";
        return treasure.getType() + " " + rarity;
    }

    public static void showTreasure(Treasure treasure, String code) {
        MenuHelper.subHeader(treasure.getTxt());
        System.out.println(treasure.getDescription());
        System.out.println("Código: " + code);
        String info = typeAndRarity(treasure);
        info = Character.toUpperCase(info.charAt(0)) + info.substring(1);
        if (treasure.getParts() > 1) {
            info += (isFeminine(treasure) ? " dividida" : " dividido") + " en " + treasure.getParts() + " partes";
        }
        System.out.println(info);
    }
}
