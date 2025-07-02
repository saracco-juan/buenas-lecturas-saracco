package View;

import Model.User;

public interface AuthView {

    //En caso de error muestro un mensaje de error
    void showErrorMessage(String message);

    //En caso de exito muestro un mensaje de exito
    void showSuccessMessage(String message);

    //Metodo para navegar a la pantalla principal
    void navigateToHome(User user);

    //Metodo para navegar a la pantalla login
    void navigateToLogin(User user);
}
