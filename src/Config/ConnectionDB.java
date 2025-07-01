package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Clase que se encarga de la conexion a la BBDD
public class ConnectionDB {

    // Variables para establecer la conexion a la BBDD

    private final String url = "jdbc:h2:~/test;DB_CLOSE_DELAY=-1";
    private final String driver_db = "org.h2.Driver";
    private final String username = "sa";
    private final String password = "";;

    // Instancia unica (patron singleton)
    private static ConnectionDB instance;

    // Conexion a la BBDD utilizando la clase Connection
    private Connection conn;

    // El constructor establece la conexion
    private ConnectionDB() throws SQLException, ClassNotFoundException {
        //Cargo el driver de JDBC a la conexion
        Class.forName(driver_db);
        //Establezvo la conexion a la BBDD
        this.conn = DriverManager.getConnection(url, username, password);
        //Este comando sirve para que cada consulta sql se confime al ejecutarse (tuve problemas de persistencia y esta lo soluciono)
        this.conn.setAutoCommit(true);
    }

    //Con este getter voy a retornar la conexion
    public Connection getConnection() {
        return this.conn;
    }

    //Aplico el patron singleton
    public static ConnectionDB getInstance() throws SQLException, ClassNotFoundException {

        //Si la instancia no existe o esta cerrada la creo
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new ConnectionDB();
        }
        //Sino la retorno
        return instance;
    }

}
