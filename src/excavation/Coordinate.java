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

    @Override
    /**
     * Compara por fila y columna. Sirve para detectar coordenadas repetidas
     en una misma entrada.*/
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

    @Override
    public int hashCode(){
        return 31 * this.row + this.column;
    }

    @Override
    public String toString(){
        return "(" + this.row + "," + this.column + ")";
    }
}
