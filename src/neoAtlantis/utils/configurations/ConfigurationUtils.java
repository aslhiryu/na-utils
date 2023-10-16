package neoAtlantis.utils.configurations;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import neoAtlantis.utils.data.DataUtils;
import org.apache.log4j.Logger;

/**
 * Obejo utileria para apoyar las actividades de configuración de entornos
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ConfigurationUtils {
    private static final Logger DEBUGER = Logger.getLogger(ConfigurationUtils.class);

    /**
     * Busca las los comodines en la frase para reemplazarla por los valores de entorno
     * @param frase Frase en la que se busca los comodines
     * @param entorno Valores de entorno
     * @return 
     */
    public static String parseWindcars(String frase, Map<String, Object> entorno){
        DEBUGER.debug("Se busca comodines para '"+frase+"', con entorno: "+entorno);
        
        return frase.replaceAll("%HOME_WEBINF%", (String)entorno.get("homeWebInf")).
                replaceAll("%HOME_WEB%", (String)entorno.get("homeWeb")).
                replaceAll("%HOME_CLASS%", (String)entorno.get("homeClass"));
    }

    /**
     * Recupero un objeto definido como propiedad a traves de su tipo
     * @param tipo Tipo de objeto a recuperar
     * @param objeto Objeto
     * @param entorno Entorno de la aplicación
     * @return 
     */
    public static Object parseObject(String tipo, String objeto, Map<String,Object> entorno) throws ClassNotFoundException{
        if( tipo!=null && tipo.equalsIgnoreCase("int") ){
            return Integer.parseInt(objeto);
        }
        else if( tipo!=null && tipo.equalsIgnoreCase("class") ){
            return Class.forName(objeto);
        }
        else if( tipo!=null && tipo.equalsIgnoreCase("boolean") ){
            return DataUtils.validateTrueBoolean(objeto);
        }
        else if( tipo!=null && tipo.equalsIgnoreCase("appContext") ){
            return (ServletContext)entorno.get("appContext");
        }
        else if( tipo!=null && tipo.equalsIgnoreCase("props") ){
            Properties p=new Properties();
            try{
                p.load(new FileInputStream(objeto));
            }catch(Exception ex){
                throw new RuntimeException("No se puede generar el properties a partir de '"+objeto+"'.");
            }

            return p;
        }

        return objeto;
    }
}
