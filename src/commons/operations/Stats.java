package commons.operations;

import java.util.HashMap;
import java.util.Map;

/**
 * Información de las estadísticas de lugares
 * o de tesoros.
 * @author jagoldar
 */
public class Stats {
    /**El registro de veces que se ha encontrado cada tesoro */
    private Map<String, Integer> treasures;
    /**El registro de veces que se ha visitado cada región */
    private Map<String, Integer> places;

    /**Constructor por defecto */
    public Stats() {
        this.treasures = new HashMap<>();
        this.places = new HashMap<>();
    }

    // TODO: Las estadísticas de los tesoros encontrados
    public void getStatsTreasures() {}

    // TODO: Las estadísticas de los lugares visitados
    public void getStatsPlaces() {}
}
