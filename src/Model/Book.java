package Model;

import java.util.Date;
import Enum.Genre;

public class Book {

    private String workId;
    private String name;
    private String authorName;
    private Genre genre;
    private String summary;
    private String numberOfPages;
    private Date publishDate;
    private String imageURL;

    public Book(String workId, String name, String authorName){
        this.workId = workId;
        this.name = name;
        this.authorName = authorName;
    }

    public Book(String workId, String name, String authorName, Genre genre, String summary, String numberOfPages, Date publishDate, String imageURL) {
        this.workId = workId;
        this.name = name;
        this.authorName = authorName;
        this.genre = genre;
        this.summary = summary;
        this.numberOfPages = numberOfPages;
        this.publishDate = publishDate;
        this.imageURL = imageURL;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setName(String name) {
        this.name = name;
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



    public String getName() {
        return name;
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

    @Override
    public String toString() {
        // Aquí puedes personalizar cómo se ve.
        // La forma "Título - Autor" es una excelente opción.
        return this.getName() + " - " + this.getAuthorName(); // opcion para ver el ISBN -> + " - " + this.getWorkId();
    }

    // También es CRUCIAL implementar equals() y hashCode() para que las listas funcionen bien.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return java.util.Objects.equals(getWorkId(), book.getWorkId()); // Comparamos por ISBN, que es único.
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getWorkId());
    }
}


