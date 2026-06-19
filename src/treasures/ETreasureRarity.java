package treasures;

/**
 * Las distintas rarezas de los tesoros.
 * @author Adrián (Galveill).
 */
public enum ETreasureRarity {
    
    COMMON("común", "C"),
	UNCOMMON("infrecuente", "I"),
	RARE("raro", "R");

	/** El nombre de la rareza. */
	private String txt;
	/** El código de la rareza. */
	private String code;

	/**
	 * Constructor básico
	 * @param txt El nombre de la rareza.
	 * @param code El código de la rareza.
	 */
	private ETreasureRarity(String txt, String code) {
		this.txt = txt;
		this.code = code;
	}

	/**
	 * @return El código asociado.
	 */
	public String getCode()
	{
		return this.code;
	}

	/**
	 * Devuelve la rareza con el código asociado.
	 * @param code El código de la rareza.
	 * @return La rareza con el código asociado.
	 */
	public static ETreasureRarity getByCode(String code) {
		for (ETreasureRarity tRare : ETreasureRarity.values()) {
			if(tRare.code == code) {
				return tRare;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return this.txt;
	}
}
