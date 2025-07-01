package Model;

import java.util.List;

//La clase response me sirve para unificar mis respuestas. Recibe un objeto generico "<T>"
public class Response<T> {

    //Atributos
    private String message;
    private String code;
    private boolean status;
    private List<T> list;
    private T obj;

    //Constructor por defecto
    public Response(String message, String code, boolean status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    //Constructor por defecto que ademas contiene una lista
    public Response(String message, String code, boolean status, List<T> list) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.list = list;
    }
    //Constructor por defecto que ademas contiene un objeto
    public Response(String message, String code, boolean status,  T obj) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.obj = obj;
    }

    //Getters y setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
