package helpers;

import java.util.ArrayList;
import java.util.List;

import excavation.Coordinate;
import excavation.Rejilla;

/**
 * Traduce y valida las coordenadas que escribe el usuario durante la excavación,
 * Pendiente de acordar con Tino: InputValidator lee el texto y esta clase lo valida.
 * @author Andrés.
 */
public class CoordinateValidator {

	/**
	 * Traduce una coordenada de texto a una posición de la rejilla.
	 * Admite minúsculas y espacios alrededor ("  3b ").
	 * @param text El texto escrito por el usuario.
	 * @param rejilla La rejilla, para saber qué filas y letras existen.
	 * @return La coordenada (fila y columna empezando en 0), o null si el formato no es válido
	 * o se sale de la rejilla.
	 */
	public static Coordinate parse(String text, Rejilla rejilla) {
		// TODO: separar la parte numérica de las letras y comprobar los límites
		return null;
	}

	/**
	 * Traduce y valida una entrada con una o varias coordenadas separadas por espacios o comas	 * La entrada entera es inválida si alguna coordenada:
	 * no tiene formato válido, se sale de la rejilla, ya está excavada o está repetida.
	 * Tampoco puede haber más coordenadas que acciones restantes.
	 * @param input El texto escrito por el usuario.
	 * @param rejilla La rejilla de la excavación.
	 * @param remainingActions Las acciones que le quedan al arqueólogo.
	 * @return La lista de coordenadas, o una lista vacía si la entrada no es válida.
	 */
	public static List<Coordinate> parseAll(String input, Rejilla rejilla, int remainingActions) {
		List<Coordinate> result = new ArrayList<>();
		// TODO
		return result;
	}
}
