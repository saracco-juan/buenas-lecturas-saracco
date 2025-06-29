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

    // Acceso a la conexion a traves del constructor del padre (BaseDAO)
    public UserDao(Connection conn) {
        super(conn);
    }

    //Los primeros tres metodos son muy similares entre ellos

    // Metodo para insertar un nuevo usuario en la BBDD. Recibe un usuario 'o'
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

    // Metodo para actualizar un usuario en la BBDD. Recibe un usuario 'o'
    @Override
    public Response<User> update(User o){

        String sql = "UPDATE user SET name = ?, email = ?, password = ? WHERE id = ?";

        try{

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, o.getName());
            ps.setString(2, o.getEmail());
            ps.setString(3, o.getPassword());

            //Actualizamos el statement y lo ejecuta. Se utiliza executeUpdate ya que es uno de estos -> insert/update/delete.
            int rows = ps.executeUpdate();

            if(rows > 0){
                return new Response<>( "Usuario actualizado correctamente", "200", true);
            }else{

                if(rows == 0){
                    return new Response<>("Error: No se encontro el usuario", "404", false);
                }

                return new Response<>("Error al actualizar", "500", false);
            }


        }catch (SQLException e){
            return new Response<>( "Error al actualizar: " + e.getMessage(), "500", false);
        }

    }

    // Metodo para eliminar un usuario de la BBDD. Recibe un id de usuario
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

    // Metodo para leer un usuario de la BBDD. Recibe un id de usuario
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



    //Me quede aca clase 27/05!!




    // Metodo para leer todos los usuarios de la BBDD
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
    //Metodo para buscar un usuario en la BBDD por email
    public Response<User> findByEmail(String email){
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
}

