package neoAtlantis.utils.data;

/**
 * Generador de Codigos de Confirmaci&oacute;n en mayusculas.
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class UpperConfirmationCode extends BasicConfirmationCode{
    /**
     * Constructor que permite definir el tama&ntilde;o del c&oacute;digo
     * @param limit 
     */
    public UpperConfirmationCode(int limit){
        super(limit);
    }
    
    /**
     * Genera un c&oacute;digo de confirmaci&oacute;n.
     * @return Cadena con el c&oacute;digo
     */
    @Override
    public String create() {
        return super.create().toUpperCase();
    }
}
