package Service;

import Config.ConnectionDB;
import DAO.AuthorDAO;
import DAO.BookDAO;
import DAO.ReviewDAO;
import DAO.UserDao;
import Model.Book;
import Model.Response;
import Model.Review;
import Model.User;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookService {

    //Dependencias
    private final BookDAO bookDAO;
    private final UserDao userDao;
    private final ReviewDAO reviewDao;



    // Constructor alternativo para pruebas (permite inyectar un "mock")
    public BookService(BookDAO bookDAO,  UserDao userDao, ReviewDAO reviewDao) {
        this.bookDAO = bookDAO;
        this.userDao = userDao;
        this.reviewDao = reviewDao;
    }

    public CompletableFuture<List<Book>> searchBookByTitle(String title) {
        return bookDAO.searchByTitle(title);
    }

    //Metodo para buscar libro por key
    public CompletableFuture<Book> findBookByKey(String key) {
        return bookDAO.findByKey(key);
    }

    public Response<Void> removeBookFromUserList(User user, Book book, String listType) {
        return userDao.removeBookFromList(user.getId(), book.getWorkId(), listType);
    }

    public Response<Void> moveBookToReadList(User user, Book book) {
        // La lógica de negocio está encapsulada en la llamada al DAO.
        // Aquí especificamos de qué lista a qué lista se mueve.
        return userDao.moveBookBetweenLists(user.getId(), book.getWorkId(), "WANT_TO_READ", "READ");
    }

    /**
     * Guarda o actualiza una reseña para un libro y usuario específicos.
     * Este método será llamado desde el controlador cuando el usuario envíe el formulario de reseña.
     *
     * @param user El usuario que escribe la reseña.
     * @param book El libro que está siendo reseñado.
     * @param rating La calificación de 1 a 5.
     * @param comment El texto del comentario.
     * @return Una respuesta indicando si la operación fue exitosa y conteniendo la reseña guardada en el campo 'obj'.
     */
    public Response<Review> saveOrUpdateReview(User user, Book book, int rating, String comment) {
        // 1. Crear un nuevo objeto Review con la información proporcionada.
        Review review = new Review();
        review.setUserId(user.getId());
        review.setBookWorkId(book.getWorkId());
        review.setRating(rating);
        review.setComment(comment);

        // 2. Llamar al método del DAO que se encarga de la lógica INSERT/UPDATE.
        Response<Review> daoResponse = reviewDao.saveOrUpdate(review);

        // 3. Si la operación en la BBDD fue exitosa (usando tu método getStatus()),
        //    actualizamos el objeto Book en memoria con el dato de la respuesta (usando tu método getObj()).
        if (daoResponse.getStatus()) {
            book.setReview(daoResponse.getObj());
        }

        // 4. Devolvemos la respuesta del DAO al controlador.
        return daoResponse;
    }


    /**
     * Carga las reseñas de un usuario y las asocia a los libros correspondientes en su lista de "Leídos".
     * Este método es la forma más eficiente de cargar todas las reseñas.
     * Debe ser llamado después de que el usuario haya cargado sus listas de libros.
     *
     * @param user El objeto User, que ya debe tener su lista `readBooks` poblada.
     */
    public void enrichBookListsWithReviews(User user) {
        System.out.println("---[DEBUG] Iniciando enrichBookListsWithReviews para usuario: " + user.getId());

        if (user == null || user.getReadBooks() == null || user.getReadBooks().isEmpty()) {
            System.out.println("---[DEBUG] No hay libros leídos para enriquecer. Saliendo.");
            return;
        }

        // 1. Obtener TODAS las reseñas del usuario.
        Response<List<Review>> reviewsResponse = reviewDao.findAllByUserId(user.getId());

        // **AÑADIMOS DEPURACIÓN AQUÍ**
        if (!reviewsResponse.getStatus()) {
            System.out.println("---[DEBUG] Error del DAO al buscar reseñas: " + reviewsResponse.getMessage());
            return;
        }
        if (reviewsResponse.getObj() == null || reviewsResponse.getObj().isEmpty()) {
            System.out.println("---[DEBUG] El usuario no tiene ninguna reseña en la BBDD.");
            return;
        }

        List<Review> userReviews = reviewsResponse.getObj();
        System.out.println("---[DEBUG] Reseñas encontradas en BBDD: " + userReviews.size());
        for(Review r : userReviews) {
            System.out.println("    - Reseña para libro: " + r.getBookWorkId() + ", Rating: " + r.getRating());
        }

        // 2. Crear el Mapa para búsqueda rápida.
        java.util.Map<String, Review> reviewMap = new java.util.HashMap<>();
        for (Review review : userReviews) {
            reviewMap.put(review.getBookWorkId(), review);
        }
        System.out.println("---[DEBUG] Mapa de reseñas creado con " + reviewMap.size() + " entradas.");

        // 3. Iterar y asignar.
        int reviewsAssigned = 0;
        System.out.println("---[DEBUG] Recorriendo la lista de libros leídos del usuario (" + user.getReadBooks().size() + " libros).");
        for (Book book : user.getReadBooks()) {
            System.out.println("    - Comprobando libro: " + book.getWorkId() + " (" + book.getName() + ")");
            Review reviewForThisBook = reviewMap.get(book.getWorkId());
            if (reviewForThisBook != null) {
                book.setReview(reviewForThisBook);
                reviewsAssigned++;
                System.out.println("        -> ¡ÉXITO! Reseña asignada al libro.");
            } else {
                System.out.println("        -> No se encontró reseña para este libro en el mapa.");
            }
        }

        System.out.println("---[DEBUG] Proceso finalizado. Total de reseñas asignadas: " + reviewsAssigned);
    }

}
