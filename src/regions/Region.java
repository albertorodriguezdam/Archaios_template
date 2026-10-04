package regions;

import java.util.ArrayList;
import java.util.List;

import treasures.ETreasureRarity;
import treasures.Treasure;

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

    // TODO: Devuelve un tesoro aleatorio
    public Treasure getRandomTreasure() {
        return null;
    }

    // TODO: Devuelve los tesoros de una rareza concreta
    public List<Treasure> getTreasuresByRarity(ETreasureRarity rarity) {
        return null;
    }

    // TODO: Devuelve un tesoro aleatorio de una rareza concreta
    public Treasure getRandomTreasureByRarity(ETreasureRarity rarity) {
        return null;
    }
}