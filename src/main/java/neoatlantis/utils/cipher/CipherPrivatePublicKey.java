package neoatlantis.utils.cipher;

import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import neoatlantis.utils.cipher.exceptions.EncryptionException;
import neoatlantis.utils.cipher.interfaces.DataCipher;
import org.apache.commons.codec.binary.Base64;

/**
 * Cifrador de Datos basado en el Llave publica y privada
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CipherPrivatePublicKey implements DataCipher {
    public static final int PRIVATE_CIPHER=0;
    public static final int PUBLIC_CIPHER=1;
    
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private int mode=PUBLIC_CIPHER;
    private String classProvider=null; 

    public CipherPrivatePublicKey(PublicKey publicKey, PrivateKey privateKey){
        this(publicKey, privateKey, PUBLIC_CIPHER, null);
    }
    
    public CipherPrivatePublicKey(PublicKey publicKey, PrivateKey privateKey, int mode, String className){
        this.privateKey=privateKey;
        this.publicKey=publicKey;
        this.mode=mode;
        this.classProvider=className;
    }
    



    @Override
    public String cipher(String texto) throws EncryptionException {
        byte[] res;
        Cipher cipher;
        
        try{
            if( this.getClassProvider()!=null ){
                cipher = Cipher.getInstance("RSA", this.getClassProvider());
            }
            else{
                cipher = Cipher.getInstance("RSA");
            }
            cipher.init(Cipher.ENCRYPT_MODE, (this.getMode()==PUBLIC_CIPHER? this.getPublicKey(): this.getPrivateKey()));
            res=cipher.doFinal(texto.getBytes());
            return new String(Base64.encodeBase64(res));
        }
        catch(Exception ex){
            throw new EncryptionException(ex);
        }
    }

    @Override
    public String decipher(String cifrado) throws EncryptionException {
        byte[] res;
        Cipher cipher;
                
        try{
            if( this.getClassProvider()!=null ){
                cipher = Cipher.getInstance("RSA", this.getClassProvider());
            }
            else{
                cipher = Cipher.getInstance("RSA");
            }
            cipher.init(Cipher.DECRYPT_MODE, (this.getMode()==PUBLIC_CIPHER? this.getPrivateKey(): this.getPublicKey()));
            res=Base64.decodeBase64(cifrado);
            return new String(cipher.doFinal(res));
        }
        catch(Exception ex){
            throw new EncryptionException(ex);
        }
    }

    /**
     * @return the publicKey
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @return the privateKey
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @return the classProvider
     */
    public String getClassProvider() {
        return classProvider;
    }

    /**
     * @param classProvider the classProvider to set
     */
    public void setClassProvider(String classProvider) {
        this.classProvider = classProvider;
    }
    
}
