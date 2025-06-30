package DAO;

import Model.Response;
import Model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO extends BaseDAO<Review> {

    public ReviewDAO(Connection conn) {
        super(conn);
    }

    /**
     * Guarda una reseña nueva (INSERT) o actualiza una existente (UPDATE).
     * Primero busca si ya existe una reseña para ese usuario y libro.
     * Es el método más recomendado para usar desde el servicio.
     * @param review El objeto Review a guardar o actualizar.
     * @return Una respuesta con el estado de la operación.
     */
    public Response<Review> saveOrUpdate(Review review) {
        // Primero, verificamos si ya existe una reseña para este usuario y libro
        String checkSql = "SELECT ID FROM REVIEWS WHERE USER_ID = ? AND BOOK_ISBN = ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setLong(1, review.getUserId());
            checkPs.setString(2, review.getBookWorkId());
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // La reseña ya existe, así que la actualizamos
                long existingId = rs.getLong("ID");
                review.setId(existingId);
                return update(review);
            } else {
                // La reseña no existe, así que la creamos
                return create(review);
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la reseña.");
            e.printStackTrace();
            return new Response<>("Error de BBDD al verificar la reseña: " + e.getMessage(), "500", false, null);
        }
    }

    @Override
    public Response<Review> create(Review review) {
        // Nota: usamos BOOK_ISBN porque así se llama la columna en tu tabla
        String sql = "INSERT INTO REVIEWS (USER_ID, BOOK_ISBN, RATING, COMMENT, CREATED_AT) VALUES (?, ?, ?, ?, ?)";
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
        // Implementación estándar de borrado, por si la necesitas
        String sql = "DELETE FROM REVIEWS WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                return new Response<>("Reseña eliminada correctamente.", "200", true, null);
            } else {
                return new Response<>("No se encontró la reseña a eliminar.", "404", false, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("Error SQL al eliminar la reseña: " + e.getMessage(), "500", false, null);
        }
    }

    @Override
    public Response<Review> read(int id) {
        // En este DAO es más útil buscar por usuario y libro
        // Pero implementamos el read por ID para cumplir con ICrud
        String sql = "SELECT * FROM REVIEWS WHERE ID = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Review review = buildReviewFromResultSet(rs);
                return new Response<>("Reseña encontrada", "200", true, review);
            } else {
                return new Response<>("Reseña no encontrada", "404", false, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("Error SQL al leer la reseña: " + e.getMessage(), "500", false, null);
        }
    }

    /**
     * Busca todas las reseñas de un usuario específico.
     * Muy eficiente para cargar todas las reseñas del usuario al iniciar sesión.
     * @param userId El ID del usuario.
     * @return Una respuesta con la lista de reseñas.
     */
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
            return new Response<>("Error SQL al buscar reseñas por usuario: " + e.getMessage(), "500", false, null);
        }
    }

    @Override
    public Response<List<Review>> readAll() {
        // Generalmente no es útil leer TODAS las reseñas de la BBDD,
        // pero se implementa para cumplir con la interfaz ICrud.
        String sql = "SELECT * FROM REVIEWS";
        List<Review> reviews = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                reviews.add(buildReviewFromResultSet(rs));
            }
            return new Response<List<Review>>("Todas las reseñas recuperadas.", "200", true, reviews);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response<>("Error SQL al leer todas las reseñas: " + e.getMessage(), "500", false, null);
        }
    }

    /**
     * Método helper para construir un objeto Review desde un ResultSet
     * para evitar la duplicación de código.
     */
    private Review buildReviewFromResultSet(ResultSet rs) throws SQLException {
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