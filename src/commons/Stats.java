package commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.repositories.RegionRepository;
import helpers.UtilsHelper;
import regions.Region;
import treasures.Treasure;

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

    /**
     * Muestra por consola las estadísticas de cada región visitada,
     * incluyendo el número de visitas, las piezas diferentes encontradas,
     * el total de piezas disponibles, el porcentaje de completitud y el
     * número total de hallazgos.
     * 
     * Las piezas diferentes se identifican mediante su código completo,
     * mientras que el total de hallazgos incluye las piezas repetidas.
     */
    public void getStatsPlaces() {

        if (places.isEmpty()) { 
            System.out.println("No hay excavaciones registradas");
            return;
        }

        for (String regionCode : places.keySet()) { 
            int visits = places.get(regionCode);
            int totalFound = 0; 

            List<String> foundTreasureCodes = new ArrayList<>();   

            for (String treasureCode : treasures.keySet()) { 
                if (treasureCode.startsWith(regionCode + "_")) {
                    int findCount = treasures.get(treasureCode); 
                    totalFound += findCount; 
                    foundTreasureCodes.add(treasureCode); 
                }
            }

            int distinctFound = foundTreasureCodes.size(); 
            
            Region currentRegion = RegionRepository.getRegion(regionCode); 
            if (currentRegion == null) { 
                System.out.println("No se ha encontrado la región cuyo código es: " + regionCode);
                continue;
            }

            int totalAvailable = 0;
            for (Treasure treasure : currentRegion.getTreasures()) { 
                totalAvailable += treasure.getParts(); 
            }
            
            int completionPercent = UtilsHelper.getPercent(distinctFound, totalAvailable);

            System.out.println("---------- " + regionCode + " ----------");
            System.out.println("Veces visitada: " + visits);
            System.out.println("Tesoros encontrados: " + distinctFound + "/" + totalAvailable + " (" + completionPercent + "%)");
            System.out.println("Total de tesoros excavados: " + totalFound);
        }
    }
}
