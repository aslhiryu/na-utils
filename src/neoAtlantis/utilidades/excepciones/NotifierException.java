package neoAtlantis.utilidades.excepciones;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class NotifierException  extends RuntimeException {

    public NotifierException(Exception ex){
        super(ex);
    }

    public NotifierException(String msg){
        super(msg);
    }
}
