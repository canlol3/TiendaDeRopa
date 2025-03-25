import java.sql.*;
import java.text.SimpleDateFormat;
public class Ventas {
    public static void verComprasCliente(Connection conexion,String nombre){
        String query = """
            SELECT v.Venta_id, c.Nombre AS Cliente, v.Fecha, v.Total, v.Metodo_pago
            FROM Ventas v
            LEFT JOIN Clientes c ON v.Cliente_id = c.Cliente_id
            WHERE c.Nombre = ?
            ORDER BY v.Fecha DESC;
        """;
        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1,nombre);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat fecha_format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            boolean hayCompras = false;

            while (rs.next()) {
                hayCompras = true;
                int idVenta = rs.getInt("Venta_id");
                String cliente = (rs.getString("Cliente") != null) ? rs.getString("Cliente") : "Desconocido";
                Timestamp fecha = rs.getTimestamp("Fecha");
                String fecha_formateada = (fecha != null) ? fecha_format.format(fecha) : " "; 
                double total = rs.getDouble("Total");
                String metodoPago = rs.getString("Metodo_pago");
                System.out.printf("ID Venta: %2d | Cliente: %-11s | Fecha: %-10s | Total: %-7.2f | Método: %-10s\n",
                                  idVenta, cliente, fecha_formateada, total, metodoPago);

            }
            if(!hayCompras){
                System.out.println("No se ha registrado ventas como: "+nombre);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todas las ventas ");
            e.printStackTrace();
        }
    }

    public static void verTodasVentas(Connection conexion){
        String clientes = "SELECT DISTINCT Nombre FROM Clientes";
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(clientes);
            while (rs.next()) {
                String nombre =rs.getString("Nombre");
                verComprasCliente(conexion, nombre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registrarVenta(Connection conexion,String nombre_producto, String talla,int cant,String pago,String nombre_cliente){
        int clienteID = Clientes.verificarOInsertarCliente(conexion, nombre_cliente);

        int productoID = Productos.verificarProducto(conexion, nombre_producto);
        if(productoID==-1){
            System.out.println("El producto no existe");
            return;
        }
        int tallaID = Tallas.verificarTalla(conexion, talla);
        if(tallaID ==-1){
            System.out.println("La talla no existe");
            return;
        }
        if(!Inventario.verificarStock(conexion, productoID, tallaID, cant)){
            System.out.println("No se pueden retirar mas de los que hay en el inventario");
            return;
        }
        double precio = Productos.obtenerPrecio(conexion, nombre_producto);
        double total = precio * cant;

        int ventaID = insertarVenta(conexion, pago, clienteID,total);

        DetalleVenta.insertarDetalle(conexion, ventaID, productoID, tallaID, cant, precio);

    }
    public static int insertarVenta(Connection conexion, String metodoPago, int clienteID,double total){
        try {
            String insertarVenta = "INSERT INTO Ventas (Cliente_id, Fecha, Total, Metodo_pago) VALUES (?, NOW(), ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(insertarVenta, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clienteID);
            ps.setDouble(2, total);
            ps.setString(3, metodoPago);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int ventaID = -1;
            if(rs.next()){
                ventaID = rs.getInt(1);
            }else{
                throw new SQLException("Error al obtener el id de la venta");
            }
            return ventaID;
        } catch (SQLException e) {
            System.out.println("Error al insertar la venta");
            e.printStackTrace();
            return -1;
        }
        
    }
     
}