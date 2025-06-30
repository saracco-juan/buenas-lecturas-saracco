package Controller;

import Service.BookService;
import View.HomePanel;
import View.HomeView;

import javax.swing.*;

public class BookController {

    //Dependencia con el book service
    private final BookService bookService;

    //Dependencia con la vista
    private HomeView view;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public void setView(HomeView view) {
        this.view = view;
    }

    public void handleSearch(String query){

        System.out.println("2. BookController recibió la orden de búsqueda.");


        //Validacion
        if (query == null || query.trim().isEmpty()) {
            view.showErrorMessage("El campo de búsqueda no puede estar vacío.");
            return;
        }

        // Llama al servicio. La operación es asíncrona.
        bookService.searchBookByTitle(query).thenAccept(books -> {
            System.out.println("5. Respuesta de la API recibida y procesada. Libros encontrados: " + books.size());
            SwingUtilities.invokeLater(() -> {
                // Llama al método que acabas de crear en HomePanel
                System.out.println("6. Actualizando la vista (JList).");
                view.displaySearchResults(books);
            });
        });
        System.out.println("3. Petición al servicio enviada (asíncrona). El código continúa...");


    }

}
