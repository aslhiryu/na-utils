package neoAtlantis.utils.codeBars.exceptions;

/**
 * Excepci&oacute;n generada al momento de generar un codigo de barras.
 * @author Hiryu (aslhiryu@gmail.com)
 * @version 1.0
 */
public class CodeBarException extends RuntimeException {
    /**
     * Constructor base.
     * @param ex Excepci&oacute;n padre
     */
    public CodeBarException(Exception ex){
        super(ex);
    }

    /**
     * Constructor base.
     * @param ex Excepci&oacute;n padre
     */
    public CodeBarException(String ex){
        super(ex);
    }
}
