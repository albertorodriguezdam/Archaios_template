package commons.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import regions.IDifficultRegion;
import regions.Region;
import repositories.Repositorio;

/**
 * Repositorio encargado de registrar y gestionar las regiones del sistema.
 * Conserva las consultas estáticas y utiliza el almacenamiento de Terra.
 * @author Javier A. Goldar Picón.
 */
public class RegionRepository extends Repositorio<Region> {

    /** Instancia que almacena las regiones compartidas por el sistema. */
    private static final RegionRepository INSTANCE = new RegionRepository();

    /**
     * La carga y validación de los catálogos se realiza en Archaios.preInit().
     * Las regiones se incorporan después mediante registerRegion().
     * Este método no realiza una segunda carga ni modifica los registros.
     */
    @Override
    public void populate() {
        // La inicialización está coordinada por Archaios.preInit().
    }

    /**
     * Registra una región utilizando su código identificativo como clave.
     * Si el código ya existe, sustituye su referencia como en la versión anterior.
     * @param region La región a registrar.
     */
    public static void registerRegion(Region region) {
        RegionRepository.INSTANCE.register(region);
    }

    /**
     * Busca una región registrada mediante su código.
     * @param code El código identificativo a buscar.
     * @return La región correspondiente o null si no está registrada.
     */
    public static Region getRegion(String code) {
        return RegionRepository.INSTANCE.getElement(code);
    }

    /**
     * @return Copia de las regiones registradas, ordenadas por código.
     */
    public static List<Region> getRegionsByCode() {
        List<Region> regions = RegionRepository.INSTANCE.getAllElements();
        regions.sort(Comparator.comparing(Region::getCode));
        return regions;
    }

    /**
     * Obtiene las regiones que implementan IDifficultRegion, ordenadas por código.
     * @return Una nueva lista con las regiones difíciles.
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
     * Obtiene las regiones que no implementan IDifficultRegion, ordenadas por código.
     * @return Una nueva lista con las regiones básicas.
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