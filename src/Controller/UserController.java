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
    //Guardo el usuario que esta logeado
    private User loggedInUser;


    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void setView(AuthView view) {
        //Este metodo para que el Frame le diga al controlador quien es su vista
        this.view = view;
    }

    public void handleLogin(String email, String password) {
        //Recibe datos para login por parametro (que van a ser enviados desde la vista)


        //Pequeña validacion para datos nulos (Logica de negocio)
        if(email == null || email.isEmpty() ) {

            System.out.println("Error: El email no puede ser nulo.");
            return;

        } else if ( password == null || password.isEmpty()) {

            System.out.println("Error: La password no puede ser nulo.");
            return;
        }

        //Instancio el objeto response para darle uso, le seteo la response del metodo login servicio
        Response<User> loginResponse = userService.login(email, password);

        //Si tuve exito (true), envio un mensaje y navego a la vista. Sino, muestro el error y viceversa
        if(loginResponse.getStatus()){

            //System.out.println("Login exitoso. Bienvenido " +  loginResponse.getObj().getName());
            view.navigateToHome(loginResponse.getObj());
        }else{
            //System.out.println("Error de login: " +  loginResponse.getMessage());
            view.showErrorMessage(loginResponse.getMessage());
        }

    }

    public void handleRegister(String name, String email, String password) {
        //Recibe datos para el registro por parametro (que van a ser enviados desde la vista)


        //Logica de negocio
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

        //Instancio el objeto response para darle uso, le seteo la response del metodo register del servicio
        Response<User> registerResponse = userService.register(name, email, password);

        //Si tuve exito (true), envio un mensaje y navego a la vista. Sino muestro un error
        if(registerResponse.getStatus()){
            //System.out.println("Register exitoso. Bienvenido " +  registerResponse.getObj().getName());
            view.showSuccessMessage(registerResponse.getMessage());
            //Le paso el usuario que se logeo a la vista
            view.navigateToLogin(registerResponse.getObj());
        }else{
            //System.out.println("Error de registro: " +  registerResponse.getMessage());
            view.showErrorMessage(registerResponse.getMessage());
        }

    }

    public void addBookToWhishlist(User user, Book book) {

        //Ejecuto el metodo para añadir un libro a la lista de quiero leer del servico
        Response<User> response = userService.addBookToWishlist(user, book);

        if (response.getStatus()) {

            if (this.loggedInUser != null) {
                //Si fue true, accedo al usuario logeado y le agrego el libro
                this.loggedInUser.getWantToRead().add(book);
            }

            if (view instanceof HomeView) {
                //Refresco la vista
                ((HomeView) view).refreshProfileView(this.loggedInUser);
            }

            (view).showSuccessMessage(response.getMessage());

        } else {
            (view).showErrorMessage(response.getMessage());
        }

    }


}
