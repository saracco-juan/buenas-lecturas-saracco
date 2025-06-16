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

    // Metodo para insertar un nuevo usuario en la BBDD. Recibe un usuario 'o'.
    @Override
    public Response<User> create(User o) {

        //Lo primero que hice fue crear la consulta SQL (sin los datos ("VALUES"))
        String sql = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";

        //Intento ejecutar la consulta (por eso va dentro de un try)
        try{
            //Creo un nuevo objeto PreparedStatement y le paso por parametro la query
            PreparedStatement ps = conn.prepareStatement(sql);

            //Ahora si seteo los valores deseados
            ps.setString(1, o.getName());
            ps.setString(2, o.getEmail());
            ps.setString(3, o.getPassword());

            //Con esta linea se ejecuta la consulta guardada en ps
            ps.executeUpdate();

            //Retorno el objeto response con un mensaje de exito, codigo de exito y true
            return new Response<>( "Usuario creado correctamente", "200", true);

        //En caso de fallar, atajo el error
        }catch (SQLException e){

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

            //Actualizamos el statement y lo ejecuta. Se utiliza executeUpdate ya que es uno de estos -> insert/update/delete.
            ps.executeUpdate();

            return new Response<>( "Usuario actualizado correctamente", "200", true);

        }catch (SQLException e){
            return new Response<>( "Error al actualizar: " + e.getMessage(), "500", false);
        }

    }

    @Override
    public Response<User> delete(int id){

        String sql = "DELETE FROM user WHERE id = ?";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return new Response<>( "Usuario eliminado correctamente", "200", true);

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

                ResultSet rs = ps.executeQuery();

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
        String sql = "SELECT * FROM user WHERE email = ?";

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

