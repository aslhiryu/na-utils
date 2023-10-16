package neoAtlantis.utils.ldap.exceptions;

/**
 *
 * @author desarrollo.alberto
 */
public class LdapConnectionException extends Exception{
    public LdapConnectionException(String msg){
        super(msg);
    }

    public LdapConnectionException(String msg, Throwable ex){
        super(msg, ex);
    }
}
