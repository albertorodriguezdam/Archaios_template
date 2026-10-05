package skills;

/**
 * @author Alberto Rodríguez Domínguez
 * SkillTesoros
 */

public class SkillTreasures extends Skill{

    /**Cantidad aleatoria extra. */
    private int extraRandom; 
    /**Cantidad común extra. */
    private int extraComun; 
    /**Cantidad infrecuente extra. */
    private int extraInfrecuente; 
    /**Cantidad rara extra. */
    private int extraRaro; 
    /**Cantidad de probabilidad infrecuente extra. */
    private int extraInfrecuenteProb; 
    /**Cantidad de probabilidad rara extra. */
    private int extraRaroProb; 

    /**
     * Constructor base de la clase SkillTesoros.
     * @param nombre El nombre identificativo de la habilidad.
     * @param descripcion Descripción de lo que hace la habilidad.
     * @param extraRandom Cantidad aleatoria extra.
     * @param extraComun Cantidad común extra.
     * @param extraInfrecuente antidad infrecuente extra.
     * @param extraRaro Cantidad rara extra.
     * @param extraInfrecuenteProb Cantidad de probabilidad infrecuente extra.
     * @param extraRaroProb Cantidad de probabilidad rara extra.
     */
    public SkillTreasures(String nombre, String descripcion, int extraRandom, int extraComun, 
                        int extraInfrecuente, int extraRaro, int extraInfrecuenteProb, int extraRaroProb){

            super(nombre, descripcion, ESkillType.TREASURE); 
            this.extraRandom=extraRandom; 
            this.extraComun=extraComun; 
            this.extraInfrecuente=extraInfrecuente; 
            this.extraRaro=extraRaro; 
            this.extraInfrecuenteProb=extraInfrecuenteProb; 
            this.extraRaroProb=extraRaroProb; 
    }

    /**
     * Constructor para habilidades que solo otorgan tesoros aleatorios (Extra I, II, III).
     */
    public SkillTreasures(String nombre, String descripcion, int extraRandom) {
        this(nombre, descripcion, extraRandom, 0, 0, 0, 0, 0);
    }

    /**
     * Constructor para habilidades de rareza fija (Aprendiz, Experto, Maestro).
     */
    public SkillTreasures(String nombre, String descripcion, int extraComun, int extraInfrecuente, int extraRaro) {
        this(nombre, descripcion, 0, extraComun, extraInfrecuente, extraRaro, 0, 0);
    }

    /**
     * Constructor para habilidades de incremento de probabilidad (Especialistas).
     */
    public SkillTreasures(String nombre, String descripcion, int extraInfrecuenteProb, int extraRaroProb) {
        this(nombre, descripcion, 0, 0, 0, 0, extraInfrecuenteProb, extraRaroProb);
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
        return extraComun; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros infrecuentes que se deben generar en el tablero.
     * @return catidad de tesoros infrecuentes extra.
     */
    @Override 
    public int getExtraUncommonTreasures(){
        return extraInfrecuente; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros raros que se deben generar en el tablero.
     * @return catidad de tesoros raros extra.
     */
    @Override 
    public int getExtraRareTreasures(){
        return extraRaro; 
    }
    
    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de tipo infrecuente.
     * @return incremento de la probabilidad.
     */
    @Override 
    public int getExtraUncommonProb(){
        return extraInfrecuenteProb; 
    }

    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de tipo raro.
     * @return incremento de la probabilidad.
     */
    @Override 
    public int getExtraRareProb(){
        return extraRaroProb; 
    }
}
