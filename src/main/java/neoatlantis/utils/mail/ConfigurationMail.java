package neoatlantis.utils.mail;

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
public class ConfigurationMail {
    private static final Logger DEBUGER=Logger.getLogger(ConfigurationMail.class);

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
            
            if (e.getName().equalsIgnoreCase("title") != false) {
                p.setProperty("title", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("host") != false) {
                p.setProperty("host", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("from") != false) {
                p.setProperty("from", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("to") != false) {
                p.setProperty("to", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("user") != false) {
                p.setProperty("user", e.getValue());
            } 
            else if (e.getName().equalsIgnoreCase("pass") != false) {
                p.setProperty("pass", e.getValue());
            } 
        }
        
        DEBUGER.info("Configuración de Mail parseada de XML");
        validateConfig(p);
        
        return p;
    }

    public static void validateConfig(Properties p) throws Exception {
        if (p.getProperty("host") == null || p.getProperty("host").length() == 0) {
            throw new Exception("Falta el servidor de correos.");
        }
        if (p.getProperty("from") == null || p.getProperty("from").length() == 0) {
            throw new Exception("Falta el remitente del correo.");
        }
        if (p.getProperty("to") == null || p.getProperty("to").length() == 0) {
            throw new Exception("Falta el destinatario del correo.");
        }
        if (p.getProperty("title") == null || p.getProperty("title").length() == 0) {
            p.setProperty("title", "Notificación");
        }
        
        DEBUGER.info("Configuración de MAIL correcta.");
    }

    public static Properties parseConfig(Properties config, boolean debug) {
        Properties p=new Properties();
        
        DEBUGER.debug("Configuración proporcionada: "+config);
        p.setProperty("mail.host", config.getProperty("host"));
        p.setProperty("mail.smtp.host", config.getProperty("host"));
        if( config.getProperty("user")!=null ){
            p.setProperty("mail.user", config.getProperty("user"));
        }
        p.setProperty("mail.from", config.getProperty("from"));
        p.setProperty("mail.debug", ""+debug);
        
        return p;
    }
}
