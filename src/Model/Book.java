package Model;

import java.util.Date;
import Enum.Genre;

public class Book {

    //Atirbutos del objeto libro
    private String workId;
    private String name;
    private String authorName;

    private Review review;

    //Constructor
    public Book(String workId, String name, String authorName){
        this.workId = workId;
        this.name = name;
        this.authorName = authorName;
    }


    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
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

    public String getName() {
        return name;
    }


    //Metodo toString para visualizar el objeto
    @Override
    public String toString() {
        return this.getName() + " - " + this.getAuthorName(); // opcion para ver el ISBN -> + " - " + this.getWorkId();
    }

    //Este metodo define cuando objetos se consideran iguales ("this" vs "o" pasado por parametro)
    @Override
    public boolean equals(Object o) {
        //Si son iguales retorno true
        if (this == o) return true;
        //Si o es null o no es el del tipo Book (this.getClass), retorno false
        if (o == null || getClass() != o.getClass()) return false;
        //Por ultimo casteo el objeto "o" a tipo libro y comparo su workId (isbn)
        Book book = (Book) o;
        return java.util.Objects.equals(getWorkId(), book.getWorkId());
    }

    //Este metodo genera un hashCode basado en WorkId (ISBN)
    @Override
    public int hashCode() {
        return java.util.Objects.hash(getWorkId());
    }
}


