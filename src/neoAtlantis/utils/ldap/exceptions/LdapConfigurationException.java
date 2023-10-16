package neoAtlantis.utils.ldap.exceptions;

/**
 *
 * @author desarrollo.alberto
 */
public class LdapConfigurationException extends Exception {
    public LdapConfigurationException(String msg){
        super(msg);
    }

    public LdapConfigurationException(String msg, Throwable ex){
        super(msg, ex);
    }
}
