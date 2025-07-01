package Model;

import java.util.ArrayList;


public class User {

    //Atributos
    private int id;
    private String name;
    private String email;
    private String password;

    //Listas
    private ArrayList<Book> readBooks;
    private ArrayList<Book> wantToRead;
    private ArrayList<Book> suggestions;

    //Constructor
    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Constructor sobrecarga (Asi construyo mi objeto en el metodo register del servicio)
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

        readBooks = new ArrayList<>();
        wantToRead = new ArrayList<>();
        suggestions = new ArrayList<>();

    }

    //Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Book> getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(ArrayList<Book> readBooks) {
        this.readBooks = readBooks;
    }

    public ArrayList<Book> getWantToRead() {
        return wantToRead;
    }

    public void setWantToRead(ArrayList<Book> wantToRead) {
        this.wantToRead = wantToRead;
    }

    public ArrayList<Book> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<Book> suggestions) {
        this.suggestions = suggestions;
    }
}
