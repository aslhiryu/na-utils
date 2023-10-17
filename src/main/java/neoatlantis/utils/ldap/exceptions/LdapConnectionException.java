package neoatlantis.utils.ldap.exceptions;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class LdapConnectionException extends Exception{
    public LdapConnectionException(String msg){
        super(msg);
    }

    public LdapConnectionException(String msg, Throwable ex){
        super(msg, ex);
    }
}
