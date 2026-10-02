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

    /**Constructor base de la clase Skill */
    public Skill(String nombre, String descripcion, String tipo){

        this.nombre=nombre;
        this.descripcion=descripcion;
        this.tipo=tipo;
        this.dependencias=new ArrayList<>();
        this.incompatibilidades=new ArrayList<>();
    }

    /**Getters básicos de la clases. */
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
        return dependencias;
    }

    public List<Skill> getIncompatibilidades() {
        return incompatibilidades;
    }

    /**
     * @param skill objeto de tipo Skill 
     * Añade una Skill en la lista de dependencias,
     *  solo sino es null y sino la contiene. */
    public void addDependencia(Skill skill){
        if (skill!=null&& !this.dependencias.contains(skill)) {
            this.dependencias.add(skill); 
        }
    }

    /**
     * @param skill objeto de tipo Skill 
     * Añade una Skill en la lista de incompatibilidades,
     * solo sino es null y sino la contiene, 
     * además esta incompatibilidad es bidireccional, 
     * lo que significa que al añadirlo a la lista de incompatibilidades de una,
     * automáticamente se añade a la lista de imcompatibilidades, 
     * de la Skill con la que se está comparando. */
    public void addIncompatibilidad(Skill skill){

        if (skill!=null && this.incompatibilidades.contains(skill)) {

            this.incompatibilidades.add(skill);

            if (!skill.incompatibilidades.contains(this)) {
                skill.incompatibilidades.add(this);
            }
        }
    }

    /**Devuelve la lista de terrenos que añade o modifica esta habilidad. */
    public List<String> getTerrenos(){
        return Collections.emptyList();
    }

    
}

