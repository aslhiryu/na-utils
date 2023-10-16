package neoAtlantis.utils.ldap;

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
    private static final Logger DEBUGGER=Logger.getLogger(ConfigurationLDAP.class);
    
    public static final String URL_PARAM="url";
    public static final String BASE_PARAM="base";
    public static final String USER_PARAM="dataUser";
    public static final String MAIL_PARAM="dataMail";
    public static final String NAME_PARAM="dataName";
    public static final String ORDER_PARAM="order";
    public static final String TIMEOUT_PARAM="timeout";

    /**
     * Carga la configuración para el LDAP a partir de un archivo XML
     * @param xml Flujo de datos al archivo XML
     * @return Configuración del LDAP
     * @throws Exception 
     */
    public static Properties parseXmlConfiguration(InputStream xml) throws Exception {
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
            
            if (e.getName().equalsIgnoreCase(URL_PARAM) != false) {
                p.setProperty(URL_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(BASE_PARAM) != false) {
                p.setProperty(BASE_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(USER_PARAM) != false) {
                p.setProperty(USER_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(MAIL_PARAM) != false) {
                p.setProperty(MAIL_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(NAME_PARAM) != false) {
                p.setProperty(NAME_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ORDER_PARAM) != false) {
                p.setProperty(ORDER_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(TIMEOUT_PARAM) != false) {
                p.setProperty(TIMEOUT_PARAM, e.getValue());
            }
        }
        if (p.getProperty(ORDER_PARAM) == null || p.getProperty(ORDER_PARAM).length() == 0) {
            p.setProperty(ORDER_PARAM, p.getProperty(USER_PARAM));
        }
        
        DEBUGGER.info("Configuración de LDAP parseada de XML");
        validateConfig(p);
        
        return p;
    }

    /**
     * Valida que la configuración del LDAP tenga los atribnutos apropiados
     * @param p Configuración a validar
     * @throws Exception 
     */
    public static void validateConfig(Properties p) throws Exception {
        if (p.getProperty(URL_PARAM) == null || p.getProperty(URL_PARAM).length() == 0) {
            throw new Exception("Falta la URL del servidor.");
        }
        if (p.getProperty(BASE_PARAM) == null || p.getProperty(BASE_PARAM).length() == 0) {
            throw new Exception("Falta el contexto base del LDAP.");
        }
        if (p.getProperty(USER_PARAM) == null || p.getProperty(USER_PARAM).length() == 0) {
            p.setProperty(USER_PARAM, "userPrincipalName");
        }
        if (p.getProperty(MAIL_PARAM) == null || p.getProperty(MAIL_PARAM).length() == 0) {
            p.setProperty(MAIL_PARAM, "mail");
        }
        if (p.getProperty(NAME_PARAM) == null || p.getProperty(NAME_PARAM).length() == 0) {
            p.setProperty(NAME_PARAM, "name");
        }
        if (p.getProperty(ORDER_PARAM) == null || p.getProperty(ORDER_PARAM).length() == 0) {
            p.setProperty(ORDER_PARAM, "userPrincipalName");
        }
        if (p.getProperty(TIMEOUT_PARAM) == null || p.getProperty(TIMEOUT_PARAM).length() == 0) {
            p.setProperty(TIMEOUT_PARAM, "10");
        } 
        else {
            try {
                Integer.parseInt(p.getProperty(TIMEOUT_PARAM));
            } catch (Exception ex) {
                throw new Exception("El timeout debe ser un valor n\u00famerico.");
            }
        }
        DEBUGGER.info("Configuración de LDAP correcta.");
    }
    
}
