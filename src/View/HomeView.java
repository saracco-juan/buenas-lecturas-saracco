package View;

import Model.Book;

import java.util.List;

public interface HomeView {
    void displaySearchResults(List<Book> books);
    void showErrorMessage(String message);
}
