package commons.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base de repositorios.
 * @author Adrián.
 */
public abstract class Repository<T extends IRegistrable> {
	
	/** Los elementos del repositorio del sistema. */
	private Map<String, T> elems = new HashMap<>();

	/**
	 * Añade los elementos.
	 */
	public abstract void populate();

	/**
	 * Añade el elemento a la lista de elementos del sistema.
	 * @param elem El elemento a registrar.
	 */
	protected void register(T elem)
	{
		this.elems.put(elem.getCode(), elem);
	}

	/**
	 * Devuelve el elemento asociado a ese código.
	 * @param code El código identificador del elemento.
	 * @return El elemento con ese código o null si no está registrado.
	 */
	public T getElement(String code)
	{
		return this.elems.get(code);
	}

	/**
	 * @return Los elementos del sistema con sus códigos asociados.
	 */
	public Map<String, T> getCodedElements()
	{
		return this.elems;
	}

	/**
	 * @return Los elementos del sistema en formato lista
	 */
	public List<T> getAllElements()
	{
		return new ArrayList<>(this.elems.values());
	}

}