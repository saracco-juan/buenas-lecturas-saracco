package Service;

import DAO.BookDAO;
import DAO.UserDao;
import Model.Book;
import Model.Response;
import Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    //Dependencias
    private final UserDao userDao;
    private final BookService bookService;


    public UserServiceImpl(UserDao userDao,  BookService bookService) {
        this.userDao = userDao;
        this.bookService = bookService;
    }


    @Override
    public Response<User> login(String email, String password) {
    // Paso 1: Busco al usuario por email
    Response<User> daoResponse = userDao.findByEmail(email);

    // Si el DAO no encontró al usuario, el email es incorrecto.
    if (!daoResponse.getStatus()) {
        return new Response<>("Email incorrecto", "401", false);
    }

    User userFromDB = daoResponse.getObj();

    // Paso 2: Verifico la contraseña
    if (!userFromDB.getPassword().equals(password)) {
        return new Response<>("Contraseña incorrecta", "401", false);
    }

    // --- SI LA CONTRASEÑA ES CORRECTA, CARGO LAS LISTAS ---

    // Paso 3: Obtengo las claves de ambas listas desde la BBDD
    List<String> wantToReadKeys = userDao.getBookKeysForList(userFromDB.getId(), "WANT_TO_READ");
    List<String> readKeys = userDao.getBookKeysForList(userFromDB.getId(), "READ");

    // Paso 4: Hago la peticion a la api para llenar la lista

    // Creao una lista de promesas para la lista "Quiero Leer"
    List<CompletableFuture<Book>> wantToReadFutures = wantToReadKeys.stream()
            .map(key -> bookService.findBookByKey(key))
            .collect(Collectors.toList());

    // Creao una lista de promesas para la lista "Leídos"
    List<CompletableFuture<Book>> readFutures = readKeys.stream()
            .map(key -> bookService.findBookByKey(key))
            .collect(Collectors.toList());

    // Combino ambas listas (de arriba) en una sola lista para esperarlos a todos juntos
    List<CompletableFuture<Book>> allBookFutures = new ArrayList<>();
    allBookFutures.addAll(wantToReadFutures);
    allBookFutures.addAll(readFutures);

    // Paso 5: Esperar a que TODAS las peticiones terminen
    try {
        // Espera a que se completen todos los futuros
        CompletableFuture.allOf(allBookFutures.toArray(new CompletableFuture[0])).join();

        // Ahora que todos terminaron, obtengo los resultados
        ArrayList<Book> wantToReadList = new ArrayList<>();

        // Paso 6: Asignar los libros a las listas

        //Itero la lista y asingo los libros
        for (CompletableFuture<Book> future : wantToReadFutures) {
            Book book = future.get();
            if (book != null) wantToReadList.add(book);
        }

        //Posible mejora -> Usar el modelo user?

        //Seteo la lista al usuario
        userFromDB.setWantToRead(wantToReadList);

        //Mismo proceidimiento con la otra lista
        ArrayList<Book> readList = new ArrayList<>();
        for (CompletableFuture<Book> future : readFutures) {
            Book book = future.get();
            if (book != null) readList.add(book);
        }

        //Seteo la lista al usuario
        userFromDB.setReadBooks(readList);

        bookService.enrichBookListsWithReviews(userFromDB);


    } catch (Exception e) {
        System.err.println("ERROR: Error al cargar las listas de libros del usuario durante el login.");
        e.printStackTrace();
        //En caso de que falle la carga de libros devuelvo el usuario con las listas vacias para no romper
        userFromDB.setWantToRead(new ArrayList<>());
        userFromDB.setReadBooks(new ArrayList<>());
    }

    return new Response<>("Login exitoso", "200", true, userFromDB);
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

    @Override
    public Response<User> addBookToWishlist(User user, Book book) {

        //Logica de negocio
        if (user.getWantToRead().contains(book)) {
            return new Response<>("Este libro ya esta en tu lista de 'Quiero Leer'.", "409", false);
        }

        Response<?> daoResponse = userDao.addBookToWishlist(user.getId(), book.getWorkId());

        if (daoResponse.getStatus()) {
            return new Response<>("Libro añadido a tu lista 'Quiero Leer'.", "200", true);
        } else {
            return new Response<>(daoResponse.getMessage(), "500", false);
        }

    }


    //Metodos sin usar

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
