package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import commons.Stats;
import commons.repositories.RegionRepository;
import commons.repositories.SkillRepository;
import commons.repositories.TreasureRepository;
import excavation.ETerrain;
import excavation.Excavation;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import people.Archeologist;
import regions.IDifficultRegion;
import skills.Skill;
import treasures.Treasure;

/** Lee y escribe partidas en el formato JSON de Prueba1.save. */
public final class SaveManager {
    private static final Path SAVE_DIRECTORY = Path.of("saves");
    private static final String EXTENSION = ".save";
    private static final Gson GSON = new GsonBuilder().setStrictness(Strictness.STRICT)
        .setPrettyPrinting().create();
    private static final String SAVED_SMALL_SQUARE = "S_SQUARE";
    private static final String SAVED_DESERT_SKILL = "Especialista en desierto";
    private static final String DESERT_SKILL = "Especialización en desierto";

    private SaveManager() { }

    /** Estado de una partida después de validar y reconstruir todos sus datos. */
    public record SavedGame(String uid, String name, List<Archeologist> archeologists,
            Stats stats, int points) { }

    public static List<Path> listSaves() throws IOException {
        List<Path> saves = new ArrayList<>();
        if (Files.notExists(SaveManager.SAVE_DIRECTORY)) return saves;

        try (DirectoryStream<Path> files = Files.newDirectoryStream(SaveManager.SAVE_DIRECTORY)) {
            for (Path file : files) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (name.endsWith(SaveManager.EXTENSION)
                        && Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
                    saves.add(file);
                }
            }
        }
        saves.sort(Comparator.comparing(Path::getFileName));
        return saves;
    }

    /** El nombre debe poder usarse como nombre de archivo en Windows y Linux. */
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank() || name.equals(".") || name.equals("..")) return false;
        for (char character : name.toCharArray()) {
            if (Character.isISOControl(character) || "\\/:*?\"<>|".indexOf(character) >= 0) return false;
        }
        String base = name.split("\\.", 2)[0].stripTrailing().toUpperCase(Locale.ROOT);
        return !base.matches("CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9]");
    }

    public static Path pathFor(String name) {
        if (!SaveManager.isValidName(name)) {
            throw new IllegalArgumentException("El nombre de partida no es válido.");
        }
        return SaveManager.SAVE_DIRECTORY.resolve(name + SaveManager.EXTENSION);
    }

    public static SavedGame read(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonElement document = SaveManager.GSON.fromJson(reader, JsonElement.class);
            return SaveManager.decode(document);
        } catch (JsonParseException | IllegalArgumentException exception) {
            throw new IOException("El archivo no contiene una partida compatible. "
                + exception.getMessage(), exception);
        }
    }

    /** Escribe un archivo completo antes de publicar o reemplazar el guardado. */
    public static void write(Path file, SavedGame game, boolean newGame) throws IOException {
        JsonObject document = SaveManager.encode(game);
        try {
            SaveManager.decode(document);
        } catch (IllegalArgumentException exception) {
            throw new IOException("Los datos de la partida no se pueden guardar. "
                + exception.getMessage(), exception);
        }

        Path directory = file.getParent();
        try {
            Files.createDirectories(directory);
        } catch (IOException exception) {
            throw new IOException("No se pudo preparar la carpeta " + directory + ".", exception);
        }

        Path temporary = Files.createTempFile(directory, "partida-", ".tmp");
        try {
            Files.writeString(temporary, SaveManager.GSON.toJson(document), StandardCharsets.UTF_8);
            if (newGame) {
                Files.move(temporary, file);
            } else {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (AtomicMoveNotSupportedException exception) {
            throw new IOException("No se puede sustituir el archivo de forma segura. Se conserva el guardado anterior.", exception);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    private static JsonObject encode(SavedGame game) {
        JsonObject result = new JsonObject();
        result.addProperty("UID", game.uid());
        result.addProperty("Partida", game.name());
        JsonArray people = new JsonArray();
        for (Archeologist person : game.archeologists()) {
            JsonObject data = new JsonObject();
            data.addProperty("Nombre", person.getName());
            data.addProperty("Nv", person.getLevel());
            data.addProperty("Exp", person.getXp());
            JsonArray terrains = new JsonArray();
            for (ETerrain terrain : Excavation.getAvailableTerrains(person)) {
                terrains.add(terrain == ETerrain.SMALL_SQUARE ? SaveManager.SAVED_SMALL_SQUARE : terrain.name());
            }
            data.add("Terrenos", terrains);
            JsonArray skills = new JsonArray();
            for (Skill skill : person.getSkills()) {
                String name = skill.getName();
                skills.add(SaveManager.DESERT_SKILL.equals(name) ? SaveManager.SAVED_DESERT_SKILL : name);
            }
            data.add("Habilidades", skills);
            data.addProperty("Especialización", person.getSpecialization());
            data.add("Licencias", SaveManager.GSON.toJsonTree(person.getLicenses()));
            people.add(data);
        }
        result.add("Arqueólogos", people);
        JsonObject statistics = new JsonObject();
        statistics.add("Tesoros", SaveManager.GSON.toJsonTree(new TreeMap<>(game.stats().getTreasureCounts())));
        statistics.add("Lugares", SaveManager.GSON.toJsonTree(new TreeMap<>(game.stats().getVisitCounts())));
        result.add("Estadísticas", statistics);
        result.addProperty("Puntos", game.points());
        return result;
    }

    private static SavedGame decode(JsonElement document) {
        JsonObject data = SaveManager.objectWithFields(document, "la partida", "UID", "Partida",
            "Arqueólogos", "Estadísticas", "Puntos");
        String uid = SaveManager.readRequiredText(data.get("UID"), "UID");
        String name = SaveManager.readText(data.get("Partida"), "Partida");
        if (!SaveManager.isValidName(name)) {
            throw new IllegalArgumentException("El nombre de partida no es válido.");
        }
        List<Archeologist> people = new ArrayList<>();
        for (JsonElement value : SaveManager.readArray(data.get("Arqueólogos"), "Arqueólogos")) {
            people.add(SaveManager.readArcheologist(value));
        }
        if (people.isEmpty()) throw new IllegalArgumentException("La partida debe tener un arqueólogo.");

        Stats stats = SaveManager.readStats(data.get("Estadísticas"));
        int points = SaveManager.readInt(data.get("Puntos"), "Puntos");
        return new SavedGame(uid, name, people, stats, points);
    }

    private static Archeologist readArcheologist(JsonElement value) {
        JsonObject data = SaveManager.objectWithFields(value, "un arqueólogo", "Nombre", "Nv", "Exp",
            "Terrenos", "Habilidades", "Especialización", "Licencias");
        String name = SaveManager.readRequiredText(data.get("Nombre"), "Nombre");
        int level = SaveManager.readInt(data.get("Nv"), "Nv");
        int xp = SaveManager.readInt(data.get("Exp"), "Exp");
        Archeologist person = new Archeologist(name, level, xp);

        List<Skill> skills = new ArrayList<>();
        for (String skillName : SaveManager.readTexts(data.get("Habilidades"), "Habilidades")) {
            String catalogName = SaveManager.SAVED_DESERT_SKILL.equals(skillName) ? SaveManager.DESERT_SKILL : skillName;
            Skill skill = SkillRepository.get(catalogName);
            if (skill == null) throw new IllegalArgumentException("Habilidad desconocida. " + skillName);
            skills.add(skill);
        }
        person.restoreSkills(skills);

        List<String> licenses = SaveManager.readTexts(data.get("Licencias"), "Licencias");
        for (String code : licenses) {
            if (!(RegionRepository.getRegion(code) instanceof IDifficultRegion)) {
                throw new IllegalArgumentException("Licencia regional no válida. " + code);
            }
        }
        String specialization = SaveManager.readText(data.get("Especialización"), "Especialización");
        if (!specialization.isEmpty() && RegionRepository.getRegion(specialization) == null) {
            throw new IllegalArgumentException("Región de especialización desconocida. " + specialization);
        }
        person.restoreRegionalData(licenses, specialization);
        SaveManager.checkTerrains(data.get("Terrenos"), person);
        return person;
    }

    private static void checkTerrains(JsonElement value, Archeologist person) {
        Set<ETerrain> terrains = new HashSet<>();
        for (String name : SaveManager.readTexts(value, "Terrenos")) {
            ETerrain terrain = SaveManager.SAVED_SMALL_SQUARE.equals(name) ? ETerrain.SMALL_SQUARE : ETerrain.valueOf(name);
            if (!terrains.add(terrain)) throw new IllegalArgumentException("Terreno repetido. " + name);
        }
        if (!terrains.equals(new HashSet<>(Excavation.getAvailableTerrains(person)))) {
            throw new IllegalArgumentException("Los terrenos de " + person.getName()
                + " no corresponden a su nivel y habilidades.");
        }
    }

    private static Stats readStats(JsonElement value) {
        JsonObject data = SaveManager.objectWithFields(value, "Estadísticas", "Tesoros", "Lugares");
        Map<String, Integer> treasures = SaveManager.readCounts(data.get("Tesoros"), "Tesoros");
        for (String code : treasures.keySet()) {
            if (!code.matches("[A-Z]{3}_[A-Z]{3}_[0-9]{3}_[0-9]{4}")) {
                throw new IllegalArgumentException("Código de pieza no válido. " + code);
            }
            Treasure treasure = TreasureRepository.getTreasure(code);
            int part = Integer.parseInt(CodeHelper.getTreasurePart(code));
            if (treasure == null || part < 1 || part > treasure.getParts()) {
                throw new IllegalArgumentException("Pieza desconocida. " + code);
            }
        }
        Map<String, Integer> visits = SaveManager.readCounts(data.get("Lugares"), "Lugares");
        for (String code : visits.keySet()) {
            if (RegionRepository.getRegion(code) == null) {
                throw new IllegalArgumentException("Región desconocida en las estadísticas. " + code);
            }
        }
        Stats stats = new Stats();
        stats.restoreCounts(treasures, visits);
        return stats;
    }

    private static Map<String, Integer> readCounts(JsonElement value, String field) {
        JsonObject object = SaveManager.readObject(value, field);
        Map<String, Integer> counts = new TreeMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            counts.put(entry.getKey(), SaveManager.readInt(entry.getValue(), field + " / " + entry.getKey()));
        }
        return counts;
    }

    private static JsonObject objectWithFields(JsonElement value, String label, String... fields) {
        JsonObject object = SaveManager.readObject(value, label);
        if (!object.keySet().equals(Set.of(fields))) {
            throw new IllegalArgumentException("Hay campos ausentes o desconocidos en " + label + ".");
        }
        return object;
    }

    private static JsonObject readObject(JsonElement value, String field) {
        if (value == null || !value.isJsonObject()) {
            throw new IllegalArgumentException("Se esperaba un objeto en " + field + ".");
        }
        return value.getAsJsonObject();
    }

    private static JsonArray readArray(JsonElement value, String field) {
        if (value == null || !value.isJsonArray()) {
            throw new IllegalArgumentException("Se esperaba una lista en " + field + ".");
        }
        return value.getAsJsonArray();
    }

    private static List<String> readTexts(JsonElement value, String field) {
        List<String> texts = new ArrayList<>();
        for (JsonElement item : SaveManager.readArray(value, field)) {
            texts.add(SaveManager.readRequiredText(item, field));
        }
        return texts;
    }

    private static String readRequiredText(JsonElement value, String field) {
        String text = SaveManager.readText(value, field);
        if (text.isBlank()) throw new IllegalArgumentException(field + " no puede estar vacío.");
        return text;
    }

    private static String readText(JsonElement value, String field) {
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Se esperaba texto en " + field + ".");
        }
        return value.getAsString();
    }

    private static int readInt(JsonElement value, String field) {
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException("Se esperaba un número entero en " + field + ".");
        }
        try {
            int number = value.getAsBigDecimal().intValueExact();
            if (number < 0) throw new IllegalArgumentException(field + " no puede ser negativo.");
            return number;
        } catch (ArithmeticException | NumberFormatException exception) {
            throw new IllegalArgumentException(field + " debe ser un entero válido.", exception);
        }
    }
}
