package commons;

import commons.repositories.RegionRepository;
import commons.repositories.TreasureRepository;
import helpers.CodeHelper;
import helpers.UtilsHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import regions.Region;
import treasures.ETreasureType;
import treasures.Treasure;

/**
 * Información de las estadísticas de lugares o de tesoros.
 * @author Javier A. Goldar Picón.
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

    /**
     * Muestra por consola los tesoros encontrados, agrupados por región
     * y ordenados alfabéticamente según su código identificativo.
     *
     * Obtiene las regiones a partir de los códigos de los tesoros registrados
     * y recupera la información de cada tesoro mediante TreasureRepository.
     *
     * Los tesoros divididos en varias partes se muestran con su nombre,
     * seguido de los códigos completos de las piezas encontradas y sus
     * respectivas cantidades. Los tesoros de una sola parte se muestran
     * directamente con su nombre y el número de veces que se han encontrado.
     */
    public void showTreasuresByRegion() {

        if (treasures.isEmpty()) { 
            System.out.println("No has encontrado ningún tesoro.");
            return;
        }
        Set<String> regionCodes = new TreeSet<>();

        for (String treasureCode : treasures.keySet()) {
            String regionCode = CodeHelper.getRegion(treasureCode);
            regionCodes.add(regionCode);
        }

        for (String regionCode : regionCodes) {
            
            helpers.MenuHelper.subHeader(RegionRepository.getRegion(regionCode).getClass().getSimpleName());

            String previousBaseCode = "";

            for (String treasureCode : new TreeSet<>(treasures.keySet())) {
                if (treasureCode.startsWith(regionCode + "_")) {
                    String baseCode = CodeHelper.getFullTreasure(treasureCode);
                    Treasure currentTreasure = TreasureRepository.getTreasure(baseCode);

                    if (currentTreasure == null) { 
                        System.out.println("No se ha encontrado el tesoro cuyo código es: " + baseCode);
                        continue;
                    }

                    if (!baseCode.equals(previousBaseCode)) {
                        previousBaseCode = baseCode;

                        if (currentTreasure.getParts() > 1) {
                            System.out.println("-" + currentTreasure.getTxt() + ":");
                        }
                    }

                    if (currentTreasure.getParts() > 1) {
                        int findCount = treasures.get(treasureCode);
                        System.out.println("    " + treasureCode + ": " + findCount);
                    } else {
                        int findCount = treasures.get(treasureCode);
                        System.out.println("-" + currentTreasure.getTxt() + ": " + findCount);
                    }
                }
            }
        }
    }

    /**
     * Muestra por consola las estadísticas de los tesoros encontrados,
     * agrupándolos por tipo y ordenándolos alfabéticamente según su código.
     *
     * Identifica los tipos mediante CodeHelper y recupera los objetos
     * Treasure correspondientes desde TreasureRepository.
     *
     * Los tesoros divididos en varias partes se muestran con su nombre,
     * seguido de los códigos completos de las piezas encontradas y sus
     * respectivas cantidades. Los tesoros de una sola parte se muestran
     * directamente con su nombre y número de hallazgos.
     */
    public void showTreasuresByType() {

        if (treasures.isEmpty()) { 
            System.out.println("No has encontrado ningún tesoro.");
            return;
        }
        Set<String> typeCodes = new TreeSet<>();

        for (String treasureCode : treasures.keySet()) {
            String typeCode = CodeHelper.getType(treasureCode);
            typeCodes.add(typeCode);
        }

        for (String typeCode : typeCodes) {

            String typeName = typeCode;

            for (ETreasureType treasureType : ETreasureType.values()) {
                if (treasureType.getCode().equals(typeCode)) {
                    typeName = UtilsHelper.ucfirst(treasureType.toString());
                    break;
                }
            }

            
            helpers.MenuHelper.subHeader(typeName);

            String previousBaseCode = "";

            for (String treasureCode : new TreeSet<>(treasures.keySet())) {
                if (CodeHelper.getType(treasureCode).equals(typeCode)) {
                    String baseCode = CodeHelper.getFullTreasure(treasureCode);
                    Treasure currentTreasure = TreasureRepository.getTreasure(baseCode);

                    if (currentTreasure == null) {
                        System.out.println("No se ha encontrado el tesoro cuyo código es: " + baseCode);
                        continue;
                    }

                    if (!baseCode.equals(previousBaseCode)) {
                        previousBaseCode = baseCode;

                        if (currentTreasure.getParts() > 1) {
                            System.out.println("-" + currentTreasure.getTxt() + ":");
                        }
                    }

                    if (currentTreasure.getParts() > 1) {
                        int findCount = treasures.get(treasureCode);
                        System.out.println("    " + treasureCode + ": " + findCount);
                    } else {
                        int findCount = treasures.get(treasureCode);
                        System.out.println("-" + currentTreasure.getTxt() + ": " + findCount);
                    }
                }
            }
        }
    }

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
            System.out.println("No hay estadísticas de ninguna región.");
            return;
        }

        for (String regionCode : places.keySet()) { 
            int visits = places.get(regionCode);
            int totalFound = 0;
            int distinctFound = 0;

            for (String treasureCode : treasures.keySet()) { 
                if (treasureCode.startsWith(regionCode + "_")) {
                    int findCount = treasures.get(treasureCode); 
                    totalFound += findCount; 
                    distinctFound++;
                }
            }
            
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

            
            helpers.MenuHelper.subHeader(RegionRepository.getRegion(regionCode).getClass().getSimpleName());
            System.out.println("Veces visitada: " + visits);
            System.out.println("Tesoros encontrados: " + distinctFound + "/" + totalAvailable + " (" + completionPercent + "%)");
            System.out.println("Total de tesoros excavados: " + totalFound);
        }
    }
}
