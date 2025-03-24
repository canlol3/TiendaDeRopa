import java.sql.*;

public class Productos {
    public static int verificarProducto(Connection conexion, String nombre) {
        try {
            String verificarNombre = "SELECT Producto_id FROM Productos WHERE Nombre = ?";
            PreparedStatement st = conexion.prepareStatement(verificarNombre);
            st.setString(1, nombre);
            ResultSet rsProducto = st.executeQuery();
            if (rsProducto.next()) {
                return rsProducto.getInt("Producto_id");
            } else {
                System.out.println("No se encontro un producto con ese nombre");
                System.out.println("¿Quieres añadir un producto nuevo? (S/N)");
                String opcion = System.console().readLine();
                if (opcion.equals("S")) {
                    return insertarProducto(conexion, nombre);

                } else {
                    // return anadirStock(conexion);
                }
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el producto");
            e.printStackTrace();
            return -1;
        }
    }

    public static int insertarProducto(Connection conexion, String nombre) {
        try {
            String obtenerMaxID = "SELECT MAX(Producto_id) AS max_id FROM Productos";
            PreparedStatement psMaxID = conexion.prepareStatement(obtenerMaxID);
            ResultSet rsMaxID = psMaxID.executeQuery();
            int nuevoProductoID = 1; 
            if (rsMaxID.next()) {
                nuevoProductoID = rsMaxID.getInt("max_id") + 1; 
            }

            String insertar = "INSERT INTO Productos (Producto_id,Nombre, Descripción,Categoría, Precio) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(insertar, PreparedStatement.RETURN_GENERATED_KEYS);

            System.out.print("Ingrese una descripción: ");
            String descripcion = System.console().readLine();
            System.out.print("Ingrese el precio: ");
            double precio = Double.parseDouble(System.console().readLine());
            System.out.print("Ingrese una categoría: ");
            String categoria = System.console().readLine();
            ps.setInt(1, nuevoProductoID);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setString(4, categoria);
            ps.setDouble(5, precio);
            ps.executeUpdate();
            System.out.println("Producto nuevo añadido correctamente " + nombre);

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Error al obtener el ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }
    public static double obtenerPrecio(Connection conexion, String nombre){
        try {
            String obtenerPrecio = "SELECT Precio FROM Productos WHERE Nombre = ?";
            PreparedStatement ps = conexion.prepareStatement(obtenerPrecio);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getDouble("Precio");
            
            }else{
                System.out.println("El producto no existe");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el precio");
            e.printStackTrace();
            return -1;
        }
        
    }

}