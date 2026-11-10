package regions;

import java.util.List;
import java.util.Map;

import treasures.Treasure;

/**
 * Representa a Sumeria, una región difícil de Imperios de las arenas.
 * Recibe su catálogo de tesoros ya cargado y define su distribución
 * de piedra, arena y tierra.
 * 
 * @author Javier A. Goldar Picón
 */
public class Sumeria extends Region implements IDifficultRegion {
    
    /**El código único identificativo de la región */
    private static final String CODE = "SUM";

    /**
     * Crea la región de Sumeria con su catálogo de tesoros ya cargado.
     *
     * @param treasures Los tesoros que se incorporan al catálogo de Sumeria.
     */
    public Sumeria(List<Treasure> treasures) {
        super(treasures);
    }

    /**    
     * @return El código identificativo de Sumeria.
     */
    @Override
    public String getCode() {
        return Sumeria.CODE;
    }

    /**
     * @return El nombre de la expansión de Sumeria, «Imperios de las arenas».
     */
    @Override
    public String getExpansionName() {
        return "Imperios de las arenas";
    }

    /**
     * Describe el terreno de Sumeria con un 75% de piedra y un 25% de arena.
     *
     * @return El mapa inmodificable que asocia cada símbolo de terreno con su porcentaje.
     */
    @Override
    public Map<Character, Integer> getTerrainDistribution() {

        return Map.of(
                '^', 30,
                '~', 50,
                '·', 20
        );
    }
}
