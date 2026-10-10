import java.util.Random;
import treasures.ETreasureRarity;
/**
 * El sistema de excavación: genera la rejilla, esconde los tesoros, aplica las habilidades
 * del arqueólogo y lleva el proceso por turnos hasta su final.
 * @author Andrés.
 */
public class Excavation {
    /** Proporción de casillas con tesoro (10 %, redondeando hacia arriba). */
	public static final double RATIO_TREASURE = 0.10;
	/** Proporción máxima de casillas con tesoro, incluidas las habilidades (50 %). */
	public static final double MAX_RATIO_TREASURE = 0.50;
	/** Proporción de casillas que dan las acciones base (25 %). */
	public static final double BASE_ACTIONS_RATIO = 0.25;
	/** Proporción máxima de acciones respecto a las casillas (75 %). */
	public static final double MAX_ACTIONS_RATIO = 0.75;
	/** Proporción máxima de casillas que pueden revelar las habilidades de revelación (25 %). */
	public static final double MAX_REVEAL_RATIO = 0.25;
	/** Probabilidad base de tesoro común. */
	public static final int PROB_COMMON = 60;
	/** Probabilidad base de tesoro infrecuente. */
	public static final int PROB_UNCOMMON = 30;
	/** Probabilidad base de tesoro raro. */
	public static final int PROB_RARE = 10;
	/** Número de '?' que pone cada habilidad de suposición. */
	public static final int GUESSES_PER_SKILL = 3;

    public static final Random RANDOM = new Random();

    /** El terreno de la excavación. */
	private ETerrain terrain;
	/** La rejilla de la excavación. */
	private Rejilla rejilla;
	/** Las acciones que le quedan al arqueólogo. */
	private int actions;
	/** El número de tesoros escondidos. */
	private int totalTreasures;
	/** El número de tesoros encontrados. */
	private int foundTreasures;

    public Excavation(ETerrain terrain){
        this.terrain = terrain;
        this.rejilla = new Rejilla(terrain);
        this.foundTreasures = 0;
    }
    /**
	 * @return El terreno de la excavación.
	 */
	public ETerrain getTerrain() {
		return this.terrain;
	}

	/**
	 * @return La rejilla de la excavación.
	 */
	public Rejilla getRejilla() {
		return this.rejilla;
	}

	/**
	 * @return Las acciones que le quedan al arqueólogo.
	 */
	public int getActions() {
		return this.actions;
	}

	/**
	 * Calcula los tesoros base de un terreno, el 10 % de las casillas redondeando hacia arriba,
	 * como mínimo 1. Los mínimos del tamaño van incluidos en esta cifra.
	 * @param terrain El terreno.
	 * @return El número de tesoros base.
	 */
	public static int calculateBaseTreasure(ETerrain terrain){
		return 0;
	}
	/**
	 * 
	 */
	public static int calculateMaxTreasure(ETerrain terrain){
		return 0;
	}

	public static int calcActions(ETerrain terrain, int extraActions, double factor) {
		// TODO
		return 0;
	}

	private ETreasureRarity randomRarity(int extraUncommonProb, int extraRareProb) {
		// TODO: usar RANDOM y terrain.getSize().allowsRare()
		return ETreasureRarity.COMMON;
	}

	private void generateTreasures(){

	}
	
	private void applyCleaning(){

	}

	private void applyGuesses(int skills){

	}

	private void showInvestigation(){
		
	}
}
