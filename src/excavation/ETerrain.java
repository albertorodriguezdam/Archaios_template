package excavation;

/**
 * Los tipos de terreno de excavación: tres tamaños con cinco formas cada uno.
 * Cada terreno guarda sus medidas y las reglas que dependen de su tamaño
 * (nivel necesario, tesoros mínimos, si permite raros y la experiencia extra).
 * Las medidas siguen el formato del enunciado: columnas x filas (por ejemplo,
 * "Horizontal: 10x2" son 10 columnas y 2 filas).
 * @author Andrés.
 */
public enum ETerrain {
    // tamaño, forma, columnas, filas, nivel, comunes, infrecuentes, raros, permite raros, xp extra
    SMALL_ROW(ETerrainSize.SMALL, "fila", 5, 3, 0, 1, 0, 0, false, 2),
    SMALL_HORIZONTAL(ETerrainSize.SMALL, "horizontal", 10, 2, 0, 1, 0, 0, false, 2),
    SMALL_COLUMN(ETerrainSize.SMALL, "columna", 3, 5, 0, 1, 0, 0, false, 2),
    SMALL_VERTICAL(ETerrainSize.SMALL, "vertical", 2, 10, 0, 1, 0, 0, false, 2),
    SMALL_SQUARE(ETerrainSize.SMALL, "cuadrado", 5, 5, 0, 1, 0, 0, false, 2),

    MEDIUM_ROW(ETerrainSize.MEDIUM, "fila", 20, 5, 2, 3, 1, 0, true, 5),
    MEDIUM_HORIZONTAL(ETerrainSize.MEDIUM, "horizontal", 30, 3, 2, 3, 1, 0, true, 5),
    MEDIUM_COLUMN(ETerrainSize.MEDIUM, "columna", 5, 20, 2, 3, 1, 0, true, 5),
    MEDIUM_VERTICAL(ETerrainSize.MEDIUM, "vertical", 3, 30, 2, 3, 1, 0, true, 5),
    MEDIUM_SQUARE(ETerrainSize.MEDIUM, "cuadrado", 10, 10, 2, 3, 1, 0, true, 5),

    LARGE_ROW(ETerrainSize.LARGE, "fila", 20, 10, 4, 5, 3, 1, true, 10),
    LARGE_HORIZONTAL(ETerrainSize.LARGE, "horizontal", 40, 5, 4, 5, 3, 1, true, 10),
    LARGE_COLUMN(ETerrainSize.LARGE, "columna", 10, 20, 4, 5, 3, 1, true, 10),
    LARGE_VERTICAL(ETerrainSize.LARGE, "vertical", 5, 40, 4, 5, 3, 1, true, 10),
    LARGE_SQUARE(ETerrainSize.LARGE, "cuadrado", 15, 15, 4, 5, 3, 1, true, 10);

    /** El tamaño al que pertenece el terreno. */
    private ETerrainSize size;
    /** El nombre de la forma (fila, horizontal, columna, vertical o cuadrado). */
    private String shape;
    /** El número de columnas (eje horizontal, letras). */
    private int columns;
    /** El número de filas (eje vertical, números). */
    private int rows;
    /** El nivel mínimo del arqueólogo para excavar en este terreno. */
    private int requiredLevel;
    /** El número mínimo de tesoros comunes garantizados. */
    private int minCommon;
    /** El número mínimo de tesoros infrecuentes garantizados. */
    private int minUncommon;
    /** El número mínimo de tesoros raros garantizados. */
    private int minRare;
    /** Si en este terreno pueden aparecer tesoros raros. */
    private boolean allowsRare;
    /** La experiencia extra que gana el arqueólogo al excavar en este terreno. */
    private int extraExp;

    /**
     * Constructor básico.
     *
     * @param size          El tamaño del terreno.
     * @param shape         El nombre de la forma.
     * @param columns       El número de columnas.
     * @param rows          El número de filas.
     * @param requiredLevel El nivel mínimo del arqueólogo.
     * @param minCommon     Los tesoros comunes mínimos.
     * @param minUncommon   Los tesoros infrecuentes mínimos.
     * @param minRare       Los tesoros raros mínimos.
     * @param allowsRare    Si pueden aparecer tesoros raros.
     * @param extraExp      La experiencia extra al terminar la excavación.
     */
    private ETerrain(ETerrainSize size, String shape, int columns, int rows, int requiredLevel,
            int minCommon, int minUncommon, int minRare, boolean allowsRare, int extraExp) {
        this.size = size;
        this.shape = shape;
        this.columns = columns;
        this.rows = rows;
        this.requiredLevel = requiredLevel;
        this.minCommon = minCommon;
        this.minUncommon = minUncommon;
        this.minRare = minRare;
        this.allowsRare = allowsRare;
        this.extraExp = extraExp;
    }

    /**
     * @return El tamaño al que pertenece el terreno.
     */
    public ETerrainSize getSize() {
        return this.size;
    }

    /**
     * @return El nombre de la forma.
     */
    public String getShape() {
        return this.shape;
    }

    /**
     * @return El número de columnas.
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * @return El número de filas.
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * @return El número total de casillas del terreno.
     */
    public int getCells() {
        return this.columns * this.rows;
    }

    /**
     * @return El número mínimo de tesoros comunes garantizados.
     */
    public int getMinCommon() {
        return this.minCommon;
    }

    /**
     * @return El número mínimo de tesoros infrecuentes garantizados.
     */
    public int getMinUncommon() {
        return this.minUncommon;
    }

    /**
     * @return El número mínimo de tesoros raros garantizados.
     */
    public int getMinRare() {
        return this.minRare;
    }

    /**
     * @return El total de tesoros mínimos garantizados.
     */
    public int getMinTotal() {
        return this.minCommon + this.minUncommon + this.minRare;
    }

    /**
     * @return Si en este terreno pueden aparecer tesoros raros (nunca en los
     *         pequeños, ni con habilidades).
     */
    public boolean allowsRare() {
        return this.allowsRare;
    }

    /**
     * @return La experiencia extra que gana el arqueólogo al excavar en este
     *         terreno.
     */
    public int getExtraExp() {
        return this.extraExp;
    }

    /**
     * Indica si un arqueólogo de cierto nivel puede excavar en este terreno.
     *
     * @param level El nivel del arqueólogo.
     * @return Si el nivel alcanza el mínimo requerido.
     */
    public boolean isAllowedFor(int level) {
        return level >= this.requiredLevel;
    }

    /**
     * @return El terreno con su tamaño, forma y medidas, por ejemplo
     *         "pequeño fila (5x3)".
     */
    @Override
    public String toString() {
        return this.size + " " + this.shape + " (" + this.columns + "x" + this.rows + ")";
    }
}
