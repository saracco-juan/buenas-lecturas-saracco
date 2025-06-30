package View;

import Model.Book;
import Model.User;

import java.util.List;

public interface HomeView {
    void displaySearchResults(List<Book> books);
    void showErrorMessage(String message);
    void refreshProfileView(User updatedUser);
}
