package DAO;

import Model.Response;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Clase que hereda de baseDAO
public class UserDao extends BaseDAO<User> {

    public UserDao(Connection conn) {
        //Accedo a la conexion a traves del constructor del padre
        super(conn);
    }

    @Override
    public Response<User> create(User o) {

        //Lo primero que hice fue crear la consulta SQL (sin los datos ("VALUES"))
        String sql = "INSERT INTO app_user (name, email, password) VALUES (?, ?, ?)";

        //Intento ejecutar la consulta (por eso va dentro de un try)
        try{
            //Creo un nuevo objeto PreparedStatement y le paso por parametro la query
            PreparedStatement ps = conn.prepareStatement(sql);

            //Ahora si seteo los valores deseados
            ps.setString(1, o.getName());
            ps.setString(2, o.getEmail());
            ps.setString(3, o.getPassword());

            //Con esta linea se ejecuta la consulta guardada en ps, lo guardo en una variable
            int filasAfectadas = ps.executeUpdate();

            //leo si hubo filas afectadas
            if(filasAfectadas>0){

                //Creo un result set con los nuevos ids generados por la BBDD
                ResultSet idGenerado = ps.getGeneratedKeys();

                //Leo la siguiente linea
                if(idGenerado.next()){
                    //En caso de haber un nuevo id se lo seteo al objeto user que pase por param
                    int nuevoId = idGenerado.getInt(1);
                    o.setId(nuevoId);
                }
            }

            //Retorno el objeto response con un mensaje de exito, codigo de exito , true y el objeto que cree
            return new Response<>( "Usuario creado correctamente", "200", true, o);

        //En caso de fallar, atajo el error
        }catch (SQLException e){

            System.out.println("Error al insertar el usuario -- JUAN");
            e.printStackTrace();
            //Retorno el objeto response con un mensaje de error, codigo de error y false
            return new Response<>( "Error al insertar: " + e.getMessage(), "500", false);

        }

    }

    @Override
    public Response<User> update(User o){

        String sql = "UPDATE user SET name = ?, email = ?, password = ? WHERE id = ?";

        try{

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, o.getName());
            ps.setString(2, o.getEmail());
            ps.setString(3, o.getPassword());

            //Actualizo el statement y lo ejecuto. Se utiliza executeUpdate ya que es uno de estos -> insert/update/delete.
            int rows = ps.executeUpdate();

            if(rows > 0){
                return new Response<>( "Usuario actualizado correctamente", "200", true);
            }else{

                if(rows == 0){
                    return new Response<>("ERROR: No se encontro el usuario", "404", false);
                }

                return new Response<>("ERROR: Error al actualizar", "500", false);
            }


        }catch (SQLException e){
            return new Response<>( "ERROR: Error al actualizar: " + e.getMessage(), "500", false);
        }

    }

    @Override
    public Response<User> delete(int id){

        String sql = "DELETE FROM user WHERE id = ?";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if(rows == 1){
                return new Response<>( "Usuario eliminado correctamente", "200", true);
            }else{
                return new Response<>("Error al eliminar", "500", false);
            }


        }catch (SQLException e){
            return new Response<>( "Error al eliminar: " + e.getMessage(), "500", false);
        }
    }

    @Override
    public Response<User> read(int id){

        String sql = "SELECT * FROM user WHERE id = ?";
        User userFound = null;

        try{

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                int userId = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");

                userFound = new User(userId, name, email, password);
            }

            if(userFound == null){
                return new Response<>("Usuario no encontrado", "200", false);
            }else {
                return new Response<>("Usuario encontrado correctamente", "200", true, userFound);
            }
        }catch (SQLException e){
            return new Response<>( "Error al consultar: " + e.getMessage(), "500", false);
        }

    }

    @Override
    public Response<List<User>> readAll() {

            String sql = "SELECT * FROM user";
            User userFound = null;
            List<User> usersFound = new ArrayList<>();

            try{

                PreparedStatement ps = conn.prepareStatement(sql);

                //Armo el result set que devuelve el execute query (porque es un SELECT)
                ResultSet rs = ps.executeQuery();

                //Mientas haya un resultado en la fila, sigo ejecutando el while
                while(rs.next()){
                    int userId = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    userFound = new User(userId, name, email, password);

                    usersFound.add(userFound);
                }

                return new Response<List<User>>("Lista de usuarios encontrada correctamente", "200", true, usersFound);

            }catch (SQLException e){
                return new Response<>("Error al obtener la lista de usuarios: " + e.getMessage(), "500", false);
            }

        }

    public Response<User> findByEmail(String email){
        //Metodo para buscar un usuario en la BBDD por email
        String sql = "SELECT * FROM app_user WHERE email = ?";

        User userFound = null;

        try{

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                userFound = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

            if(userFound == null){
                return new Response<>("Usuario no encontrado", "200", false);
            }else{
                return new Response<>("Usuario encontrado correctamente", "200", true, userFound);
            }

        }catch (SQLException e){
            return new Response<>("Error al obtener el usuario: " + e.getMessage(), "500", false);
        }
    }

    public Response<?> addBookToWishlist(int userId, String workId) {
        //Armo la consulta (List Type es un identificador de lista)
        String sql = "INSERT INTO user_books (user_id, book_isbn, list_type) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, workId);
            ps.setString(3, "WANT_TO_READ"); // -> identificador de lista

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                return new Response<>("Relación creada en BBDD", "201", true);
            } else {
                return new Response<>("No se pudo crear la relación en BBDD", "500", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("Error de SQL: " + e.getMessage(), "500", false);
        }
    }

    public List<String> getBookKeysForList(int userId, String listType) {
        //Metodo para seleccionar la key (ISBN) de una lista
        String sql = "SELECT book_isbn FROM user_books WHERE user_id = ? AND list_type = ?";
        List<String> keys = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, listType);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                keys.add(rs.getString("book_isbn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public Response<Void> removeBookFromList(int userId, String bookIsbn, String listType) {
        //Metodo para remover un libro de las listas
        String sql = "DELETE FROM user_books WHERE user_id = ? AND book_isbn = ? AND list_type = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, bookIsbn);
            ps.setString(3, listType);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return new Response<>("Libro eliminado de la lista correctamente.", "200", true, null);
            } else {
                return new Response<>("ERROR: El libro no se encontró en la lista especificada.", "404", false);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: Error al eliminar el libro de la lista del usuario.");
            e.printStackTrace();
            return new Response<>("ERROR: Error de base de datos al eliminar el libro: " + e.getMessage(), "500", false);
        }
    }

    public Response<Void> moveBookBetweenLists(int userId, String bookIsbn, String fromListType, String toListType) {
        //Este metodo sirve para mover un libro entre listas
        String sql = "UPDATE user_books SET list_type = ? WHERE user_id = ? AND book_isbn = ? AND list_type = ?";

        //Debug
        System.out.println("---[DAO DEBUG]---");
        System.out.println("Intentando ejecutar UPDATE:");
        System.out.println("A que Lista quiero mover el libro: " + toListType);
        System.out.println("Id del usuario: " + userId);
        System.out.println("WorkID del libro: " + bookIsbn);
        System.out.println("Desde que Lista quiero mover el libro: " + fromListType);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toListType);
            ps.setInt(2, userId);
            ps.setString(3, bookIsbn);
            ps.setString(4, fromListType);

            int affectedRows = ps.executeUpdate();

            //Debug
            System.out.println("---[DAO DEBUG]---");
            System.out.println("Filas afectadas por el UPDATE (moverEntreListas): " + affectedRows);
            System.out.println("-----------------");

            if (affectedRows > 0) {
                return new Response<>("Libro movido de lista correctamente.", "200", true);
            } else {
                return new Response<>("ERROR: El libro no se encontró en la lista de origen especificada.", "404", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("ERROR: Error de base de datos al mover el libro: " + e.getMessage(), "500", false);
        }
    }
}

