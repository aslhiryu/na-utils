package pruebas;

import neoAtlantis.utils.cipher.CipherMd5Des;


public class PruebaCifradoMD5DES {
    public static void main(String[] args) {
        CipherMd5Des c=new CipherMd5Des("NA-Platform");
        
        System.out.println("RES: "+c.cipher("123456"));
        
        String cTmp="MD5DES{a9k2HA6rterterterterLrrs=}";
        System.out.println("Cad: "+cTmp.replaceAll("MD5DES", "").substring(1, cTmp.length()-7));
    }
}
