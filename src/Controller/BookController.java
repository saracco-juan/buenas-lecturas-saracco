package Controller;

import Service.BookService;
import View.HomePanel;

import javax.swing.*;

public class BookController {

    //Dependencia con el book service
    private final BookService bookService;

    //Dependencia con la vista
    private HomePanel view;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public void setView(HomePanel view) {
        this.view = view;
    }

    public void handleSearch(String query){

        //Validacion
        if (query == null || query.trim().isEmpty()) {
            view.showErrorMessage("El campo de búsqueda no puede estar vacío.");
            return;
        }

        // Llama al servicio. La operación es asíncrona.
        bookService.searchBookByTitle(query).thenAccept(books -> {
            SwingUtilities.invokeLater(() -> {
                // Llama al método que acabas de crear en HomePanel
                view.updateResultsList(books);
            });
        });


    }

}
