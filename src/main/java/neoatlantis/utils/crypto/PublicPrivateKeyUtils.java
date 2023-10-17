package neoatlantis.utils.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * Objeto utilitario que permite la generación y uso de llave publica/privada
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PublicPrivateKeyUtils {
    public static final String PRIVATE_KEY="privada";
    public static final String PUBLIC_KEY="publica";
    
    private static final Logger DEBUGER=Logger.getLogger(PublicPrivateKeyUtils.class);
    
    public static Map<String,Key> createKeyPair(int size) throws Exception{
        try{
            HashMap<String, Key> llaves=new HashMap();        

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(size, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            KeyPair pair = keyGen.generateKeyPair();

            PrivateKey pri = pair.getPrivate();
            PublicKey pub = pair.getPublic();

            llaves.put(PRIVATE_KEY, pri);
            llaves.put(PUBLIC_KEY, pub);

            return llaves;
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar las llaves.", ex);
            throw ex;
        }
    }
    
    public static Map<String, String> createStringKeyPair(int size) throws Exception{
        try{
            HashMap<String,String> llaves=new HashMap();        

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(size, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            KeyPair pair = keyGen.generateKeyPair();

            PrivateKey pri = pair.getPrivate();
            PublicKey pub = pair.getPublic();

            llaves.put(PRIVATE_KEY, new String(Base64.encodeBase64(pri.getEncoded())).replaceAll("\n", ""));
            llaves.put(PUBLIC_KEY, new String(Base64.encodeBase64(pub.getEncoded())).replaceAll("\n", ""));

            return llaves;
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar las llaves.", ex);
            throw ex;
        }
    }

    public static PrivateKey createPrivateKeyFromFile(String file) throws Exception{
        try{
            StringBuilder sb=new StringBuilder("");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            boolean agrega=false;
            
            while ( (line=br.readLine())!=null ) {
                //valida si existe la clausula d einicio d ela llave
                if( line.contains("PRIVATE KEY") ){
                    agrega=!agrega;
                    continue;
                }
                //reviso si debo agregar el contenido
                if( agrega ){
                    sb.append(line);
                }
            }
            
            DEBUGER.debug("Private Key leido: "+sb);
            return createPrivateKey(sb.toString());
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar la llave privada.", ex);
            throw ex;
        }
    }
    
    public static PrivateKey createPrivateKey(String llave) throws Exception{
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec( Base64.decodeBase64(llave.replaceAll("\n", "")) );
            return keyFactory.generatePrivate(privateKeySpec);
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar la llave privada.", ex);
            throw ex;
        }
    }

    public static PublicKey createPublicKeyFromFile(String file) throws Exception{
        try{
            StringBuilder sb=new StringBuilder("");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            boolean agrega=false;
            
            while ( (line=br.readLine())!=null ) {
                //valida si existe la clausula d einicio d ela llave
                if( line.contains("PUBLIC KEY") ){
                    agrega=!agrega;
                    continue;
                }
                //reviso si debo agregar el contenido
                if( agrega ){
                    sb.append(line);
                }
            }
            
            DEBUGER.debug("Public Key leido: "+sb);
            return createPublicKey(sb.toString());
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar la llave publica.", ex);
            throw ex;
        }
    }
    
    public static PublicKey createPublicKey(String data) throws Exception{
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(Base64.decodeBase64(data));
            return keyFactory.generatePublic(pubSpec);
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar la llave publica.", ex);
            throw ex;
        }
    }

    public static PublicKey createPublicKeyByCertificateFromFile(String file) throws Exception{
        try{
            StringBuilder sb=new StringBuilder("");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            boolean agrega=false;
            
            while ( (line=br.readLine())!=null ) {
                //valida si existe la clausula d einicio d ela llave
                if( line.contains("CERTIFICATE") ){
                    agrega=!agrega;
                    continue;
                }
                //reviso si debo agregar el contenido
                if( agrega ){
                    sb.append(line);
                }
            }
            
            DEBUGER.debug("Certificado leido: "+sb);
            return createPublickeyByCertificate(sb.toString());
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar la llave publica de un certificado.", ex);
            throw ex;
        }
    }
    
    public static PublicKey createPublickeyByCertificate(String llave) throws Exception{
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec( Base64.decodeBase64(llave.replaceAll("\n", "")) );
            return keyFactory.generatePublic(publicKeySpec);
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar la llave publica del certificado.", ex);
            throw ex;
        }
    }

    public static X509Certificate createCertificateFromFile(String file) throws Exception{
        try{
            StringBuilder sb=new StringBuilder("");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            boolean agrega=false;
            
            while ( (line=br.readLine())!=null ) {
                //valida si existe la clausula de inicio d ela llave
                if( line.contains("CERTIFICATE") ){
                    agrega=!agrega;
                    continue;
                }
                //reviso si debo agregar el contenido
                if( agrega ){
                    sb.append(line);
                }
            }
            
            DEBUGER.debug("Certificado leido: "+sb);
            return createCertificate(sb.toString());
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar el certificado.", ex);
            throw ex;
        }
    }
    
    public static X509Certificate createCertificate(String llave) throws Exception{
        try{
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec( Base64.decodeBase64(llave.replaceAll("\n", "")) );
            ByteArrayInputStream bis=new ByteArrayInputStream(publicKeySpec.getEncoded());
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            return (X509Certificate)fact.generateCertificate(bis);
        }
        catch(Exception ex){
            DEBUGER.error("No se logro generar el certificado.", ex);
            throw ex;
        }
    }
    
    
    public static void main(String[] args) {
        int size=1024;
        File fTmp;
        String dir="./";
        
        for(int i=0; args!=null&&i<args.length; i++){
            if(args[i].indexOf("-size=")==0){
                try{
                    size=Integer.parseInt(args[i].replaceAll("-size=", ""));
                }
                catch(Exception ex){
                    System.out.println("Valor no valido '"+args[i].replaceAll("-size=", "")+"', para el tamaño de las llaves");
                    System.exit(1);
                }
            }
            else if(args[i].indexOf("-output=")==0){
                dir=args[i].replaceAll("-output=", "");
            }
            else if(args[i].equals("-help")){
                System.out.println("Generador de juego de llaves");
                System.out.println("Ej: <java_dir>"+File.separator+"bin"+File.separator+"java -cp \"commons-codec.jar"+File.pathSeparator +"NA_Utils.jar\" "+PublicPrivateKeyUtils.class.getCanonicalName()+" <-size=tam> <-output=dir>");
                System.out.println("\t-size=tam\t Define el tamaño para el par de llaves");
                System.out.println("\t-output=dir\t Define el directorio donde se depositaran el par de llaves");
                System.exit(0);
            }
        }
        
        fTmp=new File(dir);
        if(dir.isEmpty() || !fTmp.exists()){
            System.out.println("El directorio de destino '"+dir+"' no es válido");
            System.exit(2);
        }
        
        //realizo la generación d ela llaves 
        System.out.println("Inicio la generación de las llaves con un tamaño de "+size+" bits");
        try{
            Map<String, String> llaves=createStringKeyPair(size);
            String[] cads;
            
            PrintWriter fos = new PrintWriter(fTmp.getAbsolutePath()+File.separator+"private.txt");
            cads=llaves.get(PRIVATE_KEY).split("(?<=\\G.{64})");
            for(int i=0; cads!=null&&i<cads.length; i++){
                fos.println(cads[i]);
            }
            fos.close();
            System.out.println("Llave privada generada en "+fTmp.getAbsolutePath()+File.separator+"private.txt");
            
            fos = new PrintWriter(fTmp.getAbsolutePath()+File.separator+"public.txt");
            cads=llaves.get(PUBLIC_KEY).split("(?<=\\G.{64})");
            for(int i=0; cads!=null&&i<cads.length; i++){
                fos.println(cads[i]);
            }
            fos.close();
            System.out.println("Llave publica generada en "+fTmp.getAbsolutePath()+File.separator+"public.txt");
        }
        catch(Exception ex){
            System.out.println("Error al generar el juego de llaves: "+ex.getMessage());
            System.exit(3);
        }
    }
}
