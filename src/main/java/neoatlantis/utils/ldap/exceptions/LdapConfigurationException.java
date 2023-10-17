package neoatlantis.utils.ldap.exceptions;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class LdapConfigurationException extends Exception {
    public LdapConfigurationException(String msg){
        super(msg);
    }

    public LdapConfigurationException(String msg, Throwable ex){
        super(msg, ex);
    }
}
