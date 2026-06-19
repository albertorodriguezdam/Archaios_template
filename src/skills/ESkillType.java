package skills;

/**
 * Tipos de habilidades
 * @author Adrián (Galveill).
 */
public enum ESkillType {
	TREASURE("tesoro"),
	EXCAVATION("excavación"),
	TERRAIN("terreno"),
	EXPERT("especialista");
	
	/** El texto asociado. */
	private String txt;

	/**
	 * Constructor para establecer el texto.
	 * @param txt El texto interior.
	 */
	private ESkillType(String txt)
	{
		this.txt = txt;
	}

	@Override
	public String toString()
	{
		return this.txt;
	}
}
