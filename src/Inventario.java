import java.sql.*;

public class Inventario {
   public static void verInventario(Connection conexion) {
      String query = """
            SELECT p.Nombre,t.Talla,i.Cantidad
            from Inventario i
            join Productos p on i.Producto_id = p.Producto_id
            join Tallas t on i.Talla_id = t.Talla_id
            order by p.Nombre, t.Talla;
            """;
      try {
         Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(query);

         System.out.println("\nInventario Disponible:");
         System.out.println("---------------------------------------------------------------");
         while (rs.next()) {
            String nombre = rs.getString("Nombre");
            String talla = rs.getString("Talla");
            int stock = rs.getInt("Cantidad");
            System.out.printf("Producto: %25s | Talla: %-5s | Stock: %-5d \n", nombre, talla, stock);

         }
         System.out.println("---------------------------------------------------------------");

      } catch (SQLException e) {
         System.out.println("Error al obtener el inventario ");
         e.printStackTrace();
      }

   }
   public static void anadirStock(Connection conexion) {
      try {
         String obtenerMaxID = "SELECT MAX(Inventario_id) AS max_id FROM Inventario";
         PreparedStatement psMaxID = conexion.prepareStatement(obtenerMaxID);
         ResultSet rsMaxID = psMaxID.executeQuery();
         int nuevoInventarioID = 1;    
         if (rsMaxID.next()) {
             nuevoInventarioID = rsMaxID.getInt("max_id") + 1; 
         }

         System.out.print("Ingrese el nombre del producto: ");
         String nombre = System.console().readLine();
         int productoID = Productos.verificarProducto(conexion, nombre);
         if (productoID == -1) {
            System.out.println("Error en el nombre del producto");
            return;
         }

         System.out.print("Ingrese la talla: ");
         String talla = System.console().readLine();
         int tallaID = Tallas.verificarTalla(conexion, talla);

         System.out.print("Introduce la cantidad: ");
         int cant = Integer.parseInt(System.console().readLine());
         verificarInventarioYañadir(conexion, productoID, tallaID, cant,nuevoInventarioID);

      } catch (Exception e) {
         System.out.println("Error al añadir al stock un producto existente");
         e.printStackTrace();
      }
   }
   public static void verificarInventarioYañadir(Connection conexion, int productoID, int tallaID,int cant,int inventarioID){
      try {
         String verificarInvent = "SELECT Cantidad FROM Inventario WHERE Producto_id = ? AND Talla_id = ?";
         PreparedStatement ps = conexion.prepareStatement(verificarInvent);
         ps.setInt(1,productoID);
         ps.setInt(2, tallaID);
         ResultSet rs = ps.executeQuery();
         
         if(rs.next()){
            int cantActual = rs.getInt("Cantidad");
            int cantNueva = cantActual+cant;

            String update = "UPDATE Inventario SET Cantidad = ? WHERE Producto_id = ? AND Talla_id = ?";
            PreparedStatement psUpdate =  conexion.prepareStatement(update);
            psUpdate.setInt(1,cantNueva);
            psUpdate.setInt(2, productoID);
            psUpdate.setInt(3, tallaID);
            psUpdate.executeUpdate();
            System.out.println("Stock actualizado " + cantNueva + " unidades nuevas para el producto con el id "+ productoID); 
         }else{
            System.out.println("El producto con esa talla no existe en el inventario. Se ha añadido ");
            String insertarNuevo = "INSERT INTO Inventario (Inventario_id,Producto_id, Talla_id, Cantidad) VALUES (?, ?, ?, ?)";      
            PreparedStatement psNuevo = conexion.prepareStatement(insertarNuevo);
            psNuevo.setInt(1,inventarioID);
            psNuevo.setInt(2, productoID); 
            psNuevo.setInt(3, tallaID); 
            psNuevo.setInt(4, cant);
            psNuevo.executeUpdate(); 

         }
      } catch (SQLException e) {
         System.out.println("Error al verificar inventario");
         e.printStackTrace();
      }
      
   }
   public static void eliminarInventario(Connection conexion,String nombre, String talla,int cant,boolean admin){

      try {
         String obtenerProductoID = "SELECT Producto_id FROM Productos WHERE Nombre = ?";
          PreparedStatement ps = conexion.prepareStatement(obtenerProductoID);
         ps.setString(1, nombre);
         ResultSet rsProducto = ps.executeQuery();

         if(rsProducto.next()){
            int productoID = rsProducto.getInt("Producto_id");
            String obtenerTallaID = "SELECT Talla_id FROM Tallas WHERE Talla = ?";
            PreparedStatement psTalla = conexion.prepareStatement(obtenerTallaID);
            psTalla.setString(1, talla);
            ResultSet rsTalla = psTalla.executeQuery();

            if(rsTalla.next()){
               int tallaID =rsTalla.getInt("Talla_id");

               String verificarInventario = "SELECT Cantidad FROM Inventario WHERE Producto_id = ? AND Talla_id = ?";
               PreparedStatement psInventario = conexion.prepareStatement(verificarInventario);
               psInventario.setInt(1,productoID);
               psInventario.setInt(2, tallaID);
               ResultSet rsInventario = psInventario.executeQuery();
               
               if(rsInventario.next()){
                  int cantidadNueva = rsInventario.getInt("Cantidad")-cant;
                  if(cantidadNueva<=0){
                     String eliminarFila = "DELETE FROM Inventario WHERE Producto_id = ? AND Talla_id = ?";
                     PreparedStatement psEliminar = conexion.prepareStatement(eliminarFila);
                     psEliminar.setInt(1,productoID);
                     psEliminar.setInt(2, tallaID);
                     psEliminar.executeUpdate();
                     if(admin){
                        System.out.println("Producto eliminado del inventario: "+nombre+" Talla: "+talla);
                     }
                  }else{
                     String actualizarInventario= "UPDATE Inventario SET Cantidad = ? WHERE Producto_id = ? AND Talla_id = ?";
                     PreparedStatement psActualizar = conexion.prepareStatement(actualizarInventario);
                     psActualizar.setInt(1, cantidadNueva);
                     psActualizar.setInt(2, productoID);
                     psActualizar.setInt(3, tallaID);
                     psActualizar.executeUpdate();
                     if(admin){
                        System.out.println("Stock actualizado: "+ cantidadNueva+ " unidades para: "+nombre+ " Talla: "+talla);
                     }
                  }
               }else{
                  System.out.println("No se encontro el producto con esa talla");
               }
            }else{
               System.out.println("No se encontro esa talla");
            }
         }else{
            System.out.println("No se encontro un producto con ese nombre");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
   public static boolean verificarStock(Connection conexion, int productoID, int tallaID,int cant){
      String verificarStock= "SELECT Cantidad FROM Inventario WHERE Producto_id = ? AND Talla_id = ?";
      try {
         PreparedStatement ps = conexion.prepareStatement(verificarStock);
         ps.setInt(1, productoID);
         ps.setInt(2,tallaID);
         ResultSet rs = ps.executeQuery();
         if(!rs.next() || rs.getInt("Cantidad")<cant){
            return false;
         }else{
            return true;
         }
      } catch (SQLException e) {
         System.out.println("Error al verificar el stock");
         e.printStackTrace();
         return false;
      }
      
   }
}
