package people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import skills.Skill;

public class Archeologist {

    private String name; 
    private int level; 
    private int xp; 
    private List<Skill> habilities; 
    private List<String> terrains; 
    private  String rarity; 

    public Archeologist(String name, String rarity) {
        this.name = name;
        this.level = 0;
        this.xp = 0;
        this.rarity = (rarity != null) ? rarity : "COMUN";
        this.habilities = new ArrayList<>();
        this.terrains = new ArrayList<>();
        this.terrains.add("Cuadrado");
    }

    public int getXpToUpLevel(){

        if (level<5) {
             return 50+(50*level); 
        } else {
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public List<Skill> getHabilities() {
        return Collections.unmodifiableList(habilities);
    }

    public List<String> getTerrains() {
        return Collections.unmodifiableList(terrains);
    }

    public String getRarity() {
        return rarity;
    }

    
}
