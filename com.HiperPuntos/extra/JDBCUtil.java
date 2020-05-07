package extra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

    static final Properties CONN_PROPSS= new Properties();

    static{
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException ex){
            ex.printStackTrace();
            System.err.println("No se puede registrar el driver");
            System.exit(-1);
        }
        CONN_PROPSS.put("useSSL", "false");
        CONN_PROPSS.put("allowPublicKeyRetrieval", "true");
        CONN_PROPSS.put("serverTimezone", "America/Argentina/Cordoba");

        CONN_PROPSS.put("port", "3306");
        CONN_PROPSS.put("user","");
        CONN_PROPSS.put("password", "");
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
            "jdbc:mysql://localhost/hiperpuntos", CONN_PROPSS);
    }

}
