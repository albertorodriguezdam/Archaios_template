package treasures;

import java.util.List;
import java.lang.StringBuilder;

import helpers.UtilsHelper;

/**
 * La base del tesoro
 * @author jagoldar
 */
public class Treasure {
    /**El código del tesoro */
    private String code;
    /**El nombre del tesoro */
    private String txt;
    /**La descripción del tesoro */
    private String description;
    /**La rareza del tesoro */
    private ETreasureRarity rarity;
    /**El tipo de tesoro */
    private ETreasureType type;
    /**El número de partes de un tesoro */
    private int parts;
    /**Los puntos que se otorgan al encontrar partes de un tesoro*/
    private int points;

    /**
     * Constructor básico
     * @param code El código del tesoro
     * @param txt El nombre del tesoro
     * @param description La descripción del tesoro
     * @param rarity La rareza del tesoro
     * @param type El tipo de tesoro
     * @param parts El número de partes de un tesoro
     * @param points Los puntos que se otorgan al encontrar partes de un tesoro
     */
    public Treasure(String code, String txt, String description, ETreasureRarity rarity, ETreasureType type, int parts, int points) {
        this.code = code;
        this.txt = txt;
        this.description = description;
        this.rarity = rarity;
        this.type = type;
        this.parts = parts;
        this.points = points;
    }

    /**
     * @return El código del tesoro
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code El código del tesoro
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return El nombre del tesoro
     */
    public String getTxt() {
        return this.txt;
    }

    /**
     * @param txt El nombre del tesoro
     */
    public void setTxt(String txt) {
        this.txt = txt;
    }

    /**
     * @return La descripción del tesoro
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description La descripción del tesoro
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return La rareza del tesoro
     */
    public ETreasureRarity getRarity() {
        return this.rarity;
    }

    /**
     * @param rarity La rareza del tesoro
     */
    public void setRarity(ETreasureRarity rarity) {
        this.rarity = rarity;
    }

    /**
     * @return El tipo de tesoro
     */
    public ETreasureType getType() {
        return this.type;
    }

    /**
     * @param type El tipo de tesoro
     */
    public void setType(ETreasureType type) {
        this.type = type;
    }

    /**
     * @return El número de partes de un tesoro
     */
    public int getParts() {
        return this.parts;
    }

    /**
     * @param parts El número de partes de un tesoro
     */
    public void setParts(int parts) {
        this.parts = parts;
    }

    /**
     * @return Los puntos que se otorgan al encontrar partes de un tesoro
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * @param points Los puntos que se otorgan al encontrar partes de un tesoro
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Genera el código completo de una parte aleatoria del tesoro,
     * añadiendo al código base el número de parte con cuatro dígitos.
     * 
     * @return El código completo de la parte generada o null si el tesoro no tiene partes
     */
    public String getRandomPartCode() {

        if (parts <= 0) {
            return null;
        }

        List<Integer> randomIdx = UtilsHelper.genRandomIdxs(1, parts);
        int partNumber = randomIdx.get(0) + 1;

        String partNumberText = Integer.toString(partNumber);
        int necessaryZeros = 4 - partNumberText.length();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < necessaryZeros; i++) {
            sb.append("0");
        }
        sb.append(partNumberText);

        return getCode() + "_" + sb.toString();
    }

    @Override 
    public String toString() {
        return "Código: " + this.code + "\n" +
               "Nombre: " + this.txt + "\n" +
               "Descripción: " + this.description + "\n" +
               "Rareza: " + this.rarity + "\n" +
               "Tipo: " + this.type + "\n" +
               "Parte: " + this.parts + "\n" +
               "Puntos: " + this.points;
    }
}
