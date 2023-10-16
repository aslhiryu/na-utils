package neoAtlantis.utils.ldap.exceptions;

/**
 *
 * @author desarrollo.alberto
 */
public class LdapInvalidUserException extends Exception {
    public LdapInvalidUserException(String msg){
        super(msg);
    }

    public LdapInvalidUserException(String msg, Throwable ex){
        super(msg, ex);
    }
}
