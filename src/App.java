import java.sql.Connection;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Connection conexion = ConnectionBD.conectar();
        if(conexion==null){
            System.out.println("No hay conexión con la base de datos (MAIN)");
            return;
        }
        String usuario;
        String contrasena;
        System.out.print("Introduzca el usuario: ");
        usuario = System.console().readLine();
        System.out.print("Introduzca la contraseña: ");
        contrasena = System.console().readLine();
        boolean login = Usuarios.login(conexion,usuario,contrasena);
        
        if(login){
            System.out.println("Eres admin");
            gestionEmpleado(conexion);
        }else{
            gestionCliente(conexion);
        }
        
        conexion.close();

    }

    public static void gestionCliente(Connection conexion) {
        int opcion = 0;
        while (opcion != 4) {
            menus(1);
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(System.console().readLine());
            switch (opcion) {
                case 1:
                    // Ver ropa
                    Inventario.verInventario(conexion);
                    break;
                case 2:
                    // Comprar
                    break;
                case 3:
                    // Ver mis compras
                    break;
                case 4:
                    // Salir
                    System.out.println("Adios");
                    break;
            }
        }
    }
    public static void gestionEmpleado(Connection conexion) {
        int opcion = 0;
        while (opcion != 5) {
            menus(2);
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(System.console().readLine());
            switch (opcion) {
                case 1:
                    // Ver inventario
                    Inventario.verInventario(conexion);
                    break;
                case 2:
                    // Ver ventas
                    Ventas.verTodas(conexion);
                    break;
                case 3:
                    // Añadir inventario
                    Inventario.anadirStock(conexion);
                    break;
                case 4:
                    // Eliminar inventario
                    System.out.print("Introduce el nombre del producto: ");
                    String nombre = System.console().readLine().toLowerCase();
                    System.out.print("Introduce la talla del producto: ");
                    String talla = System.console().readLine().toLowerCase();
                    System.out.print("Introduce la cantidad a retirar: ");
                    int cant = Integer.parseInt(System.console().readLine());
                    Inventario.eliminarInventario(conexion, nombre, talla, cant);
                    break;
                case 5:
                    // Salir
                    System.out.println("Adios");
                    break;
            }
        }
    }
    public static void menus(int num) {
        switch (num) {
            case 1:
                System.out.print("""
                        --------------------
                        - Ver Ropa        1
                        - Comprar         2
                        - Ver mis Compras 3
                        - Salir           4
                        """);
                break;
            case 2:
                System.out.print("""
                        -----------------------
                        - Ver Inventario      1
                        - Ver ventas          2
                        - Añadir inventario   3
                        - Eliminar inventario 4
                        - Salir               5
                        """);
                break;

        }

        
    }
}
