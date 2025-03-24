import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Clientes {

    public static int verificarOInsertarCliente(Connection conexion, String nombre){
        try {
            String verificarCliente= "SELECT Cliente_id FROM Clientes WHERE Nombre = ?";
            PreparedStatement ps = conexion.prepareStatement(verificarCliente);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("Cliente_id");
            }else{
                System.out.println("Ingrese su teléfono: ");
                String telefono = System.console().readLine();
                return insertarCliente(conexion, nombre, telefono);    
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar al cliente");
            e.printStackTrace();
            return -1;
        }
    }
    public static int insertarCliente(Connection conexion,String nombre,String telefono){
        try {
            String insertar = "INSERT INTO Clientes (Nombre, Telefono) VALUES (?, ?)";
            PreparedStatement ps = conexion.prepareStatement(insertar, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new SQLException("Error al obtener el ID del cliente");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente");
            e.printStackTrace();
            return -1;
        }

    }
}