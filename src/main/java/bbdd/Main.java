package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    // @TODO Sistituye xxx por los parámetros de tu conexión
    private final static String DB_SERVER = "localhost";
    private final static int DB_PORT = 3306;
    private final static String DB_NAME = "titanic_spaceship";
    private final static String DB_USER = "root";
    private final static String DB_PASS = "Fran2005";
    private static Connection conn;

    public static void main (String [] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
    
        // @TODO Prueba sus funciones
        // 1. Añade los planetas a la base de datos
        nuevoPlaneta("Kepler-186f", 3.3e24, 8800, "Copernico");
        nuevoPlaneta("HD 209458 b (Osiris)", 1.4e27, 100000, "Beta Pictoris");
        nuevoPlaneta("LHS 1140 b", 8.3e24, 8800, "Copernico");
        // 2. Muestra por pantalla la lista de pasajeros de la cabina A-60-S
        System.out.println("LISTA DE PASAJEROS CABINA:");
        listaPasajerosCabina("A" , 60, "S");
        // 3. Muestra por pantalla una lista de sistemas, planetas y número de pasajeros con origen en ellos
        System.out.println("LISTA DE ORIGENES:");
        listaOrigenes();
        conn.close();
    }

    private static void nuevoPlaneta (String nombre, double masa, int radio, String sistema) throws SQLException {
        // @TODO Añade planetas a la base de datos
        Statement stmt = null;
        String sql = "INSERT INTO planetas(nombre,masa,radio,sistema) VALUES('"+nombre+"','"+masa +"','"+radio+"','"+sistema+"')";
        try {
            stmt = conn.createStatement();
            int result = stmt.executeUpdate(sql);
            if (result==1) {
                System.out.println("Planeta " + nombre + " insertado con exito");
            }
        } catch (SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void listaPasajerosCabina (String cubierta, int cabina, String lado) throws SQLException {
        // @TODO Muestra por pantalla una lista de pasajeros de una cabina
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT nombre,edad FROM pasajeros WHERE cubierta='"+cubierta+"' AND numero_cabina='"+cabina+"' AND lado_cabina='"+lado+"'";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Pasajero:");
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Edad: " + rs.getString("edad"));
                System.out.println("-------------");
            }
        } catch (SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void listaOrigenes() throws SQLException {
        // @TODO Muestra por pantalla una lista de planetas, sistemas y número de pasajeros provinientes de ellos
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT pa.nombre,pa.sistema, count(*) AS cantidad FROM planetas pa \n" +
                "INNER JOIN pasajeros p ON p.sistema_natal = pa.sistema AND p.planeta_natal = pa.nombre\n" +
                "GROUP BY pa.nombre,pa.sistema";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Sistema: " + rs.getString("sistema"));
                System.out.println("Planeta: " + rs.getString("nombre"));
                System.out.println("Pasajeros natales: " + rs.getString("cantidad"));
                System.out.println("-------------");
            }
        } catch (SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }
}