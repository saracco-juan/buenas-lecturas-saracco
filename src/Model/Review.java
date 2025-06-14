package Model;

public class Review {

    private int id;
    private String comment;
    // Este atributo tome la decision de usar el Double como objeto,
    // en vez de double primitivo para poder poner null como valor
    private Double value;
    private User user;
    private Book book;

    public Review(int id, String comment, Double value, User user, Book book) {
        this.id = id;
        this.comment = comment;
        this.value = value;
        this.user = user;
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
