package excavation;

/**
 * Los nombres de los tamaños de terreno. Todas las reglas de cada tamaño
 * (nivel, tesoros mínimos, raros y experiencia) están en {@link ETerrain}.
 * Se mantiene de forma provisional porque el menú de excavación agrupa
 * los terrenos por tamaño.
 * @author Andrés.
 */
public enum ETerrainSize {
    SMALL("pequeño"),
    MEDIUM("mediano"),
    LARGE("grande");

    /** El nombre del tamaño. */
    private String txt;

    /**
     * Constructor básico.
     * @param txt El nombre del tamaño.
     */
    private ETerrainSize(String txt) {
        this.txt = txt;
    }

    /**
     * @return El nombre del tamaño.
     */
    @Override
    public String toString() {
        return this.txt;
    }
}
