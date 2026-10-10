package regions;

import java.util.ArrayList;
import java.util.List;

import treasures.ETreasureRarity;
import treasures.Treasure;
import helpers.UtilsHelper;

/**
 * Clase base para todas las regiones
 * @author jagoldar
 */
public abstract class Region {
    /**El código identificativo de la región */
    protected static String code;
    /**La lista de tesoros que sen encuentran en una región */
    protected List<Treasure> treasureList;

    /**Constructor por defecto */
    public Region() {
        this.treasureList = new ArrayList<>();
    }

    /**
     * @return El código identificativo de la región
     */
    public static String getCode() {
        return code;
    }

    /**
     * @param code El código identificativo de la región
     */
    public static void setCode(String code) {
        Region.code = code;
    }

    /**
     * @return La lista de tesoros que sen encuentran en una región
     */
    public List<Treasure> getTreasures() {
        return this.treasureList;
    }

    /**
     * Obtiene un tesoro de forma aleatoria de la lista de 
     * tesoros disponibles en la región
     * 
     * @return El tesoro ubicado en la posición obtenida.
     */
    public Treasure getRandomTreasure() {
        
        if (treasureList == null || treasureList.isEmpty()) {
            return null;
        }
        
        List<Integer> randomIdx = UtilsHelper.genRandomIdxs(1, treasureList.size());
        int index = randomIdx.get(0);

        return treasureList.get(index);
    }

    /**
     * Obtiene una lista con los tesoros de la región que coinciden
     * con la rareza especificada.
     * 
     * @param rarity La rareza de los tesoros a buscar
     * @return La lista de tesoros que coinciden con la rareza especificada
     */
    public List<Treasure> getTreasuresByRarity(ETreasureRarity rarity) {
        
        ArrayList<Treasure> rarityTreasuresList = new ArrayList<>();

        for (Treasure t : treasureList) {
            if (t.getRarity() == rarity) {
                rarityTreasuresList.add(t);
            }
        }

        return rarityTreasuresList;
    }

    /**
     * Obtiene un tesoro de forma aleatoria entre aquellos que 
     * coinciden con una rareza especificada.
     * 
     * @param rarity La rareza del tesoro a buscar
     * @return El tesoro aleatorio que coincide con la rareza especificada
     */
    public Treasure getRandomTreasureByRarity(ETreasureRarity rarity) {
        
        List<Treasure> filteredList = getTreasuresByRarity(rarity);

        if (filteredList.isEmpty()) {
            return null;
        }

        List<Integer> randomIdx = UtilsHelper.genRandomIdxs(1, filteredList.size());
        int index = randomIdx.get(0);
        
        return filteredList.get(index);
    }
}