package regions;

import java.util.List;

import treasures.Treasure;

/**
 * Región de Grecia, con sus respectivos tesoros.
 * @author Javier A. Goldar Picón.
 */
public class Grecia extends Region {

    /**El código único identificativo de la región */
    private static final String CODE = "GRC";

    /**
     * Crea la región de Grecia con su catálogo de tesoros ya cargado.
     *
     * @param treasures Tesoros que se incorporan al catálogo de Grecia.
     */
    public Grecia(List<Treasure> treasures) {
        super(treasures);
    }

    /**
     * @return El código identificativo de Grecia.
     */
    @Override
    public String getCode() {
        return Grecia.CODE;
    }
}