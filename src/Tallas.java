import java.sql.*;
public class Tallas{
    public static int verificarTalla(Connection conexion, String talla){
        try {
            String verificarTalla = "SELECT Talla_id FROM Tallas WHERE Talla = ?";
            PreparedStatement psTalla = conexion.prepareStatement(verificarTalla);
            psTalla.setString(1, talla);
            ResultSet rsTalla = psTalla.executeQuery();

            if(rsTalla.next()){
               return rsTalla.getInt("Talla_id");
            }else{
                String insertarTalla = "INSERT INTO Tallas (Talla) VALUES (?)";
                PreparedStatement psInsertTalla = conexion.prepareStatement(insertarTalla,PreparedStatement.RETURN_GENERATED_KEYS);
                psInsertTalla.setString(1, talla);
                int filas = psInsertTalla.executeUpdate();

                if(filas>0){
                    ResultSet generarID = psInsertTalla.getGeneratedKeys();
                    if(generarID.next()){
                        int tallaNueva = generarID.getInt(1);
                        System.out.println("Se ha añadido una nueva talla -> "+talla);
                        return tallaNueva;
                    }
                }
                throw new SQLException("Error al obtener el ID de la talla");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la talla");
            e.printStackTrace();
            return -1;
        }

    }
}