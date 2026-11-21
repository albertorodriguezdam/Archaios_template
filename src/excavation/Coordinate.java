package excavation;

/**
 * Representa una posición de la rejilla mediante índices de fila y columna que comienzan en cero.
 */
public class Coordinate {
    /**
     * Fila
     */
    private final int row;
    /**
     * Columna
     */
    private final int column;
    /**
     * Constructor básico
     * @param row la fila
     * @param column la columna
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }
    /**
     * 
     * @return La fila
     */
    public int getRow() {
        return row;
    }
    /**
     * 
     * @return La columna
     */
    public int getColumn() {
        return column;
    }

        /**
     * Compara por fila y columna. Sirve para detectar coordenadas repetidas
     * en una misma entrada.
     * @param obj El objeto con el que se compara.
     * @return Si es una coordenada con la misma fila y columna.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Coordinate)) {
            return false;
        }
        Coordinate other = (Coordinate) obj;
        return this.row == other.row && this.column == other.column;
    }

    /**
     * Calcula el hash a partir de la fila y la columna, para que dos
     * coordenadas iguales den el mismo valor.
     * @return El código hash de la coordenada.
     */
    @Override
    public int hashCode(){
        return 31 * this.row + this.column;
    }

    /**
     * @return La coordenada en formato (fila,columna), empezando en 0.
     */
    @Override
    public String toString(){
        return "(" + this.row + "," + this.column + ")";
    }
}
