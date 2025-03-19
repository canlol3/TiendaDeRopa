import java.sql.*;
import java.text.SimpleDateFormat;
public class Ventas {
    public static void verTodas(Connection conexion){
        String query = """
            SELECT v.Venta_id, c.Nombre AS Cliente, v.Fecha, v.Total, v.Metodo_pago
            FROM Ventas v
            LEFT JOIN Clientes c ON v.Cliente_id = c.Cliente_id
            ORDER BY v.Fecha DESC;
        """;
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);
            SimpleDateFormat fecha_format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.println("-----------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int idVenta = rs.getInt("Venta_id");
                String cliente = (rs.getString("Cliente") != null) ? rs.getString("Cliente") : "Desconocido";
                Timestamp fecha = rs.getTimestamp("Fecha");
                String fecha_formateada = (fecha != null) ? fecha_format.format(fecha) : " "; 
                double total = rs.getDouble("Total");
                String metodoPago = rs.getString("Metodo_pago");
                System.out.printf("ID Venta: %2d | Cliente: %-11s | Fecha: %-10s | Total: %-7.2f | Método: %-10s\n",
                                  idVenta, cliente, fecha_formateada, total, metodoPago);

            }
            System.out.println("-----------------------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error al obtener todas las ventas ");
            e.printStackTrace();
        }
    }
     
}