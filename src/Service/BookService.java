package Service;

import Config.ConnectionDB;
import DAO.AuthorDAO;
import DAO.BookDAO;
import DAO.UserDao;
import Model.Book;
import Model.Response;
import Model.User;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookService {

    //Dependencia con el bookDAO
    private final BookDAO bookDAO;
    private final UserDao userDao;



    // Constructor alternativo para pruebas (permite inyectar un "mock")
    public BookService(BookDAO bookDAO,  UserDao userDao) {
        this.bookDAO = bookDAO;
        this.userDao = userDao;
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

}
