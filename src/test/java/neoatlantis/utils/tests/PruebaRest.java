package neoatlantis.utils.tests;

import neoatlantis.utils.web.RestServiceClient;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PruebaRest {
    public static void main(String[] args) {
        (new PruebaRest()).pruevaCustoimerRst();
    }
    
    void pruevaCustoimerRst(){
        RestServiceClient cli=new RestServiceClient("http://www.thomas-bayer.com/sqlrest");
        
        System.out.println("==> "+ cli.doMethodPost("CUSTOMER/18/", String.class));
    }
}
