package neoatlantis.utils.cipher.exceptions;

/**
 * Excepci&oacute;n generada por un {@link neoatlantis.utilidades.accessController.cipher.interfaces.DataCipher Cifrador de Datos}
 * al momento de cifrar o descifrar datos.
 * @author Hiryu (aslhiryu@gmail.com)
 * @version 1.0
 */
public class EncryptionException extends RuntimeException {
    /**
     * Constructor base.
     * @param ex Excepci&oacute;n padre
     */
    public EncryptionException(Exception ex){
        super(ex);
    }

    /**
     * Constructor base.
     * @param ex Excepci&oacute;n padre
     */
    public EncryptionException(String ex){
        super(ex);
    }
}
