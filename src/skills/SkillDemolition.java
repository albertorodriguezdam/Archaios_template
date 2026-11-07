package skills;

/**
 * @author Alberto Rodríguez Domínguez
 * Habilidad que convierte una cantidad de casillas de un terreno a tierra.
 */
public class SkillDemolition extends Skill{

    /**Símbolo del terreno que se quiere transformar. */
    private final char terrainSymbol;
    /**Número positivo de conversiones que aporta. */
    private final int conversions;

    /**
     * Crea una habilidad de conversión de terreno.
     * @param name Nombre de la habilidad.
     * @param description Descripción de lo que hace la habilidad.
     * @param terrainSymbol Símbolo del terreno que se quiere transformar.
     * @param conversions Número positivo de conversiones que aporta.
     */
    public SkillDemolition(String name, String description, char terrainSymbol, int conversions){
        
        super(name, description, ESkillType.EXPERT);

        if (conversions <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de conversiones debe ser mayor que cero."
            );
        }

        this.terrainSymbol = terrainSymbol;
        this.conversions = conversions;
    }

    /**    
     * Devuelve las conversiones aportadas para un terreno.
     * @param terrainSymbol Símbolo del terreno que se quiere transformar.
     * @return Cantidad configurada si coincide el símbolo; 0 en otro caso.
     */
    @Override
    public int getTerrainConversions(char terrainSymbol) {
        return terrainSymbol == this.terrainSymbol ? conversions : 0;
    }

}
