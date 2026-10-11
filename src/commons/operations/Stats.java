package commons.operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    /**
     * Registra una nueva visita a una región, incrementando su contador.
     * Si la región no tiene visitas registradas comienza con una.
     * 
     * @param regionCode El código identificativo de la región visitada.
     */
    public void registerVisit(String regionCode) {

        int currentVisits = places.getOrDefault(regionCode, 0);
        places.put(regionCode, currentVisits + 1);
    }

    /**
     * Registra un tesoro encontrado, incrementando su contador. Si es la 
     * primera vez que se encuentra ese código, su contador comienza en uno.
     * 
     * @param treasureCode El código completo del tesoro encontrado.
     */
    public void registerTreasure(String treasureCode) {

        int currentFinds = treasures.getOrDefault(treasureCode, 0);
        treasures.put(treasureCode, currentFinds + 1);
    }

    // TODO: Las estadísticas de los tesoros encontrados
    public void getStatsTreasures() {}

    // TODO: Las estadísticas de los lugares visitados
    public void getStatsPlaces() {

        if (places.isEmpty()) {
            System.out.println("No hay excavaciones registradas");
            return;
        }

        for (String regionCode : places.keySet()) {
            int visits = places.get(regionCode);
            int totalFound = 0;

            Set<String> foundTreasureCodes = new HashSet<>();

            for (String treasureCode : treasures.keySet()) {
                if (treasureCode.startsWith(regionCode + "_")) {
                    int findCount = treasures.get(treasureCode);
                    totalFound += findCount;
                }
            }

            System.out.println("Región: " + regionCode + " | Excavaciones: " + visits + " | Tesoros encontrados: " + totalFound);
        }
    }
}
