package commons.repositories;

import java.util.HashMap;
import java.util.Map;

import regions.Region;
import treasures.Treasure;

/**
 * Repositorio encargado de registrar y gestionar los tesoros del sistema.
 * Permite almacenar y consultar los tesoros mediante su código identificativo.
 * @author Javier A. Goldar Picón.
 */
public class TreasureRepository {

    /**
     * Almacena los tesoros registrados en el sistema, asociando
     * el código base de cada tesoro con su objeto Treasure.
     * El mapa es compartido por todas las clases que accedan al repositorio.
     */
    private static final Map<String, Treasure> treasures = new HashMap<>();

    /**
     * Registra un tesoro en el repositorio utilizando su código base
     * como clave. Si ya existe un tesoro con el mismo código,
     * sustituye el objeto registrado anteriormente.
     *
     * @param treasure El tesoro que se desea registrar.
     */
    public static void registerTreasure(Treasure treasure) {
        TreasureRepository.treasures.put(treasure.getCode(), treasure);
    }

    /**
     * Obtiene un tesoro registrado en el repositorio mediante su código base.
     *
     * @param code El código base del tesoro a buscar.
     * @return El tesoro correspondiente al código buscado.
     */
    public static Treasure getTreasure(String code) {
        
        return TreasureRepository.treasures.get(code);
    }

    /**
     * Registra en el repositorio todos los tesoros de una región.
     * Reutiliza los objetos Treasure existentes en la región.
     *
     * @param region La región cuyos tesoros se desean registrar.
     */
    public static void registerRegionTreasures(Region region) {

        for (Treasure treasure : region.getTreasures()) {
            registerTreasure(treasure);
        }
    }
}
