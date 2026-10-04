package treasures;

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
