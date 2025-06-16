package DAO;

import Model.Response;

import java.util.List;

// Esta interface va a permitir estandarizar los metodos de CRUD en todas las clases que la implementen
public interface ICrud<T> {

     Response<T> create(T o);

     Response<T> update(T o);

     Response<T> delete(int id);

     Response<T> read(int id);

     Response<List<T>> readAll();
}
