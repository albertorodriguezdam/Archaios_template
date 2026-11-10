package regions;

import java.util.List;

import treasures.Treasure;

public class Roma extends Region {
    /**El código único identificativo de la región */
    private static final String CODE = "ROM";

    /**
     * Crea la región de Roma con su catálogo de tesoros ya cargado.
     *
     * @param treasures Los tesoros que se incorporan al catálogo de Roma.
     */
    public Roma(List<Treasure> treasures) {
        super(treasures);
    }

    /**    
     * @return El código identificativo de Roma.
     */
    @Override
    public String getCode() {
        return Roma.CODE;
    }
}
