package skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alberto Rodríguez Domínguez
 * Clase base de todas las habilidades de Archaios 
 * */
public class Skill {

    /**Nombre de la habilidad. */
    private String name; 
    /**Descripción de lo que hace la habilidad. */
    private String description; 
    /**Tipo de habilidad: puede ser de terreno, excavación o tesoros. */
    private String type; 
    /**Lista de habilidades que el arqueólogo debe haber aprendido antes de conseguir una en concreto. */
    private List<Skill> dependecies; 
    /**Lista de habilidades que son incompatibles con otras al mismo tiempo. */
    private List<Skill> incompatibilities;

    /**
     * Constructor base de la clase Skill.
     * @param name El name identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidd.
     * @param type El type de habilidad.
    */
    public Skill(String name, String description, String type){

        this.name=name;
        this.description=description;
        this.type=type;
        this.dependecies=new ArrayList<>();
        this.incompatibilities=new ArrayList<>();
    }

    /**
     * Constructor que inicializa una habilidad a partir de su type enumerado.
     * Convierte el type a su representación textual.
     * @param name El name identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidd.
     * @param type El type de habilidad según la enumeración.
    */
    public Skill(String name, String description, ESkillType type){
        this(name, description, type !=null ? type.toString() : "");
    }

    /**
     * @return Nombre de la habilidad.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Descripción de la habilidad.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Tipo de habilidad(Tesoros, excavación, terrenos, especialista)
     */
    public String getType() {
        return type;
    }

    /**
     * @return Lista no modificable de dependencias entre habilidades.
     */
    public List<Skill> getDependecies() {
        return Collections.unmodifiableList(dependecies);
    }

    /**
     * @return Lista no modificable de incompatibilidades entre habilidades.
     */
    public List<Skill> getIncompatibilities() {
        return Collections.unmodifiableList(incompatibilities);
    }

    /**
     * Añade una Skill en la lista de dependecies,
     * solo sino es null y sino la contiene.
     * @param skill objeto de type Skill. 
     */
    public void addDependencie(Skill skill){
        if (skill!=null&& !this.dependecies.contains(skill)) {
            this.dependecies.add(skill); 

            if (!skill.incompatibilities.contains(this)) {
            skill.incompatibilities.add(this);
            }
        }
    }

    /**
     * Añade una Skill en la lista de incompatibilities,
     * solo sino es null y sino la contiene, 
     * además esta incompatibilidad es bidireccional, 
     * lo que significa que al añadirlo a la lista de incompatibilities de una,
     * automáticamente se añade a la lista de imcompatibilidades, 
     * de la Skill con la que se está comparando. 
     * @param skill objeto de type Skill.
     */
    public void addIncompatibility(Skill skill){

        if (skill!=null && this.incompatibilities.contains(skill)) {

            this.incompatibilities.add(skill);

            if (!skill.incompatibilities.contains(this)) {
                skill.incompatibilities.add(this);
            }
        }
    }

    /**
     * @return Devuelve la lista inmutable o vacía de terrenos que añade. 
     */
    public List<String> getTerrains(){
        return Collections.emptyList();
    }

    /**Bonos de tesoros (Skill tesoros)*/

    /**
     * Devuelve la cantidad adicional de tesoros aleatorios que se deben generar en el tablero.
     * @return catidad de tesoros aleatorios extra, 0 por defecto.
     */
    public int getExtraRandomTreasures(){
        return 0; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros comunes que se deben generar en el tablero.
     * @return catidad de tesoros comunes extra, 0 por defecto.
     */
    public int getExtraCommonTreasures(){
        return 0; 
    }
    
    /**
     * Devuelve la cantidad adicional de tesoros infrecuentes que se deben generar en el tablero.
     * @return catidad de tesoros infrecuentes extra, 0 por defecto.
     */
    public int getExtraUncommonTreasures(){
        return 0; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros raros que se deben generar en el tablero.
     * @return catidad de tesoros raros extra, 0 por defecto.
     */
    public int getExtraRareTreasures(){
        return 0; 
    }
    
    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de type infrecuente.
     * @return incremento de la probabilidad, 0 por defecto.
     */
    public int getExtraUncommonProb(){
        return 0; 
    }

    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de type raro.
     * @return incremento de la probabilidad, 0 por defecto.
     */
    public int getExtraRareProb(){
        return 0; 
    }

    /**Bonos de excavación (Skill excavación)[SIN HCER] */

    /**Bonos de terreno (Skill terreno)[SIN HACER] */


    /**
     * Devueleve el objeto Skill en forma de String.
     * @return Skill a texto.
     */
    @Override
    public String toString() {
        return name + " (" + type + "): " + description;
    }
    
}

