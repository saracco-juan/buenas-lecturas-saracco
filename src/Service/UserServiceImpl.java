package Service;

import DAO.UserDao;
import Model.Book;
import Model.Response;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Response<User> login(String email, String password) {

        Response<User> DAOresponse = userDao.findByEmail(email);

        if(!DAOresponse.getStatus()){
            return new Response<>("Email incorrecto", "401", false);
        }

        User userFromDB = DAOresponse.getObj();

        //Obtengo la lista de "Quiero leer del usuario"
        List<String> wantToReadIsbns = userDao.getBookIsbnsForList(userFromDB.getId(), "WANT_TO_READ");

        // Asegurémonos de que las listas en el objeto User no sean null
        if (userFromDB.getWantToRead() == null) {
            userFromDB.setWantToRead(new ArrayList<>());
        }
        if (userFromDB.getReadBooks() == null) {
            userFromDB.setReadBooks(new ArrayList<>());
        }

        if(userFromDB.getPassword().equals(password)){
            return new Response<>("Usuario Logueado", "200", true, userFromDB);
        }else{
            return new Response<>("Contraseña incorrecta", "401", false);
        }

    }

    @Override
    public Response<User> register(String name, String email, String password) {

        User newUser = new User(name, email, password);

        Response<User> findEmailResponse = userDao.findByEmail(newUser.getEmail());

        //Chequeo si el usaurio ya existe en la BBDD
        if(findEmailResponse.getStatus()){
            return new Response<>("Email ya existente", "401", false);

        //Sino existe, ahi lo creo con el userDao.create
        }else{
                Response<User> createResponse = userDao.create(newUser);

                if(createResponse.getStatus()){
                    return new Response<>("Usuario registrado", "200", true, createResponse.getObj());
                }else{
                    return new Response<>("Error al crear el usuario", "401", false);
                }
        }
    }

    // Fin de metodos para LOGIN/Register

    @Override
    public Response<User> addBookToWishlist(User user, Book book) {

        System.out.println("3. UserServiceImpl va a procesar la petición.");


        // Lógica de negocio
        if (user.getWantToRead().contains(book)) {
            System.out.println("   - El libro ya existe en la lista. Devolviendo error.");
            return new Response<>("Este libro ya está en tu lista de 'Quiero Leer'.", "409", false);
        }

        System.out.println("   - Llamando al DAO para guardar en BBDD...");
        Response<?> daoResponse = userDao.addBookToWishlist(user.getId(), book.getWorkId());

        // Si se guardó en la BBDD, actualiza el objeto en memoria
        if (daoResponse.getStatus()) {
            user.getWantToRead().add(book);
            return new Response<>("Libro añadido a tu lista 'Quiero Leer'.", "200", true);
        } else {
            // Si falló el guardado en BBDD, devuelve el error del DAO
            return new Response<>(daoResponse.getMessage(), "500", false);
        }

    }

    @Override
    public Response<User> removeBookFromWishlist(User user, Book book) {
        return null;
    }

    @Override
    public Response<User> addBookToReadlist(User user, Book book) {
        return null;
    }

    @Override
    public Response<User> removeBookFromReadlist(User user, Book book) {
        return null;
    }

    @Override
    public Response<List<Book>> readWishlist(User user) {
        return null;
    }

    @Override
    public Response<List<Book>> readReadlist(User user) {
        return null;
    }

    @Override
    public Response<List<Book>> readSugestions(User user) {
        return null;
    }




}
