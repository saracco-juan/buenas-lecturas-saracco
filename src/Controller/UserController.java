package Controller;

import Model.Book;
import Model.Response;
import Model.User;
import Service.UserService;
import View.AuthView;
import View.HomeView;

//Esta clase se encarga de conectar la vista y el servicio
public class UserController {

    //Genero la dependencia con el servicio de user
    private final UserService userService;
    //Genero la dependencai del autentificador de user
    private AuthView view;
    private User loggedInUser;

    // El Frame principal llamará a este método después de un login exitoso.
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    //Recibo la dependecia en el constructor y la seteo
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Este metodo para que el Frame le diga al controlador quien es su vista
    public void setView(AuthView view) {
        this.view = view;
    }

    //Recibe datos para login por parametro (que van a ser enviados desde la vista)
    public void handleLogin(String email, String password) {

        //Pequeña validacion para datos nulos
        if(email == null || email.isEmpty() ) {

            System.out.println("Error: El email no puede ser nulo.");
            return;

        } else if ( password == null || password.isEmpty()) {

            System.out.println("Error: La password no puede ser nulo.");
            return;
        }

        //Instancio el objeto response para darle uso, le seto la response del metodo login servicio
        Response<User> loginResponse = userService.login(email, password);

        //Si tuve exito (true), envio un mensaje y navego a la vista. Sino, muestro el error viceversa
        if(loginResponse.getStatus()){

            //System.out.println("Login exitoso. Bienvenido " +  loginResponse.getObj().getName());
            view.navigateToHome(loginResponse.getObj());
        }else{
            //System.out.println("Error de login: " +  loginResponse.getMessage());
            view.showErrorMessage(loginResponse.getMessage());
        }

    }

    //Recibe datos para el registro por parametro (que van a ser enviados desde la vista)
    public void handleRegister(String name, String email, String password) {

        if(email == null || email.isEmpty() ) {
            System.out.println("Error: El email no puede ser nulo.");
            return;
        }else if ( password == null || password.isEmpty()) {
            System.out.println("Error: La password no puede ser nulo.");
            return;
        }else if (name == null || name.isEmpty()) {
            System.out.println("Error: El nombre no puede ser nulo.");
            return;
        }

        //Instancio el objeto response para darle uso, le seto la response del metodo login servicio
        Response<User> registerResponse = userService.register(name, email, password);

        //Si tuve exito (true), envio un mensaje y navego a la vista. Sino muestro un error
        if(registerResponse.getStatus()){
            //System.out.println("Register exitoso. Bienvenido " +  registerResponse.getObj().getName());
            view.showSuccessMessage(registerResponse.getMessage());
            view.navigateToLogin(registerResponse.getObj());
        }else{
            //System.out.println("Error de registro: " +  registerResponse.getMessage());
            view.showErrorMessage(registerResponse.getMessage());
        }

    }

    public void addBookToWhishlist(User user, Book book) {

        System.out.println("2. UserController recibió la orden de añadir '" + book.getName() + "' a la lista.");

        Response<User> response = userService.addBookToWishlist(user, book);

        System.out.println("4. Controlador recibió respuesta del servicio. Estado: " + response.getStatus());

//        if(response.getStatus()){
//            ((AuthView) view).showSuccessMessage(response.getMessage());
//        }else{
//            ((AuthView) view).showErrorMessage(response.getMessage());
//        }

        if (response.getStatus()) {
            // --- INICIO DE LOS CAMBIOS ---

            // 1. ACTUALIZAR EL MODELO EN MEMORIA
            //    El objeto 'user' que tenemos aquí es el original, no el actualizado.
            //    La forma más segura es añadir el libro directamente al 'loggedInUser' del controlador.
            //    Si tu método de servicio devuelve el User actualizado en response.getData(), sería aún mejor.
            //    Por ahora, lo añadimos manualmente.
            if (this.loggedInUser != null) {
                this.loggedInUser.getWantToRead().add(book);
            }

            // 2. NOTIFICAR A LA VISTA QUE SE REFRESQUE
            //    Llamamos al mismo método que usamos para eliminar, para que el ProfilePanel se actualice.
            //    Necesitamos castear 'view' a 'HomeView' para acceder a este método.
            if (view instanceof HomeView) {
                ((HomeView) view).refreshProfileView(this.loggedInUser);
            }

            // --- FIN DE LOS CAMBIOS ---

            // Mostramos el mensaje de éxito al final.
            ((AuthView) view).showSuccessMessage(response.getMessage());

        } else {
            ((AuthView) view).showErrorMessage(response.getMessage());
        }

    }


}
