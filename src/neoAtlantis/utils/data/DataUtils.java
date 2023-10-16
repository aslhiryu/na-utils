package neoAtlantis.utils.data;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import neoAtlantis.utils.cipher.CipherMd5Des;
import neoAtlantis.utils.cipher.exceptions.EncryptionException;
import org.apache.log4j.Logger;

/**
 * Objeto utileria para apoyar las actividades de validación de datos
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class DataUtils {
    private static final Logger DEBUGGER=Logger.getLogger(DataUtils.class);

    private DataUtils(){        
    }
    
    public static String cleanSpecialCharacters(String c){
        if(c==null){
            return null;
        }
        else{
            StringBuilder sb=new StringBuilder("");
            char[] cars=c.toCharArray();
            
            for(char car: cars){
                if(car>='a' && car<='z'){
                    sb.append(car);
                }
                else if(car>='A' && car<='Z'){
                    sb.append(car);
                }
                else if(car>='0' && car<='9'){
                    sb.append(car);
                }
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Valida si una cadena es un tipo boleano en true
     * @param cad Cadena a validar
     * @return 
     */
    public static boolean validateTrueBoolean(String cad){
        if( cad!=null && (cad.equalsIgnoreCase("true") || cad.equalsIgnoreCase("si") || cad.equalsIgnoreCase("1")
                || cad.equalsIgnoreCase("t") || cad.equalsIgnoreCase("verdadero") || cad.equalsIgnoreCase("v")
                 || cad.equalsIgnoreCase("s")) ){
            return true;
        }
        
        return false;
    }

    /**
     * Valida si una cadena es un tipo boleano en false
     * @param cad Cadena a validar
     * @return 
     */
    public static boolean validateFalseBoolean(String cad){
        if( cad!=null && (cad.equalsIgnoreCase("false") || cad.equalsIgnoreCase("no") || cad.equalsIgnoreCase("0")
                || cad.equalsIgnoreCase("f") || cad.equalsIgnoreCase("false") || cad.equalsIgnoreCase("n")) ){
            return true;
        }
        
        return false;
    }
    
    /**
     * Permite recuperar un dato cifrado de la plataforma
     * @param cad Cadena a descifrar
     * @return Cadena en claro
     */
    public static String getCipherData(String cad){
        if(cad==null){
            return null;
        }
        
        if( cad.startsWith("MD5DES{") && cad.endsWith("}") ){
            DEBUGGER.debug("Dato a descifrar: "+cad.replaceAll("MD5DES", "").substring(1, cad.length()-7));
            return (new CipherMd5Des("NA-Platform")).decipher(cad.replaceAll("MD5DES", "").substring(1, cad.length()-7));
        }
        else if(cad.indexOf("{")>2){
            throw new EncryptionException("El tipo de cifrado no es soportado o esta mal integrado el dato cifrado [ALG]{[DATA]}");
        }
        else{
            DEBUGGER.debug("Dato sin cifrar");
            return cad;
        }
    }
    
    public static Date getToday(){
        Calendar cal=Calendar.getInstance();
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    public static String formatDecimal(double decimal){
        DecimalFormat df=new DecimalFormat("###,###,##0.00");
        return df.format(decimal);
    }
    
    public static String getStringToday(){
        return formatDate(getToday());
    }
    
    public static String formatDateTimeday(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        if(date==null){
            return "";
        }
        else{
            return sdf.format(date);
        }
    }
    
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
        
        if(date==null){
            return "";
        }
        else{
            return sdf.format(date);
        }
    }
    
    public static String formatDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        
        if(date==null){
            return "";
        }
        else{
            return sdf.format(date);
        }
    }

    public static String formatTime(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        
        if(date==null){
            return "";
        }
        else{
            return sdf.format(date);
        }
    }

    public static String formatSimpleTime(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        
        if(date==null){
            return "";
        }
        else{
            return sdf.format(date);
        }
    }

    public static String formatHexadecimal(byte[] data, boolean separate){
        StringBuilder sb = new StringBuilder();
        for (int i=0; data!=null&&i<data.length; i++){
            if(i>0&&separate){
                sb.append(" ");
            }
            sb.append(String.format("%02X", data[i]));
        }
        
        return sb.toString();
    }

    /**
     * Valida si una cadena contiene a una dirección de correo valida
     * @param mail Cadena con el correo a validar
     * @return true si es valida
     */
    public static boolean validateEMail(String mail){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = (Pattern.compile(EMAIL_PATTERN)).matcher(mail);
        
        return matcher.matches();
    }

    public static boolean validateRFC(String rfc){
        String RFC_PATTERN = "^([A-Z]{3,4})+([0-9]{6})+([A-Z0-9]{3})?$";
        Matcher matcher = (Pattern.compile(RFC_PATTERN)).matcher(rfc);
        
        return matcher.matches();
    }

    public static boolean validateIP(String ip){
        String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        Matcher matcher = (Pattern.compile(IP_PATTERN)).matcher(ip);
        
        return matcher.matches();
    }
    
    public static String ArrayJoin(Object[] array, String del){
        StringBuilder sb=new StringBuilder("");
        
        for(int i=0; array!=null&&i<array.length; i++){
            if(i>0){
                sb.append(del);
            }
            sb.append(array[i]);
        }
        
        return sb.toString();
    }
}
