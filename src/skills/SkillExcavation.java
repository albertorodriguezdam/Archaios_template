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
    private int cellsReveled; 
    /**Marcador que determina si el arqueólogo puede hacer un desglose de los tesoros de cada rareza que hay. */
    private boolean investigation; 

    /**
     * Constructor base de la clase SkillExcavation.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param extraActions Cantidad de acciones extra.
     * @param extraActionsPercent Cantidad de porcentaje extra, sobre la cantidad de acciones que posee el arqueólogo.
     * @param emptyCellsReveled Cantidad de celdas vacías reveladas.
     * @param cellsReveled Cantidad de celdas que puedes revelar a mano.
     * @param investigation Cantidad de tesoros de cada rareza.
     */
    public SkillExcavation(String name, String description, int extraActions, int extraActionsPercent, 
                        int emptyCellsReveled, int cellsReveled, boolean investigation){

                super(name, description, ESkillType.EXCAVATION); 
                this.extraActions=extraActions; 
                this.extraActionsPercent=extraActionsPercent; 
                this.emptyCellsReveled=emptyCellsReveled; 
                this.cellsReveled=cellsReveled; 
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
     * @param cellsReveled Cantidad de celdas con interrogación que te indican una pista.
     * @return Objeto SkillExcavation(Escaneo A, B, C o D).
     */
    public static SkillExcavation createScan(String name, String description, int cellsReveled){
        return new SkillExcavation(name, description, 0, 0, 0, cellsReveled, false); 
    }
    
    /**
     * Método estático para la creación de la habilidad Investigación.
     * @param name Nombre identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param investigation Marcador que determina si el arqueólogo puede hacer un desglose de los tesoros de cada rareza que hay.
     * @return Objeto SkillExcavation(Investigación).
     */
    public static SkillExcavation createInvestigation(String name, String description, boolean investigation){
        return new SkillExcavation(name, description, 0, 0, 0, 0, true); 
    }

    @Override 
    public int getExtraActions(){
        return extraActions; 
    }

    @Override
    public int getExtraActionsPercent(){
        return extraActionsPercent; 
    }

    @Override
    public int getEmptyCellsReveled(){
        return emptyCellsReveled; 
    }

    @Override
    public int getCellsReveled(){
        return cellsReveled; 
    }

    @Override
    public boolean hashInvetigation(){
        return false; 
    }

}
