import java.util.Random;
import treasures.ETreasureRarity;
/**
 * El sistema de excavación: genera la rejilla, esconde los tesoros, aplica las habilidades
 * del arqueólogo y lleva el proceso por turnos hasta su final.
 * @author Andrés.
 */
public class Excavation {
    // Proporción de casillas con tesoro (10 %, redondeando hacia arriba). 
	public static final double RATIO_TREASURE = 0.10;
	// Proporción máxima de casillas con tesoro, incluidas las habilidades (50 %).
	public static final double MAX_RATIO_TREASURE = 0.50;
	// Proporción de casillas que dan las acciones base (25 %).
	public static final double BASE_ACTIONS_RATIO = 0.25;
	// Proporción máxima de acciones respecto a las casillas (75 %).
	public static final double MAX_ACTIONS_RATIO = 0.75;
	// Proporción máxima de casillas que pueden revelar las habilidades de revelación (25 %).
	public static final double MAX_REVEAL_RATIO = 0.25;
	// Probabilidad base de tesoro común. 
	public static final int PROB_COMMON = 60;
	// Probabilidad base de tesoro infrecuente.
	public static final int PROB_UNCOMMON = 30;
	// Probabilidad base de tesoro raro.
	public static final int PROB_RARE = 10;
	// Número de '?' que pone cada habilidad de suposición.
	public static final int GUESSES_PER_SKILL = 3;

    public static final Random RANDOM = new Random();

    // El terreno de la excavación.
	private ETerrain terrain;
	// La rejilla de la excavación. 
	private Rejilla rejilla;
	// Las acciones que le quedan al arqueólogo.
	private int actions;
	// El número de tesoros escondidos.
	private int totalTreasures;
	// El número de tesoros encontrados.
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
	 * Calcula los tesoros base de un terreno, el 10 % de las casillas redondeando hacia arriba, como mínimo 1. Los mínimos del tamaño van incluidos en esta cifra.
	 * @param terrain El terreno.
	 * @return El número de tesoros base.
	 */
	public static int calculateBaseTreasure(ETerrain terrain){
		return 0;
	}

	/**
	 * Calcula el máximo de tesoros de un terreno: el 50 % de las casillas.
	 * @param terrain El terreno.
	 * @return El número máximo de tesoros, contando los extra de las habilidades.
	 */
	public static int calculateMaxTreasure(ETerrain terrain){
		return 0;
	}

	/**
	 * Calcula las acciones: (25 % de las casillas + extra del arqueólogo) * factor de las habilidades,
	 * limitado a estar entre el 25 % y el 75 % de las casillas.
	 * @param terrain El terreno.
	 * @param extraActions Las acciones extra del arqueólogo.
	 * @param factor El factor multiplicador de las habilidades (1.0 si no tiene ninguna).
	 * @return El número de acciones.
	 */
	public static int calcActions(ETerrain terrain, int extraActions, double factor) {
		// TODO
		return 0;
	}

	/**
	 * Elige una rareza al azar: 60 % común, 30 % infrecuente y 10 % raro, más los bonos de probabilidad.
	 * En los terrenos que no permiten raros nunca puede salir un raro.
	 * @param extraUncommonProb El porcentaje extra para infrecuentes.
	 * @param extraRareProb El porcentaje extra para raros.
	 * @return La rareza elegida.
	 */
	private ETreasureRarity randomRarity(int extraUncommonProb, int extraRareProb) {
		// TODO: usar RANDOM y terrain.getSize().allowsRare()
		return ETreasureRarity.COMMON;
	}

	/**
	 * Genera la lista de tesoros en el orden del enunciado, sin pasar del máximo:
	 * 1) los mínimos del tamaño, 2) aleatorios hasta llegar a los tesoros base,
	 * 3) los extra del arqueólogo: raros, infrecuentes, comunes y por último aleatorios.
	 */
	private void generateTreasures(){

	}
	
	/**
	 * Habilidades de limpieza: descubre casillas vacías al azar antes de empezar.
	 * Entre limpieza y suposición no se puede revelar más del 25 % de las casillas.
	 * @param amount El número de casillas a limpiar.
	 */
	private void applyCleaning(){

	}

	/**
	 * Coloca los tesoros generados en casillas aleatorias distintas de la rejilla.
	 * Pista: UtilsHelper.genRandomIdxs(num, total) y convertir cada índice en fila y columna.
	 */
	private void placeTreasure(){

	}

	/**
	 * Habilidades de suposición: por cada una, elige 3 casillas (una con tesoro y dos vacías)
	 * y las marca con '?'. Se aplica después de la limpieza: si una de las elegidas ya está limpiada,
	 * no se busca otra y queda una '?' menos.
	 * @param skills El número de habilidades de suposición.
	 */
	private void applyGuesses(int skills){

	}

	/**
	 * Habilidad de investigación: muestra cuántos tesoros quedan y de qué rareza.
	 */
	private void showInvestigation(){
		
	}
	
	/**
	 * Indica si la excavación ha terminado: sin acciones o con todos los tesoros encontrados.
	 * @return Si ha terminado.
	 */
	public boolean isFinished() {
		return this.actions <= 0 || this.foundTreasures >= this.totalTreasures;
	}
	
	/**
	 * Bucle de la excavación. En cada turno muestra las acciones restantes,
	 * la investigación si la tiene y la rejilla, y pide las coordenadas a excavar
	 * hasta que la excavación termine. Si se encuentran todos los tesoros, lo notifica.
	 */
	public void excavate(){

	}

	/**
	 * Finaliza la excavación: muestra la rejilla final, la rejilla descubierta y el resumen
	 * de tesoros encontrados por rareza.
	 * para que Archaios los registre en Stats y los envíe al Museo.
	 */
	public void finish(){

	}
}
