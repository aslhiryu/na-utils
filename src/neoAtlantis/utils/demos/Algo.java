package neoAtlantis.utils.demos;

import java.util.Properties;
import neoAtlantis.utils.web.Method;
import neoAtlantis.utils.web.WebClient;


/**
 *
 * @author desarrollo.alberto
 */
public class Algo {
    public static void main(String[] args) {
        Properties head=new Properties();
        String ruta=".";
        String url=null;
        byte[] req;
        
        for(int i=0; args!=null&&i<args.length; i++){
            if(args[i].equalsIgnoreCase("-help")){
                displayHelp();
            }
        }
        
    }

    private static void displayHelp() {
        System.out.println("WebClient");
        System.out.println("Usage:  Java -cp \"NAUtils.jar\" neoAtlantis.utils.web.Algo -url=url_consulta <-req=ruta_archivo_request> <-head=ruta_archivo_cabecera> <-archivos=ruta_directorio>");
        System.out.println("\t-url=url_consulta\tURL de la pagina a consultar");
        System.out.println("\t-req=ruta_archivo_request\tRuta del archivo que contiene los datos de la petición a realizar");
        System.out.println("\t-head=ruta_archivo_cabecera\tRuta del archivo Properties que contiene la información de los parametros a asignarse a la cabecera");
        System.out.println("\t-url=url_consulta\tURL de la pagina a consultar");
    }
    
    public void realizaPeticion(Properties params, String path, String url, byte[] req) throws Exception{
        WebClient wb=new WebClient(params);
        
        wb.setSaveFiles(true);
        wb.setPathFiles(path);
        wb.sendRequest(url, Method.POST, req);
    }
}
