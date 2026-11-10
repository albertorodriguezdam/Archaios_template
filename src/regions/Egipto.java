package regions;

import java.util.List;
import java.util.Map;

import treasures.Treasure;

/**
 * Representa a Egipto, una región difícil de Imperios de las arenas.
 * Recibe su catálogo de tesoros ya cargado y proporciona sus porcentajes de terreno.
 * 
 * @author Javier A. Goldar Picón
 */
public class Egipto extends Region implements IDifficultRegion {
    
    /**El código único identificativo de la región */
    private static final String CODE = "EGP";

    /**
     * Crea la región de Egipto con su catálogo de tesoros ya cargado.
     *
     * @param treasures Los tesoros que se incorporan al catálogo de Egipto.
     */
    public Egipto(List<Treasure> treasures) {
        super(treasures);
    }

    /**    
     * @return El código identificativo de Egipto.
     */
    @Override
    public String getCode() {
        return Egipto.CODE;
    }

    /**
     * @return El nombre de la expansión de Egipto, «Imperios de las arenas».
     */
    @Override
    public String getExpansionName() {
        return "Imperios de las arenas";
    }

    /**
     * Describe el terreno de Petra con un 10 % de agua, un 35 % de tierra
     * y un 55 % de arena.
     *
     * @return El mapa inmodificable que asocia cada símbolo de terreno con su porcentaje.
     */
    @Override
    public Map<Character, Integer> getTerrainDistribution() {

        return Map.of(
                '–', 10,
                '·', 35,
                '~', 55
        );
    }
}
