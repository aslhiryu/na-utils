package neoAtlantis.utilidades.configFiles;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.*;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.jdom.*;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ClassGenerator {
    static final Logger logger = Logger.getLogger(ClassGenerator.class);
    
    public static Object generaInstancia(Element raiz, List<Object> params, Properties frases, Map<String,Object> entorno){
        String clase;
        List<Element> lTmp;

        //objento el tipo de objeto a generar
        if(raiz==null || (clase=raiz.getAttributeValue("class"))==null || clase.length()==0 ){
            logger.debug("No esta bien definida la configuración del elemento raiz: "+raiz);
            return null;
        }

        //recupero los parametros
        lTmp=raiz.getChildren();
        for(Element eTmp: lTmp){
            if(eTmp.getName().equalsIgnoreCase("param") && eTmp.getAttributeValue("name")!=null && eTmp.getAttributeValue("value")!=null ){
                params.add( defineTipoObjeto(eTmp.getAttributeValue("type"), parseaComodinesConfig(eTmp.getAttributeValue("value"), frases), entorno));
            }
        }
        
        logger.debug("Se encontraron "+(params!=null? params.size(): -1)+" parametros.");
        logger.debug(params);

        //genero la instancia

        return generaInstancia(clase, params);
    }

    public static Object generaInstancia(String clase, List<Object> params){
        Object obj=null;
        boolean correcto;
        Constructor[] lTmp2;

        try{
            Class c=Class.forName(clase);
            lTmp2=c.getConstructors();
            
            //reviso que constrcutor cumple con los parametros
            for(Constructor cns: lTmp2){
                correcto=true;
                logger.debug("Constructor encontrado: "+cns.toGenericString());

                if( cns.getGenericParameterTypes().length==params.size() ){
                    try{
                        obj=cns.newInstance(params.toArray());
                    }
                    catch(Exception ex1){
                        logger.info("Intento generar la instancia con el constructor "+cns.toGenericString()+", "+params+" pero fallo por: "+ex1.getMessage());
                        correcto=false;
                    }
                }
                else{
                    correcto=false;
                }

                //valida que se haya generado una correcta instancia
                if( correcto ){
                    logger.debug("Se genera una instancia del objeto "+cns.toGenericString()+".");
                    break;
                }
            }
        }
        catch(Exception ex){
            logger.error("Error al intentar generar una instancia de '"+clase+"'.", ex);
        }
        
        return obj;
    }
    
    public static String parseaComodinesConfig(String cad, Properties frases){
        return cad.replaceAll("%HOME_WEBINF%", frases.getProperty("homeWebInf")).
                replaceAll("%HOME_WEB%", frases.getProperty("homeWeb")).
                replaceAll("%HOME_CLASS%", frases.getProperty("homeClass"));
    }

    public static Object defineTipoObjeto(String tipo, String objeto, Map<String,Object> entorno){
        if( tipo!=null && tipo.equals("int") ){
            return Integer.parseInt(objeto);
        }
        else if( tipo!=null && tipo.equals("appContext") ){
            return (ServletContext)entorno.get("appContext");
        }
        else if( tipo!=null && tipo.equals("props") ){
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

    public static Properties generaComodinesHomeWeb(String homeWeb){
        Properties p=new Properties();

        p.setProperty("homeWeb", homeWeb);
        p.setProperty("homeWebInf", homeWeb+"/WEB-INF/");
        p.setProperty("homeClass", homeWeb+"/WEB-INF/classes/");

        return p;
    }
}
