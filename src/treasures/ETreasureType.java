package treasures;

/**
 * El tipo del tesoro.
 * @author Adrián (Galveill).
 */
public enum ETreasureType {
	COIN("moneda", "MON"),
	TILE("tesela", "TES"),
	BEAD("cuenta", "CUE"),
	JEWELRY("joyería", "JOY"),
	CERAMIC("cerámica", "CER"),
	EQUIP("equipo", "EQP"),
	STELE("estela", "EST"),
	FRAGMENT("fragmento", "FRA");

	/** El nombre del tipo. */
	private String txt;
	/** El código del tipo. */
	private String code;

	/**
	 * Constructor básico
	 * @param txt El nombre del tipo.
	 * @param code El código del tipo.
	 */
	private ETreasureType(String txt, String code) {
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
	 * Devuelve el tipo con el código asociado.
	 * @param code El código del tipo.
	 * @return El tipo con el código asociado.
	 */
	public static ETreasureType getByCode(String code) {
		for (ETreasureType tType : ETreasureType.values()) {
			if(tType.code == code) {
				return tType;
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
