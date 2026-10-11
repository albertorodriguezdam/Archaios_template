package regions;

public class Grecia extends Region {
    /**El código único identificativo de la región */
    private static final String CODE = "GRC";

    /**Constructor por defecto que hereda del padre */
    public Grecia() {
        super();
    }

    /**
     * Devuelve el código identificativo de Grecia.
     * 
     * @return El código identificativo de Grecia.
     */
    @Override
    public String getCode() {
        return CODE;
    }
}
