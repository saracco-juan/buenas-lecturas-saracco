package Service;

import Model.Book;
import Model.Response;
import Model.User;

import java.util.List;

//Esta interfaz la va a utilizar mi UserServiceImpl (implementacion)
public interface UserService {

     Response<User> login(String username, String password);

     Response<User> register(String name, String email, String password);

     Response<User> addBookToWishlist(User user, Book book);

     Response<User> removeBookFromWishlist(User user, Book book);

     Response<User> addBookToReadlist(User user, Book book);

     Response<User> removeBookFromReadlist(User user, Book book);

     Response<List<Book>> readWishlist(User user);

     Response<List<Book>> readReadlist(User user);

     Response<List<Book>> readSugestions(User user);
}
