package skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alberto Rodríguez Domínguez
 * SkillTerrain
 */
public class SkillTerrain extends Skill{

    /**Lista de los formatos de terrenos que desbloquea esta habilidad. */
    private List<String> unlockedTerrains; 

    /**
     * Constructor principal de la clase SkillTerrain.
     * @param nameEl Nombres identificativo de la habilidad.
     * @param description Descripción de lo que hace la habilidd.
     * @param unlockedTerrains Lista de los formatos de terrenos que desbloquea esta habilidad.
     */
    public SkillTerrain(String name, String description, List<String> unlockedTerrains){
        super(name, description, ESkillType.TERRAIN);
        if (unlockedTerrains!=null) {
            this.unlockedTerrains=new ArrayList<>(unlockedTerrains); 
        } else this.unlockedTerrains=new ArrayList<>(); 
    }


    /**
     * Devuelve una lista no modificable de terrenos añadidos por esta licencia.
     * @return Lista no modificable con los formatos de terrenos disponibles.
     */
    @Override
    public List<String> getTerrains() {
        return Collections.unmodifiableList(this.unlockedTerrains);
    }
}
