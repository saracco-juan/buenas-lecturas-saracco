package Model;


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


    @Override
    public String toString() {
        //Metodo toString para visualizar el objeto

        return this.getName() + " - " + this.getAuthorName(); // opcion para ver el ISBN -> + " - " + this.getWorkId();
    }

    @Override
    public boolean equals(Object o) {
        //Este metodo define cuando objetos se consideran iguales ("this" vs "o" pasado por parametro)

        //Si son iguales retorno true
        if (this == o) return true;
        //Si o es null o no es el del tipo Book (this.getClass), retorno false
        if (o == null || getClass() != o.getClass()) return false;
        //Por ultimo casteo el objeto "o" a tipo libro y comparo su workId (isbn)
        Book book = (Book) o;
        return java.util.Objects.equals(getWorkId(), book.getWorkId());
    }

    @Override
    public int hashCode() {
        //Este metodo genera un hashCode basado en WorkId (ISBN)
        return java.util.Objects.hash(getWorkId());
    }
}


