package neoAtlantis.utilidades.ldap;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ConfigurationLDAP {
    private static final Logger LOGGER=Logger.getLogger(ConfigurationLDAP.class);
    
    public static Properties parseConfiguracionXML(InputStream xml) throws Exception {
        Document doc;
        Element e;
        Element raiz;
        Iterator i;
        List hojas;
        Properties p;
        SAXBuilder builder;

        builder = new SAXBuilder(false);
        doc = builder.build(xml);
        raiz = doc.getRootElement();
        p =  new Properties();
        hojas = raiz.getChildren();
        i = hojas.iterator();
        
        while (i.hasNext()) {
            e = (Element) i.next();
            
            if (e.getName().equalsIgnoreCase("url") != false) {
                p.setProperty("url", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("base") != false) {
                p.setProperty("base", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("bindType") != false) {
                p.setProperty("bindType", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("dataUser") != false) {
                p.setProperty("dataUser", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("dataMail") != false) {
                p.setProperty("dataMail", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("dataName") != false) {
                p.setProperty("dataName", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("order") != false) {
                p.setProperty("order", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("timeout") != false) {
                p.setProperty("timeout", e.getValue());
            }
        }
        if (p.getProperty("order") == null || p.getProperty("order").length() == 0) {
            p.setProperty("order", p.getProperty("dataUser"));
        }
        
        LOGGER.info("Configuración de LDAP parseada de XML");
        validaConfigProperties(p);
        
        return p;
    }

    public static void validaConfigProperties(Properties p) throws Exception {
        if (p.getProperty("url") == null || p.getProperty("url").length() == 0) {
            throw new Exception("Falta la URL del servidor.");
        }
        if (p.getProperty("base") == null || p.getProperty("base").length() == 0) {
            throw new Exception("Falta el contexto base del LDAP.");
        }
        if (p.getProperty("bindType") == null || p.getProperty("bindType").length() == 0) {
            p.setProperty("bindType", "ldap");
        }
        if (p.getProperty("dataUser") == null || p.getProperty("dataUser").length() == 0) {
            p.setProperty("dataUser", "sAMAccountName");
        }
        if (p.getProperty("dataMail") == null || p.getProperty("dataMail").length() == 0) {
            p.setProperty("dataMail", "mail");
        }
        if (p.getProperty("dataName") == null || p.getProperty("dataName").length() == 0) {
            p.setProperty("dataName", "displayName");
        }
        if (p.getProperty("order") == null || p.getProperty("order").length() == 0) {
            p.setProperty("order", "sAMAccountName");
        }
        if (p.getProperty("timeout") == null || p.getProperty("timeout").length() == 0) {
            p.setProperty("timeout", "10");
        } 
        else {
            try {
                Integer.parseInt(p.getProperty("timeout"));
            } catch (Exception ex) {
                throw new Exception("El timeout debe ser un valor n\u00famerico.");
            }
        }
        LOGGER.info("Configuración de LDAP correcta.");
    }
}
