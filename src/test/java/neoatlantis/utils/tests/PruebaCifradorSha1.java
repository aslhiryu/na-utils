package neoatlantis.utils.tests;

import neoatlantis.utils.cipher.CipherSha512;
import neoatlantis.utils.cipher.interfaces.DataCipher;

public class PruebaCifradorSha1 {
    public static void main(String[] args) {
        DataCipher cif=new CipherSha512();
        String cad="CRm$1127";
        
        System.out.println("Texto original: " + cad);
        cad = cif.cipher(cad);
        System.out.println("Texto cifrado: "+cad);
    }
    
}
