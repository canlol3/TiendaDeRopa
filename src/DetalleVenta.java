import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetalleVenta {

    public static void insertarDetalle(Connection conexion, int ventaID, int productoID, int tallaID, int cant,double precio){
        try {
            String insertarDetalle = "INSERT INTO DetalleVenta (Venta_id, Producto_id, Cantidad, PrecioUnitario) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(insertarDetalle);
            ps.setInt(1, ventaID);
            ps.setInt(2, productoID);
            ps.setInt(3, cant);
            ps.setDouble(4, precio);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al insertar el detalle de venta");
            e.printStackTrace();
        }
        

    }
}