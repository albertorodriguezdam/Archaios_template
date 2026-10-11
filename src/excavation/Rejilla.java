import java.util.ArrayList;
import java.util.List;

import treasures.ETreasureRarity;

/**
 * La rejilla de excavación: guarda lo que hay escondido en cada casilla y lo que ve el jugador,
 * y se encarga de dibujarla. No conoce los objetos Treasure, solo su rareza; los tesoros
 * concretos los gestiona {@link Excavation}.
 * @author Andrés.
 */
public class Rejilla {
	// Casilla sin excavar. 
	public static final char UNDUG = '·';
	// Casilla excavada sin tesoro (o limpiada por una habilidad). 
	public static final char EMPTY = 'x';
	// Casilla marcada por una habilidad de suposición. 
	public static final char GUESS = '?';
	// Las letras disponibles para las columnas. Pendiente de confirmar con el profesor el "abecedario (23)". 
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    
	// El número de filas.
	private int rows;
	// El número de columnas. 
	private int columns;
	// Lo que ve el jugador en cada casilla: UNDUG, EMPTY, GUESS o la letra de la rareza. 
	private char[][] cells;
	// La rareza del tesoro escondido en cada casilla, o null si está vacía.
	private ETreasureRarity[][] hidden;
	// Las letras de las columnas, en orden (A, B, ... o AA, AB, ...).
	private List<String> columnLetters;

    /**
	 * Crea una rejilla vacía con las medidas del terreno.
	 * @param terrain El terreno a excavar.
	 */
    public Rejilla(ETerrain terrain) {
		this(terrain.getRows(), terrain.getColumns());
	}

    /**
	 * Crea una rejilla vacía, con todas las casillas sin excavar y sin tesoros.
	 * @param rows El número de filas.
	 * @param columns El número de columnas.
	 */
    public Rejilla(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.cells = new char[rows][columns];
		this.hidden = new ETreasureRarity[rows][columns];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				this.cells[r][c] = UNDUG;
			}
		}
		this.columnLetters = generateLetters(columns);
	}

	/**
	 * @return El número de filas.
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * @return El número de columnas.
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * @return El número total de casillas.
	 */
	public int getCellCount() {
		return this.rows * this.columns;
	}

	/**
	 * @return Las letras de las columnas, en orden.
	 */
	public List<String> getColumnLetters() {
		return this.columnLetters;
	}

	/**
	 * Indica si una coordenada está dentro de la rejilla.
	 * @param coord La coordenada.
	 * @return Si la fila y la columna están dentro de los límites.
	 */
	public boolean isInside(Coordinate coord) {
		// TODO
		return false;
	}

	/**
	 * Esconde un tesoro de cierta rareza en una casilla.
	 * @param coord La casilla.
	 * @param rarity La rareza del tesoro.
	 */
	public void placeTreasure(Coordinate coord, ETreasureRarity rarity) {
		// TODO
	}

	/**
	 * @param coord La casilla.
	 * @return Si hay un tesoro escondido en la casilla.
	 */
	public boolean hasTreasure(Coordinate coord) {
		// TODO
		return false;
	}

	/**
	 * Indica si la casilla ya está descubierta y no se puede volver a excavar.
	 * @param coord La casilla.
	 * @return Si la casilla ya está excavada o limpiada.
	 */
	public boolean isRevealed(Coordinate coord) {
		// TODO
		return false;
	}

	/**
	 * Excava una casilla: muestra EMPTY si no hay nada o la letra de la rareza
	 * (ETreasureRarity.getCode()) si hay un tesoro.
	 * @param coord La casilla a excavar.
	 * @return La rareza del tesoro encontrado, o null si estaba vacía.
	 */
	public ETreasureRarity dig(Coordinate coord) {
		// TODO
		return null;
	}

	/**
	 * Habilidad de limpieza: descubre una casilla vacía antes de empezar, marcándola con EMPTY.
	 * @param coord La casilla vacía a limpiar.
	 */
	public void clean(Coordinate coord) {
		// TODO
	}

	/**
	 * Habilidad de suposición: marca una casilla con GUESS si no está ya descubierta.
	 * @param coord La casilla a marcar.
	 */
	public void markGuess(Coordinate coord) {
		// TODO
	}

	/**
	 * Busca la columna que corresponde a unas letras.
	 * @param letters Las letras de la columna, en mayúsculas (por ejemplo "B" o "AA").
	 * @return El índice de la columna empezando en 0, o -1 si no existe en esta rejilla.
	 */
	public int getColumnIndex(String letters) {
		// TODO: pista, columnLetters ya tiene un método que hace justo esto
		return -1;
	}

	/**
	 * Dibuja la rejilla tal como la ve el jugador.
	 * Arriba van las letras y a la izquierda los números de fila (empezando en 1).
	 * Todo tiene que quedar alineado aunque haya filas de 2 cifras o columnas de 2 letras,
	 * con cada símbolo centrado bajo su letra.
	 * @return El texto de la rejilla, listo para imprimir.
	 */
	public String render() {
		// TODO
		return "";
	}

	/**
	 * Dibuja la rejilla descubierta para el final de la excavación:
	 * las casillas vacías con EMPTY y los tesoros, encontrados o no, con la letra de su rareza.
	 * @return El texto de la rejilla descubierta.
	 */
	public String renderUncovered() {
		// TODO: intenta reutilizar el código de render() en vez de copiarlo
		return "";
	}

	/**
	 * Genera las letras de las columnas: A, B, C... y, si no hay suficientes, AA, AB, AC...
	 * Prueba primero con cadenas de 1 letra, luego de 2, etc., hasta tener las necesarias.
	 * @param amount El número de columnas.
	 * @return La lista con exactamente <code>amount</code> letras, en orden.
	 */
	public static List<String> generateLetters(int amount) {
		List<String> result = new ArrayList<>();
		// TODO: llamar al método recursivo con longitudes 1, 2... mientras falten letras
		return result;
	}

	/**
	 * Parte recursiva de la generación de letras.
	 * Va añadiendo una letra del alfabeto a <code>current</code> y se llama a sí mismo con la cadena nueva.
	 * Caso base: la cadena tiene la longitud buscada y se añade a la lista.
	 * Hay que dejar de añadir (y de llamar) en cuanto la lista tenga <code>amount</code> elementos.
	 * @param current La cadena construida hasta ahora (empieza vacía "").
	 * @param length La longitud que deben tener las cadenas en esta pasada.
	 * @param amount El número total de letras que se necesitan.
	 * @param result La lista donde se van guardando las letras.
	 */
	private static void generateLetters(String current, int length, int amount, List<String> result) {
		// TODO
	}
}
