package Controller;

import Model.Response;
import Model.User;
import Service.UserService;
import Service.UserServiceImpl;

//Esta clase se encarga de conectar la vista y el servicio
public class UserController {

    //Genero la dependencia con el servicio de user
    private final UserService userService;

    //Recibo la dependecia en el constructor y la seteo
    public UserController(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    //Recibe datos para login por parametro (que van a ser enviados desde la vista)
    public void hanldeLogin(String email, String password) {

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

        //Si tuve exito (true), envio un mensaje y viceversa
        if(loginResponse.getStatus()){

            System.out.println("Login exitoso. Bienvenido " +  loginResponse.getObj().getName());
        }else{

            System.out.println("Error de login: " +  loginResponse.getMessage());
        }

    }

    //Recibe datos para el registro por parametro (que van a ser enviados desde la vista)
    public void hanldeRegister(String name, String email, String password) {

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

        //Si tuve exito (true), envio un mensaje y viceversa
        if(registerResponse.getStatus()){
            System.out.println("Register exitoso. Bienvenido " +  registerResponse.getObj().getName());
        }else{
            System.out.println("Error de registro: " +  registerResponse.getMessage());
        }

    }


}
