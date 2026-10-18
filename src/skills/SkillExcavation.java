package skills;

/**
 * @author Alberto Rodríguez Domínguez
 * SkillExcavation
 */
public class SkillExcavation extends Skill{

    /**Cantidad de acciones extra. */
    private int extraActions; 
    /**Cantidad de porcentaje extra, sobre la cantidad de acciones que ya posees. */
    private int extraActionsPercent; 
    /**Cantidad de celdas vacías reveladas. */
    private int emptyCellsReveled; 
    /**Cantidad de celdas con interrogación que te indican una pista. */
    private int scanHints; 
    /**Marcador que determina si el arqueólogo puede hacer un desglose de los tesoros de cada rareza que hay. */
    private boolean investigation; 

    /**
     * Constructor base de la clase SkillExcavation.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraActions Cantidad de acciones extra.
     * @param extraActionsPercent Cantidad de porcentaje extra, sobre la cantidad de acciones que posee el arqueólogo.
     * @param emptyCellsReveled Cantidad de celdas vacías reveladas.
     * @param scanHints Cantidad de celdas con interrogación que te indican una pista.
     * @param investigation Indica si habilita el desglose de tesoros por rareza.
     */
    public SkillExcavation(String name, String description, int extraActions, int extraActionsPercent, 
                        int emptyCellsReveled, int scanHints, boolean investigation){

                super(name, description, ESkillType.EXCAVATION); 
                this.extraActions=extraActions; 
                this.extraActionsPercent=extraActionsPercent; 
                this.emptyCellsReveled=emptyCellsReveled; 
                this.scanHints=scanHints; 
                this.investigation=investigation; 
    }

    /**
     * Método etático para la creación de las habilidades Excavación I,II o III.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraActions Cantidad de acciones extra.
     * @return Objeto SkillExcavation(Excavación I,II o III).
     */
    public static SkillExcavation createExcavation(String name, String description, int extraActions){
        return new SkillExcavation(name, description, extraActions, 0, 0, 0, false); 
    }

    /**
     * Método estático para la creación de las habilidades Experiencia I y II.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraActionsPercent  Cantidad de porcentaje extra, sobre la cantidad de acciones que posee el arqueólogo.
     * @return Objeto SkillExcavation(Experiencia I o II).
     */
    public static SkillExcavation createExperience(String name, String description, int extraActionsPercent){
        return new SkillExcavation(name, description, 0, extraActionsPercent, 0, 0, false); 
    }

    /**
     * Método estático para la creación de las habilidades Limpieza I y II.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param emptyCellsReveled Cantidad de celdas vacías reveladas.
     * @return Objeto SkillExcavation(Limpieza I o II).
     */
    public static SkillExcavation createCleaning(String name, String description, int emptyCellsReveled){
        return new SkillExcavation(name, description, 0, 0, emptyCellsReveled, 0, false); 
    }

    /**
     * Método estático para la creación de las habilidades Escaneo A, B, C y D. 
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param scanHints Cantidad de posiciones que se marcarán como pistas.
     * @return Objeto SkillExcavation(Escaneo A, B, C o D).
     */
    public static SkillExcavation createScan(String name, String description, int scanHints){
        return new SkillExcavation(name, description, 0, 0, 0, scanHints, false); 
    }
    
    /**
     * Método estático para la creación de la habilidad Investigación.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param investigation Marcador que determina si el arqueólogo puede hacer un desglose de los tesoros de cada rareza que hay.
     * @return Objeto SkillExcavation(Investigación).
     */
    public static SkillExcavation createInvestigation(String name, String description, boolean investigation){
        return new SkillExcavation(name, description, 0, 0, 0, 0, investigation); 
    }

    /**
     * Devuelve el número de acciones de excavación a mayores de las ya otorgadas.
     * @return Cantidad de acciones extra.
     */
    @Override 
    public int getExtraActions(){
        return extraActions; 
    }

    /**
     * Devuelve el porcentaje incrementado sobre las acciones de excavación base.
     * @return Cantidad de porcentaje extra, sobre la cantidad de acciones que posee el arqueólogo.
     */
    @Override
    public int getExtraActionsPercent(){
        return extraActionsPercent; 
    }

    /**
     * Devuelve la cantidad de celdas vacías en la rejilla que se descubren automáticamente al inicio de la excavación.
     * @return Cantidad de celdas vacías reveladas.
     */
    @Override
    public int getEmptyCellsReveled(){
        return emptyCellsReveled; 
    }

    /**
     * Devuelve el número de celdas marcados como pistas de escaneo con el símbolo: "?".
     * @return Cantidad de celdas con interrogación que te indican una pista.
     */
    @Override
    public int getScanHints(){
        return scanHints; 
    }

    /**
     * Indica si la habilidad puede consultar el desglose de tesoros y rarezas en cada ronda de exacavación.
     * @return Marcador que determina si el arqueólogo puede hacer un desglose de los tesoros de cada rareza que hay.
     */
    @Override
    public boolean hasInvestigation(){
        return this.investigation;  
    }

}
