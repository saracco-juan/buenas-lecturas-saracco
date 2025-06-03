package Model;

import java.util.ArrayList;
import java.util.Date;

public class Author {

    private int id;
    private String name;
    private String lastName;
    private String biography;
    private Date dateOfBirth;
    private ArrayList<Book> writtenBooks;

    public Author(int id, String name, String lastName, String biography, Date dateOfBirth) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.biography = biography;
        this.dateOfBirth = dateOfBirth;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
