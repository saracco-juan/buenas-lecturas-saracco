package DAO;

import Model.Response;

// Esta interface va a permitir estandarizar los metodos de CRUD en todas las clases que la implementen
public interface ICrud<T> {

     Response<T> Create(T o);

     Response<T> Update(T o);

     Response<T> delete(int id);

     Response<T> Read(int id);

     Response<T> ReadAll();
}
