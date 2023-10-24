package neoatlantis.utils.web;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import org.apache.log4j.Logger;

/**
 *
 * @author hiryu
 */
public class MBeanRegister {
    private static final Logger DEBUGGER=Logger.getLogger(MBeanRegister.class);

    private MBeanRegister(){
    }
    

    public static void registerMBean(Object bean, Class type, String name){
        try{
            StandardMBean mbean = new StandardMBean(bean, type);
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName oName = new ObjectName(name);
            mbs.registerMBean(mbean, oName);
            DEBUGGER.debug("Publica el MBean de tipo: "+type);
        }
        catch(Exception ex){
            DEBUGGER.warn("No se logro cargar el MBean de tipo: "+type, ex);
        }        
    }
    
    public static void unregisterMBean(String name){
        try{
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName oName = new ObjectName(name);
            if( mbs.isRegistered(oName) ){
                mbs.unregisterMBean(oName);            
                DEBUGGER.debug("Remueve el MBean: "+name);
            }
        }
        catch(Exception ex){
            DEBUGGER.warn("No se logro remover el MBean: "+name, ex);
        }
    }
}
