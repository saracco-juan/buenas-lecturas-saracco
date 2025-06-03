package DAO;

import Model.Response;

import java.sql.Connection;
import java.sql.SQLException;

// Clase abstracta que implementa el patron DAO (Data Access Object)
// Se encarga de definir operaciones basicas (CRUD) para interactuar con la base de datos
// Utiliza un tipo generico <T> que sera definido por las subclases concretas
public abstract class DAO<T> {

    protected  Connection conn;

    // Con este metodo recibe la conexion por paramentro y la setea en conn
    public DAO(Connection conn) {
        this.conn = conn;
    }

    // Metodos abstractos para operaciones CRUD que deben ser implementados por las subclases.
    // Devuelvo la clase response que la diseñe para devolver una respuesta especializada para cada metodo
    public abstract Response<T> create(T o) throws SQLException;

    public abstract Response<T> Update(T o) throws SQLException;

    public abstract Response<T> delete(int id) throws SQLException;

    public abstract Response<T> Read(int id) throws SQLException;

    public abstract Response<T>  ReadAll() throws SQLException;


}
