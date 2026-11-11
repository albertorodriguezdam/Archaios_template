package commons.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regions.IDifficultRegion;
import regions.Region;

/**
 * Repositorio encargado de registrar y gestionar las regiones del sistema.
 * Permite almacenar y consultar regiones mediante su código identificativo.
 * @author Javier A. Goldar Picón.
 */
public class RegionRepository {

    /**
     * Almacena las regiones registradas en el sistema, asociando
     * el código identificativo de cada región con su objeto Region.
     * El mapa es compartido por todas las clases que accedan al repositorio.
     */
    private static final Map<String, Region> regions = new HashMap<>();

    /**
     * Registra una nueva región en el repositorio utilizando su código
     * identificativo como clave.
     * 
     * @param region La región a registrar.
     */
    public static void registerRegion(Region region) {
        RegionRepository.regions.put(region.getCode(), region);
    }

    /**
     * Busca una región registrada en el repositorio mediante su
     * código identificativo.
     * 
     * @param code El código identificativo a buscar.
     * @return La región correspondiente al código buscado.
     */
    public static Region getRegion(String code) {

        return RegionRepository.regions.get(code);
    }

    /**
     * @return Copia de las regiones registradas, ordenadas por código.
     */
    public static List<Region> getRegionsByCode() {
        List<Region> regions = new ArrayList<>(RegionRepository.regions.values());
        regions.sort(Comparator.comparing(Region::getCode));
        return regions;
    }

    /**
     * Obtiene las regiones registradas que implementan IDifficultRegion,
     * ordenadas por su código.
     *
     * @return La nueva lista de regiones difíciles, vacía si no hay ninguna registrada.
     */
    public static List<Region> getDifficultRegions() {

        List<Region> difficultRegions = new ArrayList<>();

        for (Region region : RegionRepository.getRegionsByCode()) {
            if (region instanceof IDifficultRegion) {
                difficultRegions.add(region);
            }
        }

        return difficultRegions;
    }

    /**
     * Obtiene las regiones registradas que no implementan IDifficultRegion,
     * ordenadas por su código.
     *
     * @return La nueva lista de regiones básicas, vacía si no hay ninguna.
     */
    public static List<Region> getBasicRegions() {
        
        List<Region> basicRegions = new ArrayList<>();

        for (Region region : RegionRepository.getRegionsByCode()) {
            if (!(region instanceof IDifficultRegion)) {
                basicRegions.add(region);
            }
        }

        return basicRegions;
    }
}