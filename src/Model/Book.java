package Model;

import java.util.Date;
import Enum.Genre;

public class Book {

    private long ISBN;
    private String name;
    private Author author;
    private Genre genre;
    private String summary;
    private String numberOfPages;
    private Date publishDate;
    private String imageURL;

    public Book(long ISBN, String name, Author author, Genre genre, String summary, String numberOfPages, Date publishDate, String imageURL) {
        this.ISBN = ISBN;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.summary = summary;
        this.numberOfPages = numberOfPages;
        this.publishDate = publishDate;
        this.imageURL = imageURL;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(Author author) {
        this.author = author;
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

    public long getISBN() {
        return ISBN;
    }

    public String getName() {
        return name;
    }

    public Author getAuthor() {
        return author;
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
