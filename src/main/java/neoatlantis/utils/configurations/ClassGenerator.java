package neoatlantis.utils.configurations;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Objeto que genera intancias de clases dinamicamente
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ClassGenerator {
    private static final Logger DEBUGER = Logger.getLogger(ClassGenerator.class);
    
    /**
     * Crea una instancia de un objeto a partir de una definición en XML
     * @param raiz Nodo raiz del XML donde radica la declaración
     * @param entorno Objetos del entorno
     * @return Objeto instanciado
     */
    public static Object createInstance(final Element raiz, final Map<String,Object> entorno){
        String clase=null;
        List<Element> lTmp;
        List<Object> params=new ArrayList<Object>();

        DEBUGER.debug("Intento generar una instancia del elemnto: "+raiz+", con entorno: "+entorno);
        try{
            //obtengo el tipo de objeto a generar
            if(raiz==null || (clase=raiz.getAttributeValue("class"))==null || clase.length()==0 ){
                DEBUGER.debug("No esta bien definida la configuración del elemento raiz: "+raiz);
                return null;
            }
            
            //recupero los parametros
            lTmp=raiz.getChildren();
            for(Element eTmp: lTmp){
                if(eTmp.getName().equalsIgnoreCase("param") && eTmp.getAttributeValue("name")!=null && eTmp.getAttributeValue("value")!=null ){
                    params.add( ConfigurationUtils.parseObject(eTmp.getAttributeValue("type"), ConfigurationUtils.parseWindcars(eTmp.getAttributeValue("value"), entorno), entorno));
                }
            }

            DEBUGER.debug("Se encontraron "+(params!=null? params.size(): -1)+" parametros.");
            DEBUGER.debug(params);
        }
        catch(Exception ex){
            DEBUGER.error("Error al parsear los datos para la instancia de '"+clase+"'.", ex);
        }

        //genero la instancia
        return createInstance(clase, params);
    }
    
    /**
     * Genera una instancia una clase
     * @param clase Clase de la que se desea la instancia
     * @param params Parametros necesarios para la intanciación
     * @return Objeto intanciado
     */
    public static Object createInstance(String clase, List<Object> params){
        try{
        return createInstance(Class.forName(clase), params);
        }
        catch(Exception ex){
            DEBUGER.error("Error al intentar generar una instancia de '"+clase+"'.", ex);
        }
        
        return null;
    }
    
    /**
     * Genera una instancia una clase
     * @param clase Clase de la que se desea la instancia
     * @param params Parametros necesarios para la intanciación
     * @return Objeto intanciado
     */
    public static Object createInstance(String clase, Object[] params){
        return createInstance(clase, Arrays.asList(params));
    }
    
    /**
     * Genera una instancia una clase
     * @param clase Clase de la que se desea la instancia
     * @param params Parametros necesarios para la intanciación
     * @return Objeto intanciado
     */
    public static Object createInstance(Class clase, Object[] params){
        return createInstance(clase, Arrays.asList(params));
    }
    
    /**
     * Genera una instancia una clase
     * @param clase Clase de la que se desea la instancia
     * @param params Parametros necesarios para la intanciación
     * @return Objeto intanciado
     */
    public static Object createInstance(Class clase, List<Object> params){
        Object obj=null;
        boolean correcto;
        Constructor[] lTmp2;

        try{
            lTmp2=clase.getConstructors();
            
            //reviso que constrcutor cumple con los parametros
            for(Constructor cns: lTmp2){
                correcto=true;
                DEBUGER.debug("Constructor encontrado: "+cns.toGenericString());

                if( cns.getGenericParameterTypes().length==params.size() ){
                    try{
                        obj=cns.newInstance(params.toArray());
                    }
                    catch(Exception ex1){
                        DEBUGER.info("Intento generar la instancia con el constructor "+cns.toGenericString()+", "+params+" pero fallo por: "+ex1.getMessage());
                        correcto=false;
                    }
                }
                else{
                    correcto=false;
                }

                //valida que se haya generado una correcta instancia
                if( correcto ){
                    DEBUGER.debug("Se genera una instancia del objeto "+cns.toGenericString()+".");
                    break;
                }
            }
        }
        catch(Exception ex){
            DEBUGER.error("Error al intentar generar una instancia de '"+clase+"'.", ex);
        }
        
        return obj;
    }
}
