package commons;

import commons.repositories.SkillRepository;
import java.util.ArrayList;
import java.util.List;
import people.Archeologist;
import skills.Skill;

/** Datos de ejemplo en memoria para recorrer el menú conel argumento --pruebas. */
public final class DatosPrueba {
    // Puntos de ejemplo para poder probar las opciones que tienen coste.
    public static final int INITIAL_POINTS = 500;

    // Los datos se preparan desde los métodos estáticos de esta clase.
    private DatosPrueba() { }

    /** Prepara casos independientes usando los métodos del modelo existente. */
    public static List<Archeologist> createArcheologists() {
        SkillRepository.register();
        List<Archeologist> archeologists = new ArrayList<>();
        // Los tres primeros casos muestran cero, una y dos habilidades iniciales.
        archeologists.add(createArcheologist("Comun", 0));
        archeologists.add(createArcheologist("Infrecuente", 0, "Excavación I"));
        archeologists.add(createArcheologist("Raro", 0, "Escaneo A", "Investigación"));
        // Las licencias de terreno se ponen en personajes distintos porque son incompatibles.
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

    /** Tres visitas a Grecia y cinco hallazgos, incluyendo una pieza repetida. */
    public static Stats createStats() {
        Stats stats = new Stats();
        stats.registerVisit("GRC");
        stats.registerVisit("GRC");
        stats.registerVisit("GRC");
        // La misma pieza se repite para comprobar la diferencia entre hallazgos y piezas distintas.
        stats.registerTreasure("GRC_MON_001_0001");
        stats.registerTreasure("GRC_MON_001_0001");
        stats.registerTreasure("GRC_TES_001_0001");
        stats.registerTreasure("GRC_TES_001_0002");
        stats.registerTreasure("GRC_CER_001_0001");
        return stats;
    }

    // Prepara un personaje usando los métodos del modelo y las habilidades del catálogo.
    private static Archeologist createArcheologist(String name, int xp, String... skillNames) {
        Archeologist archeologist = new Archeologist(name);
        // Se añade experiencia acumulada para que el propio modelo calcule nivel y resto.
        archeologist.addXp(xp);
        for (String skillName : skillNames) {
            Skill skill = SkillRepository.get(skillName);
            // Un dato de prueba incompatible o inexistente se detecta al preparar el personaje.
            if (!archeologist.learnSkill(skill)) {
                throw new IllegalStateException("No se puede preparar " + name + " con " + skillName);
            }
        }
        return archeologist;
    }
}
