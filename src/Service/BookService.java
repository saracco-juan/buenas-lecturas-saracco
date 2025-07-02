package Service;

import DAO.BookDAO;
import DAO.ReviewDAO;
import DAO.UserDao;
import Model.Book;
import Model.Response;
import Model.Review;
import Model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BookService {

    //Dependencias
    private final BookDAO bookDAO;
    private final UserDao userDao;
    private final ReviewDAO reviewDao;

    public BookService(BookDAO bookDAO,  UserDao userDao, ReviewDAO reviewDao) {
        this.bookDAO = bookDAO;
        this.userDao = userDao;
        this.reviewDao = reviewDao;
    }

    public CompletableFuture<List<Book>> searchBookByTitle(String title) {
        //Metodo para buscar libro por title
        return bookDAO.searchByTitle(title);
    }

    public CompletableFuture<Book> findBookByKey(String key) {
        //Metodo para buscar libro por key
        return bookDAO.findByKey(key);
    }

    public Response<Void> removeBookFromUserList(User user, Book book, String listType) {
        return userDao.removeBookFromList(user.getId(), book.getWorkId(), listType);
    }

    public Response<Void> moveBookToReadList(User user, Book book) {
        return userDao.moveBookBetweenLists(user.getId(), book.getWorkId(), "WANT_TO_READ", "READ");
    }

    public Response<Review> saveOrUpdateReview(User user, Book book, int rating, String comment) {
        //Crear un nuevo objeto Review
        Review review = new Review();
        review.setUserId(user.getId());
        review.setBookWorkId(book.getWorkId());
        review.setRating(rating);
        review.setComment(comment);

        //Llamo al metodo del DAO que se encarga de la logica INSERT o UPDATE.
        Response<Review> daoResponse = reviewDao.saveOrUpdate(review);

        if (daoResponse.getStatus()) {
            book.setReview(daoResponse.getObj());
        }

        return daoResponse;
    }

    public void enrichBookListsWithReviews(User user) {

        if (user == null || user.getReadBooks() == null || user.getReadBooks().isEmpty()) {
            System.out.println("No hay libros leidos");
            return;
        }

        //Obtengo las reseñas del usuario
        Response<List<Review>> reviewsResponse = reviewDao.findAllByUserId(user.getId());

        List<Review> userReviews = reviewsResponse.getObj();

        //Creo un Map para una busqueda rapida y le agrego la reseña
        Map<String, Review> reviewMap = new HashMap<>();
        for (Review review : userReviews) {
            reviewMap.put(review.getBookWorkId(), review);
        }


        for (Book book : user.getReadBooks()) {

            Review reviewForThisBook = reviewMap.get(book.getWorkId());

            if (reviewForThisBook != null) {
                book.setReview(reviewForThisBook);

            } else {
                System.out.println("No se encontro reseña para este libro en el Map");
            }
        }
    }

}
