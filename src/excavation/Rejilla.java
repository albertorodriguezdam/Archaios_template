import java.util.ArrayList;
import java.util.List;

import treasures.ETreasureRarity;

public class Rejilla {
	public static final char UNDUG = '·';
	public static final char EMPTY = 'x';
	public static final char GUESS = '?';
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    
	private int rows;
	private int columns;
	private char[][] cells;
	private ETreasureRarity[][] hidden;
	private List<String> columnLetters;

    public Rejilla(ETerrain terrain) {
		this(terrain.getRows(), terrain.getColumns());
	}

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

	public int getRows() {
		return this.rows;
	}

	
	public int getColumns() {
		return this.columns;
	}

	public int getCellCount() {
		return this.rows * this.columns;
	}

	public List<String> getColumnLetters() {
		return this.columnLetters;
	}

	public boolean isInside(Coordinate coord) {
		// TODO
		return false;
	}

	public void placeTreasure(Coordinate coord, ETreasureRarity rarity) {
		// TODO
	}

	public boolean hasTreasure(Coordinate coord) {
		// TODO
		return false;
	}

	public boolean isRevealed(Coordinate coord) {
		// TODO
		return false;
	}
	public ETreasureRarity dig(Coordinate coord) {
		// TODO
		return null;
	}

	public void clean(Coordinate coord) {
		// TODO
	}

	public void markGuess(Coordinate coord) {
		// TODO
	}

	public int getColumnIndex(String letters) {
		// TODO: pista, columnLetters ya tiene un método que hace justo esto
		return -1;
	}

	public String render() {
		// TODO
		return "";
	}

	public String renderUncovered() {
		// TODO: intenta reutilizar el código de render() en vez de copiarlo
		return "";
	}

	public static List<String> generateLetters(int amount) {
		List<String> result = new ArrayList<>();
		// TODO: llamar al método recursivo con longitudes 1, 2... mientras falten letras
		return result;
	}

	private static void generateLetters(String current, int length, int amount, List<String> result) {
		// TODO
	}
}
