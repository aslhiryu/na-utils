package neoAtlantis.utils.web;

import com.google.gson.Gson;
import java.nio.charset.Charset;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class RestServiceClient {
    protected static final Logger LOGGER=Logger.getLogger(RestServiceClient.class);
    private String endpoint;
    
    public RestServiceClient(String endpoint){
        this.endpoint=endpoint;
    }
   
    
    public <T extends Object> T doMethodPost(String method, Object  data, Class<T> tipo) throws RestClientException{
        Gson gson=new  Gson();

        String request=gson.toJson(data);
        String response=requestPost(method, request);
            
        return tipo.cast(gson.fromJson(response, tipo));
    }
    
    public <T extends Object> T doMethodPost(String method, Class<T> tipo) throws RestClientException{
        Gson gson=new  Gson();

        String request=null;
        String response=requestPost(method, request);
            
        return tipo.cast(gson.fromJson(response, tipo));
    }
    
    protected String requestPost(String method, String request) throws RestClientException{
        RestTemplate restTemplete = new RestTemplate();
        HttpEntity<String> entity;
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        
        LOGGER.debug("Inicia una peticion el el endpoint: "+this.endpoint);
        LOGGER.debug("Invoca al metodo: "+method);
        
        headers.setContentType(mediaType);
        LOGGER.debug("=================== REQUEST ===================");
        LOGGER.debug(request);
        LOGGER.debug("=============================================");

        entity = new HttpEntity<String>(request, headers);
        String response= restTemplete.postForObject(this.endpoint+"/"+ method, entity, String.class);
        LOGGER.debug("=================== RESPONSE ===================");
        LOGGER.debug(response);
        LOGGER.debug("==============================================");

        return response;
    }    
    
}
