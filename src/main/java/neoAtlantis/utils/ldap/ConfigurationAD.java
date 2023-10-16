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
public class ConfigurationAD {
    private static final Logger DEBUGGER=Logger.getLogger(ConfigurationAD.class);
    
    public static final String PHOTO_PARAM = "dataPhoto";
    public static final String UNIT_PARAM = "dataUnit";
    public static final String EMPLOYMENT_PARAM = "dataEmployment";
    public static final String BOSS_PARAM = "dataBoss";
    public static final String PHONE_PARAM = "dataPhone";
    
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
            
            if (e.getName().equalsIgnoreCase(ConfigurationLDAP.URL_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.URL_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ConfigurationLDAP.BASE_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.BASE_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ConfigurationLDAP.USER_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.USER_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ConfigurationLDAP.MAIL_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.MAIL_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ConfigurationLDAP.NAME_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.NAME_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(PHOTO_PARAM) != false) {
                p.setProperty(PHOTO_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(UNIT_PARAM) != false) {
                p.setProperty(UNIT_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(EMPLOYMENT_PARAM) != false) {
                p.setProperty(EMPLOYMENT_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(BOSS_PARAM) != false) {
                p.setProperty(BOSS_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(PHONE_PARAM) != false) {
                p.setProperty(PHONE_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ConfigurationLDAP.ORDER_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.ORDER_PARAM, e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase(ConfigurationLDAP.TIMEOUT_PARAM) != false) {
                p.setProperty(ConfigurationLDAP.TIMEOUT_PARAM, e.getValue());
            }
        }
        if (p.getProperty(ConfigurationLDAP.ORDER_PARAM) == null || p.getProperty(ConfigurationLDAP.ORDER_PARAM).length() == 0) {
            p.setProperty(ConfigurationLDAP.ORDER_PARAM, p.getProperty(ConfigurationLDAP.USER_PARAM));
        }
        
        DEBUGGER.info("Configuración de AD parseada de XML");
        validateConfig(p);
        
        return p;
    }

    /**
     * Valida que la configuración del LDAP tenga los atribnutos apropiados
     * @param p Configuración a validar
     * @throws Exception 
     */
    public static void validateConfig(Properties p) throws Exception {
        if (p.getProperty(ConfigurationLDAP.URL_PARAM) == null || p.getProperty(ConfigurationLDAP.URL_PARAM).length() == 0) {
            throw new Exception("Falta la URL del servidor.");
        }
        if (p.getProperty(ConfigurationLDAP.BASE_PARAM) == null || p.getProperty(ConfigurationLDAP.BASE_PARAM).length() == 0) {
            throw new Exception("Falta el contexto base del AD.");
        }
        if (p.getProperty(ConfigurationLDAP.USER_PARAM) == null || p.getProperty(ConfigurationLDAP.USER_PARAM).length() == 0) {
            p.setProperty(ConfigurationLDAP.USER_PARAM, "sAMAccountName");
        }
        if (p.getProperty(ConfigurationLDAP.MAIL_PARAM) == null || p.getProperty(ConfigurationLDAP.MAIL_PARAM).length() == 0) {
            p.setProperty(ConfigurationLDAP.MAIL_PARAM, "mail");
        }
        if (p.getProperty(ConfigurationLDAP.NAME_PARAM) == null || p.getProperty(ConfigurationLDAP.NAME_PARAM).length() == 0) {
            p.setProperty(ConfigurationLDAP.NAME_PARAM, "displayName");
        }
        if (p.getProperty(PHOTO_PARAM) == null || p.getProperty(PHOTO_PARAM).length() == 0) {
            p.setProperty(PHOTO_PARAM, "thumbnailPhoto");
        }
        if (p.getProperty(UNIT_PARAM) == null || p.getProperty(UNIT_PARAM).length() == 0) {
            p.setProperty(UNIT_PARAM, "department");
        }
        if (p.getProperty(EMPLOYMENT_PARAM) == null || p.getProperty(EMPLOYMENT_PARAM).length() == 0) {
            p.setProperty(EMPLOYMENT_PARAM, "title");
        }
        if (p.getProperty(BOSS_PARAM) == null || p.getProperty(BOSS_PARAM).length() == 0) {
            p.setProperty(BOSS_PARAM, "manager");
        }
        if (p.getProperty(PHONE_PARAM) == null || p.getProperty(PHONE_PARAM).length() == 0) {
            p.setProperty(PHONE_PARAM, "telephoneNumber");
        }
        if (p.getProperty(ConfigurationLDAP.ORDER_PARAM) == null || p.getProperty(ConfigurationLDAP.ORDER_PARAM).length() == 0) {
            p.setProperty(ConfigurationLDAP.ORDER_PARAM, "sAMAccountName");
        }
        if (p.getProperty(ConfigurationLDAP.TIMEOUT_PARAM) == null || p.getProperty(ConfigurationLDAP.TIMEOUT_PARAM).length() == 0) {
            p.setProperty(ConfigurationLDAP.TIMEOUT_PARAM, "10");
        } 
        else {
            try {
                Integer.parseInt(p.getProperty(ConfigurationLDAP.TIMEOUT_PARAM));
            } catch (Exception ex) {
                throw new Exception("El timeout debe ser un valor númerico.");
            }
        }
        DEBUGGER.info("Configuración de AD correcta.");
    }
    
}
