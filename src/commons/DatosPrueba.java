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
     * Conserva los siete casos originales y añade demostraciones de la entrega II.
     * @return Lista de Arqueólogos con habilidades y niveles de prueba.
     */
    public static List<Archeologist> createArcheologists() {
        List<Archeologist> archeologists = new ArrayList<>();
        
        archeologists.add(DatosPrueba.createArcheologist("Comun", 0));
        archeologists.add(DatosPrueba.createArcheologist("Infrecuente", 0, "Excavación I"));
        archeologists.add(DatosPrueba.createArcheologist("Raro", 0, "Escaneo A", "Investigación"));
        archeologists.add(DatosPrueba.createArcheologist("LicenciaA", 150,
            "Licencia A", "Aprendiz", "Excavación I"));
        archeologists.add(DatosPrueba.createArcheologist("LicenciaB", 500,
            "Licencia B", "Excavación I", "Excavación II", "Experiencia I"));
        // Con 49 puntos queda a un punto de alcanzar el nivel 1.
        archeologists.add(DatosPrueba.createArcheologist("SubirNivel", 49));
        // 749 de experiencia acumulada dejan al personaje en nivel 4 con 249/250.
        archeologists.add(DatosPrueba.createArcheologist("Especializar", 749,
            "Excavación I", "Excavación II", "Experiencia I", "Investigación"));

        // Entrega II: todos estos casos de nivel 2 pueden usar el cuadrado mediano.
        // Se comparan los mismos terrenos con y sin las habilidades de la expansión.
        archeologists.add(DatosPrueba.createLicensedArcheologist("EgiptoBase", 150,
            List.of("EGP"), "Investigación", "Excavación I"));
        archeologists.add(DatosPrueba.createLicensedArcheologist("EgiptoDesierto", 150,
            List.of("EGP"), "Investigación", "Excavación I", "Especialización en desierto"));
        archeologists.add(DatosPrueba.createLicensedArcheologist("PetraBase", 150,
            List.of("PTR"), "Investigación", "Excavación I"));
        archeologists.add(DatosPrueba.createLicensedArcheologist("PetraDemoliciones", 150,
            List.of("PTR"), "Investigación", "Excavación I", "Especialista en demoliciones A"));
        archeologists.add(DatosPrueba.createLicensedArcheologist("SumeriaCombinado", 150,
            List.of("SUM"), "Investigación", "Especialización en desierto",
            "Especialista en demoliciones A"));

        // Con dos licencias previas solo falta Sumeria y la siguiente cuesta 40 puntos.
        archeologists.add(DatosPrueba.createLicensedArcheologist("DosLicencias", 150,
            List.of("EGP", "PTR"), "Investigación", "Excavación I"));

        // 950 acumulados: nivel 5 y 200 XP adicionales. Los bonos regionales solo
        // se activan en Sumeria; dispone de todas las licencias para poder comparar.
        Archeologist specialist = DatosPrueba.createLicensedArcheologist("EspecialistaSumeria", 950,
            List.of("EGP", "PTR", "SUM"), "Investigación", "Excavación I", "Excavación II",
            "Especialización en desierto", "Especialista en demoliciones A");
        specialist.setSpecialization("SUM");
        archeologists.add(specialist);
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
     * Añade permisos regionales a un personaje preparado con habilidades del catálogo.
     * Estas licencias de ejemplo no descuentan puntos del saldo de pruebas.
     * @param name Nombre del arqueólogo.
     * @param xp Experiencia acumulada que se entrega al modelo.
     * @param licenses Códigos de las regiones difíciles autorizadas.
     * @param skillNames Nombres de habilidades del catálogo.
     * @return Arqueólogo independiente con sus licencias regionales.
     */
    private static Archeologist createLicensedArcheologist(String name, int xp,
            List<String> licenses, String... skillNames) {
        Archeologist archeologist = DatosPrueba.createArcheologist(name, xp, skillNames);
        for (String code : licenses) {
            archeologist.addLicense(code);
        }
        return archeologist;
    }

    /**
 * Prepara un arqueólogo con experiencia acumulada y habilidades del catálogo.
 *
 * @param name Nombre del arqueólogo.
 * @param xp Experiencia total que se añade desde el nivel inicial.
 * @param skillNames Nombres de las habilidades, en orden de aprendizaje.
 * @return Arqueólogo preparado para las pruebas.
 * @throws IllegalStateException si alguna habilidad no existe o no puede aprenderse.
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
