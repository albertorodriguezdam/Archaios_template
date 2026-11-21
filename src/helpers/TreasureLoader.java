package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import treasures.ETreasureRarity;
import treasures.ETreasureType;
import treasures.Treasure;

/**
 * Carga catálogos de regiones desde archivos JSON. Convierte sus entradas
 * en objetos Treasure y comprueba que sus códigos sean válidos y únicos
 * dentro de cada catálogo.
 *
 * @author Javier A. Goldar Picón
 */
public class TreasureLoader {

    /**
     * Carga y valida los tesoros de un catálogo JSON en el orden del archivo.
     *
     * @param filePath Ruta del catálogo. Su nombre identifica la región, por ejemplo, GRC.json.
     * @return Lista de tesoros construidos a partir de las entradas del catálogo.
     * @throws IOException Si falla la apertura o el cierre del archivo, si un código de rareza 
     * o tipo es desconocido o si un código de tesoro es inválido, incoherente con el catálogo o duplicado.
     */
    public static List<Treasure> loadTreasuresFromJson(String filePath) throws IOException {

        JsonArray jsonTreasures = TreasureLoader.readJsonArray(filePath);
        List<Treasure> treasures = new ArrayList<>();
        Set<String> seenCodes = new HashSet<>();
        String catalogFileName = new File(filePath).getName();

        for (JsonElement element : jsonTreasures) {
            JsonObject jsonTreasure = element.getAsJsonObject();
            Treasure treasure = TreasureLoader.parseTreasure(jsonTreasure);
            String code = treasure.getCode();
            TreasureLoader.validateTreasureCode(code, treasure.getType(), catalogFileName);
            
            if (!seenCodes.add(code)) {
                throw new IOException("Código de tesoro duplicado: " + code);
            }

            treasures.add(treasure);
        }

        return treasures;
    }

    /**
     * Lee un catálogo en UTF-8 y obtiene con Gson el array de la raíz.
     * Cierra los lectores al terminar.
     *
     * @param filePath Ruta del archivo JSON.
     * @return Array JSON con las entradas del catálogo.
     * @throws IOException Si falla la apertura del archivo o el cierre de los lectores.
     */
    private static JsonArray readJsonArray(String filePath) throws IOException {

        File file = new File(filePath);

        try (FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader textReader = new InputStreamReader(inputStream, "UTF-8")) {
            
            JsonElement jsonRoot = JsonParser.parseReader(textReader);
            return jsonRoot.getAsJsonArray();
        }
    }

    /**
     * Lee los campos de una entrada JSON, convierte sus códigos de rareza
     * y tipo en constantes y construye el objeto Treasure.
     *
     * @param jsonTreasure Entrada JSON con los datos de un tesoro.
     * @return Tesoro construido a partir de la entrada.
     * @throws IOException Si el código de rareza o de tipo es desconocido.
     */
    private static Treasure parseTreasure(JsonObject jsonTreasure) throws IOException {
        
        String code = jsonTreasure.get("code").getAsString();
        String name = jsonTreasure.get("name").getAsString();
        String description = jsonTreasure.get("description").getAsString();
        int parts = jsonTreasure.get("parts").getAsInt();
        int points = jsonTreasure.get("points").getAsInt();
        String rarityCode = jsonTreasure.get("rarity").getAsString();
        String typeCode = jsonTreasure.get("type").getAsString();

        ETreasureRarity rarity = TreasureLoader.parseRarity(rarityCode);
        ETreasureType type = TreasureLoader.parseType(typeCode);

        return new Treasure(code, name, description, rarity, type, parts, points);
    }

    /**
     * Busca la constante de rareza cuyo código coincide con el recibido.
     *
     * @param rarityCode Código de rareza leído del catálogo.
     * @return Constante de ETreasureRarity asociada al código.
     * @throws IOException Si el código de rareza es desconocido.
     */
    private static ETreasureRarity parseRarity(String rarityCode) throws IOException {
        
        for (ETreasureRarity currentRarity : ETreasureRarity.values()) {
            if (currentRarity.getCode().equals(rarityCode)) {
                return currentRarity;
            }
        }

        throw new IOException("Código de rareza desconocido: " + rarityCode);
    }

    /**
     * Busca la constante de tipo de tesoro cuyo código coincide con el recibido.
     *
     * @param typeCode Código de tipo leído del catálogo.
     * @return Constante de ETreasureType asociada al código.
     * @throws IOException Si el código de tipo es desconocido.
     */
    private static ETreasureType parseType(String typeCode) throws IOException {
        for (ETreasureType currentType : ETreasureType.values()) {
            if (currentType.getCode().equals(typeCode)) {
                return currentType;
            }
        }

        throw new IOException("Código de tipo de tesoro desconocido: " + typeCode);
    }

    /**
     * Comprueba que el código base sigue el formato RRR_TTT_NNN
     * y que su número está entre 001 y 999.
     * Verifica que el tipo coincide con el indicado en el tesoro y que
     * la región corresponde al nombre del archivo del catálogo.
     *
     * @param code Código base del tesoro.
     * @param type Tipo indicado en la entrada del catálogo.
     * @param catalogFileName Nombre del archivo JSON que identifica la región.
     * @throws IOException Si el código es inválido o si su tipo o región no coinciden.
     */
    private static void validateTreasureCode(String code, ETreasureType type, String catalogFileName) throws IOException {
        
        boolean validFormat = code.matches("[A-Z]{3}_[A-Z]{3}_[0-9]{3}");

        if (!validFormat || code.endsWith("_000")) {
            throw new IOException("Código de tesoro inválido: " + code);
        }

        String typeInCode = CodeHelper.getType(code);
        String expectedTypeCode = type.getCode();

        if (!expectedTypeCode.equals(typeInCode)) {
            throw new IOException("El código " + code + " contiene el tipo " + typeInCode 
                                + ", pero el catálogo indica " + expectedTypeCode);
        }

        String regionInCode = CodeHelper.getRegion(code);
        String expectedFileName = regionInCode + ".json";

        if (!expectedFileName.equals(catalogFileName)) {
            throw new IOException("El código " + code + " pertenece al catálogo "
                                + expectedFileName + " y aparece en " + catalogFileName);
        }
    }
}