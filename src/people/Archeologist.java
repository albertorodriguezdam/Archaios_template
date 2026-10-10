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
    private List<Skill> habilities; 
    /**Lista de terrenos accesibles para el arqueólogo. */
    private List<String> terrains; 
    /**Rareza del arqueólogo (Común, infrecuente o raro). */
    private  String rarity; 

    /**
     * Constructor base de la clase Arqueólogo, que inicializa el nievl y experiencia a 0,
     * las dos listas a ArrayList y añade el único terreno disponible al instanciar
     * un nuevo arqueólogo: "COMUN".
     * @param name Nombre del arqueólogo.
     * @param rarity Rareza del arqueólogo (Común, infrecuente o raro).
     */
    public Archeologist(String name, String rarity) {
        this.name = name;
        this.level = 0;
        this.xp = 0;
        this.rarity = (rarity != null) ? rarity : "COMUN";
        this.habilities = new ArrayList<>();
        this.terrains = new ArrayList<>();
        this.terrains.add("Cuadrado");
    }

    /**
     * método de obtención de la cantidad de experiencia restante del arqueólogo,
     * para subir al siguiente nivel. En el caso de que el arqueólogo alacance el nivel máximo
     * (nivel 5), devolverá 0.
     * @return Cantidad de xp restante para subir de nivel, si es nivel máximo devuelve 0; 
     */
    public int getXpToUpLevel(){

        if (level<5) {
             return 50+(50*level); 
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
        int uploadedLevels=0; 

        if (xpGained>0) {

            this.xp+=xpGained; 

            while (level<5&&this.xp>=getXpToUpLevel()) {
                this.xp-=getXpToUpLevel(); 
                this.level++;
                uploadedLevels++;
                updateTerrainByLevel();
            }

            return uploadedLevels; 
        } else {
            return uploadedLevels; 
        }
    }

    private void updateTerrainByLevel(){

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
    public List<Skill> getHabilities() {
        return Collections.unmodifiableList(habilities);
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

    
}
