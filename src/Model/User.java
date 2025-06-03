package Model;
import Enum.Role;

import java.util.ArrayList;


public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private ArrayList<Book> readBooks;
    private ArrayList<Book> wantToRead;
    private ArrayList<Author> favouriteAuthors;


    public User(int id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
