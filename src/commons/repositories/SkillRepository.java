package commons.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import people.Archeologist;
import skills.*;

/**
 * Repositorio encargado de registrar y gestionar las habilidades del sistema.
 * @author Constantin Laurentiu
 */
public final class SkillRepository {
    private static final Map<String, Skill> skills = new LinkedHashMap<>();

    /**No se crean objetos del repositorio se accede mediante sus métodos estáticos .*/
    private SkillRepository() { }

    /**Crea el catálogo una vez y añade sus dependencias e incompatibilidades.*/
    public static void register() {
        
        if (!skills.isEmpty()) return;
        
        register(new SkillTreasures("Aprendiz", "Aumenta el número de tesoros posibles de la excavación en 5 comunes", 0, 5, 0, 0, 0, 0));
        register(new SkillExcavation("Escaneo A", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        register(new SkillExcavation("Escaneo B", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        register(new SkillExcavation("Escaneo C", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        register(new SkillExcavation("Escaneo D", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        register(new SkillTreasures("Especialista M", "Aumenta la probabilidad de aparición de tesoros infrecuentes en un 5% y raros en un 5% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 5, 5));
        register(new SkillTreasures("Especialista R", "Aumenta la probabilidad de aparición de tesoros raros en un 10% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 0, 10));
        register(new SkillTreasures("Especialista U", "Aumenta la probabilidad de aparición de tesoros infrecuentes en un 10% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 10, 0));
        register(new Skill("Especialista en desierto", "Establece a 1 el coste de excavar arena (~).", ESkillType.EXPERT));
        register(new SkillExcavation("Excavación I", "Aumenta el número de acciones de excavación en 5.", 5, 0, 0, 0, false));
        register(new SkillExcavation("Excavación II", "Aumenta el número de acciones de excavación en 10.", 10, 0, 0, 0, false));
        register(new SkillExcavation("Excavación III", "Aumenta el número de acciones de excavación en 15.", 15, 0, 0, 0, false));
        register(new SkillExcavation("Experiencia I", "Aumenta el número de acciones de excavación en un 20%.", 0, 20, 0, 0, false));
        register(new SkillExcavation("Experiencia II", "Aumenta el número de acciones de excavación en un 30%.", 0, 30, 0, 0, false));
        register(new SkillTreasures("Experto", "Aumenta el número de tesoros posibles de la excavación en 5 comunes y 5 infrecuentes", 0, 5, 5, 0, 0, 0));
        register(new SkillTreasures("Extra I", "Aumenta en 3 el número de tesoros de la excavación.", 3, 0, 0, 0, 0, 0));
        register(new SkillTreasures("Extra II", "Aumenta en 7 el número de tesoros de la excavación.", 7, 0, 0, 0, 0, 0));
        register(new SkillTreasures("Extra III", "Aumenta en 10 el número de tesoros de la excavación.", 10, 0, 0, 0, 0, 0));
        register(new SkillExcavation("Investigación", "Hace un desglose de cuantos tesoros de cada rareza hay.", 0, 0, 0, 0, true));
        register(new SkillTerrain("Licencia A", "Desbloquea los terrenos de excavación de tipo filas y columnas.", List.of("Fila", "Columna")));
        register(new SkillTerrain("Licencia B", "Desbloquea los terrenos de excavación de tipo horizontales y verticales.", List.of("Horizontal", "Vertical")));
        register(new SkillExcavation("Limpieza I", "Desvela 5 posiciones de excavación sin tesoro antes del proceso de excavación.", 0, 0, 5, 0, false));
        register(new SkillExcavation("Limpieza II", "Desvela 10 posiciones de excavación sin tesoro antes del proceso de excavación.", 0, 0, 10, 0, false));
        register(new SkillTreasures("Maestro", "Aumenta el número de tesoros posibles de la excavación en 5 infrecuentes y 2 raros", 0, 0, 5, 2, 0, 0));
        get("Especialista R").addIncompatibility(get("Especialista U"));
        get("Especialista M").addIncompatibility(get("Especialista R"));
        get("Especialista M").addIncompatibility(get("Especialista U"));
        get("Licencia A").addIncompatibility(get("Licencia B"));
        get("Excavación II").addDependency(get("Excavación I"));
        get("Excavación III").addDependency(get("Excavación II"));
        get("Experiencia II").addDependency(get("Experiencia I"));
        get("Experto").addDependency(get("Aprendiz"));
        get("Extra II").addDependency(get("Extra I"));
        get("Extra III").addDependency(get("Extra II"));
        get("Limpieza II").addDependency(get("Limpieza I"));
        get("Maestro").addDependency(get("Experto"));
    }

    
    /**
     * Guarda cada habilidad utilizando su nombre como clave. 
     * @param skill Habilidad a registrar.
     */
    private static void register(Skill skill) { skills.put(skill.getName(), skill); }

   
    /**
     * Recupera la misma instancia del catálogo a partir de su nombre.
     * @param name Nombre de la habilidad.
     * @return instancia registrada de la habilidad a partir de su nombre.
     */
    public static Skill get(String name) { return skills.get(name); }

    /**
     * Devuelve las habilidades de un tipo ordenadas por nombre.
     * @param type Tipo de habilidad.
     * @return Lista de habilidades ordenadas por nombre.
     */
    public static List<Skill> getByType(ESkillType type) {
        List<Skill> result = new ArrayList<>();
        for (Skill skill : skills.values()) {
           
            if (skill.getType().equals(type.toString())) result.add(skill);
        }
        result.sort(Comparator.comparing(Skill::getName));
        return result;
    }

    /**
     * Obtiene las habilidades implementadas que puede aprender el arqueólogo.
     * @param archeologist Arqueólogo cuyas habilidades se comprueban.
     * @return Nueva lista de habilidades disponibles para aprender.
     */
    public static List<Skill> getLearnable(Archeologist archeologist) {
        List<Skill> result = new ArrayList<>();

        for (Skill skill : skills.values()) {
            
            if (!ESkillType.EXPERT.toString().equals(skill.getType())
                    && archeologist.canLearn(skill)) {
                result.add(skill);
            }
        }

        return result;
    }
}
