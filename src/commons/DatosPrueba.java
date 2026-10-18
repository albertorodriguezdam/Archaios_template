package commons;

import commons.repositories.SkillRepository;
import java.util.ArrayList;
import java.util.List;
import people.Archeologist;
import skills.Skill;

/** Datos de ejemplo en memoria para recorrer el menú con el argumento --pruebas. 
 * @author Constantin Laurentiu.
*/
public final class DatosPrueba {
    /**Puntos de ejemplo para poder probar las opciones que tienen coste. */ 
    public static final int INITIAL_POINTS = 500;

    /**Los datos se preparan desde los métodos estáticos de esta clase. */ 
    private DatosPrueba() { }

    
    /**
     * Prepara casos independientes usando los métodos del modelo existente.
     * @return Lista de Arqueólogos con habilidades y niveles de prueba.
     */
    public static List<Archeologist> createArcheologists() {
        SkillRepository.register();
        List<Archeologist> archeologists = new ArrayList<>();
        
        archeologists.add(createArcheologist("Comun", 0));
        archeologists.add(createArcheologist("Infrecuente", 0, "Excavación I"));
        archeologists.add(createArcheologist("Raro", 0, "Escaneo A", "Investigación"));
        archeologists.add(createArcheologist("LicenciaA", 150,
            "Licencia A", "Aprendiz", "Excavación I"));
        archeologists.add(createArcheologist("LicenciaB", 500,
            "Licencia B", "Excavación I", "Excavación II", "Experiencia I"));
        // Con 49 puntos queda a un punto de alcanzar el nivel 1.
        archeologists.add(createArcheologist("SubirNivel", 49));
        // 749 de experiencia acumulada dejan al personaje en nivel 4 con 249/250.
        archeologists.add(createArcheologist("Especializar", 749,
            "Excavación I", "Excavación II", "Experiencia I", "Investigación"));
        return archeologists;
    }

    /**
     * Método que crea tres visitas a Grecia y cinco hallazgos, incluyendo una pieza repetida.
     * @return Estadísticas de prueba
     */
    public static Stats createStats() {
        Stats stats = new Stats();
        stats.registerVisit("GRC");
        stats.registerVisit("GRC");
        stats.registerVisit("GRC");
        stats.registerTreasure("GRC_MON_001_0001");
        stats.registerTreasure("GRC_MON_001_0001");
        stats.registerTreasure("GRC_TES_001_0001");
        stats.registerTreasure("GRC_TES_001_0002");
        stats.registerTreasure("GRC_CER_001_0001");
        return stats;
    }

    /**
     * Prepara un personaje usando los métodos del modelo y las habilidades del catálogo.
     * @param name Nombre del arqueólogo.
     * @param xp Experiencia del arqueólogo.
     * @param skillNames Lista de habilidades por nombre.
     * @return Instancia de arqueólogo de prueba.
     */
    private static Archeologist createArcheologist(String name, int xp, String... skillNames) {
        Archeologist archeologist = new Archeologist(name);
        archeologist.addXp(xp);
        for (String skillName : skillNames) {
            Skill skill = SkillRepository.get(skillName);
          
            if (!archeologist.learnSkill(skill)) {
                throw new IllegalStateException("No se puede preparar " + name + " con " + skillName);
            }
        }
        return archeologist;
    }
}
