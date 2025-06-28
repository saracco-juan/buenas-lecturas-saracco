package View;

import Model.User;

public interface AuthView {

    //En caso de error muestro un mensaje generico de exito
    void showErrorMessage(String message);

    //En caso de exito muestro un mensaje generico de exito
    void showSuccessMessage(String message);

    //Orden para navegar a la pantalla principal después de un login/registro exitoso
    //Le paso el objeto User para que la pantalla principal sepa el usuario
    void navigateToHome(User user);
}
