import java.util.ArrayList;
import java.util.List;

/**
 * Los tipos de terreno de excavación: tres tamaños con cinco formas cada uno.
 * Las medidas siguen el formato del enunciado: columnas x filas (por ejemplo,
 * "Horizontal: 10x2" son 10 columnas y 2 filas).
 */
public enum ETerrain {
    SMALL_ROW(ETerrainSize.SMALL, "fila", 5, 3),
    SMALL_HORIZONTAL(ETerrainSize.SMALL, "horizontal", 10, 2),
    SMALL_COLUMN(ETerrainSize.SMALL, "columna", 3, 5),
    SMALL_VERTICAL(ETerrainSize.SMALL, "vertical", 2, 10),
    SMALL_SQUARE(ETerrainSize.SMALL, "cuadrado", 5, 5),

    MEDIUM_ROW(ETerrainSize.MEDIUM, "fila", 20, 5),
    MEDIUM_HORIZONTAL(ETerrainSize.MEDIUM, "horizontal", 30, 3),
    MEDIUM_COLUMN(ETerrainSize.MEDIUM, "columna", 5, 20),
    MEDIUM_VERTICAL(ETerrainSize.MEDIUM, "vertical", 3, 30),
    MEDIUM_SQUARE(ETerrainSize.MEDIUM, "cuadrado", 10, 10),

    LARGE_ROW(ETerrainSize.LARGE, "fila", 20, 10),
    LARGE_HORIZONTAL(ETerrainSize.LARGE, "horizontal", 40, 5),
    LARGE_COLUMN(ETerrainSize.LARGE, "columna", 10, 20),
    LARGE_VERTICAL(ETerrainSize.LARGE, "vertical", 5, 40),
    LARGE_SQUARE(ETerrainSize.LARGE, "cuadrado", 15, 15);

    /** El tamaño al que pertenece el terreno. */
    private ETerrainSize size;
    /** El nombre de la forma (fila, horizontal, columna, vertical o cuadrado). */
    private String shape;
    /** El número de columnas (eje horizontal, letras). */
    private int columns;
    /** El número de filas (eje vertical, números). */
    private int rows;

    /**
     * Constructor básico.
     * 
     * @param size    El tamaño del terreno.
     * @param shape   El nombre de la forma.
     * @param columns El número de columnas.
     * @param rows    El número de filas.
     */
    private ETerrain(ETerrainSize size, String shape, int columns, int rows) {
        this.size = size;
        this.shape = shape;
        this.columns = columns;
        this.rows = rows;
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
     * @return Si el terreno es cuadrado, el único que se desbloquea al subir de
     *         nivel.
     */
    public boolean isSquare() {
        return this.columns == this.rows;
    }

    /**
     * Devuelve el terreno cuadrado de un tamaño. Lo usa el arqueólogo al subir a
     * los niveles 2 y 4.
     * 
     * @param size El tamaño buscado.
     * @return El terreno cuadrado de ese tamaño.
     */
    public static ETerrain getSquare(ETerrainSize size) {
        for (ETerrain terrain : ETerrain.values()) {
            if (terrain.size == size && terrain.isSquare()) {
                return terrain;
            }
        }
        return null;
    }

    /**
     * Devuelve todos los terrenos de un tamaño.
     * 
     * @param size El tamaño buscado.
     * @return La lista de terrenos de ese tamaño, en orden de declaración.
     */
    public static List<ETerrain> getBySize(ETerrainSize size) {
        List<ETerrain> result = new ArrayList<>();
        for (ETerrain terrain : ETerrain.values()) {
            if (terrain.size == size) {
                result.add(terrain);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.size + " " + this.shape + " (" + this.columns + "x" + this.rows + ")";
    }
}
