package Model;

import java.time.LocalDateTime;

public class Review {

    //Atributos
    private long id;
    private long userId;
    private String bookWorkId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    //Getters y Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBookWorkId() {
        return bookWorkId;
    }

    public void setBookWorkId(String bookWorkId) {
        this.bookWorkId = bookWorkId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
