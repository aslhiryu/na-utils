package pruebas;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Map;
import neoAtlantis.utils.cipher.CipherPrivatePublicKey;
import neoAtlantis.utils.crypto.PublicPrivateKeyUtils;
import neoAtlantis.utils.data.DataUtils;

/**
 *
 * @author desarrollo.alberto
 */
public class PruebaCripto {
    public static void main(String[] args) throws Exception {
        //pruebaKayPar();
        //PublicPrivateKeyUtils.createPublicKeyFromFile("i:/proyectos/ine/INE_KPUB-2048.pem");
        X509Certificate c=PublicPrivateKeyUtils.createCertificateFromFile("i:/proyectos/ine/cerInst2/Cert_Inst_SE_004B_test.cer");
        //System.out.println("CER: "+c);
        System.out.println("Ll: "+DataUtils.formatHexadecimal(c.getPublicKey().getEncoded(), true));
        System.out.println("Tamaño: "+c.getPublicKey().getEncoded().length);
        //System.out.println("SER: "+c.getSerialNumber().toString(16));
        //PublicKey key=PublicPrivateKeyUtils.createPublicKeyFromFile("i:/proyectos/ine/cerInst2/INSTITUTONACIONALELECTORAL_publica.key");
        PublicKey key=PublicPrivateKeyUtils.createPublicKeyFromFile("i:/proyectos/ine/cerInst2/SECRETARIADEECONOMIA_publica.key");
        byte[] dat=key.getEncoded();
        //System.out.println("Data: "+dat);
        System.out.println("Ll: "+DataUtils.formatHexadecimal(dat, true));
        System.out.println("Tamaño: "+dat.length);
    }
    
    public static void pruebaKayPar() throws Exception{
        Map<String,String> keys=PublicPrivateKeyUtils.createStringKeyPair(2048);
        String  cadTmp="cadena para probar las llaves";
        String res;
        PublicKey pub;
        
        System.out.println("Privada: "+keys.get("privada"));
        System.out.println("Publica: "+keys.get("publica"));
        
        PrivateKey pri=PublicPrivateKeyUtils.createPrivateKey(keys.get("privada"));        
        System.out.println("PRI: "+pri);
        
        pub=PublicPrivateKeyUtils.createPublickeyByCertificate("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCa9VPYPEVDsVXJsmQ2f0CW7IksOqfMZbYsoA6tXOnoGfQGfi9wQCC/m1kyA5Xu05De0S3DQ5shHPdYIYlg1bHt4SE+iyHKzHdtW0J1/REzja3vARe67enWq0BhydDhmf8+ElyYbutS62ZWgyuAUvQcBJj4QN2drKFOK2mMl9twwIDAQAB");
        System.out.println("PUB2: "+pub);

        pub=PublicPrivateKeyUtils.createPublickeyByCertificate("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGcDlVKIarSPJdGAhocsT/hEDp46SDfI4DDE9A0fnADwK5rmGRajPIlaEsffFoX9frhwdUuCDrBUSrBknA8xswS/vK2xUpTsJj/WFjMZ/XpecXTChMjfHfosNVzCeIaZt3cq1NE4FLt8LChZ0HSuKcXmhv1UvbR1JYx5I+epL7OwIDAQAB");
        System.out.println("PUB3: "+pub);
        
        pub=PublicPrivateKeyUtils.createPublickeyByCertificate(keys.get("publica"));
        System.out.println("PUB: "+pub);

        CipherPrivatePublicKey cif=new CipherPrivatePublicKey(pub, pri);
        res=cif.cipher(cadTmp);
        System.out.println("Cifrado: "+res);
        //System.out.println("Cifrado: "+new String(res));
        //System.out.println("Cifrado: "+(new BASE64Encoder()).encode(res));
        
        res=cif.decipher(res);
        System.out.println("Descifrado: "+res);
        //System.out.println("Cifrado: "+new String(res));
        //System.out.println("Cifrado: "+(new BASE64Encoder()).encode(res));
        
    }
}
