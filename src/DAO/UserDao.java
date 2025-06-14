package DAO;

import Model.Response;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDAO<User> {

    // Acceso a la conexion a traves del constructor del padre (BaseDAO)
    public UserDao(Connection conn) {
        super(conn);
    }

    //Los primeros tres metodos son muy similares entre ellos

    // Metodo para insertar un nuevo usuario en la BBDD. Recibe un usuario 'o'.
    @Override
    public Response<User> Create(User o) {

        //Lo primero que hice fue crear la consulta SQL (sin los datos ("VALUES"))
        String sql = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";

        //Intento ejecutar la consulta (por eso va dentro de un try)
        try{
            //Creo un nuevo objeto PreparedStatement y le paso por parametro la query
            PreparedStatement ps = conn.prepareStatement(sql);

            //Ahora si seteo los valors deseados
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
    public Response<User> Update(User o){

        String sql = "UPDATE user SET name = ?, email = ?, password = ? WHERE id = ?";

        try{

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, o.getName());
            ps.setString(2, o.getEmail());
            ps.setString(3, o.getPassword());

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
    public Response<User> Read(int id){

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
    public Response<User> ReadAll() {

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

                return new Response<>("Lista de usuarios encontrada correctamente", "200", true, usersFound);

            }catch (SQLException e){
                return new Response<>("Error al obtener la lista de usuarios: " + e.getMessage(), "500", false);
            }

        }
    }

