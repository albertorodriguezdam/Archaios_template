package people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import skills.Skill;

/**
 * @author Alberto Rodríguez Domínguez
 * Clase encargada del Arqueólogo.
 */
public class Archeologist {

    /**Nombre del arqueólogo. */
    private String name; 
    /**Nivel del arqueólogo. */
    private int level; 
    /**Experiencia del arqueólogo. */
    private int xp; 
    /**Lista de habilidades pertenecientes al arqueólogo. */
    private List<Skill> skills; 
    /**Lista de terrenos accesibles para el arqueólogo. */
    private List<String> terrains; 
    /**Rareza del arqueólogo (Común, infrecuente o raro). */
    private  String rarity; 

    /**
     * Constructor base de la clase Arqueólogo, que inicializa el nivel y experiencia a 0,
     * las dos listas a ArrayList y añade el único terreno disponible al instanciar
     * un nuevo arqueólogo: "pequeño".
     * @param name Nombre del arqueólogo.
     * @param rarity Rareza del arqueólogo (Común, infrecuente o raro).
     */
    public Archeologist(String name, String rarity) {
        this.name = name;
        this.level = 0;
        this.xp = 0;
        this.rarity = (rarity != null) ? rarity.toUpperCase() : "COMUN";
        this.skills = new ArrayList<>();
        this.terrains = new ArrayList<>();
        this.terrains.add("Pequeño");
    }

    /**
     * Constructor para arqueólogos de rareza común por defecto.
     * @param name Nombre del arqueólogo.
     */
    public Archeologist(String name){
        this(name, "COMUN");
    }

    /**
     * método de obtención de la cantidad de experiencia restante del arqueólogo,
     * para subir al siguiente nivel. En el caso de que el arqueólogo alacance el nivel máximo
     * (nivel 5), devolverá 0.
     * @return Cantidad de xp restante para subir de nivel, si es nivel máximo devuelve 0; 
     */
    public int getXpToNextLevel(){

        if (this.level<5) {
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

            while (level<5&&this.xp>=getXpToNextLevel()) {
                this.xp-=getXpToNextLevel(); 
                this.level++;
                levelGained++;
                updateTerrainByLevel();
            }

            return levelGained; 
        } else {
            return levelGained; 
        }
    }

    /**
     * Actualiza la lista de terrenos, añadiendo nuevos, dependiendo en que nivel
     * esté en arqueólogo. Añade terrenos medianos a nivel 2 y terreno grandes a nivel 4.
     */
    private void updateTerrainByLevel(){

        if (this.level >= 2 && !this.terrains.contains("Mediano")) {

        this.terrains.add("Mediano");
        }

        if (this.level >= 4 && !this.terrains.contains("Grande")) {

            this.terrains.add("Grande");
        }
    }

    /**
     * Obtención de los puntos que devueleve la acción de licenciar(vender), 
     * un arqueólogo.
     * @return Cantidad de puntos recibidos por licenciar un arqueólogo.
     */
    public int getLicensingPoints(){

        int points=0; 

        if (level>0) {
            points=level*10; 
        } 

        if ("INFRECUENTE".equals(rarity)) {
            points+=10; 
        } else if ("RARO".equals(rarity)) {
            points+=20; 
        }

        return points; 
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
                if (!this.terrains.contains(t)) {
                    this.terrains.add(t);
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

        if (level==5) {
            total+=(xp/25); 
        }

        return total; 
    }

    /**
     * Calcula el porcentaje sumado sobre la cantidad de acciones totales, 
     * a partir de la lista de habilidades.
     * @return Porcentaje total de acciones añadidas, sobre las que ya le pertenecen al arqueólogo.
     */
    public int getTotalExtraActionsPercent(){

        int total=0;

        for (Skill s : skills) {
            total+=s.getExtraActionsPercent(); 
        }

        return total; 
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

        if (level==5) {
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

        if (level==5) {
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

        if (level==5) {
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

        if (level==5) {
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
     * Obtención de la lista de habilidades disponibles del arqueólogo.
     * @return Lista inmutable de las habilidades disponibles del arqueólogo.
     */
    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    /**
     * Obtención de la lista de terrenos disponibles del arqueólogo.
     * @return Lista inmutable de los terrenos disponibles del arqueólogo.
     */
    public List<String> getTerrains() {
        return Collections.unmodifiableList(terrains);
    }

    /**
     * Obtención de la rareza del arqueólogo.
     * @return Rareza del arqueólogo.
     */
    public String getRarity() {
        return rarity;
    }

    /**    
     * Método que devuelve el objeto arqueólogo en una String.
     * @see java.lang.Object#toString()
     */
    @Override 
    public  String toString(){
        return name + " [Nivel: " + level + " | XP: " + xp + "/" + (level < 5 ? getXpToNextLevel() : "MAX") + "]"
                + " - Rareza: " + rarity
                + " - Terrenos: " + terrains;
    }
}
