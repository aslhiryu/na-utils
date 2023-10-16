package pruebas;

import java.io.IOException;
import java.util.Properties;
import pruebas.utils.MiAdDAO;
import pruebas.utils.Persona;

/**
 *
 * @author desarrollo.alberto
 */
public class PruebaAD {
    public static void main(String[] args) throws Exception {
        getUser();
    }
    
    
    static public void getUser() throws Exception{
        Properties p=new Properties();
        p.load(PruebaAD.class.getResourceAsStream("/activeDirectory.properties"));
        
        MiAdDAO dao=new MiAdDAO(p);
        
        Persona pTmp= dao.getPerson("testcrm1");
        System.out.println("=>"+pTmp);
    }
}
