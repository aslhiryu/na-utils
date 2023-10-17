package neoatlantis.utils.web;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Objeto que permite realizar una conección y recuperar la información de un sitio web
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class WebClient {
    private static final Logger LOGGER=Logger.getLogger(WebClient.class);
    
    private Properties headerParams;
    private int event;
    private boolean saveFiles;
    private String pathFiles="./";

    public WebClient(Properties headerParams) {
        this.headerParams = headerParams;
    }
    
    public WebClient(Properties headerParams, boolean saveFiles) {
        this.headerParams = headerParams;
        this.saveFiles=saveFiles;
    }
    
    public String getPathFiles(){
        return this.pathFiles;
    }    
    
    public void setPathFiles(String pathFiles){
        this.pathFiles=pathFiles;
    }
    
    public void setHeaderParams(Properties headerParams){
        this.headerParams=headerParams;
    }
    
    public void setSaveFiles(boolean saveFiles){
        LOGGER.debug("Modifica la opción de salvado de peticiones a: "+saveFiles);
        this.saveFiles=saveFiles;
    }
    
    public boolean getSaveFiles(){
        return this.saveFiles;
    }
    
    public int getEvent(){
        return this.event;
    }
    
    public byte[] sendRequest(String url) throws Exception{
        return sendRequest(url, Method.GET);
    }
    
    public byte[] sendRequest(String url, Method method) throws Exception{
        return sendRequest(url, method, new byte[0]);
    }
    
    public byte[] sendRequest(String url, Method method, byte[] request) throws Exception{
        return sendRequest(new URL(url), method, request);
    }

    public byte[] sendRequest(URL url) throws Exception{
        return sendRequest(url, Method.GET);
    }
    
    public byte[] sendRequest(URL url, Method method) throws Exception{
        return sendRequest(url, method, new byte[0]);
    }
    
    public byte[] sendRequest(URL urlRequest, Method method, byte[] request) throws Exception{
        HttpURLConnection connection=null;
        byte[] response=null;
        InputStream inResponse;
        int bTmp;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        
        //valida el tipo de metodo d ela invocaion
        if( method==null ){
            method=Method.POST;
        }
        
        LOGGER.debug("Intenta conectarse a la URL: "+urlRequest.toString());
        
        try{
            //genero la conexión
            connection = (HttpURLConnection) urlRequest.openConnection();
            LOGGER.debug("Realiza la invación con el methodo: "+method.toString());
            connection.setRequestMethod(method.toString());
            connection.setRequestProperty(method.toString(), urlRequest.toString()+" HTTP/1.1");
            connection.setFixedLengthStreamingMode(request.length);
            connection.setRequestProperty("Content-Length", ""+request.length);
            connection.setRequestProperty("Host", urlRequest.getHost()+":"+urlRequest.getPort());
            //agrega datos adicionales a la cabecera
            if( this.headerParams!=null ){
                for(Object n: this.headerParams.keySet()){
                    connection.setRequestProperty(n.toString(), this.headerParams.getProperty(n.toString()));
                }
            }
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            displayElements("REQUEST", connection.getRequestProperties(), request);
            if(this.saveFiles){
                saveFile("Request", request);
            }
            
            //envia la peticion
            DataOutputStream outRequest = new DataOutputStream(connection.getOutputStream());
            outRequest.write(request);
            outRequest.close();

            //recupero la respuesta
            this.event=connection.getResponseCode();
            if( connection.getResponseCode()==200 ){
                inResponse = connection.getInputStream();
            }
            else{
                inResponse= connection.getErrorStream();
            }

            //vacio la respuesta en un arreglo
            while(inResponse!=null &&(bTmp=inResponse.read())!=-1){
                bos.write(bTmp);
            }
            response=bos.toByteArray();
            
            displayElements("RESPONSE", connection.getHeaderFields(), response);
            if(this.saveFiles){
                saveFile("Response", bos.toByteArray());
            }
            
            return response;
        }
        catch(Exception ex){
            LOGGER.error("Problema al recuperar la información de la URL: "+urlRequest.toString(), ex);
            throw ex;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    
    private void displayElements(String name, Map<String, List<String>> header, byte[] content){
        String cTmp;
        Iterator<String> iTmp;

        LOGGER.debug("===================== "+name+" =====================");
        if(header!=null){
            iTmp=header.keySet().iterator();
            while(iTmp.hasNext()){
                cTmp=iTmp.next();
                LOGGER.debug(cTmp+"  => "+header.get(cTmp));
            }
        }
        LOGGER.debug("----------------------------------------------------");
        if(content!=null){
            LOGGER.debug(new String(content));
        }
        LOGGER.debug("==================================================");        
    }
    
    private void saveFile(String prefijo, byte[] content) throws FileNotFoundException, IOException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        File fTmp=new File(this.pathFiles);
         
        try{
            //valida si existe el directorio destino, si no lo crea
            if( !fTmp.exists() ){
                fTmp.mkdirs();
            }
            fTmp=new File(this.pathFiles+File.separator+prefijo+sdf.format(new Date()));
            LOGGER.debug("Guarda archivo: "+fTmp.getAbsolutePath());

            if(content!=null){
                FileOutputStream fos=new FileOutputStream(fTmp);
                fos.write(content);
                fos.close();        
            }
        }
        catch(Exception ex){
            LOGGER.error("No se logro guardar el archivo en "+fTmp.getAbsolutePath(), ex);
        }
    }
}
