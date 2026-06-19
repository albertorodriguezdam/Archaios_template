package commons.operations;

/**
 * Definición de las clases operacionales que realizan la lógica del menú principal.
 * @author Adrián (Galveill).
 */
public interface IOperation {
    /**
     * @return El nombre de la opración, el texto a mostrar en el menú principal y como cabecera de la sección.
     */
    public String getName();

    /**
     * Realiza la lógica de la operación.
     */
    public void operation();
}
