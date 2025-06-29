package Service;

import DAO.BookDAO;
import Model.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookService {

    //Dependencia con el bookDAO
    private final BookDAO bookDAO;

    public BookService() {
        // En una aplicación más grande, se usaría inyección de dependencias.
        // Aquí, simplemente creamos la instancia directamente.
        this.bookDAO = new BookDAO();
    }

    // Constructor alternativo para pruebas (permite inyectar un "mock")
    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public CompletableFuture<List<Book>> searchBookByTitle(String title) {
        return bookDAO.searchByTitle(title);
    }




}
