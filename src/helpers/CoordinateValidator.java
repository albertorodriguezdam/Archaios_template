package helpers;

import excavation.Coordinate;
import excavation.Rejilla;
import java.util.ArrayList;
import java.util.List;

/**
 * Traduce y valida las coordenadas que escribe el usuario durante la
 * excavación, por ejemplo "3B" o "10AA" (número de fila y letras de columna).
 *
 * @author Andrés.
 */
public class CoordinateValidator {

    /**
     * Traduce una coordenada de texto a una posición de la rejilla. Admite
     * minúsculas y espacios alrededor (" 3b ").
     *
     * @param text El texto escrito por el usuario.
     * @param rejilla La rejilla, para saber qué filas y letras existen.
     * @return La coordenada (fila y columna empezando en 0), o null si el
     * formato no es válido o se sale de la rejilla.
     */
    public static Coordinate parse(String text, Rejilla rejilla) {
        if (text == null) {
            return null;
        }
        String value = text.trim().toUpperCase();

        /**
         * Aquí buscamos donde acaban los números.
         */
        int i = 0;
        while (i < value.length() && Character.isDigit(value.charAt(i))) {
            i++;
        }
        /**
         * Tiene que haber al menos un número y al menos una letra.
         */
        if (i == 0 || i == value.length()) {
            return null;
        }

        String numberPart = value.substring(0, i);
        String letterPart = value.substring(i);
        /**
         * La parte de las letras solo pueden tener letras
         */
        for (int j = 0; j < letterPart.length(); j++) {
            if (!Character.isLetter(letterPart.charAt(j))) {
                return null;
            }
        }
        int row;
        try {
            row = Integer.parseInt(numberPart) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
        int column = rejilla.getColumnIndex(letterPart);
        Coordinate coord = new Coordinate(row, column);
        if (!rejilla.isInside(coord)) {
            return null;
        }
        return coord;
    }

    /**
     * Traduce y valida una entrada con una o varias coordenadas separadas por espacios o comas	
	 * por ejemplo "3B,4C,10AA".
	 * La entrada entera es inválida si alguna coordenada: no
     * tiene formato válido, se sale de la rejilla, ya está excavada o está
     * repetida. Tampoco puede haber más coordenadas que acciones restantes.
     *
     * @param input El texto escrito por el usuario.
     * @param rejilla La rejilla de la excavación.
     * @param remainingActions Las acciones que le quedan al arqueólogo.
     * @return La lista de coordenadas, o una lista vacía si la entrada no es
     * válida.
     */
    public static List<Coordinate> parseAll(String input, Rejilla rejilla, int remainingActions) {
        List<Coordinate> result = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) {
            System.out.println("No has introducido ninguna coordenada.");
            return result;
        }
        /**
         * Cambiamos las comas por espacios y separamos por uno o más espacios.
         */

        String[] parts = input.replace(",", " ").trim().split("\\s+");
        if (parts.length > remainingActions) {
            if (remainingActions == 1) {
                System.out.println("Solo te queda una acción.");
            } else {
                System.out.println("Solo te quedan " + remainingActions + " acciones.");
            }
            return result;
        }

        for (String part : parts) {
            Coordinate coord = parse(part, rejilla);

            if (coord == null) {
                System.out.println("La coordenada \"" + part + "\" no es válida.");
                result.clear();
                return result;
            }
            if (rejilla.isRevealed(coord)) {
                System.out.println("La casilla " + rejilla.getLabel(coord) + " ya está descubierta.");
                result.clear();
                return result;
            }
            if (result.contains(coord)) {
                System.out.println("La casilla " + rejilla.getLabel(coord) + " está repetida.");
                result.clear();
                return result;
            }
            result.add(coord);
        }
        return result;
    }
}
