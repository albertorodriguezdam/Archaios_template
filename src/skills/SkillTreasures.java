package skills;

/**
 * @author Alberto Rodríguez Domínguez
 * SkillTesoros
 */

public class SkillTreasures extends Skill{

    /**Cantidad aleatoria extra. */
    private int extraRandom; 
    /**Cantidad común extra. */
    private int extraCommon; 
    /**Cantidad infrecuente extra. */
    private int extraUncommon; 
    /**Cantidad rara extra. */
    private int extraRare; 
    /**Cantidad de probabilidad infrecuente extra. */
    private int extraUncommonProb; 
    /**Cantidad de probabilidad rara extra. */
    private int extraRareProb; 

    /**
     * Constructor base de la clase SkillTesoros.
     * @param name El nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraRandom Cantidad aleatoria extra.
     * @param extraCommon Cantidad común extra.
     * @param extraUncommon Cantidad infrecuente extra.
     * @param extraRare Cantidad rara extra.
     * @param extraUncommonProb Cantidad de probabilidad infrecuente extra.
     * @param extraRareProb Cantidad de probabilidad rara extra.
     */
    public SkillTreasures(String name, String description, int extraRandom, int extraCommon, 
                        int extraUncommon, int extraRare, int extraUncommonProb, int extraRareProb){

            super(name, description, ESkillType.TREASURE); 
            this.extraRandom=extraRandom; 
            this.extraCommon=extraCommon; 
            this.extraUncommon=extraUncommon; 
            this.extraRare=extraRare; 
            this.extraUncommonProb=extraUncommonProb; 
            this.extraRareProb=extraRareProb; 
    }

    /**
     * Constructor para habilidades que solo otorgan tesoros aleatorios (Extra I, II, III).
     * @param name El nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraRandom Cantidad aleatoria extra.
     */
    public SkillTreasures(String name, String description, int extraRandom) {
        this(name, description, extraRandom, 0, 0, 0, 0, 0);
    }

    /**
     * Constructor para habilidades de rareza fija (Aprendiz, Experto, Maestro).
     * @param name El nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraCommon Cantidad común extra.
     * @param extraUncommon Cantidad infrecuente extra.
     * @param extraRare Cantidad rara extra.
     */
    public SkillTreasures(String name, String description, int extraCommon, int extraUncommon, int extraRare) {
        this(name, description, 0, extraCommon, extraUncommon, extraRare, 0, 0);
    }

    /**
     * Constructor para habilidades de incremento de probabilidad.
     * @param name El nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraUncommonProb Cantidad infrecuente extra.
     * @param extraRareProb Cantidad rara extra.
     */
    public SkillTreasures(String name, String description, int extraUncommonProb, int extraRareProb) {
        this(name, description, 0, 0, 0, 0, extraUncommonProb, extraRareProb);
    }

    /**
     * Devuelve la cantidad adicional de tesoros aleatorios que se deben generar en el tablero.
     * @return catidad de tesoros aleatorios extra.
     */
    @Override 
    public int getExtraRandomTreasures(){
        return extraRandom; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros comunes que se deben generar en el tablero.
     * @return catidad de tesoros comunes extra.
     */
    @Override 
    public int getExtraCommonTreasures(){
        return extraCommon; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros infrecuentes que se deben generar en el tablero.
     * @return catidad de tesoros infrecuentes extra.
     */
    @Override 
    public int getExtraUncommonTreasures(){
        return extraUncommon; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros raros que se deben generar en el tablero.
     * @return catidad de tesoros raros extra.
     */
    @Override 
    public int getExtraRareTreasures(){
        return extraRare; 
    }
    
    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de tipo infrecuente.
     * @return incremento de la probabilidad de tesoros infrecuentes.
     */
    @Override 
    public int getExtraUncommonProb(){
        return extraUncommonProb; 
    }

    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de tipo raro.
     * @return incremento de la probabilidad de tesoros raros.
     */
    @Override 
    public int getExtraRareProb(){
        return extraRareProb; 
    }
}
