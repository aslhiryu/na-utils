package neoatlantis.utils.ldap.exceptions;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class LdapInvalidUserException extends Exception {
    public LdapInvalidUserException(String msg){
        super(msg);
    }

    public LdapInvalidUserException(String msg, Throwable ex){
        super(msg, ex);
    }
}
