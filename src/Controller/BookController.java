package Controller;

import Model.Book;
import Model.Response;
import Model.Review;
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
            }else if ("READ".equals(listType)) {
                // ¡ESTA ES LA LÓGICA QUE FALTABA!
                loggedInUser.getReadBooks().remove(book);
            }

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

    // Pega este nuevo método en tu clase BookController.java

    /**
     * Gestiona la petición desde la vista para guardar o actualizar una reseña de un libro.
     * @param book El libro que se está reseñando.
     * @param rating La calificación de 1 a 5.
     * @param comment El comentario de la reseña.
     */
    public void saveReview(Book book, int rating, String comment) {
        if (loggedInUser == null) {
            view.showErrorMessage("Error: No hay un usuario activo para guardar una reseña.");
            return;
        }

        // 1. Validaciones básicas de los datos de entrada
        if (rating < 1 || rating > 5) {
            view.showErrorMessage("La calificación debe estar entre 1 y 5 estrellas.");
            return;
        }
        if (comment == null || comment.trim().isEmpty()) {
            view.showErrorMessage("El comentario no puede estar vacío.");
            return;
        }

        // 2. Delegar la operación a la capa de servicio
        // Llamamos al método que creamos en BookService
        Response<Review> response = bookService.saveOrUpdateReview(loggedInUser, book, rating, comment);

        // 3. Procesar la respuesta del servicio
        if (response.getStatus()) {
            // --- ¡ÉXITO! ---
            // Nuestro BookService ya se encargó de actualizar el objeto 'book' en memoria
            // con la nueva reseña. ¡No tenemos que hacerlo aquí!
            // Simplemente notificamos a la vista que refresque el panel para mostrar los cambios.
            view.refreshProfileView(loggedInUser);

            // Opcional: Mostrar un mensaje de éxito. Podrías usar el del response.
            //view.showSuccessMessage(response.getMessage()); // Asumiendo que tienes un método showSuccessMessage en tu HomeView
        } else {
            // --- ERROR ---
            // Mostrar el mensaje de error que viene desde las capas inferiores.
            view.showErrorMessage("Error al guardar la reseña: " + response.getMessage());
        }
    }



}
