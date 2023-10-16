package neoAtlantis.utils.cipher;

import java.security.MessageDigest;
import neoAtlantis.utils.cipher.exceptions.EncryptionException;
import neoAtlantis.utils.cipher.interfaces.DataCipher;

/**
 * Cifrador de Datos basado en el algoritmo SHA256.
 * @author Hiryu (aslhiryu@gmail.com)
 * @version 1.0
 */
public class CipherSha512 implements DataCipher {

    /**
     * Cifra un dato
     * @param str Dato a cifrar
     * @return Dato cifrado
     * @throws EncryptionException
     */
    @Override
    public String cipher(String str) throws EncryptionException {
        byte[] digest, buffer = str.getBytes();
        String hash = "";

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(buffer);
            digest = md.digest();

            for(byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) hash += "0";
                hash += Integer.toHexString(b);
            }
        }catch(Exception ex){
            throw new EncryptionException(ex);
        }


        return hash;
    }

    /**
     * Descifra un dato
     * @param str Dato a descifrar
     * @return Dato descifrado
     */
    @Override
    public String decipher(String str) throws EncryptionException {
        throw new EncryptionException("No es posible descifrar con SHA512.");
    }

}
