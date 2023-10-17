package neoatlantis.utils.data;

/**
 * Generador de Codigos de Confirmaci&oacute;n en munisculas.
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class LowerConfirmationCode extends BasicConfirmationCode{
    /**
     * Constructor que permite definir el tama&ntilde;o del c&oacute;digo
     * @param limit 
     */
    public LowerConfirmationCode(int limit){
        super(limit);
    }
    
    /**
     * Genera un c&oacute;digo de confirmaci&oacute;n.
     * @return Cadena con el c&oacute;digo
     */
    @Override
    public String create() {
        return super.create().toLowerCase();
    }
}
