package DAO;

import Model.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// FUNDAMENTAL --> Esta clase DAO la van a implementar las clases que se involucren con la BBDD (user y review),
// ya que las otras clases no van a necesitarlo porque van a consumir la API.


// Clase abstracta que implementa el patron DAO (Data Access Object)
// Se encarga de definir operaciones basicas (CRUD) para interactuar con la base de datos
// Utiliza un tipo generico <T> que sera definido por las subclases concretas
public abstract class BaseDAO<T> implements ICrud<T>{

    protected  Connection conn;

    // Con este metodo recibe la conexion por paramentro y la setea en conn
    public BaseDAO(Connection conn) {
        this.conn = conn;
    }

    // Metodos abstractos para operaciones CRUD que deben ser implementados por las subclases.
    // Devuelvo la clase response que la diseñe para devolver una respuesta especializada para cada metodo
    @Override
    public abstract Response<T> create(T o);

    @Override
    public abstract Response<T> update(T o);

    @Override
    public abstract Response<T> delete(int id);

    @Override
    public abstract Response<T> read(int id);

    @Override
    public abstract Response<List<T>> readAll();
}
