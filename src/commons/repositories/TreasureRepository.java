package commons.repositories;

import helpers.CodeHelper;
import regions.Region;
import treasures.Treasure;

/**
 * Repositorio encargado de registrar y gestionar los tesoros del sistema.
 * Permite almacenar y consultar los tesoros mediante su código identificativo.
 * @author Javier A. Goldar Picón.
 */
public class TreasureRepository {

    /**
     * Busca un tesoro a partir de su código base o completo.
     * Extrae el código de la región, la recupera del repositorio
     * y busca el tesoro en su lista de tesoros.
     *
     * @param code El código base o completo del tesoro a buscar.
     * @return El objeto Treasure encontrado o null si la región o el tesoro no existen.
     */
    public static Treasure getTreasure(String code) {
        
        String baseCode = CodeHelper.getFullTreasure(code);
        String regionCode = CodeHelper.getRegion(baseCode);
        Region region = RegionRepository.getRegion(regionCode);

        if (region == null) {
            return null;
        }

        for (Treasure treasure : region.getTreasures()) {
            if (treasure.getCode().equals(baseCode)) {
                return treasure;
            }
        }
        
        return null;
    }
}