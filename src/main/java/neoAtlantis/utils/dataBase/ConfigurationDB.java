package neoAtlantis.utils.dataBase;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 * Clase de apoyo para configurar o utilizar acceso a Bases de datos
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ConfigurationDB {
    private static final Logger logger = Logger.getLogger(ConfigurationDB.class);

    /**
     * Prepara la configuración a partir de un archivo XML
     * @param xml Flujo de datos al XML
     * @return Configuración
     * @throws Exception 
     */
    public static Properties parseXMLConfiguration(InputStream xml) throws Exception{
        SAXBuilder builder = new SAXBuilder(false);
        Document doc = builder.build(xml);
        Element e, raiz = doc.getRootElement();
        Properties p = new Properties();
        List hojas = raiz.getChildren();
        Iterator i = hojas.iterator();

        while (i.hasNext()) {
            e = (Element) i.next();
            if (e.getName().equalsIgnoreCase("driver")) {
                p.setProperty("driver", e.getValue());
            }
            else if (e.getName().equalsIgnoreCase("url")) {
                p.setProperty("url", e.getValue());
            }
            else if (e.getName().equalsIgnoreCase("user")) {
                p.setProperty("user", e.getValue());
            }
            else if (e.getName().equalsIgnoreCase("pass")) {
                p.setProperty("pass", e.getValue());
            }
            else if (e.getName().equalsIgnoreCase("jndi")) {
                p.setProperty("jndi", e.getValue());
            }
        }
        logger.info("Configuración de BD parseada de XML");

        //valida la existencia de los objetos necesarios
        validateConfiguration(p);

        return p;
    }

    /**
     * Valida que se tenga los datos de configuración correctos
     * @param p Configuración a validar
     * @throws Exception 
     */
    public static void validateConfiguration(Properties p) throws Exception{
        if (p.getProperty("jndi") == null || p.getProperty("jndi").length() == 0) {
            if (p.getProperty("url") == null || p.getProperty("url").length() == 0) {
                throw new Exception("Falta la URL para la conexión.");
            }
            if (p.getProperty("driver") == null || p.getProperty("driver").length() == 0) {
                throw new Exception("Falta el Driver para la conexión.");
            }
            if (p.getProperty("user") == null || p.getProperty("user").length() == 0) {
                throw new Exception("Falta el Usuario para la conexión.");
            }
            if (p.getProperty("pass") == null) {
                throw new Exception("Falta el Usuario para la conexión.");
            }
        }
        else{
            return;
        }

        logger.info("Configuracion de BD correcta.");
    }
    
    /**
     * Genera una conexión a una BD
     * @param config Configuración de conexión
     * @return Conexión generada
     * @throws Exception 
     */
    public static Connection createConection(Properties config) throws Exception {
        try {
            if( config.getProperty("jndi") !=null && config.getProperty("jndi").length()>0 ){
                logger.info("Intenta generar conexión por jndi.");
                
                return ((DataSource)(new InitialContext()).lookup(config.getProperty("jndi"))).getConnection();
            }
            else{
                logger.info("Intenta generar conexión por jdbc.");

                Class.forName(config.getProperty("driver"));
                return DriverManager.getConnection(config.getProperty("url"), config.getProperty("user"), config.getProperty("pass"));
            }
        } catch (Exception ex) {
            logger.error("Error al generar la conexión.", ex);
            
            throw ex;
        }
    }

    
}

