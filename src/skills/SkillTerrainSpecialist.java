package skills;

/**
 * @author Alberto Rodríguez Domínguez
 * Habilidad que modifica excavar un tipo de terreno.
 */
public class SkillTerrainSpecialist extends Skill{

    /**Símbolo del terreno que se va a excavar. */
    private final char terrainSymbol;
    /**Coste claculado por el motor de excavación. */
    private final int cost;

    /**
     * Crea una especialización para un tipo de terreno.
     * @param name Nombre de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param terrainSymbol Símbolo del terreno que se va a excavar.
     * @param cost Coste claculado por el motor de excavación.
     */
    public SkillTerrainSpecialist( String name, String description, char terrainSymbol, int cost){

        super(name, description, ESkillType.EXPERT);

         if (cost <= 0) {
            throw new IllegalArgumentException(
                "El coste debe ser mayor que cero."
            );
        }

        this.terrainSymbol = terrainSymbol;
        this.cost = cost;
    }

    /**  
     * Devuelve el coste ofrecido por el terreno afectado.
     * @param terrainSymbol Símbolo del terreno que se va a excavar.
     * @param baseCost Coste positivo calculado por el motor de excavación.
     * @return Coste configurado si coincide el símbolo, en otro caso coste base.
     */
    @Override
    public int getExcavationCost(char terrainSymbol, int baseCost) {
        return terrainSymbol == this.terrainSymbol ? cost : baseCost;
    }
}
