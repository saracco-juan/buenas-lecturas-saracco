package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Las anotaciones me sirven a mi para entender mejor mi codigo :)
public class ConnectionDB {

    // Variables para establecer la conexion a la BBDD, van en final porque no van a ser modificadas
    private final String url = "jdbc:h2:~/test";
    private final String driver_db = "org.h2.Driver";
    private final String username = "sa";
    private final String password = "";

    // Instancia unica (patron singleton)
    private static ConnectionDB instance;
    // Conexion a la BBDD utilizando la clase Connection
    private Connection conn;

    // Metodo para conectarse a la BBDD
    private ConnectionDB() throws SQLException {

        // Pruebo la conexion -> TRY
        try {
            // Esta linea lo que hace es cargar el driver de JDBC (para H2 en este caso)
            // Una vez cargado, se registra automaticamente en el DriverManager
            Class.forName(driver_db);
            // Creamos la conexion en la varibale conn de la instancia
            this.conn = DriverManager.getConnection(url, username, password);
        }
        // Atajo los errores en caso de error -> CATCH (Multiple)
        catch (ClassNotFoundException e) {
            System.out.println("Driver Not Found");
        }  catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Metodo que devuelve la conexion para utilizarla
    public Connection getConnection() {
        return this.conn;
    }

    // Constructor de la clase static (Patron Singleton)
    public static ConnectionDB getInstance() throws SQLException {
        //Si la instancia es null, creo la conexion
        if (instance == null) {
            instance = new ConnectionDB();
        //Si la instancia existe pero la conexion esta cerrada, creo la conexion
        }else if(instance.getConnection().isClosed()){
            instance = new ConnectionDB();
        }

      // Sino, devuelvo la instance que ya existe
      return instance;
    }

}
