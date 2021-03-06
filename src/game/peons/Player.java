package game.peons;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import game.locations.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.*;

public class Player extends Peon {
    private int exp;
    private int level;
    private Location location;
    private static final HashMap<Integer, List<Integer>> characterLevels = new HashMap();

    public Player() {
        loadCharacterLevels();
        this.exp = 0;
        this.level = 1;
        this.setMagicMax(100);
        this.setMagic(100);
        this.setHealthMax(100);
        this.setHealth(100);
        this.setStrength(10);
        this.setEndurance(10);
        this.setAgility(10);
        this.setLuck(10);
        this.setIntelligence(10);
        saveToJSONFile();

    }
    public Player(String name, int exp, int level,int healthMax, int health, int magicMax, int magic, int strength, int endurance, int agility, int luck, int intelligence){
        loadCharacterLevels();
        this.setName(name);
        this.exp = exp;
        this.level = level;
        this.setMagicMax(magicMax);
        this.setMagic(magic);
        this.setStrength(strength);
        this.setEndurance(endurance);
        this.setAgility(agility);
        this.setLuck(luck);
        this.setIntelligence(intelligence);
        saveToJSONFile();
    }

    private static int removeLastX(String str, int x) {
        if (str != null) {
            str = str.substring(0, str.length() - x);
        }
        return Integer.parseInt(str);
    }

    public void loadCharacterLevels() {
        Gson gson = new Gson();
        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new FileReader("json/db/characterLevels.json"));
            List<JsonObject> list = gson.fromJson(jsonReader, List.class);
            for (Object b : list) {
                String[] tokens = b.toString().split("=");// 1: level; 2: exp; 3: healthMax; 4: magicMax; 5: strength; 6: endurance; 7: agility 8: luck; 9: intelligence.
                int level = removeLastX(tokens[1],7);
                int exp = removeLastX(tokens[2],13);
                int addHealthMax = removeLastX(tokens[3],12);
                int addMagicMax = removeLastX(tokens[4],12);
                int addStrength = removeLastX(tokens[5],13);
                int addEndurance = removeLastX(tokens[6],11);
                int addAgility = removeLastX(tokens[7],8);
                int addLuck = removeLastX(tokens[8],16);
                int addIntelligence = removeLastX(tokens[9],3);
                List<Integer> properties= Arrays.asList(exp,addHealthMax,addMagicMax,addStrength, addEndurance,addAgility,addLuck,addIntelligence);
                characterLevels.put(level, properties);

//                System.out.println(removeLastX(tokens[9],3));
//                System.out.println(b);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveToJSONFile() {
        File file = new File("json/profile/player.json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fw = new FileWriter("json/profile/player.json", true)) {
            gson.toJson(this, fw);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashMap<Integer, List<Integer>> getCharacterLevels(){
        return characterLevels;
    }
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
        System.out.println("(" + this.exp + "/" + this.getCharacterLevels().get(this.getLevel()).get(0) + ")");
        levelUp();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        System.out.println("You are now level " + this.level + " !");
    }

    public void levelUp(){// 0: exp; 1: healthMax; 2: magicMax; 3: strength; 4: endurance; 5: agility 6: luck; 7: intelligence.
        int currentLevel = this.getLevel();
        int expMax = this.getCharacterLevels().get(currentLevel).get(0);
        if (this.getExp() >= expMax){
            if (this.getCharacterLevels().containsKey(currentLevel + 1)){
                currentLevel = currentLevel + 1;
                this.setLevel(currentLevel);
                this.setHealthMax(this.getHealthMax() + this.getCharacterLevels().get(currentLevel).get(1));
                this.setMagicMax(this.getMagicMax() + this.getCharacterLevels().get(currentLevel).get(2));
                this.setStrength(this.getStrength() + this.getCharacterLevels().get(currentLevel).get(3));
                this.setEndurance(this.getEndurance() + this.getCharacterLevels().get(currentLevel).get(4));
                this.setAgility(this.getAgility() + this.getCharacterLevels().get(currentLevel).get(5));
                this.setLuck(this.getLuck() + this.getCharacterLevels().get(currentLevel).get(6));
                this.setIntelligence(this.getIntelligence() + this.getCharacterLevels().get(currentLevel).get(7));
            }


        }
    }


}
