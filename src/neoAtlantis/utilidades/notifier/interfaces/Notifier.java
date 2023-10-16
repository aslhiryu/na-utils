package neoAtlantis.utilidades.notifier.interfaces;

import java.text.SimpleDateFormat;
import neoAtlantis.utilidades.excepciones.NotifierException;

/**
 * Interface que define el comportamiento que debe de tener un <i>Notificador</i>.
 * @version 1.0
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public abstract class Notifier {
    /**
     * Variable que define el titulo del mensaje a enviar.
     */
    protected String titulo;
    /**
     * Variable que define el formato que se le daran a las fechas.
     */
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    /**
     * Genera un Notificador
     * @param tit Titulo que enviara en sus mensaje el notificador
     */
    public Notifier(String tit){
        this.titulo=tit;
    }

    /**
     * Definicion del metodo para enviar una notificación
     * @param app Nombre de la aplicación
     * @param det Detalle de la notificación
     * @return true si se logro enviar
     * @throws NotifierException
     */
    public abstract boolean enviaNotificacion(String app, String det) throws NotifierException;

    /**
     * Modifica el formato de la fechas que se utiliza. Por default es del tipo '25/12/08 23:56:20'.
     * @param cad Nueva mascara para el formato de fechas
     */
    public void modificaFormatoFecha(String cad) {
        this.sdf = new SimpleDateFormat(cad);
    }
}
