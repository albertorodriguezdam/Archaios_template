package regions;

import java.util.Map;

/**
 * Identifica una región difícil y permite consultar su expansión y sus terrenos.
 * @author Javier A. Goldar Picón
 */
public interface IDifficultRegion {

    /**
     * @return El nombre de la expansión a la que pertenece la región.
     */
    public String getExpansionName();

    /**
     * @return El mapa que asocia cada símbolo de terreno con su porcentaje.
     * Los porcentajes suman 100.
     */
    public Map<Character, Integer> getTerrainDistribution();
}