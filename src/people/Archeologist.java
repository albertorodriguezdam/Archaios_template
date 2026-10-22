package people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import skills.Skill;

/**
 * Clase encargada del Arqueólogo.
 * @author Alberto Rodríguez Domínguez
 */
public class Archeologist {

    /**Constante para el nivel máximo del arqueólogo */
    private static final int MAX_LEVEL=5; 
    /**Nombre del arqueólogo. */
    private String name; 
    /**Nivel del arqueólogo. */
    private int level; 
    /**Experiencia del arqueólogo. */
    private int xp; 
    /**Lista de habilidades pertenecientes al arqueólogo. */
    private List<Skill> skills = new ArrayList<>(); 
    /**Lista de tamaños de terrenos accesibles para el arqueólogo. */
    private List<String> terrainsSize = new ArrayList<>(); 
    /**Lista de formatos de terrenos accesibles para el arqueólogo. */
    private List<String> terrainsFormats = new ArrayList<>();
    /** Licencias regionales independientes de las habilidades Licencia A y B. */
    private List<String> licenses = new ArrayList<>();
    /** Código de región de especialización; vacío antes del nivel máximo. */
    private String specialization = "";

    /**
     * Constructor base de la clase Arqueólogo, que inicializa el nivel y experiencia a 0,
     * las tres colecciones a ArrayList y añade el único terreno disponible al instanciar
     * un nuevo arqueólogo: "pequeño" y en formato "cuadrado".
     * @param name Nombre del arqueólogo.
     */
    public Archeologist(String name) {
        this.name = name;
        this.level = 0;
        this.xp = 0;
        this.terrainsSize.add("Pequeño");
        this.terrainsFormats.add("Cuadrado"); 
    }

    /**
     * Método de obtención de la cantidad de experiencia total,
     * para subir al siguiente nivel. En el caso de que el arqueólogo alacance el nivel máximo devolverá 0.
     * @return Cantidad de xp total para subir de nivel, si es nivel máximo devuelve 0; 
     */
    public int getXpToNextLevel(){

        if (this.level<Archeologist.MAX_LEVEL) {
             return 50+(50*this.level); 
        } else {
            return 0;
        }
    }

    /**
     * Añade experiencia al arqueólogo, a partir de la experiencia ganada,
     * en el caso de subir de nivel se indica la cantidad de niveles aumentados.
     * @param xpGained Cantidad de experiencia ganada por el arqueólogo.
     * @return La cantidad de niveles que ha subido el arqueólogo gracias a la experiencia ganada.
     */
    public int addXp(int xpGained){
        int levelGained=0; 

        if (xpGained>0) {

            this.xp+=xpGained; 

            while (level<Archeologist.MAX_LEVEL&&this.xp>=getXpToNextLevel()) {
                this.xp-=getXpToNextLevel(); 
                this.level++;
                levelGained++;
                updateTerrainByLevel();
            }
        } 
            return levelGained; 
    }

    /**
     * Actualiza la lista de terrenos, añadiendo nuevos, dependiendo en que nivel
     * esté en arqueólogo. Añade terrenos medianos a nivel 2 y terreno grandes a nivel 4.
     */
    private void updateTerrainByLevel(){

        if (this.level >= 2 && !this.terrainsSize.contains("Mediano")) {

        this.terrainsSize.add("Mediano");
        }

        if (this.level >= 4 && !this.terrainsSize.contains("Grande")) {

            this.terrainsSize.add("Grande");
        }
    }

    /**
     * Calcula la compensación por licenciar al arqueólogo.
     *
     * @return Diez puntos por cada habilidad aprendida.
     */
    public int getLicensingPoints() {
        return skills.size() * 10;
    }

    /**
     * Comprueba si una habilidad dada puede ser o no aprendida por un arqueólogo.
     * @param skill Habilidad la cual se comprobará si puede ser aprendida por el arqueólogo.
     * @return False en caso de ser una habilidad nula, estar ya contenida en la lista de habiliades o
     * que contenga incompatibilidades con alguna otra habilidad de la lista de habilidades.
     * @return True si cumple las condiciones anteriores y el arqueólogo ha aprendido las 
     * habilidades previas necesarias para poder aprenderla.
     */
    public boolean canLearn(Skill skill){

        if (skill==null|| skills.contains(skill)) {
            return false; 
        }

        for (Skill s : skills) {
            if (skill.getIncompatibilities().contains(s)) {
                return false; 
            }
        }

        return skills.containsAll(skill.getDependencies()); 
    }

    /**
     * Añade un habilidad a la lista de habilidades si la puede aprender (canLearn()), 
     * en caso de ser una habilidad de terreno, añade la modificación de terreno 
     * a la lista de terrenos.
     * @param skill Habilidad para aprender el arqueólogo.
     * @return True si la habilidad ha sido aprendida por el arqueólogo.
     * @return False si la habilidad no ha sido aprendida por el arqueólogo.
     */
    public boolean learnSkill(Skill skill){

        if (canLearn(skill)) {
            this.skills.add(skill); 

            for (String t : skill.getTerrains()) {
                if (!this.terrainsFormats.contains(t)) {
                    this.terrainsFormats.add(t);
                }
            }

            return true; 

        } else {
            return false; 
        }
    }

    /**
     * Comprueba la cantidad total de acciones extra del arqueólogo,
     * a partir de su lista de habilidades. Una vez alcanzado el nivel 5
     * la experiencia obtenida aumenta los bonos, en este caso acciones,
     * aumenta 1 cada 25 de experiencia.
     * @return Cantidad total de acciones extra del arqueólogo.
     */
    public int getTotalExtraActions(){

        int total=0;

        for (Skill s : skills) {
            total+=s.getExtraActions(); 
        }

        if (level==Archeologist.MAX_LEVEL) {
            total+=(xp/25); 
        }

        return total; 
    }

    /**
     * Calcula el factor de aumento de acciones multiplicando los factores
     * individuales de las habilidades de experiencia.
     * @return Factor multiplicativo de acciones; 1.0 si no hay bonificaciones.
     */
    public double getActionsMultiplier(){

        double multiplier=1.0; 

        for (Skill s : skills) {
            multiplier*=1.0+s.getExtraActionsPercent()/100.0; 
        }

        return multiplier; 
    }

    /**
     * Calcula la cantidad de tesoros aleatorios extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * Una vez alcanzado el nivel 5, la experiencia obtenida aumenta los bonos, 
     * en este caso la aparición de tesoros aleatorios, aumenta 1 cada 50 de experiencia.
     * @return Cantidad de tesoros aleatorios extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalRandomTreasures(){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraRandomTreasures(); 
        }

        if (level==Archeologist.MAX_LEVEL) {
            total+=(xp/50); 
        }

        return total;
    }

    /**
     * Calcula la cantidad de tesoros comunes extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * Una vez alcanzado el nivel 5, la experiencia obtenida aumenta los bonos, 
     * en este caso la aparición de tesoros comunes, aumenta 1 cada 50 de experiencia.
     * @return Cantidad de tesoros comunes extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalCommonTreasures(){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraCommonTreasures(); 
        }

        if (level==Archeologist.MAX_LEVEL) {
            total+=(xp/50); 
        }

        return total;
    }

    /**
     * Calcula la cantidad de tesoros infrecuentes extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * Una vez alcanzado el nivel 5, la experiencia obtenida aumenta los bonos, 
     * en este caso la aparición de tesoros infrecuentes, aumenta 1 cada 100 de experiencia.
     * @return Cantidad de tesoros infrecuentes extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalUncommonTreasures(){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraUncommonTreasures(); 
        }

        if (level==Archeologist.MAX_LEVEL) {
            total+=(xp/100); 
        }

        return total;
    }

    /**
     * Calcula la cantidad de tesoros raros extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * Una vez alcanzado el nivel 5, la experiencia obtenida aumenta los bonos, 
     * en este caso la aparición de tesoros raros, aumenta 1 cada 200 de experiencia.
     * @return Cantidad de tesoros raros extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalRareTreasures(){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraRareTreasures(); 
        }

        if (level==Archeologist.MAX_LEVEL) {
            total+=(xp/200); 
        }

        return total;
    }

    /**
     * Calculo del aumento total de probabilidad de obtención de tesoros infrecuentes, 
     * a partir de la lista de habilidades.
     * @return Aumento total de la probabilidad de obtención de tesoros infrecuentes.
     */
    public int getTotalUncommonProb(){

        int total=0; 

        for (Skill s : skills) {
            total+=s.getExtraUncommonProb(); 
        }

        return total; 
    }

    /**
     * Calculo del aumento total de probabilidad de obtención de tesoros raros, 
     * a partir de la lista de habilidades.
     * @return Aumento total de la probabilidad de obtención de tesoros raros.
     */
    public int getTotalRareProb(){

        int total=0; 

        for (Skill s : skills) {
            total+=s.getExtraRareProb(); 
        }

        return total; 
    }

    /**
     * Obtención de la cantidad de casillas totales vacías, 
     * a partir de la lista de habilidades.
     * @return Cantidad total de casillas reveladas vacías.
     */
    public int getTotalEmptyCellsReveled(){

        int total=0; 

        for (Skill s : skills) {
            total+=s.getEmptyCellsReveled();
        }

        return total; 
    }

    /**
     * Obtención de la cantidad de casillas totales con interrogación, 
     * a partir de la lista de habilidades.
     * @return Cantidad total de casillas con interrogación.
     */
    public int getTotalScanHints(){

        int total=0; 

        for (Skill s : skills) {
            total+=s.getScanHints();
        }

        return total; 
    }

    /**
     * Se obtiene si el arqueólogo tiene la habilidad de investigación, 
     * a partir de la lista de habilidades.
     * @return True si tiene la habilidad de investigación.
     * @return False si no tiene la habilidad de investigación.
     */
    public boolean hasInvestigation() {

        for (Skill s : skills) {

            if (s.hasInvestigation()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Obtención del nombre del arqueólogo.
     * @return Nombre del arqueólogo.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtención del nivel del arqueólogo.
     * @return Nivel del arqueólogo.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Obtención de la experiencia que posee el arqueólogo.
     * @return Experiencia acumulada del arqueólogo.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Obtención de la lista de habilidades aprendidas por el arqueólogo.
     * @return Lista inmutable de las habilidades aprendidas por el arqueólogo.
     */
    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    /**
     * Obtención de la lista de tamaños de terrenos disponibles del arqueólogo.
     * @return Lista inmutable de los tamaños de terrenos disponibles del arqueólogo.
     */
    public List<String> getTerrainsSize() {
        return Collections.unmodifiableList(terrainsSize);
    }

    /**
     * Obtención de la lista de formatos de terrenos disponibles del arqueólogo.
     * @return Lista inmutable de los formatos de terrenos disponibles del arqueólogo.
     */
    public List<String> getTerrainsFormats() {
        return Collections.unmodifiableList(terrainsFormats);
    }

    /**
     *Obtención del nivel máximo al que puede llegar el arqueólogo.
     * @return Nivel máximo del arqueólogo.
     */
    public int getMAX_LEVEL(){
        return Archeologist.MAX_LEVEL;
    }

    /**
     * Permite consultar las licencias regionales sin modificar la lista.
     * @return Lista inmutable de licencias.
     */
    public List<String> getLicenses() {
        return Collections.unmodifiableList(licenses);
    }

    /**
     * Añade una licencia regional solo si todavía no está en la lista.
     * @param code Código de la región a añadir.
     */
    public void addLicense(String code) {
        if (!licenses.contains(code)) licenses.add(code);
    }

    /**
     * Devuelve el código de la región elegida para la especialización.
     * @return Código de la región elegida, cadena vacía si no tiene especialización.
     */
    public String getSpecialization() { 
        return specialization; 
    }
    
    /**
     * Establece la región de especialización del arqueólogo.
     * @param code Código de la región elegida para la especialización.
     */
    public void setSpecialization(String code) { 
        specialization = code; 
    }
    
    /**    
     * Método que devuelve el objeto arqueólogo en una String.
     * @see java.lang.Object#toString()
     */
    @Override 
    public  String toString(){
        String experience = level < Archeologist.MAX_LEVEL ? xp + "/" + getXpToNextLevel() : Integer.toString(xp);

        return name + " [Nivel: " + level + " | XP: " + experience + "]"
            + " - Tamaños: " + terrainsSize
            + " - Formatos: " + terrainsFormats;
    }

    

}
