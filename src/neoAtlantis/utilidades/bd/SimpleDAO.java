package neoAtlantis.utilidades.bd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import neoAtlantis.utilidades.entity.SimpleEntity;
import neoAtlantis.utilidades.entity.ContainerEntities;
import org.apache.log4j.Logger;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public abstract class SimpleDAO {
    protected String url;
    protected String driver;
    protected String user;
    protected String pass;
    protected String jndi;
    protected Connection con;
    protected Statement st;
    protected String sql;
    protected boolean pool=false;

    static final Logger log = Logger.getLogger(SimpleDAO.class);

    public SimpleDAO(InputStream xml) throws Exception{
        Properties p=ConfigurationDB.parseConfiguracionXML(xml);

        if (p.getProperty("jndi") == null || p.getProperty("jndi").length() == 0) {
            this.url=p.getProperty("url");
            this.driver=p.getProperty("driver");
            this.user=p.getProperty("user");
            this.pass=p.getProperty("pass");
        }
        //si existe jndi
        else{
            this.jndi=p.getProperty("jndi");
        }

        this.con = null;
        this.st = null;
        log.debug( "Intenta configurar la conexión");
    }

    public SimpleDAO(File xml) throws Exception {
        this(new FileInputStream(xml));
    }

    public SimpleDAO(String xml) throws Exception {
        this(new File(xml));
    }

    public SimpleDAO(Properties configBD) throws Exception {
        ConfigurationDB.validaConfigProperties(configBD);
        this.url=configBD.getProperty("url");
        this.driver=configBD.getProperty("driver");
        this.user=configBD.getProperty("user");
        this.pass=configBD.getProperty("pass");
        this.jndi=configBD.getProperty("jndi");
    }

    public SimpleDAO(String driver, String url, String user, String pass) throws Exception {
        this.driver=driver;
        this.url=url;
        this.user=user;
        this.pass=pass;
    }

    public List<String> getTablas() throws ClassNotFoundException, SQLException, NamingException{
        ArrayList<String> tab=new ArrayList<String>();

        //obtengo información de la BD
        log.debug("Recupero las tablas de la conexión.");
        try{
             this.generaConexion(false);
             DatabaseMetaData md = con.getMetaData();
             String esquema = md.getUserName();
             log.debug( "El esquema por default es '" + esquema + "'. " + md.getSchemaTerm());

             ResultSet rs = md.getTables(null, esquema, "%", new String[]{"TABLE"});
             while (rs.next()) {
                 tab.add(rs.getString(3));
             }
             rs.close();
        }catch(ClassNotFoundException ex1){
            log.fatal("No se encontro el driver.", ex1);
            throw ex1;
        } catch (SQLException ex2) {
            log.fatal( "No se logro generar la conexión.", ex2);
            throw ex2;
        }
        finally{
            this.cierraConexion();
        }

        return tab;
    }

    public boolean alter(String sql) throws SQLException, ClassNotFoundException, NamingException{
        boolean exito=false;

        log.debug("Intento generar un evento de modificación al esquema ("+sql+").");
        try{
            this.generaConexion(false);
            this.st=this.con.createStatement();
            exito=this.st.execute(sql);
        }catch(ClassNotFoundException ex1){
            log.fatal("No se encontro el driver.", ex1);
            throw ex1;
        }catch(SQLException ex2){
            log.fatal("No se logro generar la conexión.", ex2);
            throw ex2;
        }
        finally{
            this.cierraConexion();
        }
        log.debug("Resultado de la ejecución: "+exito);

        return exito;
    }

    //--------------- metodos protegidos
    protected void generaConexion(boolean especial) throws ClassNotFoundException, SQLException, NamingException {
        try {
            log.debug("Intenta generar conexión.");

            if( this.jndi!=null && this.jndi.length()>0 ){
                log.debug("Intenta generar conexión por jndi.");
                this.con =  ((DataSource)(new InitialContext()).lookup(this.jndi)).getConnection();
            }
            else{
                log.debug("Intenta generar conexión por jdbc.");

                Class.forName(this.driver);
                this.con =  DriverManager.getConnection(this.url, this.user, this.pass);
            }

            if (especial) {
                this.st = this.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } else {
                this.st = this.con.createStatement();
            }
        } catch (ClassNotFoundException ex1) {
            log.fatal("No se encontro el driver.", ex1);
            throw ex1;
        } catch (SQLException ex2) {
            log.fatal("No se logro generar la conexión.", ex2);
            throw ex2;
        }
    }

    protected void cierraConexion() throws SQLException {
        try {
            log.debug("Intenta cerrar la conexión. ");

            if (this.st != null) {
                this.st.close();
            }
            if (this.con != null) {
                this.con.close();
            }
        } catch (SQLException ex) {
            log.fatal("No se logro cerrar la conexión.", ex);
            throw ex;
        }

        this.st = null;
        this.con = null;
    }

    protected SimpleEntity obtieneEntidad(PreparedStatement ps) throws SQLException {
        SimpleEntity e=null;
        ResultSet res;

        try {
            log.debug("Ejecuto el query '"+this.sql+"'. ");
            res=ps.executeQuery();
            if( res.next() ){
                e=parseEntity(res);
            }

            ps.close();
            res.close();
            this.cierraConexion();
        } catch (SQLException ex) {
            log.fatal("No se logro ejecutar el query.", ex);
            throw ex;
        }

        return e;
    }

    protected ContainerEntities obtieneEntidades(PreparedStatement ps) throws SQLException {
        ContainerEntities c=new ContainerEntities();
        ResultSet res;

        try {
            log.debug("Ejecuto el query '"+this.sql+"'. ");
            res=ps.executeQuery();
            while( res.next() ){
                c.add( parseEntity(res) );
            }

            ps.close();
            res.close();
            this.cierraConexion();
        } catch (SQLException ex) {
            log.fatal("No se logro ejecutar el query.", ex);
            throw ex;
        }

        return c;
    }

    protected int modificoEntidad(PreparedStatement ps) throws SQLException {
        int res;

        try {
            log.debug("Ejecuto el query '"+this.sql+"'. ");
            res=ps.executeUpdate();

            ps.close();
            this.cierraConexion();
        } catch (SQLException ex) {
            log.fatal("No se logro ejecutar la actualización.", ex);
            throw ex;
        }

        return res;
    }

    protected PreparedStatement preparaStatement(String sql) throws Exception {
        PreparedStatement ps;

        try {
            this.sql=sql;

            this.generaConexion(true);
            ps=this.con.prepareStatement(sql);
        } catch (Exception ex) {
            log.fatal("No se logro obtener el PS.", ex);
            throw ex;
        }

        return ps;
    }

    //metodos abstractos
    public abstract SimpleEntity parseEntity(ResultSet res);
}
