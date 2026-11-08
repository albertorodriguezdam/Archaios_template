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
     * Crea un arqueólogo de nivel 0, sin experiencia, con acceso a terrenos pequeños y formato cuadrado.
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
     * Reconstruye la progresión de un arqueólogo desde datos guardados.
     * @param name Nombre del arqueólogo.
     * @param level Nivel del arqueólogo.
     * @param xp Experiencia del arqueólogo.
     * @throws IllegalArgumentException si el nivel o la experiencia no representan una progresión válida.
     */
    public Archeologist(String name, int level, int xp){
        this(name);

        if (level < 0 || level > Archeologist.MAX_LEVEL) {
        throw new IllegalArgumentException("El nivel guardado está fuera del intervalo permitido.");
        }

        if (xp < 0) {
            throw new IllegalArgumentException("La experiencia guardada no puede ser negativa.");
        }

        this.level = level;

        if (level < Archeologist.MAX_LEVEL && xp >= getXpToNextLevel()) {
            throw new IllegalArgumentException("La experiencia guardada supera el progreso permitido para el nivel.");
        }

        this.xp = xp;
        updateTerrainByLevel();
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
     * Sustituye las habilidades por las recuperadas de una partida.
     * Reconstruye los formatos de terreno a partir de esas habilidades.
     * @param savedSkills Instancias del catálogo previamente resueltas.
     * @throws IllegalArgumentException si hay habilidades nulas, repetidas,
     *         incompatibles o con dependencias ausentes.
     */
    public void restoreSkills(List<Skill> savedSkills) {
        if (savedSkills == null) {
            throw new IllegalArgumentException(
                "La lista de habilidades guardadas no puede ser null."
            );
        }

        List<Skill> restoredSkills = new ArrayList<>();

        for (Skill skill : savedSkills) {
            if (skill == null || restoredSkills.contains(skill)) {
                throw new IllegalArgumentException(
                    "Hay habilidades guardadas nulas o repetidas."
                );
            }

            restoredSkills.add(skill);
        }

        List<String> restoredFormats = new ArrayList<>();
        restoredFormats.add("Cuadrado");

        for (Skill skill : restoredSkills) {
            if (!restoredSkills.containsAll(skill.getDependencies())) {
                throw new IllegalArgumentException(
                    "Faltan dependencias de la habilidad: " + skill.getName()
                );
            }

            for (Skill other : restoredSkills) {
                if (skill.getIncompatibilities().contains(other)) {
                    throw new IllegalArgumentException(
                        "Hay habilidades incompatibles: "
                            + skill.getName() + " y " + other.getName()
                    );
                }
            }

            for (String format : skill.getTerrains()) {
                if (!restoredFormats.contains(format)) {
                    restoredFormats.add(format);
                }
            }
        }

        skills.clear();
        skills.addAll(restoredSkills);

        terrainsFormats.clear();
        terrainsFormats.addAll(restoredFormats);
    }

    /**
     * Recupera las licencias regionales y la especialización de una partida.
     * Valida los datos antes de sustituir el estado actual.
     * @param savedLicenses Códigos de las licencias guardadas.
     * @param savedSpecialization Código de especialización; vacío si no tiene.
     * @throws IllegalArgumentException si los datos son nulos, hay licencias vacías o repetidas, o la especialización no corresponde al nivel.
     */
    public void restoreRegionalData(List<String> savedLicenses, String savedSpecialization) {

        if (savedLicenses == null || savedSpecialization == null) {
            throw new IllegalArgumentException(
                "Los datos regionales guardados no pueden ser null."
            );
        }

        if (!savedSpecialization.isEmpty() && savedSpecialization.isBlank()) {
            throw new IllegalArgumentException(
                "El código de especialización no puede contener solo espacios."
            );
        }

        if (!savedSpecialization.isEmpty() && level < Archeologist.MAX_LEVEL) {
            throw new IllegalArgumentException(
                "Un arqueólogo por debajo del nivel máximo no puede estar especializado."
            );
        }

        List<String> restoredLicenses = new ArrayList<>();

        for (String code : savedLicenses) {

            if (code == null || code.isBlank() || restoredLicenses.contains(code)) {
                throw new IllegalArgumentException(
                    "Hay licencias guardadas nulas, vacías o repetidas."
                );
            }

            restoredLicenses.add(code);
        }

        licenses.clear();
        licenses.addAll(restoredLicenses);
        specialization = savedSpecialization;
    }

    /**
     * Comprueba la cantidad total de acciones extra aplicables a una región.
     * Suma los bonos de las habilidades y añade una acción por cada
     * 25 XP cuando la especialización está activa para esa región.
     * @param regionCode Código de la región donde se excava.
     * @return Cantidad total de acciones extra del arqueólogo.
     */
    public int getTotalExtraActions(String regionCode){

        int total=0;

        for (Skill s : skills) {
            total += s.getExtraActions(); 
        }

        if (isSpecializedIn(regionCode)) {
            total += xp/25; 
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
     * El extra de experiencia requiere de especialización activa sobre la región.
     * @param regionCode Código de la región donde se excava.
     * @return Cantidad de tesoros aleatorios extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalRandomTreasures(String regionCode){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraRandomTreasures(); 
        }

        if (isSpecializedIn(regionCode)) {
            total+=(xp/50); 
        }

        return total;
    }

    /**
     * Calcula la cantidad de tesoros comunes extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * El extra de experiencia requiere de especialización activa sobre la región.
     * @param regionCode Código de la región donde se excava.
     * @return Cantidad de tesoros comunes extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalCommonTreasures(String regionCode){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraCommonTreasures(); 
        }

        if (isSpecializedIn(regionCode)) {
            total+=(xp/50); 
        }

        return total;
    }

    /**
     * Calcula la cantidad de tesoros infrecuentes extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * El extra de experiencia requiere de especialización activa sobre la región.
     * @param regionCode Código de la región donde se excava.
     * @return Cantidad de tesoros infrecuentes extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalUncommonTreasures(String regionCode){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraUncommonTreasures(); 
        }

        if (isSpecializedIn(regionCode)) {
            total+=(xp/100); 
        }

        return total;
    }

    /**
     * Calcula la cantidad de tesoros raros extra que pueden aparecer 
     * a la hora de excavar, a partir de la lista de habilidades.
     * El extra de experiencia requiere de especialización activa sobre la región.
     * @param regionCode Código de la región donde se excava.
     * @return Cantidad de tesoros raros extra que podrán aparecer en la cuadrícula de excavación.
     */
    public int getTotalRareTreasures(String regionCode){

         int total=0;

        for (Skill s : skills) {
            total+=s.getExtraRareTreasures(); 
        }

        if (isSpecializedIn(regionCode)) {
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
     * Calcula el coste de excavar un terreno aplicando las habilidades.
     * @param terrainSymbol Símbolo del terreno que se va a excavar.
     * @param baseCost Coste positivo calculado por el motor de excavación.
     * @return Menor coste entre el base y los ofrecidos por las habilidades.
     * @throws IllegalArgumentException si el coste base no es positivo.
     */
    public int getExcavationCost(char terrainSymbol, int baseCost) {

        if (baseCost <= 0) {
            throw new IllegalArgumentException(
                "El coste base debe ser mayor que cero."
            );
        }

        int cost = baseCost;

        for (Skill skill : skills) {
            cost = Math.min(
                cost,
                skill.getExcavationCost(terrainSymbol, baseCost)
            );
        }

        return cost;
    }

    /**
     * Suma las conversiones a tierra aportadas por las habilidades.
     * @param terrainSymbol Símbolo del terreno que se quiere transformar.
     * @return Número total de casillas que se pueden convertir a tierra.
     */
    public int getTerrainConversions(char terrainSymbol) {
        
        int total = 0;

        for (Skill skill : skills) {
            total += skill.getTerrainConversions(terrainSymbol);
        }

        return total;
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
     * Añade una licencia regional si el código es válido y todavía no la posee.
     * @param code Código no vacío de la región.
     */
    public void addLicense(String code) {

        if (code != null && !code.isBlank() && !hasLicense(code)) {
            licenses.add(code);
        }
    }

    /**
     * Comprueba si el arqueólogo tiene una licencia regional.
     * @param code Código de la región.
     * @return true si posee la licencia; false si no la posee o el código es null.
     */
    public boolean hasLicense(String code) {
        return code != null && licenses.contains(code);
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
     * Comprueba si la especialización está activa para una región.
     * @param regionCode Código de la región donde se realiza la excavación.
     * @return true si está al nivel máximo y especializado en esa región.
     */
    public boolean isSpecializedIn(String regionCode) {
        
        return level == Archeologist.MAX_LEVEL
            && regionCode != null
            && !regionCode.isBlank()
            && regionCode.equals(specialization);
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
