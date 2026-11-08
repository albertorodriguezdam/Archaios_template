package helpers;

/**
 * Funciones auxiliares para la gestión de los códigos.
 * @author Adrián.
 */
public class CodeHelper {
	//<code>RRR_TTT_NNN_PPPP</code>
	
	/**
	 * Devuelve el código de región de un código. <code>RRR</code>
	 * @param code El código a cortar. No tiene que ser completo siempre que tenga la parte solicitada en la posición correcta.
	 * @return El código de región o el código facilitado si no tiene la longitud suficiente. <code>RRR</code>
	 * 		   Devuelve {@code null} si el código es {@code null}.
	 */
	public static String getRegion(String code)
	{
		if(code != null && code.length() >= 3)
		{
			return code.substring(0, 3);
		}else{
			return code;
		}
	}

	/**
	 * Devuelve el código de tipo de un código. <code>TTT</code>
	 * @param code El código a cortar. No tiene que ser completo siempre que tenga la parte solicitada en la posición correcta.
	 * @return El código de tipo o el código facilitado si no tiene la longitud suficiente. <code>TTT</code>
	 * 		   Devuelve {@code null} si el código es {@code null}.
	 */
	public static String getType(String code)
	{
		if(code != null && code.length() >= 7)
		{
			return code.substring(4, 7);
		}else{
			return code;
		}
	}

	/**
	 * Devuelve el código de número de tesoro de un código. <code>NNN</code>
	 * @param code El código a cortar. No tiene que ser completo siempre que tenga la parte solicitada en la posición correcta.
	 * @return El código de número de tesoro o el código facilitado si no tiene la longitud suficiente. <code>NNN</code>
	 * 		   Devuelve {@code null} si el código es {@code null}.
	 */
	public static String getTreasureNumber(String code)
	{
		if(code != null && code.length() >= 11)
		{
			return code.substring(8, 11);
		}else{
			return code;
		}
	}

	/**
	 * Devuelve el código de parte de un código. <code>PPPP</code>
	 * @param code El código a cortar. No tiene que ser completo siempre que tenga la parte solicitada en la posición correcta.
	 * @return El código de parte o el código facilitado si no tiene la longitud suficiente. <code>PPPP</code>
	 * 		   Devuelve {@code null} si el código es {@code null}.
	 */
	public static String getTreasurePart(String code)
	{
		if(code != null && code.length() >= 16)
		{
			return code.substring(12, 16);
		}else{
			return code;
		}
	}

	/**
	 * Devuelve el código entero de tesoro de un código. <code>RRR_TTT_NNN</code>
	 * @param code El código a cortar. No tiene que ser completo siempre que tenga la parte solicitada en la posición correcta.
	 * @return El código entero de tesoro o el código facilitado si no tiene la longitud suficiente. <code>RRR_TTT_NNN</code>
	 * 		   Devuelve {@code null} si el código es {@code null}.
	 */
	public static String getFullTreasure(String code)
	{
		if(code != null && code.length() >= 11)
		{
			return code.substring(0, 11);
		}else{
			return code;
		}
	}
}
