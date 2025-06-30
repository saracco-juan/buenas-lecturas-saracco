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

    private final UserDao userDao;
    private final BookService bookService;


    public UserServiceImpl(UserDao userDao,  BookService bookService)
    {
        this.userDao = userDao;
        this.bookService = bookService;
    }

//    @Override
//    public Response<User> login(String email, String password) {
//
//        Response<User> DAOresponse = userDao.findByEmail(email);
//
//        if(!DAOresponse.getStatus()){
//            return new Response<>("Email incorrecto", "401", false);
//        }
//
//        User userFromDB = DAOresponse.getObj();
//
////        //Obtengo la lista de "Quiero leer del usuario"
////        List<String> wantToReadIsbns = userDao.getBookIsbnsForList(userFromDB.getId(), "WANT_TO_READ");
////
////        // Asegurémonos de que las listas en el objeto User no sean null
////        if (userFromDB.getWantToRead() == null) {
////            userFromDB.setWantToRead(new ArrayList<>());
////        }
////        if (userFromDB.getReadBooks() == null) {
////            userFromDB.setReadBooks(new ArrayList<>());
////        }
//
//        if(userFromDB.getPassword().equals(password)){
//
//            // 1. Obtener las claves de los libros de la lista "Quiero Leer"
//            List<String> wantToReadKeys = userDao.getBookKeysForList(userFromDB.getId(), "WANT_TO_READ");
//
//            //La diferencia clave es que usamos el BookService, no el BookRepository.
//            List<CompletableFuture<Book>> bookFutures = wantToReadKeys.stream()
//                    .map(key -> bookService.findBookByKey(key)) // Asumiendo que añades findBookByKey al BookService
//                    .collect(Collectors.toList());
//
//            // 2. Obtener las claves de los libros de la lista "Leídos"
//            List<String> readBooksKeys = userDao.getBookKeysForList(userFromDB.getId(), "READ_BOOKS");
//
//            // 3. Esperar a que TODAS las peticiones terminen
//            // CompletableFuture.allOf(...) crea un futuro que se completa cuando todos los demás lo hacen.
//            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
//                    bookFutures.toArray(new CompletableFuture[0])
//            );
//
//            // 4. Procesar los resultados CUANDO todo esté listo
//            CompletableFuture<List<Book>> allBooksFuture = allFutures.thenApply(v ->
//                    bookFutures.stream()
//                            .map(CompletableFuture::join) // .join() obtiene el resultado de cada futuro individual
//                            .filter(java.util.Objects::nonNull) // Filtra los que pudieron fallar (null)
//                            .collect(Collectors.toList())
//            );
//
//            // 5. BLOQUEAR y obtener el resultado final (solo porque estamos en el login)
//            // En una app real más compleja, podríamos devolver el futuro y cargar la UI de forma asíncrona.
//            try {
//                List<Book> wantToReadList = allBooksFuture.get(); // .get() espera y obtiene el resultado
//                usuarioFromDB.setWantToRead(new ArrayList<>(wantToReadList));
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//                // Si falla la carga, al menos inicializamos la lista para evitar NullPointerExceptions
//                usuarioFromDB.setWantToRead(new ArrayList<>());
//            }
//
//            for (String key : wantToReadKeys) {
//                // Creamos un libro "dummy" solo con su ID para que la lista no esté vacía.
//                // Necesitarás un constructor adecuado en tu clase Book.
//                wantToReadList.add(new Book(key, "Cargando...", ""));
//            }
//            userFromDB.setWantToRead(wantToReadList);
//
//            ArrayList<Book> readBooksList = new ArrayList<>();
//            for (String key : readBooksKeys) {
//                readBooksList.add(new Book(key, "Cargando...", ""));
//            }
//            userFromDB.setReadBooks(readBooksList);
//
//            return new Response<>("Usuario Logueado", "200", true, userFromDB);
//        }else{
//            return new Response<>("Contraseña incorrecta", "401", false);
//        }
//
//    }
    @Override
    public Response<User> login(String email, String password) {
    // Paso 1: Buscar al usuario por email
    Response<User> daoResponse = userDao.findByEmail(email);

    // Si el DAO no encontró al usuario, el email es incorrecto.
    if (!daoResponse.getStatus()) {
        return new Response<>("Email o contraseña incorrectos", "401", false);
    }

    User userFromDB = daoResponse.getObj();

    // Paso 2: Verificar la contraseña
    if (!userFromDB.getPassword().equals(password)) {
        return new Response<>("Email o contraseña incorrectos", "401", false);
    }

    // --- SI LA CONTRASEÑA ES CORRECTA, CARGAMOS LAS LISTAS ---

    // Paso 3: Obtener las claves de ambas listas desde la BBDD
    List<String> wantToReadKeys = userDao.getBookKeysForList(userFromDB.getId(), "WANT_TO_READ");
    List<String> readKeys = userDao.getBookKeysForList(userFromDB.getId(), "READ");

    // Paso 4: Lanzar todas las peticiones a la API en paralelo

    // Creamos una lista de futuros para la lista "Quiero Leer"
    List<CompletableFuture<Book>> wantToReadFutures = wantToReadKeys.stream()
            .map(key -> bookService.findBookByKey(key))
            .collect(Collectors.toList());

    // Creamos una lista de futuros para la lista "Leídos"
    List<CompletableFuture<Book>> readFutures = readKeys.stream()
            .map(key -> bookService.findBookByKey(key))
            .collect(Collectors.toList());

    // Combinamos todos los futuros en una sola lista para esperarlos a todos juntos
    List<CompletableFuture<Book>> allBookFutures = new ArrayList<>();
    allBookFutures.addAll(wantToReadFutures);
    allBookFutures.addAll(readFutures);

    // Paso 5: Esperar a que TODAS las peticiones terminen
    try {
        // Espera a que se completen todos los futuros
        CompletableFuture.allOf(allBookFutures.toArray(new CompletableFuture[0])).join();

        // Ahora que todos terminaron, obtenemos los resultados
        ArrayList<Book> wantToReadList = new ArrayList<>();
        for (CompletableFuture<Book> future : wantToReadFutures) {
            Book book = future.get(); // .get() ahora es instantáneo porque ya esperamos
            if (book != null) wantToReadList.add(book);
        }
        userFromDB.setWantToRead(wantToReadList);

        ArrayList<Book> readList = new ArrayList<>();
        for (CompletableFuture<Book> future : readFutures) {
            Book book = future.get();
            if (book != null) readList.add(book);
        }
        userFromDB.setReadBooks(readList);



        bookService.enrichBookListsWithReviews(userFromDB);


    } catch (Exception e) {
        System.err.println("Error al cargar las listas de libros del usuario durante el login.");
        e.printStackTrace();
        // Aunque falle la carga de libros, el login fue exitoso.
        // Devolvemos el usuario con las listas vacías para no romper la app.
        userFromDB.setWantToRead(new ArrayList<>());
        userFromDB.setReadBooks(new ArrayList<>());
    }

    // Paso 6: Devolver la respuesta de éxito con el objeto User completo y enriquecido
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
