package commons.repositories;

import java.util.HashMap;
import java.util.Map;

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
        regions.put(region.getCode(), region);
    }

    /**
     * Busca una región registrada en el repositorio mediante su
     * código identificativo.
     * 
     * @param code El código identificativo a buscar.
     * @return La región correspondiente al código buscado.
     */
    public static Region getRegion(String code) {

        return regions.get(code);
    }
}