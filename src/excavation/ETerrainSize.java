/**
 * Los tamaños de terreno y las reglas de excavación que dependen de ellos.
 * Cada {@link ETerrain} pertenece a uno de estos tamaños.
 * @author Andrés.
 */
public enum ETerrainSize {
	SMALL("pequeño", 0, 1, 0, 0, false, 2),
	MEDIUM("mediano", 2, 3, 1, 0, true, 5),
	LARGE("grande", 4, 5, 3, 1, true, 10);

	/** El nombre del tamaño. */
	private String txt;
	/** El nivel mínimo del arqueólogo para excavar en este tamaño. */
	private int requiredLevel;
	/** El número mínimo de tesoros comunes garantizados. */
	private int minCommon;
	/** El número mínimo de tesoros infrecuentes garantizados. */
	private int minUncommon;
	/** El número mínimo de tesoros raros garantizados. */
	private int minRare;
	/** Si en este tamaño pueden aparecer tesoros raros. */
	private boolean allowsRare;
	/** La experiencia extra que gana el arqueólogo al excavar en este tamaño. */
	private int extraExp;

	/**
	 * Constructor básico.
	 * @param txt El nombre del tamaño.
	 * @param requiredLevel El nivel mínimo del arqueólogo.
	 * @param minCommon Los tesoros comunes mínimos.
	 * @param minUncommon Los tesoros infrecuentes mínimos.
	 * @param minRare Los tesoros raros mínimos.
	 * @param allowsRare Si pueden aparecer tesoros raros.
	 * @param extraExp La experiencia extra al terminar la excavación.
	 */
	private ETerrainSize(String txt, int requiredLevel, int minCommon, int minUncommon,
			int minRare, boolean allowsRare, int extraExp) {
		this.txt = txt;
		this.requiredLevel = requiredLevel;
		this.minCommon = minCommon;
		this.minUncommon = minUncommon;
		this.minRare = minRare;
		this.allowsRare = allowsRare;
		this.extraExp = extraExp;
	}

	/**
	 * @return El nivel mínimo del arqueólogo para excavar en este tamaño.
	 */
	public int getRequiredLevel() {
		return this.requiredLevel;
	}

	/**
	 * @return El número mínimo de tesoros comunes garantizados.
	 */
	public int getMinCommon() {
		return this.minCommon;
	}

	/**
	 * @return El número mínimo de tesoros infrecuentes garantizados.
	 */
	public int getMinUncommon() {
		return this.minUncommon;
	}

	/**
	 * @return El número mínimo de tesoros raros garantizados.
	 */
	public int getMinRare() {
		return this.minRare;
	}

	/**
	 * @return El total de tesoros mínimos garantizados.
	 */
	public int getMinTotal() {
		return this.minCommon + this.minUncommon + this.minRare;
	}

	/**
	 * @return Si en este tamaño pueden aparecer tesoros raros (nunca en los pequeños, ni con habilidades).
	 */
	public boolean allowsRare() {
		return this.allowsRare;
	}

	/**
	 * @return La experiencia extra que gana el arqueólogo al excavar en este tamaño.
	 */
	public int getExtraExp() {
		return this.extraExp;
	}

	/**
	 * Indica si un arqueólogo de cierto nivel puede excavar en este tamaño.
	 * @param level El nivel del arqueólogo.
	 * @return Si el nivel alcanza el mínimo requerido.
	 */
	public boolean isAllowedFor(int level) {
		return level >= this.requiredLevel;
	}

	@Override
	public String toString() {
		return this.txt;
	}
}
