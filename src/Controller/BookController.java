package Controller;

import Model.Book;
import Model.Response;
import Model.Review;
import Model.User;
import Service.BookService;
import View.HomeView;
import javax.swing.*;

public class BookController {

    //Dependencia con el book service
    private final BookService bookService;
    //Aca voy a guardar el usuario que esta logeado
    private User loggedInUser;
    //Dependencia con la vista
    private HomeView view;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public void setView(HomeView view) {
        this.view = view;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void handleSearch(String query){
        //Este metodo busca un libro con el bookService y muestra ese resultado en la vista

        //Validacion
        if (query == null || query.trim().isEmpty()) {
            view.showErrorMessage("El campo de busqueda no puede estar vacio.");
            return;
        }

        //LLamo al bookService
        bookService.searchBookByTitle(query).thenAccept(books -> {

            //Lo muestro en la lista
            SwingUtilities.invokeLater(() -> {
                view.displaySearchResults(books);
            });
        });

    }

    public void removeBookFromList(Book book, String listType) {

        //Primero valido que hay un usuario en la sesion (logged in user)
        if (loggedInUser == null) {
            view.showErrorMessage("Error: No hay un usuario activo para realizar esta accion.");
            return;
        }

        //LLamo a la capa de servicio y guardo la rta en un objeto response
        Response<Void> response = bookService.removeBookFromUserList(loggedInUser, book, listType);

        if (response.getStatus()) {
            //Si la respuesta es exitosa
            if ("WANT_TO_READ".equals(listType)) {
                //Remuevo el libro de la lista wantToRead
                loggedInUser.getWantToRead().remove(book);
            }else if ("READ".equals(listType)) {
                //Remuevo el libro de la lista Read
                loggedInUser.getReadBooks().remove(book);
            }

            //Notifico a la visto que debe refrescar el perfil
            view.refreshProfileView(loggedInUser);
        } else {
            view.showErrorMessage("ERROR: Error al eliminar el libro: " + response.getMessage());
        }
    }

    public void moveBookToReadList(Book book) {

        if (loggedInUser == null) {
            view.showErrorMessage("ERROR: No hay un usuario activo para realizar esta accion.");
            return;
        }

        //LLamo a la capa de servicio y ejecuto el metodo para cambiar un libro de lista
        Response<Void> response = bookService.moveBookToReadList(loggedInUser, book);

        //Si tengo exito (true)
        if (response.getStatus()) {

            //Primero elimino el libro de la lista de quiero leer
            loggedInUser.getWantToRead().remove(book);

            //Luego, lo añado a la lista de leidos
            loggedInUser.getReadBooks().add(book);

            //Refresco la vista
            view.refreshProfileView(loggedInUser);

        } else {
            view.showErrorMessage("Error al mover el libro: " + response.getMessage());
        }
    }

    public void saveReview(Book book, int rating, String comment) {

        if (loggedInUser == null) {
            view.showErrorMessage("ERROR: No hay un usuario activo para guardar una reseña.");
            return;
        }

        //Logica de negocio
        if (rating < 1 || rating > 5) {
            view.showErrorMessage("La calificación debe estar entre 1 y 5 estrellas.");
            return;
        }
        if (comment == null || comment.trim().isEmpty()) {
            view.showErrorMessage("El comentario no puede estar vacío.");
            return;
        }

        //LLamo al servicio y ejecuto el metodo de guardar o actualizar
        Response<Review> response = bookService.saveOrUpdateReview(loggedInUser, book, rating, comment);

        //Si tengo exito, refresco la vista
        if (response.getStatus()) {
            view.refreshProfileView(loggedInUser);
        } else {
            view.showErrorMessage("ERROR: Error al guardar la reseña: " + response.getMessage());
        }
    }

}
