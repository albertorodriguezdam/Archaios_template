package regions;

import java.util.List;
import java.util.Map;

import treasures.Treasure;

/**
 * Representa a Petra, una región difícil de Imperios de las arenas.
 * Recibe su catálogo de tesoros ya cargado y define su distribución
 * de piedra y arena.
 * 
 * @author Javier A. Goldar Picón
 */
public class Petra extends Region implements IDifficultRegion {
    
    /**El código único identificativo de la región */
    private static final String CODE = "PTR";

    /**
     * Crea la región de Petra con su catálogo de tesoros ya cargado.
     *
     * @param treasures Los tesoros que se incorporan al catálogo de Petra.
     */
    public Petra(List<Treasure> treasures) {
        super(treasures);
    }

    /**    
     * @return El código identificativo de Petra.
     */
    @Override
    public String getCode() {
        return Petra.CODE;
    }

    /**
     * @return El nombre de la expansión de Petra, «Imperios de las arenas».
     */
    @Override
    public String getExpansionName() {
        return "Imperios de las arenas";
    }

    /**
     * Describe el terreno de Petra con un 75% de piedra y un 25% de arena.
     *
     * @return El mapa inmodificable que asocia cada símbolo de terreno con su porcentaje.
     */
    @Override
    public Map<Character, Integer> getTerrainDistribution() {

        return Map.of(
                '^', 75,
                '~', 25
        );
    }
}
