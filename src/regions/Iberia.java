package regions;

import java.util.List;

import treasures.Treasure;

public class Iberia extends Region {
    /**El código único identificativo de la región */
    private static final String CODE = "IBR";

    /**
     * Crea la región de Iberia con su catálogo de tesoros ya cargado.
     *
     * @param treasures Los tesoros que se incorporan al catálogo de Iberia.
     */
    public Iberia(List<Treasure> treasures) {
        super(treasures);
    }

    /**    
     * @return El código identificativo de Iberia.
     */
    @Override
    public String getCode() {
        return Iberia.CODE;
    }
}
