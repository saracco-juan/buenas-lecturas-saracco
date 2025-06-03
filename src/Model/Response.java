package Model;

import java.util.List;

public class Response<T> {

    private String message;
    private String code;
    private String status;
    private List<T> list;
    private T obj;

    public Response(String message, String code, String status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public Response(String message, String code, String status, List<T> list) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.list = list;
    }

    public Response(String message, String code, String status,  T obj) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.obj = obj;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
