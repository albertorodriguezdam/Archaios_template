package excavation;

import helpers.CoordinateValidator;
import helpers.InputValidator;
import helpers.UtilsHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import people.Archeologist;
import regions.Region;
import treasures.ETreasureRarity;
import treasures.Treasure;

/**
 * El sistema de excavación: genera la rejilla, esconde los tesoros, aplica las
 * habilidades del arqueólogo y lleva el proceso por turnos hasta su final.
 *
 * @author Andrés.
 */
public class Excavation {

    /**
     * Proporción de casillas con tesoro (10 %, redondeando hacia arriba).
     */
    public static final double RATIO_TREASURE = 0.10;
    /**
     * Proporción máxima de casillas con tesoro, incluidas las habilidades (50
     * %).
     */
    public static final double MAX_RATIO_TREASURE = 0.50;
    /**
     * Proporción de casillas que dan las acciones base (25 %).
     */
    public static final double BASE_ACTIONS_RATIO = 0.25;
    /**
     * Proporción máxima de acciones respecto a las casillas (75 %).
     */
    public static final double MAX_ACTIONS_RATIO = 0.75;
    /**
     * Proporción máxima de casillas que pueden revelar las habilidades de
     * revelación (25 %).
     */
    public static final double MAX_REVEAL_RATIO = 0.25;
    /**
     * Probabilidad base de tesoro común.
     */
    public static final int PROB_COMMON = 60;
    /**
     * Probabilidad base de tesoro infrecuente.
     */
    public static final int PROB_UNCOMMON = 30;
    /**
     * Probabilidad base de tesoro raro.
     */
    public static final int PROB_RARE = 10;
    /**
     * Número de '?' que pone cada habilidad de suposición.
     */
    public static final int GUESSES_PER_SKILL = 3;

    /**
     * Generador de números aleatorios de la excavación.
     */
    private static final Random RANDOM = new Random();

    /**
     * La región donde se excava.
     */
    private Region region;
    /**
     * El arqueólogo que excava.
     */
    private Archeologist archeologist;
    /**
     * El terreno de la excavación.
     */
    private ETerrain terrain;
    /**
     * La rejilla de la excavación.
     */
    private Rejilla rejilla;
    /**
     * El tesoro escondido en cada casilla, o null si está vacía.
     */
    private Treasure[][] hiddenTreasures;
    private String[][] hiddenCodes;
    /**
     * Los tesoros generados que hay que esconder en la rejilla.
     */
    private List<Treasure> treasuresToPlace;
    /**
     * Los tesoros encontrados durante la excavación.
     */
    private List<Treasure> foundTreasuresList;
    /**
     * Los códigos completos (con parte) de los tesoros encontrados.
     */
    private List<String> foundCodes;
    /**
     * Las acciones que le quedan al arqueólogo.
     */
    private int actions;
    /**
     * El número de tesoros escondidos.
     */
    private int totalTreasures;
    /**
     * El número de tesoros encontrados.
     */
    private int foundTreasures;
    /**
     * El número de casillas reveladas por las habilidades (limpieza y
     * suposición).
     */
    private int revealedBySkills;

    /**
     * Prepara una excavación nueva: crea la rejilla, genera y esconde los
     * tesoros, aplica las habilidades de revelación y calcula las acciones.
     *
     * @param terrain Terreno elegido.
     * @param region Región donde se excava.
     * @param archeologist Arqueólogo que excava.
     */
    public Excavation(ETerrain terrain, Region region, Archeologist archeologist) {
        this.region = region;
        this.archeologist = archeologist;
        this.terrain = terrain;
        this.rejilla = new Rejilla(terrain);
        this.hiddenTreasures = new Treasure[terrain.getRows()][terrain.getColumns()];
        this.hiddenCodes = new String[terrain.getRows()][terrain.getColumns()];
        this.treasuresToPlace = new ArrayList<>();
        this.foundTreasuresList = new ArrayList<>();
        this.foundCodes = new ArrayList<>();
        this.foundTreasures = 0;
        this.revealedBySkills = 0;

        /*El orden importa: primero los tesoros, luego la limpieza y por último las suposiciones*/
        generateTreasures();
        placeTreasures();
        applyCleaning(archeologist.getTotalEmptyCellsReveled());
        /**
         * getTotalScanHints() devuelve casillas con '?'; cada suposición marca
         * 3
         */
        applyGuesses(archeologist.getTotalScanHints() / GUESSES_PER_SKILL);
        this.actions = calcActions(terrain, archeologist.getTotalExtraActions(), archeologist.getActionsMultiplier());
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
     * @return El número de tesoros escondidos en la rejilla.
     */
    public int getTotalTreasures() {
        return this.totalTreasures;
    }

    /**
     * @return Los códigos completos (con número de parte) de los tesoros
     * encontrados, listos para enviarlos al Museo y guardarlos en las
     * estadísticas.
     */
    public List<String> getFoundCodes() {
        return this.foundCodes;
    }

    /**
     * @return Los puntos conseguidos: la suma de los puntos de los tesoros
     * encontrados.
     */
    public int getEarnedPoints() {
        int points = 0;
        for (Treasure t : this.foundTreasuresList) {
            points += t.getPoints();
        }
        return points;
    }

    /**
     * @return La experiencia conseguida: los puntos más el extra del tamaño del
     * terreno (2, 5 o 10).
     */
    public int getEarnedXp() {
        return getEarnedPoints() + this.terrain.getSize().getExtraExp();
    }

    /**
     * Devuelve los terrenos en los que puede excavar un arqueólogo: los de los
     * tamaños que permite su nivel y las formas que tiene desbloqueadas.
     *
     * @param archeologist El arqueólogo.
     * @return La lista de terrenos disponibles.
     */
    public static List<ETerrain> getAvailableTerrains(Archeologist archeologist) {
        List<ETerrain> result = new ArrayList<>();
        for (ETerrain t : ETerrain.values()) {
            boolean levelOk = t.getSize().isAllowedFor(archeologist.getLevel());
            boolean sizeOk = containsIgnoreCase(archeologist.getTerrainsSize(), t.getSize().toString());
            boolean shapeOk = containsIgnoreCase(archeologist.getTerrainsFormats(), t.getShape());
            if (levelOk && sizeOk && shapeOk) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Comprueba si una lista de textos contiene un texto, sin distinguir
     * mayúsculas.
     *
     * @param list La lista de textos.
     * @param text El texto buscado.
     * @return Si el texto está en la lista.
     */
    private static boolean containsIgnoreCase(List<String> list, String text) {
        for (String s : list) {
            if (s.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula los tesoros base de un terreno, el 10 % de las casillas
     * redondeando hacia arriba, como mínimo 1. Los mínimos del tamaño van
     * incluidos en esta cifra.
     *
     * @param terrain El terreno.
     * @return El número de tesoros base.
     */
    public static int calculateBaseTreasure(ETerrain terrain) {
        int base = (int) Math.ceil(terrain.getCells() * RATIO_TREASURE);
        /**
         * Como mínimo 1 y nunca menos que los mínimos del tamaño
         */
        if (base < 1) {
            base = 1;
        }
        if (base < terrain.getSize().getMinTotal()) {
            base = terrain.getSize().getMinTotal();
        }
        return base;
    }

    /**
     * Calcula el máximo de tesoros de un terreno: el 50 % de las casillas.
     *
     * @param terrain El terreno.
     * @return El número máximo de tesoros, contando los extra de las
     * habilidades.
     */
    public static int calculateMaxTreasure(ETerrain terrain) {
        return (int) (terrain.getCells() * MAX_RATIO_TREASURE);
    }

    /**
     * Calcula las acciones: (25 % de las casillas + extra del arqueólogo) *
     * factor de las habilidades, limitado a estar entre el 25 % y el 75 % de
     * las casillas.
     *
     * @param terrain El terreno.
     * @param extraActions Las acciones extra del arqueólogo.
     * @param factor El factor multiplicador de las habilidades (1.0 si no tiene
     * ninguna).
     * @return El número de acciones.
     */
    public static int calcActions(ETerrain terrain, int extraActions, double factor) {
        int min = (int) (terrain.getCells() * BASE_ACTIONS_RATIO);
        int max = (int) (terrain.getCells() * MAX_ACTIONS_RATIO);
        int result = (int) ((terrain.getCells() * BASE_ACTIONS_RATIO + extraActions) * factor);

        if (result < min) {
            result = min;
        }
        if (result > max) {
            result = max;
        }
        return result;
    }

    /**
     * Elige una rareza al azar: 60 % común, 30 % infrecuente y 10 % raro, más
     * los bonos de probabilidad. En los terrenos que no permiten raros nunca
     * puede salir un raro.
     *
     * @param extraUncommonProb El porcentaje extra para infrecuentes.
     * @param extraRareProb El porcentaje extra para raros.
     * @return La rareza elegida.
     */
    private ETreasureRarity randomRarity(int extraUncommonProb, int extraRareProb) {
        int rareProb = PROB_RARE + extraRareProb;
        int uncommonProb = PROB_UNCOMMON + extraUncommonProb;
        if (!this.terrain.getSize().allowsRare()) {
            rareProb = 0;
        }

        /**
         * Número entre 0 y 99: los primeros valores son raros, luego
         * infrecuentes y el resto comunes
         */
        int number = RANDOM.nextInt(100);
        if (number < rareProb) {
            return ETreasureRarity.RARE;
        } else if (number < rareProb + uncommonProb) {
            return ETreasureRarity.UNCOMMON;
        } else {
            return ETreasureRarity.COMMON;
        }
    }

    /**
     * Genera la lista de tesoros en el orden del enunciado, sin pasar del
     * máximo: 1) los mínimos del tamaño, 2) aleatorios hasta llegar a los
     * tesoros base, 3) los extra del arqueólogo: raros, infrecuentes, comunes y
     * por último aleatorios.
     */
    private void generateTreasures() {
        ETerrainSize size = this.terrain.getSize();
        int base = calculateBaseTreasure(this.terrain);
        int uncommonProb = this.archeologist.getTotalUncommonProb();
        int rareProb = this.archeologist.getTotalRareProb();

        /**
         * 1) Mínimos del tamaño
         */
        addTreasures(ETreasureRarity.COMMON, size.getMinCommon());
        addTreasures(ETreasureRarity.UNCOMMON, size.getMinUncommon());
        addTreasures(ETreasureRarity.RARE, size.getMinRare());

        /**
         * 2) Aleatorios hasta llegar a los tesoros base
         */
        while (this.treasuresToPlace.size() < base) {
            if (!addTreasure(randomRarity(uncommonProb, rareProb))) {
                break;
            }
        }

        /**
         * 3) Extras del arqueólogo: raros (si el terreno los permite),
         * infrecuentes, comunes y aleatorios
         */
        if (size.allowsRare()) {
            addTreasures(ETreasureRarity.RARE, this.archeologist.getTotalRareTreasures());
        }
        addTreasures(ETreasureRarity.UNCOMMON, this.archeologist.getTotalUncommonTreasures());
        addTreasures(ETreasureRarity.COMMON, this.archeologist.getTotalCommonTreasures());
        for (int i = 0; i < this.archeologist.getTotalRandomTreasures(); i++) {
            addTreasure(randomRarity(uncommonProb, rareProb));
        }

        this.totalTreasures = this.treasuresToPlace.size();
    }

    /**
     * Añade varios tesoros de una rareza a la lista de tesoros a esconder.
     *
     * @param rarity La rareza.
     * @param amount Cuántos añadir.
     */
    private void addTreasures(ETreasureRarity rarity, int amount) {
        for (int i = 0; i < amount; i++) {
            addTreasure(rarity);
        }
    }

    /**
     * Añade un tesoro aleatorio de la región con cierta rareza, si no se ha
     * llegado al máximo. Si la región no tiene tesoros de esa rareza, se coge
     * uno cualquiera.
     *
     * @param rarity La rareza del tesoro.
     * @return Si se ha podido añadir.
     */
    private boolean addTreasure(ETreasureRarity rarity) {
        if (this.treasuresToPlace.size() >= calculateMaxTreasure(this.terrain)) {
            return false;
        }
        Treasure treasure = this.region.getRandomTreasureByRarity(rarity);
        if (treasure == null) {
            treasure = this.region.getRandomTreasure();
        }
        if (treasure == null) {
            return false;
        }
        this.treasuresToPlace.add(treasure);
        return true;
    }

    /**
     * Coloca los tesoros generados en casillas aleatorias distintas de la
     * rejilla. Cada índice aleatorio se convierte en fila y columna.
     */
    private void placeTreasures() {
        int columns = this.terrain.getColumns();
        List<Integer> positions = UtilsHelper.genRandomIdxs(this.treasuresToPlace.size(), this.terrain.getCells());

        for (int i = 0; i < positions.size(); i++) {
            int row = positions.get(i) / columns;
            int column = positions.get(i) % columns;
            Treasure treasure = this.treasuresToPlace.get(i);

            this.hiddenTreasures[row][column] = treasure;
            this.hiddenCodes[row][column] = treasure.getRandomPartCode();
            this.rejilla.placeTreasure(new Coordinate(row, column), treasure.getRarity());
        }
    }

    /**
     * Devuelve las coordenadas de la rejilla que tienen tesoro o que están
     * vacías.
     *
     * @param withTreasure true para las casillas con tesoro, false para las
     * vacías.
     * @return La lista de coordenadas.
     */
    private List<Coordinate> getCoordinates(boolean withTreasure) {
        List<Coordinate> result = new ArrayList<>();
        for (int r = 0; r < this.terrain.getRows(); r++) {
            for (int c = 0; c < this.terrain.getColumns(); c++) {
                Coordinate coord = new Coordinate(r, c);
                if (this.rejilla.hasTreasure(coord) == withTreasure) {
                    result.add(coord);
                }
            }
        }
        return result;
    }

    /**
     * @return Cuántas casillas más pueden revelar las habilidades sin pasar del
     * 25 %.
     */
    private int getRevealsLeft() {
        int maxReveal = (int) (this.terrain.getCells() * MAX_REVEAL_RATIO);
        return maxReveal - this.revealedBySkills;
    }

    /**
     * Habilidades de limpieza: descubre casillas vacías al azar antes de
     * empezar. Entre limpieza y suposición no se puede revelar más del 25 % de
     * las casillas.
     *
     * @param amount El número de casillas a limpiar.
     */
    private void applyCleaning(int amount) {
        List<Coordinate> empties = getCoordinates(false);

        /**
         * No podemos limpiar más casillas de las que hay vacías ni pasar del 25
         * %
         */
        int toClean = Math.min(amount, empties.size());
        toClean = Math.min(toClean, getRevealsLeft());
        if (toClean <= 0) {
            return;
        }

        List<Integer> chosen = UtilsHelper.genRandomIdxs(toClean, empties.size());
        for (int index : chosen) {
            this.rejilla.clean(empties.get(index));
            this.revealedBySkills++;
        }
    }

    /**
     * Habilidades de suposición: por cada una, elige 3 casillas (una con tesoro
     * y dos vacías) y las marca con '?'. Se aplica después de la limpieza: si
     * una de las elegidas ya está limpiada, no se busca otra y queda una '?'
     * menos.
     *
     * @param skills El número de habilidades de suposición.
     */
    private void applyGuesses(int skills) {
        List<Coordinate> withTreasure = getCoordinates(true);
        List<Coordinate> empties = getCoordinates(false);

        for (int i = 0; i < skills; i++) {
            /**
             * Hacen falta al menos 1 casilla con tesoro y 2 vacías
             */
            if (withTreasure.isEmpty() || empties.size() < 2) {
                return;
            }

            List<Coordinate> chosen = new ArrayList<>();
            chosen.add(withTreasure.get(RANDOM.nextInt(withTreasure.size())));
            List<Integer> emptyIdxs = UtilsHelper.genRandomIdxs(GUESSES_PER_SKILL - 1, empties.size());
            for (int index : emptyIdxs) {
                chosen.add(empties.get(index));
            }

            for (Coordinate coord : chosen) {
                if (getRevealsLeft() <= 0) {
                    return;
                }
                /**
                 * markGuess devuelve false si la casilla ya estaba limpiada o
                 * marcada
                 */
                if (this.rejilla.markGuess(coord)) {
                    this.revealedBySkills++;
                }
            }
        }
    }

    /**
     * Habilidad de investigación: muestra cuántos tesoros quedan y de qué
     * rareza.
     */
    private void showInvestigation() {
        int common = 0;
        int uncommon = 0;
        int rare = 0;

        for (Coordinate coord : getCoordinates(true)) {
            /**
             * Solo contamos los que todavía no se han excavado
             */
            if (!this.rejilla.isRevealed(coord)) {
                ETreasureRarity rarity = this.rejilla.getRarity(coord);
                if (rarity == ETreasureRarity.COMMON) {
                    common++;
                } else if (rarity == ETreasureRarity.UNCOMMON) {
                    uncommon++;
                } else {
                    rare++;
                }
            }
        }
        System.out.println("Tesoros: " + common + " comunes, "
            + uncommon + " infrecuentes y " + rare + " raros.");
    }

    /**
     * Indica si la excavación ha terminado: sin acciones o con todos los
     * tesoros encontrados.
     *
     * @return Si ha terminado.
     */
    public boolean isFinished() {
        return this.actions <= 0 || this.foundTreasures >= this.totalTreasures;
    }

    /**
     * Bucle de la excavación. En cada turno muestra las acciones restantes, la
     * investigación si la tiene y la rejilla, y pide las coordenadas a excavar
     * hasta que la excavación termine. Si se encuentran todos los tesoros, lo
     * notifica.
     */
    public void excavate() {
        while (!isFinished()) {
            System.out.print(this.rejilla.render());
            System.out.println("Número de acciones restantes: " + this.actions);
            if (this.archeologist.hasInvestigation()) {
                showInvestigation();
            } else {
                System.out.println("Tesoros: " + this.totalTreasures);
            }

            List<Coordinate> coords = new ArrayList<>();
            while (coords.isEmpty()) {
                String line = InputValidator.readLine("Conjunto de coordenadas a excavar separadas por espacios: ");
                coords = CoordinateValidator.parseAll(line, this.rejilla, this.actions);
            }
            for (Coordinate coord : coords) {
                digAt(coord);
                if (isFinished()) break;
            }
            if (!isFinished()) System.out.println("----------------------------------------");
        }
        if (this.foundTreasures >= this.totalTreasures) {
            System.out.println("-------------------- ¡Se han encontrado todos los tesoros! --------------------");
        }
    }

    /**
     * Excava una casilla: gasta una acción y, si hay tesoro, lo guarda como
     * encontrado junto con su código completo con número de parte.
     *
     * @param coord La casilla a excavar.
     */
    private void digAt(Coordinate coord) {
        this.actions--;
        ETreasureRarity rarity = this.rejilla.dig(coord);

        if (rarity != null) {
            Treasure treasure = this.hiddenTreasures[coord.getRow()][coord.getColumn()];
            this.foundTreasuresList.add(treasure);
            this.foundCodes.add(this.hiddenCodes[coord.getRow()][coord.getColumn()]);
            this.foundTreasures++;
        }
    }

    /**
     * Finaliza la excavación: muestra la rejilla final, la rejilla descubierta
     * y el resumen de tesoros encontrados por rareza, y devuelve los códigos de
     * los tesoros encontrados para que Archaios los registre en Stats y los
     * envíe al Museo.
     *
     * @return Los códigos completos (con número de parte) de los tesoros
     * encontrados.
     */
    public List<String> finish() {
        System.out.println("Excavación terminada");
        System.out.print(this.rejilla.render());
        System.out.print(this.rejilla.renderUncovered());
        System.out.println("Se han encontrado:");

        for (ETreasureRarity rarity : ETreasureRarity.values()) {
            int total = 0;
            int found = 0;
            for (Treasure t : this.treasuresToPlace) {
                if (t.getRarity() == rarity) {
                    total++;
                }
            }
            for (Treasure t : this.foundTreasuresList) {
                if (t.getRarity() == rarity) {
                    found++;
                }
            }
            /**
             * Solo mostramos las rarezas que había en la rejilla
             */
            if (total > 0) {
                System.out.println(found + "/" + total + " tesoros " + getPlural(rarity) + ".");
            }
        }
        return new ArrayList<>(this.foundCodes);
    }

    /**
     * @return Los tesoros encontrados (los objetos Treasure), para mostrar su
     * nombre, descripción, tipo y rareza.
     */
    public List<Treasure> getFoundTreasures() {
        return new ArrayList<>(this.foundTreasuresList);
    }

    /**
     * Devuelve el nombre de la rareza en plural para los mensajes.
     *
     * @param rarity La rareza.
     * @return El nombre en plural (comunes, infrecuentes o raros).
     */
    private String getPlural(ETreasureRarity rarity) {
        if (rarity == ETreasureRarity.COMMON) {
            return "comunes";
        } else if (rarity == ETreasureRarity.UNCOMMON) {
            return "infrecuentes";
        } else {
            return "raros";
        }
    }
}
