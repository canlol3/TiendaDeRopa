import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuarios {
    public static Boolean login(Connection conexion, String usuario, String contrasena){
        if(conexion==null){
            System.out.println("No hay comexión con la base de datos");
            return null;
        }
        String query = "SELECT admin FROM Usuario Where Nombre = ? AND Contraseña = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("admin");
            }else{
                System.out.println("Usuario o contraseña incorrecto");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error en el login ");
            e.printStackTrace();
            return null;
        }
    }
}
