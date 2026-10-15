package regions;

import treasures.ETreasureRarity;
import treasures.ETreasureType;
import treasures.Treasure;

public class Grecia extends Region {
    /**El código único identificativo de la región */
    private static final String CODE = "GRC";

    /** Inicializa la región y carga los tesoros de Grecia. */
    public Grecia() {
        super();
        loadTreasures();
    }

    /**
     * Devuelve el código identificativo de Grecia.
     * 
     * @return El código identificativo de Grecia.
     */
    @Override
    public String getCode() {
        return CODE;
    }

    /**Carga los tesoros correspondientes a la región de Grecia. */
    private void loadTreasures() {
        // Moneda
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.COIN.getCode() + "_001", "Calco griego", "Moneda griega de cobre que tenía el menor valor.", ETreasureRarity.COMMON, ETreasureType.COIN, 1, 1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.COIN.getCode() + "_002", "Óbolo griego", "Moneda griega de plata de poco valor.", ETreasureRarity.COMMON, ETreasureType.COIN, 1, 1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.COIN.getCode() + "_003", "Dracma griego", "Moneda griega de plata ampliamente usada.", ETreasureRarity.COMMON, ETreasureType.COIN, 1, 1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.COIN.getCode() + "_004", "Estatero", "Moneda griega de gran valor hecha de electro.", ETreasureRarity.UNCOMMON, ETreasureType.COIN, 1, 2));
        // Tesela
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.TILE.getCode() + "_001", "Mosaico de Poseidón", "Mosaico inspirado en el de la Casa de los Delfines de Delos.", ETreasureRarity.COMMON, ETreasureType.TILE, 625, 1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.TILE.getCode() + "_002", "Mosaico geométrico", "Mosaico geométrico en dos colores, típico de los primeros siglos de Grecia.", ETreasureRarity.COMMON, ETreasureType.TILE, 625, 1));
        // Cuenta
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.BEAD.getCode() + "_001", "Cuenta de vidrio", "Pequeños trozos de vidrio pulido que se utilizaban en collares y pulseras.", ETreasureRarity.COMMON, ETreasureType.BEAD, 1, 1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.BEAD.getCode() + "_002", "Cuenta de cerámica", "Pequeños trozos de cerámica pintada a mano que se utilizaban en collares y pulseras.", ETreasureRarity.COMMON, ETreasureType.BEAD, 1, 1));
        // Joyería
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.JEWELRY.getCode() + "_001", "Fíbula omega", "Hebilla para sujetar la ropa similar a un imperdible.", ETreasureRarity.UNCOMMON, ETreasureType.JEWELRY, 3, 2));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.JEWELRY.getCode() + "_002", "Anillos", "Anillos de oro, plata y bronce adornados con grabados e incrustaciones de gemas.", ETreasureRarity.RARE, ETreasureType.JEWELRY, 4, 5));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.JEWELRY.getCode() + "_003", "Brazal", "Brazal de oro decorado con filigranas y gemas.", ETreasureRarity.UNCOMMON, ETreasureType.JEWELRY, 7,3));
        // Cerámica
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_001", "Ánfora de Nola", "Ánfora típica de la región de Nola con un cuello más largo y estrecho.", ETreasureRarity.UNCOMMON, ETreasureType.CERAMIC, 34,2));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_002", "Ánfora nicosténica", "Ánfora creada en el siglo VI a.C. por Nicóstenes que imita la forma etrusca.", ETreasureRarity.UNCOMMON, ETreasureType.CERAMIC, 18,3));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_003", "Ánfora panatenaica", "Ánfora que contenía el aceite que se le daba como premio a los ganadores de los Juegos panatenaicos.", ETreasureRarity.RARE, ETreasureType.CERAMIC, 30,5));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_004", "Calpis", "Vaso griego utilizado para almacenar agua.", ETreasureRarity.COMMON, ETreasureType.CERAMIC, 22,1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_005", "Crátera", "Vasija de gran capacidad usada para guardar la mezcla de agua y vino.", ETreasureRarity.COMMON, ETreasureType.CERAMIC, 20,1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_006", "Enócoe", "Vasija con un asa usada para servir el vino.", ETreasureRarity.COMMON, ETreasureType.CERAMIC, 16,1));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_007", "Estamno", "Vasija para conservar el vino con forma de globo y asas horizontales.", ETreasureRarity.RARE, ETreasureType.CERAMIC, 20,5));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.CERAMIC.getCode() + "_008", "Hidria", "Vasija para servir y guardar agua con tres asas, dos a los lados y una a modo de jarra.", ETreasureRarity.COMMON, ETreasureType.CERAMIC, 16,1));
        // Equipo
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.EQUIP.getCode() + "_001", "Aspis", "Escudo circular de bronce, madera y cuero utilizado por los hoplitas griegos.", ETreasureRarity.UNCOMMON, ETreasureType.EQUIP, 28,3));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.EQUIP.getCode() + "_002", "Casco corintio", "Casco de bronce que cubre toda la cabeza y el cuello.", ETreasureRarity.UNCOMMON, ETreasureType.EQUIP, 30,4));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.EQUIP.getCode() + "_003", "Casco frigio", "Casco de bronce con una protuberancia en la parte superior y dos placas laterales para la cara.", ETreasureRarity.RARE, ETreasureType.EQUIP, 21,6));
        treasureList.add(new Treasure(CODE + "_" + ETreasureType.EQUIP.getCode() + "_004", "Xifos", "Espada de bronce utilizada por los hoplitas griegos.", ETreasureRarity.UNCOMMON, ETreasureType.EQUIP, 25,4));
    }
}
