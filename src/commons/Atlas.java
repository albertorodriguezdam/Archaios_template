package commons;

import helpers.InputValidator;
import helpers.MenuHelper;
import java.util.ArrayList;
import java.util.List;
import regions.Region;
import treasures.ETreasureRarity;
import treasures.ETreasureType;
import treasures.Treasure;

/** 
 * Clase encargada de mostrar la información de las regiones y las fichas de sus tesoros.
 * @author Constantin Laurentiu.
 */
public class Atlas {
    private List<Region> regions;

    /**
     * Constructor básico de Atlas.
     * @param regions Lista de regiones.
     */
    public Atlas(List<Region> regions) {
        this.regions = new ArrayList<>(regions);
        this.regions.sort(java.util.Comparator.comparing(Region::getCode));
    }

    /**
     * Consulta una región del Atlas y permite elegir otra al terminar.
     */
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

    /**
     * Primero se elige una región y después el tesoro cuya ficha se quiere ver.
     */
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

   
    /**
     * Muestra las regiones disponibles y solicita una opción al usuario.
     * @param title Título de la pantalla.
     * @param question Mensaje para solicitar la selección.
     * @return Número de la región seleccionada, comenzando en 1; 0 para volver.
     */
    private int chooseRegion(String title, String question) {
        MenuHelper.header(title);
        for (int i = 0; i < regions.size(); i++) {
            System.out.println((i + 1) + ".- " + regions.get(i).getClass().getSimpleName());
        }
        System.out.println("0.- Volver");
        return InputValidator.inputOptionQuest(0, regions.size(), question);
    }

    /**
     * Obtiene una copia de los tesoros de la región, ordenada por tipo
     * según el orden del catálogo y después por código.
     * @param region Región cuyos tesoros se consultan.
     * @return Nueva lista ordenada de tesoros.
     */
    private List<Treasure> getTreasures(Region region) {
        List<Treasure> result = new ArrayList<>(region.getTreasures());
        // Este es el orden de tipos que se utiliza en la pantalla del catálogo.
        List<ETreasureType> order = List.of(ETreasureType.COIN, ETreasureType.BEAD,
            ETreasureType.JEWELRY, ETreasureType.CERAMIC, ETreasureType.EQUIP,
            ETreasureType.STELE, ETreasureType.FRAGMENT, ETreasureType.TILE);
        result.sort((a, b) -> {
            int type = Integer.compare(order.indexOf(a.getType()), order.indexOf(b.getType()));
            return type != 0 ? type : a.getCode().compareTo(b.getCode());
        });
        return result;
    }

    /**
     * Indica si el tipo de tesoro utiliza concordancia femenina.
     * @param treasure Tesoro cuyo tipo se comprueba.
     * @return true si utiliza concordancia femenina; false si es equipo o fragmento.
     */
    private static boolean isFeminine(Treasure treasure) {
        return treasure.getType() != ETreasureType.EQUIP && treasure.getType() != ETreasureType.FRAGMENT;
    }

  
    /**
     * Genera el texto del tipo y la rareza del tesoro,
     * ajustando el género de la rareza cuando corresponde.
     * @param treasure Tesoro cuya información se obtiene.
     * @return Tipo y rareza separados por un espacio, por ejemplo "cerámica rara".
     */
    public static String typeAndRarity(Treasure treasure) {
        String rarity = treasure.getRarity().toString();
        if (treasure.getRarity() == ETreasureRarity.RARE && isFeminine(treasure)) rarity = "rara";
        return treasure.getType() + " " + rarity;
    }


    /**
     * Muestra la ficha del tesoro con su nombre, descripción, código,
     * tipo y rareza. Incluye el número de partes si tiene más de una.
     *
     * @param treasure Tesoro cuya ficha se muestra.
     * @param code Código que se mostrará, base del tesoro o completo de una pieza.
     */
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
