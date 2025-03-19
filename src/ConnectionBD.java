import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
    static String url = "jdbc:mysql://localhost:3306/Tienda";
    static String usuario = "root";
    
    public static Connection conectar(){
        try {
            return DriverManager.getConnection(url, usuario,null) ;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos " + e.getMessage());
            return null;
        }
    }
}
