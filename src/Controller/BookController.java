package Controller;

import Model.Book;
import Model.Response;
import Model.User;
import Service.BookService;
import View.HomePanel;
import View.HomeView;

import javax.swing.*;

public class BookController {

    //Dependencia con el book service
    private final BookService bookService;
    private User loggedInUser;


    //Dependencia con la vista
    private HomeView view;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public void setView(HomeView view) {
        this.view = view;
    }

    // El Frame principal llamará a este método después de un login exitoso.
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
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

    public void removeBookFromList(Book book, String listType) {
        // 1. Validar que tenemos un usuario en sesión
        if (loggedInUser == null) {
            view.showErrorMessage("Error: No hay un usuario activo para realizar esta acción.");
            return;
        }

        // 2. Delegar la operación a la capa de servicio
        Response<Void> response = bookService.removeBookFromUserList(loggedInUser, book, listType);

        // 3. Procesar la respuesta del servicio
        if (response.getStatus()) {
            // Éxito: Actualizar el modelo en memoria
            if ("WANT_TO_READ".equals(listType)) {
                // Asumo que tu clase User tiene un método como getWantToRead() que devuelve la lista
                loggedInUser.getWantToRead().remove(book);
            }
            // Aquí irían los 'else if' para otras listas como "Leídos".

            // Notificar a la vista que debe refrescar el panel de perfil
            view.refreshProfileView(loggedInUser);
        } else {
            // Error: Mostrar el mensaje que viene del servicio/repositorio
            view.showErrorMessage("Error al eliminar el libro: " + response.getMessage());
        }
    }

    // En la clase Controller/BookController.java, añade este método:

    /**
     * Gestiona la petición desde la vista para mover un libro a la lista de "Leídos".
     * @param book El libro que se va a mover.
     */
    public void moveBookToReadList(Book book) {
        if (loggedInUser == null) {
            view.showErrorMessage("Error: No hay un usuario activo para realizar esta acción.");
            return;
        }

        // 1. Delegar la operación a la capa de servicio
        Response<Void> response = bookService.moveBookToReadList(loggedInUser, book);

        // 2. Procesar la respuesta del servicio
        if (response.getStatus()) {
            // --- ¡LÓGICA CLAVE DE ACTUALIZACIÓN EN MEMORIA! ---
            // a) Quitamos el libro de la lista de origen.
            loggedInUser.getWantToRead().remove(book);

            // b) Añadimos el libro a la lista de destino.
            //    (Asegúrate de que tu User.java tenga una lista para 'Leídos' y su getter, ej. getReadBooks())
            loggedInUser.getReadBooks().add(book);

            // 3. Notificar a la vista que debe refrescar el panel de perfil
            view.refreshProfileView(loggedInUser);

            // Opcional: Mostrar un mensaje de éxito
//            view.showSuccessMessage(response.getMessage());
        } else {
            // Error: Mostrar el mensaje que viene del servicio/repositorio
            view.showErrorMessage("Error al mover el libro: " + response.getMessage());
        }
    }



}
