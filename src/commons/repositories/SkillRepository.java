package commons.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import people.Archeologist;
import repositories.Repositorio;
import skills.*;

/**
 * Repositorio encargado de registrar y gestionar las habilidades del sistema.
 * @author Constantin Laurentiu
 */
public final class SkillRepository extends Repositorio<Skill> {
    private static final SkillRepository INSTANCE = new SkillRepository();

    private SkillRepository() { }

    /** Crea el catálogo una vez y añade sus dependencias e incompatibilidades. */
    public static void register() {
        SkillRepository.INSTANCE.populate();
    }

    @Override
    public void populate() {
        if (!this.getCodedElements().isEmpty()) {
            return;
        }

        this.register(new SkillTreasures("Aprendiz", "Aumenta el número de tesoros posibles de la excavación en 5 comunes", 0, 5, 0, 0, 0, 0));
        this.register(new SkillExcavation("Escaneo A", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        this.register(new SkillExcavation("Escaneo B", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        this.register(new SkillExcavation("Escaneo C", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        this.register(new SkillExcavation("Escaneo D", "Marca tres posiciones entre las que se encuentra un tesoro y dos lugares vacíos.", 0, 0, 0, 3, false));
        this.registerTerrainSpecialist(new SkillTerrainSpecialist("Especialización en desierto",
            "Fija en 1 el coste de excavar arena.", '~', 1));
        this.register(new SkillDemolition("Especialista en demoliciones A",
            "Convierte hasta 5 piedras al azar en tierra antes de la limpieza y el escaneo.", '^', 5));
        this.register(new SkillTreasures("Especialista M", "Aumenta la probabilidad de aparición de tesoros infrecuentes en un 5% y raros en un 5% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 5, 5));
        this.register(new SkillTreasures("Especialista R", "Aumenta la probabilidad de aparición de tesoros raros en un 10% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 0, 10));
        this.register(new SkillTreasures("Especialista U", "Aumenta la probabilidad de aparición de tesoros infrecuentes en un 10% a costa de una reducción de aparición de los comunes.", 0, 0, 0, 0, 10, 0));
        this.register(new SkillExcavation("Excavación I", "Aumenta el número de acciones de excavación en 5.", 5, 0, 0, 0, false));
        this.register(new SkillExcavation("Excavación II", "Aumenta el número de acciones de excavación en 10.", 10, 0, 0, 0, false));
        this.register(new SkillExcavation("Excavación III", "Aumenta el número de acciones de excavación en 15.", 15, 0, 0, 0, false));
        this.register(new SkillExcavation("Experiencia I", "Aumenta el número de acciones de excavación en un 20%.", 0, 20, 0, 0, false));
        this.register(new SkillExcavation("Experiencia II", "Aumenta el número de acciones de excavación en un 30%.", 0, 30, 0, 0, false));
        this.register(new SkillTreasures("Experto", "Aumenta el número de tesoros posibles de la excavación en 5 comunes y 5 infrecuentes", 0, 5, 5, 0, 0, 0));
        this.register(new SkillTreasures("Extra I", "Aumenta en 3 el número de tesoros de la excavación.", 3, 0, 0, 0, 0, 0));
        this.register(new SkillTreasures("Extra II", "Aumenta en 7 el número de tesoros de la excavación.", 7, 0, 0, 0, 0, 0));
        this.register(new SkillTreasures("Extra III", "Aumenta en 10 el número de tesoros de la excavación.", 10, 0, 0, 0, 0, 0));
        this.register(new SkillExcavation("Investigación", "Hace un desglose de cuantos tesoros de cada rareza hay.", 0, 0, 0, 0, true));
        this.register(new SkillTerrain("Licencia A", "Desbloquea los terrenos de excavación de tipo filas y columnas.", List.of("Fila", "Columna")));
        this.register(new SkillTerrain("Licencia B", "Desbloquea los terrenos de excavación de tipo horizontales y verticales.", List.of("Horizontal", "Vertical")));
        this.register(new SkillExcavation("Limpieza I", "Desvela 5 posiciones de excavación sin tesoro antes del proceso de excavación.", 0, 0, 5, 0, false));
        this.register(new SkillExcavation("Limpieza II", "Desvela 10 posiciones de excavación sin tesoro antes del proceso de excavación.", 0, 0, 10, 0, false));
        this.register(new SkillTreasures("Maestro", "Aumenta el número de tesoros posibles de la excavación en 5 infrecuentes y 2 raros", 0, 0, 5, 2, 0, 0));
        this.getElement("Especialista R").addIncompatibility(this.getElement("Especialista U"));
        this.getElement("Especialista M").addIncompatibility(this.getElement("Especialista R"));
        this.getElement("Especialista M").addIncompatibility(this.getElement("Especialista U"));
        this.getElement("Licencia A").addIncompatibility(this.getElement("Licencia B"));
        this.getElement("Excavación II").addDependency(this.getElement("Excavación I"));
        this.getElement("Excavación III").addDependency(this.getElement("Excavación II"));
        this.getElement("Experiencia II").addDependency(this.getElement("Experiencia I"));
        this.getElement("Experto").addDependency(this.getElement("Aprendiz"));
        this.getElement("Extra II").addDependency(this.getElement("Extra I"));
        this.getElement("Extra III").addDependency(this.getElement("Extra II"));
        this.getElement("Limpieza II").addDependency(this.getElement("Limpieza I"));
        this.getElement("Maestro").addDependency(this.getElement("Experto"));
    }

    /** Registra una especialización incompatible con las demás especializaciones de terreno. */
    private void registerTerrainSpecialist(SkillTerrainSpecialist specialist) {
        for (Skill skill : this.getAllElements()) {
            if (skill instanceof SkillTerrainSpecialist) {
                specialist.addIncompatibility(skill);
            }
        }
        this.register(specialist);
    }

    /**
     * Recupera la misma instancia del catálogo a partir de su nombre.
     * @param name Nombre de la habilidad.
     * @return instancia registrada de la habilidad a partir de su nombre.
     */
    public static Skill get(String name) {
        return SkillRepository.INSTANCE.getElement(name);
    }

    /**
     * Devuelve las habilidades de un tipo ordenadas por nombre.
     * @param type Tipo de habilidad.
     * @return Lista de habilidades ordenadas por nombre.
     */
    public static List<Skill> getByType(ESkillType type) {
        List<Skill> result = new ArrayList<>();
        for (Skill skill : SkillRepository.INSTANCE.getAllElements()) {
           
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

        for (Skill skill : SkillRepository.INSTANCE.getAllElements()) {
            if (archeologist.canLearn(skill)) {
                result.add(skill);
            }
        }

        result.sort(Comparator.comparing(Skill::getName));
        return result;
    }
}
