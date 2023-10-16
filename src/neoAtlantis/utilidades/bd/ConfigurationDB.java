package neoAtlantis.utilidades.bd;

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
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ConfigurationDB {
    static final Logger logger = Logger.getLogger(ConfigurationDB.class);

    public static Properties parseConfiguracionXML(InputStream xml) throws Exception{
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
        validaConfigProperties(p);

        return p;
    }

    public static void validaConfigProperties(Properties p) throws Exception{
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
    
    public static Connection generaConexion(Properties config) throws Exception {
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

