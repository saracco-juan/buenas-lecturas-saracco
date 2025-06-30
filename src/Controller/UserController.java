package Controller;

import Model.Book;
import Model.Response;
import Model.User;
import Service.UserService;
import View.AuthView;

//Esta clase se encarga de conectar la vista y el servicio
public class UserController {

    //Genero la dependencia con el servicio de user
    private final UserService userService;
    //Genero la dependencai del autentificador de user
    private AuthView view;

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

        Response<User> response = userService.addBookToWishlist(user, book);

        if(response.getStatus()){
            ((AuthView) view).showSuccessMessage(response.getMessage());
        }else{
            ((AuthView) view).showErrorMessage(response.getMessage());
        }

    }


}
