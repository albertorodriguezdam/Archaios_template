package excavation;

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
	/** Casilla sin excavar. */
	public static final char UNDUG = '·';
	/** Casilla excavada sin tesoro (o limpiada por una habilidad). */
	public static final char EMPTY = 'x';
	/** Casilla marcada por una habilidad de suposición. */
	public static final char GUESS = '?';
	/** Las letras disponibles para las columnas. Pendiente de confirmar con el profesor el "abecedario (23)". */
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** El número de filas. */
	private int rows;
	/** El número de columnas. */
	private int columns;
	/** Lo que ve el jugador en cada casilla: UNDUG, EMPTY, GUESS o la letra de la rareza. */
	private char[][] cells;
	/** La rareza del tesoro escondido en cada casilla, o null si está vacía. */
	private ETreasureRarity[][] hidden;
	/** Las letras de las columnas, en orden (A, B, ... o AA, AB, ...). */
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
				this.cells[r][c] = Rejilla.UNDUG;
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
		if (coord == null) {
			return false;
		}
		boolean rowOk = coord.getRow() >= 0 && coord.getRow() < this.rows;
		boolean columnOk = coord.getColumn() >= 0 && coord.getColumn() < this.columns;
		return rowOk && columnOk;
	}

	/**
	 * Esconde un tesoro de cierta rareza en una casilla.
	 * @param coord La casilla.
	 * @param rarity La rareza del tesoro.
	 */
	public void placeTreasure(Coordinate coord, ETreasureRarity rarity) {
		this.hidden[coord.getRow()][coord.getColumn()] = rarity;
	}

	/**
	 * @param coord La casilla.
	 * @return Si hay un tesoro escondido en la casilla.
	 */
	public boolean hasTreasure(Coordinate coord) {
		return this.hidden[coord.getRow()][coord.getColumn()] != null;
	}

	/**
	 * @param coord La casilla.
	 * @return La rareza del tesoro escondido en la casilla, o null si está vacía.
	 */
	public ETreasureRarity getRarity(Coordinate coord) {
		return this.hidden[coord.getRow()][coord.getColumn()];
	}

	/**
	 * Indica si la casilla ya está descubierta y no se puede volver a excavar.
	 * Ojo: una casilla con '?' todavía se puede excavar.
	 * @param coord La casilla.
	 * @return Si la casilla ya está excavada o limpiada.
	 */
	public boolean isRevealed(Coordinate coord) {
		char symbol = this.cells[coord.getRow()][coord.getColumn()];
		return symbol != Rejilla.UNDUG && symbol != Rejilla.GUESS;
	}

	/**
	 * Excava una casilla: muestra EMPTY si no hay nada o la letra de la rareza
	 * (ETreasureRarity.getCode()) si hay un tesoro.
	 * @param coord La casilla a excavar.
	 * @return La rareza del tesoro encontrado, o null si estaba vacía.
	 */
	public ETreasureRarity dig(Coordinate coord) {
		ETreasureRarity rarity = getRarity(coord);
		if (rarity == null) {
			this.cells[coord.getRow()][coord.getColumn()] = Rejilla.EMPTY;
		} else {
			this.cells[coord.getRow()][coord.getColumn()] = rarity.getCode().charAt(0);
		}
		return rarity;
	}

	/**
	 * Habilidad de limpieza: descubre una casilla vacía antes de empezar, marcándola con EMPTY.
	 * @param coord La casilla vacía a limpiar.
	 */
	public void clean(Coordinate coord) {
		this.cells[coord.getRow()][coord.getColumn()] = Rejilla.EMPTY;
	}

	/**
	 * Habilidad de suposición: marca una casilla con GUESS si no está ya descubierta.
	 * @param coord La casilla a marcar.
	 * @return Si se ha podido marcar la casilla.
	 */
	public boolean markGuess(Coordinate coord) {
		int row = coord.getRow();
		int column = coord.getColumn();

		if(cells[row][column]!= Rejilla.UNDUG){
			return false;
		}
		cells[row][column] = Rejilla.GUESS;
		return true;
	}

	/**
	 * Busca la columna que corresponde a unas letras.
	 * @param letters Las letras de la columna, en mayúsculas (por ejemplo "B" o "AA").
	 * @return El índice de la columna empezando en 0, o -1 si no existe en esta rejilla.
	 */
	public int getColumnIndex(String letters) {
		return this.columnLetters.indexOf(letters);
	}

	/**
	 * Devuelve la coordenada tal como la ve el usuario, con el número de fila y la letra.
	 * @param coord La coordenada.
	 * @return El texto de la coordenada, por ejemplo "3B".
	 */
	public String getLabel(Coordinate coord) {
		return (coord.getRow() + 1) + this.columnLetters.get(coord.getColumn());
	}

	/**
	 * Dibuja la rejilla tal como la ve el jugador.
	 * Arriba van las letras y a la izquierda los números de fila (empezando en 1).
	 * Todo tiene que quedar alineado aunque haya filas de 2 cifras o columnas de 2 letras,
	 * con cada símbolo centrado bajo su letra.
	 * @return El texto de la rejilla, listo para imprimir.
	 */
	public String render() {
		return draw(false);
	}

	/**
	 * Dibuja la rejilla descubierta para el final de la excavación:
	 * las casillas vacías con EMPTY y los tesoros, encontrados o no, con la letra de su rareza.
	 * @return El texto de la rejilla descubierta.
	 */
	public String renderUncovered() {
		return draw(true);
	}

	/**
	 * Dibuja la rejilla. Lo usan render() y renderUncovered() para no repetir código.
	 * @param uncovered Si es true se muestra todo lo que hay escondido; si es false, lo que ve el jugador.
	 * @return El texto de la rejilla.
	 */
	private String draw(boolean uncovered) {
		// Ancho de la columna de números (1 cifra hasta 9 filas, 2 cifras a partir de 10)
		int numberWidth = String.valueOf(this.rows).length();
		// Ancho de cada columna (1 si todas las letras son simples, 2 si hay AA, AB...)
		int cellWidth = this.columnLetters.get(this.columns - 1).length();

		StringBuilder sb = new StringBuilder();

		// Cabecera con las letras
		sb.append(repeat(' ', numberWidth + 1));
		for (int c = 0; c < this.columns; c++) {
			sb.append(center(this.columnLetters.get(c), cellWidth));
			if (c < this.columns - 1) {
				sb.append(' ');
			}
		}
		sb.append('\n');

		// Una línea por cada fila
		for (int r = 0; r < this.rows; r++) {
			sb.append(padRight(String.valueOf(r + 1), numberWidth)).append(' ');
			for (int c = 0; c < this.columns; c++) {
				char symbol;
				if (uncovered) {
					symbol = getUncoveredSymbol(r, c);
				} else {
					symbol = this.cells[r][c];
				}
				sb.append(center(String.valueOf(symbol), cellWidth));
				if (c < this.columns - 1) {
					sb.append(' ');
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Devuelve el símbolo de una casilla en la rejilla descubierta.
	 * @param r La fila.
	 * @param c La columna.
	 * @return La letra de la rareza si hay tesoro, o EMPTY si no lo hay.
	 */
	private char getUncoveredSymbol(int r, int c) {
		if (this.hidden[r][c] != null) {
			return this.hidden[r][c].getCode().charAt(0);
		}
		return Rejilla.EMPTY;
	}

	/**
	 * Centra un texto en un ancho dado rellenando con espacios.
	 * Si sobra un espacio, se pone a la derecha.
	 * @param text El texto a centrar.
	 * @param width El ancho total.
	 * @return El texto centrado.
	 */
	private static String center(String text, int width) {
		int spaces = width - text.length();
		int left = spaces / 2;
		int right = spaces - left;
		return repeat(' ', left) + text + repeat(' ', right);
	}

	/**
	 * Rellena un texto con espacios por la derecha hasta un ancho dado.
	 * @param text El texto.
	 * @param width El ancho total.
	 * @return El texto con los espacios añadidos.
	 */
	private static String padRight(String text, int width) {
		return text + repeat(' ', width - text.length());
	}

	/**
	 * Repite un carácter varias veces.
	 * @param character El carácter a repetir.
	 * @param times Las veces que se repite (si es 0 o menos, devuelve una cadena vacía).
	 * @return La cadena con el carácter repetido.
	 */
	private static String repeat(char character, int times) {
		String result = "";
		for (int i = 0; i < times; i++) {
			result += character;
		}
		return result;
	}

	/**
	 * Genera las letras de las columnas: A, B, C... y, si no hay suficientes, AA, AB, AC...
	 * Prueba primero con cadenas de 1 letra, luego de 2, etc., hasta tener las necesarias.
	 * @param amount El número de columnas.
	 * @return La lista con exactamente <code>amount</code> letras, en orden.
	 */
	public static List<String> generateLetters(int amount) {
		List<String> result = new ArrayList<>();
		int length = 1;
		while (result.size() < amount) {
			generateLetters("", length, amount, result);
			length++;
		}
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
		// Si ya tenemos todas las letras, no seguimos
		if (result.size() >= amount) {
			return;
		}
		// Caso base: la cadena ya tiene la longitud buscada
		if (current.length() == length) {
			result.add(current);
			return;
		}
		// Caso recursivo: añadimos cada letra del alfabeto y seguimos
		for (int i = 0; i < ALPHABET.length(); i++) {
			generateLetters(current + ALPHABET.charAt(i), length, amount, result);
		}
	}
}