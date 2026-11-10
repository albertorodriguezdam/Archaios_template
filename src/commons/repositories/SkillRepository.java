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
        
        if (!SkillRepository.skills.isEmpty()) return;
        
        SkillRepository.register(new SkillTreasures("Aprendiz", "Aumenta el número de tesoros posibles de la excavación en 5 comunes", 0, 5, 0, 0, 0, 0));
        SkillRepository.register(new SkillExcavation("Escaneo A", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        SkillRepository.register(new SkillExcavation("Escaneo B", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        SkillRepository.register(new SkillExcavation("Escaneo C", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        SkillRepository.register(new SkillExcavation("Escaneo D", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        SkillRepository.register(new SkillTreasures("Especialista M", "Aumenta la probabilidad de aparición de tesoros infrecuentes en un 5% y raros en un 5% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 5, 5));
        SkillRepository.register(new SkillTreasures("Especialista R", "Aumenta la probabilidad de aparición de tesoros raros en un 10% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 0, 10));
        SkillRepository.register(new SkillTreasures("Especialista U", "Aumenta la probabilidad de aparición de tesoros infrecuentes en un 10% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 10, 0));
        //A añadir en la segunda entrega con el resto de especialistas dependiendo de la región
        //register(new Skill("Especialista en desierto", "Establece a 1 el coste de excavar arena (~).", ESkillType.EXPERT));
        SkillRepository.register(new SkillExcavation("Excavación I", "Aumenta el número de acciones de excavación en 5.", 5, 0, 0, 0, false));
        SkillRepository.register(new SkillExcavation("Excavación II", "Aumenta el número de acciones de excavación en 10.", 10, 0, 0, 0, false));
        SkillRepository.register(new SkillExcavation("Excavación III", "Aumenta el número de acciones de excavación en 15.", 15, 0, 0, 0, false));
        SkillRepository.register(new SkillExcavation("Experiencia I", "Aumenta el número de acciones de excavación en un 20%.", 0, 20, 0, 0, false));
        SkillRepository.register(new SkillExcavation("Experiencia II", "Aumenta el número de acciones de excavación en un 30%.", 0, 30, 0, 0, false));
        SkillRepository.register(new SkillTreasures("Experto", "Aumenta el número de tesoros posibles de la excavación en 5 comunes y 5 infrecuentes", 0, 5, 5, 0, 0, 0));
        SkillRepository.register(new SkillTreasures("Extra I", "Aumenta en 3 el número de tesoros de la excavación.", 3, 0, 0, 0, 0, 0));
        SkillRepository.register(new SkillTreasures("Extra II", "Aumenta en 7 el número de tesoros de la excavación.", 7, 0, 0, 0, 0, 0));
        SkillRepository.register(new SkillTreasures("Extra III", "Aumenta en 10 el número de tesoros de la excavación.", 10, 0, 0, 0, 0, 0));
        SkillRepository.register(new SkillExcavation("Investigación", "Hace un desglose de cuantos tesoros de cada rareza hay.", 0, 0, 0, 0, true));
        SkillRepository.register(new SkillTerrain("Licencia A", "Desbloquea los terrenos de excavación de tipo filas y columnas.", List.of("Fila", "Columna")));
        SkillRepository.register(new SkillTerrain("Licencia B", "Desbloquea los terrenos de excavación de tipo horizontales y verticales.", List.of("Horizontal", "Vertical")));
        SkillRepository.register(new SkillExcavation("Limpieza I", "Desvela 5 posiciones de excavación sin tesoro antes del proceso de excavación.", 0, 0, 5, 0, false));
        SkillRepository.register(new SkillExcavation("Limpieza II", "Desvela 10 posiciones de excavación sin tesoro antes del proceso de excavación.", 0, 0, 10, 0, false));
        SkillRepository.register(new SkillTreasures("Maestro", "Aumenta el número de tesoros posibles de la excavación en 5 infrecuentes y 2 raros", 0, 0, 5, 2, 0, 0));
        SkillRepository.get("Especialista R").addIncompatibility(SkillRepository.get("Especialista U"));
        SkillRepository.get("Especialista M").addIncompatibility(SkillRepository.get("Especialista R"));
        SkillRepository.get("Especialista M").addIncompatibility(SkillRepository.get("Especialista U"));
        SkillRepository.get("Licencia A").addIncompatibility(SkillRepository.get("Licencia B"));
        SkillRepository.get("Excavación II").addDependency(SkillRepository.get("Excavación I"));
        SkillRepository.get("Excavación III").addDependency(SkillRepository.get("Excavación II"));
        SkillRepository.get("Experiencia II").addDependency(SkillRepository.get("Experiencia I"));
        SkillRepository.get("Experto").addDependency(SkillRepository.get("Aprendiz"));
        SkillRepository.get("Extra II").addDependency(SkillRepository.get("Extra I"));
        SkillRepository.get("Extra III").addDependency(SkillRepository.get("Extra II"));
        SkillRepository.get("Limpieza II").addDependency(SkillRepository.get("Limpieza I"));
        SkillRepository.get("Maestro").addDependency(SkillRepository.get("Experto"));
    }

    
    /**
     * Guarda cada habilidad utilizando su nombre como clave. 
     * @param skill Habilidad a registrar.
     */
    private static void register(Skill skill) { SkillRepository.skills.put(skill.getName(), skill); }

   
    /**
     * Recupera la misma instancia del catálogo a partir de su nombre.
     * @param name Nombre de la habilidad.
     * @return instancia registrada de la habilidad a partir de su nombre.
     */
    public static Skill get(String name) { return SkillRepository.skills.get(name); }

    /**
     * Devuelve las habilidades de un tipo ordenadas por nombre.
     * @param type Tipo de habilidad.
     * @return Lista de habilidades ordenadas por nombre.
     */
    public static List<Skill> getByType(ESkillType type) {
        List<Skill> result = new ArrayList<>();
        for (Skill skill : SkillRepository.skills.values()) {
           
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

        for (Skill skill : SkillRepository.skills.values()) {
            
            if (!ESkillType.EXPERT.toString().equals(skill.getType())
                    && archeologist.canLearn(skill)) {
                result.add(skill);
            }
        }

        return result;
    }
}
