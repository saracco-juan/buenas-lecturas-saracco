package Model;

import java.util.Date;
import Enum.Genre;

public class Book {

    private String ISBN;
    private String name;
    private String authorName;
    private Genre genre;
    private String summary;
    private String numberOfPages;
    private Date publishDate;
    private String imageURL;

    public Book(String ISBN, String name, String authorName){
        this.ISBN = ISBN;
        this.name = name;
        this.authorName = authorName;
    }

    public Book(String ISBN, String name, String authorName, Genre genre, String summary, String numberOfPages, Date publishDate, String imageURL) {
        this.ISBN = ISBN;
        this.name = name;
        this.authorName = authorName;
        this.genre = genre;
        this.summary = summary;
        this.numberOfPages = numberOfPages;
        this.publishDate = publishDate;
        this.imageURL = imageURL;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.authorName = authorName;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return authorName;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getSummary() {
        return summary;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getImageURL() {
        return imageURL;
    }
}
