package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilidades varias.
 * @author Adrián (Galveill).
 */
public class UtilsHelper {
	
	/**
	 * Pasa a mayúsculas la primera letra de la palabra.
	 * @param word La palabra a modificar.
	 * @return La palabra con la primera letra en mayúsculas.
	 */
	public static String ucfirst(String word) {
		return word.substring(0,1).toUpperCase() + word.substring(1);
	}

	/**
	 * Pasa a mayúsculas la primera letra de cada palabra. Se considera palabra aquellas separadas por espacio.
	 * @param text El texto a modificar.
	 * @return El texto con la primera letra de cada palabra en mayúsculas.
	 */
	public static String ucfirstAll(String text) {
		String[] words = text.split(" ");
		for(int i = 0; i < words.length; i++) {
			words[i] = ucfirst(words[i]);
		}
		return String.join(" ", words);
	}

	/**
	 * Devuelve el tanto por ciento de una cantidad frente al total.
	 * @param cant La cantidad actual.
	 * @param total El total.
	 * @return El tanto por ciento de la cantidad frente al total. En caso de valores incorrectos, devolverá 0.
	 */
	public static int getPercent(int cant, int total) {
		if(cant <= 0 || total == 0) {
			return 0;
		}else{
			return (int) ((double) cant / (double) total * 100);
		}
	}

	/**
	 * Genera una serie de índices aleatorios para la selección de elementos de una colección de una longitud determinada.
	 * @param num El número de elementos a generar.
	 * @param length La longitud de la colección.
	 * @return El conjunto de índices aleatorios y no repetidos dentro del rango [0-<code>length</code>).
	 * Un conjunto vacío en caso de que <code>num</code> sea mayor que <code>length</code>.
	 */
	public static List<Integer> genRandomIdxs(int num, int length) {
		if(num <= length) {
			List<Integer> pos = new ArrayList<>(length);
			for(int i = 0; i < length; i++) {
				pos.add(i);
			}
			Collections.shuffle(pos);

			return new ArrayList<>(pos.subList(0, num));
		}else{
			return new ArrayList<>(1);
		}
	}
}
