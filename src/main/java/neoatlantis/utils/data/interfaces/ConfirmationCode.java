package neoatlantis.utils.data.interfaces;

/**
 * Interface que define el comportamiento con el que debe contar un Generador de
 * Codigos de Confirmaci&oacute;n.
 * @author Hiryu (aslhiryu@gmail.com)
 */
public interface ConfirmationCode {
    /**
     * Definici&oacute;n del metodo que genera un c&oacute;digo de confirmaci&oacute;n.
     * @return Cadena con el c&oacute;digo
     */
    public String create();
}
