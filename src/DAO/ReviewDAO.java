package DAO;

import Model.Response;
import Model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Clase que hereda de baseDAO
public class ReviewDAO extends BaseDAO<Review> {

    public ReviewDAO(Connection conn) {
        //Accedo a la conexion a traves del constructor del padre
        super(conn);
    }

    @Override
    public Response<Review> create(Review review) {
        //Armo al consulta
        String sql = "INSERT INTO REVIEWS (USER_ID, BOOK_ISBN, RATING, COMMENT, CREATED_AT) VALUES (?, ?, ?, ?, ?)";

        //En esta linea preparo la consulta con prepareStatement y le indico que voy a recuperar los ID
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getUserId());
            ps.setString(2, review.getBookWorkId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        review.setId(generatedKeys.getLong(1));
                    }
                }
                return new Response<>("Reseña creada correctamente.", "201", true, review);
            } else {
                return new Response<>("No se pudo crear la reseña.", "500", false, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("Error SQL al crear la reseña: " + e.getMessage(), "500", false, null);
        }
    }

    @Override
    public Response<Review> update(Review review) {
        //Este metodo funciona muy similar al create
        String sql = "UPDATE REVIEWS SET RATING = ?, COMMENT = ?, CREATED_AT = ? WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getRating());
            ps.setString(2, review.getComment());
            ps.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setLong(4, review.getId());

            int affectedRows = ps.executeUpdate();

            if(affectedRows > 0) {
                return new Response<>("Reseña actualizada correctamente.", "200", true, review);
            } else {
                return new Response<>("No se encontró la reseña para actualizar.", "404", false, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("Error SQL al actualizar la reseña: " + e.getMessage(), "500", false, null);
        }
    }

    @Override
    public Response<Review> delete(int id) {
        return null;
    }

    @Override
    public Response<Review> read(int id) {
        return null;
    }

    @Override
    public Response<List<Review>> readAll() {
        return null;
    }


    public Response<Review> saveOrUpdate(Review review) {
        //Verifico si ya existe una reseña para este usuario y libro
        String checkSql = "SELECT ID FROM REVIEWS WHERE USER_ID = ? AND BOOK_ISBN = ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setLong(1, review.getUserId());
            checkPs.setString(2, review.getBookWorkId());
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                //Si la reseña existe (rs.next = true), la actualizo
                long existingId = rs.getLong("ID");
                review.setId(existingId);
                //llamo al metodo update
                return update(review);
            } else {
                // La reseña no existe, entonces la creo. LLamo al metodo create
                return create(review);
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la reseña.");
            e.printStackTrace();
            return new Response<>("Error de BBDD al verificar la reseña: " + e.getMessage(), "500", false, null);
        }
    }


    public Response<List<Review>> findAllByUserId(long userId) {
        String sql = "SELECT * FROM REVIEWS WHERE USER_ID = ?";
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reviews.add(buildReviewFromResultSet(rs));
            }
            return new Response<List<Review>>("Reseñas del usuario recuperadas.", "200", true, reviews);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("ERROR: Error SQL al buscar reseñas por usuario: " + e.getMessage(), "500", false, null);
        }
    }


    private Review buildReviewFromResultSet(ResultSet rs) throws SQLException {

        //Este metodo lo utilizo para construir un objeto review a partir de una Result set

        Review review = new Review();
        review.setId(rs.getLong("ID"));
        review.setUserId(rs.getLong("USER_ID"));
        review.setBookWorkId(rs.getString("BOOK_ISBN"));
        review.setRating(rs.getInt("RATING"));
        review.setComment(rs.getString("COMMENT"));
        if(rs.getTimestamp("CREATED_AT") != null){
            review.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        }
        return review;
    }
}