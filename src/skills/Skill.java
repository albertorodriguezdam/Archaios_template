package skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**Clase base de todas las habilidades de Archaios */
public class Skill {

    /**Nombre de la habilidad. */
    private String nombre; 
    /**Descripción de lo que hace la habilidad. */
    private String descripcion; 
    /**Tipo de habilidad: puede ser de terreno, excavación o tesoros. */
    private String tipo; 
    /**Lista de habilidades que el arqueólogo debe haber aprendido antes de conseguir una en concreto. */
    private List<Skill> dependencias; 
    /**Lista de habilidades que son incompatibles con otras al mismo tiempo. */
    private List<Skill> incompatibilidades;

    /**
     * Constructor base de la clase Skill.
     * @param nombre El nombre identificativo de la habilidad.
     * @param descripcion Descripción de lo que hace la habilidd.
     * @param tipo El tipo de habilidad.
    */
    public Skill(String nombre, String descripcion, String tipo){

        this.nombre=nombre;
        this.descripcion=descripcion;
        this.tipo=tipo;
        this.dependencias=new ArrayList<>();
        this.incompatibilidades=new ArrayList<>();
    }

    /**
     * Constructor que inicializa una habilidad a partir de su tipo enumerado.
     * Convierte el tipo a su representación textual.
     * @param nombre El nombre identificativo de la habilidad.
     * @param descripcion Descripción de lo que hace la habilidd.
     * @param tipo El tipo de habilidad según la enumeración.
    */
    public Skill(String nombre, String descripcion, ESkillType tipo){
        this(nombre, descripcion, tipo !=null ? tipo.toString() : "");
    }

    /**Getters básicos de la clase Skill. */
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public List<Skill> getDependencias() {
        return Collections.unmodifiableList(dependencias);
    }

    public List<Skill> getIncompatibilidades() {
        return Collections.unmodifiableList(incompatibilidades);
    }

    /**
     * Añade una Skill en la lista de dependencias,
     * solo sino es null y sino la contiene.
     * @param skill objeto de tipo Skill. 
     */
    public void addDependencia(Skill skill){
        if (skill!=null&& !this.dependencias.contains(skill)) {
            this.dependencias.add(skill); 

            if (!skill.incompatibilidades.contains(this)) {
            skill.incompatibilidades.add(this);
            }
        }
    }

    /**
     * Añade una Skill en la lista de incompatibilidades,
     * solo sino es null y sino la contiene, 
     * además esta incompatibilidad es bidireccional, 
     * lo que significa que al añadirlo a la lista de incompatibilidades de una,
     * automáticamente se añade a la lista de imcompatibilidades, 
     * de la Skill con la que se está comparando. 
     * @param skill objeto de tipo Skill.
     */
    public void addIncompatibilidad(Skill skill){

        if (skill!=null && this.incompatibilidades.contains(skill)) {

            this.incompatibilidades.add(skill);

            if (!skill.incompatibilidades.contains(this)) {
                skill.incompatibilidades.add(this);
            }
        }
    }

    /**
     * @return Devuelve la lista inmutable o vacía de terrenos que añade. 
     */
    public List<String> getTerrenos(){
        return Collections.emptyList();
    }

    /**Bonos de tesoros (Skill tesoros)*/

    /**
     * Devuelve la cantidad adicional de tesoros aleatorios que se deben generar en el tablero.
     * @return catidad de tesoros aleatorios extra, 0 por defecto.
     */
    public int getExtraRandomTesoros(){
        return 0; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros comunes que se deben generar en el tablero.
     * @return catidad de tesoros comunes extra, 0 por defecto.
     */
    public int getExtraComunTesoros(){
        return 0; 
    }
    
    /**
     * Devuelve la cantidad adicional de tesoros infrecuentes que se deben generar en el tablero.
     * @return catidad de tesoros infrecuentes extra, 0 por defecto.
     */
    public int getExtraInfrecuenteTesoros(){
        return 0; 
    }

    /**
     * Devuelve la cantidad adicional de tesoros raros que se deben generar en el tablero.
     * @return catidad de tesoros raros extra, 0 por defecto.
     */
    public int getExtraRaroTesoros(){
        return 0; 
    }
    
    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de tipo infrecuente.
     * @return incremento de la probabilidad, 0 por defecto.
     */
    public int getExtraInfrecuenteProb(){
        return 0; 
    }

    /**
     * Devuelve el porcentaje adicional aplicado a la base de generación de tesoros, 
     * de tipo raro.
     * @return incremento de la probabilidad, 0 por defecto.
     */
    public int getExtraRaroProb(){
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
        return nombre + " (" + tipo + "): " + descripcion;
    }
    
}

