package View;

import Controller.BookController;
import Controller.UserController;
import Model.Book;
import Model.User;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.JFrame;

//Mi clase Frame utiliza la interfaz AuthView que tiene 
//3 metodos que ayudan al flujo de las vistas
public class Frame extends javax.swing.JFrame implements AuthView, HomeView{

    //El card layout es el que se encarga de manejar las vistas
    private final CardLayout cardLayout;
    
    //Usuario logeado
    private User loggedInUser;
    
    //paneles
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private HomePanel homePanel;
    private ProfilePanel profilePanel;
    private ReviewsPanel reviewsPanel;
    
    private BookController bookController;
    private final UserController userController;

    public Frame(UserController userController, BookController bookController) {
           
        // Guardamos las referencias a los controladores
        this.userController = userController;
        this.bookController = bookController;

        initComponents();

        //Configuracion básica del JFrame
        setTitle("Buenas Lecturas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Estableci el CardLayout en el Content Pane del Frame
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        //Instancio los paneles
        loginPanel = new LoginPanel(this, userController);
        registerPanel = new RegisterPanel(this, userController);
        homePanel = new HomePanel(this, userController, bookController);
        profilePanel = new ProfilePanel(this);
        reviewsPanel = new ReviewsPanel(this);
        
        profilePanel.setController(bookController);

        //Añadi los paneles al contentPane
        getContentPane().add(loginPanel, "LOGIN_PANEL");
        getContentPane().add(registerPanel, "REGISTER_PANEL");
        getContentPane().add(homePanel, "HOME_PANEL");
        getContentPane().add(profilePanel, "PROFILE_PANEL");
        getContentPane().add(reviewsPanel, "REVIEWS_PANEL");

        // --- 5. Ajustar el tamaño del Frame y hacerlo visible ---
        pack(); // Ajusta el tamaño del Frame al tamaño preferido de sus componentes.
        setLocationRelativeTo(null); // Centra la ventana.
        
         cardLayout.show(getContentPane(), "LOGIN_PANEL");
    }
    
    public void showPanel(String panelName){
        cardLayout.show(getContentPane(), panelName);
    }
    
 
    public void showReviewsPanel(User user) {
        if (reviewsPanel != null) {
            // 1. Pasa los datos al panel para que se actualice
            reviewsPanel.displayReviews(user);
            
            // 2. Obtiene el CardLayout y muestra el panel de reseñas
            cardLayout.show(getContentPane(), "REVIEWS_PANEL");
        }
    }
    
    
    //Metodos de la interfaz
    @Override
    public void showErrorMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showSuccessMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    //Metodo para navegar al home
    @Override
    public void navigateToHome(User user) {
        //SYSO de test
        System.out.println("Navegando a la pantalla principal para el usuario: " + user.getName());
        
        // 1. GUARDA EL USUARIO EN EL FRAME
         this.loggedInUser = user;
         
        // 2. Notifica a los controladores quién es el usuario activo
        this.userController.setLoggedInUser(this.loggedInUser);
        this.bookController.setLoggedInUser(this.loggedInUser);
    
         // 3. PASA EL USUARIO AL HOMEPANEL
         //    Ahora que tenemos el usuario, se lo damos al panel que lo necesita.
         homePanel.setCurrentUser(this.loggedInUser);
         profilePanel.setCurrentUser(this.loggedInUser);
        
        //Accedo al metodo show panel y le paso el nombre del panel home
        this.showPanel("HOME_PANEL");
    }
    
    @Override
    public void displaySearchResults(List<Book> books) {
       // El Frame recibe la orden, pero se la pasa al panel correcto.
        if (homePanel != null) {
            homePanel.updateResultsList(books);
        }
    }
    
    @Override
    public void refreshProfileView(User updatedUser) {
        System.out.println("Frame: Recibida orden para refrescar la vista del perfil.");
        
        // 1. Actualizamos la copia del usuario en el Frame. ¡MUY IMPORTANTE!
        this.loggedInUser = updatedUser;
        
        // 2. Le decimos al ProfilePanel que se redibuje con la nueva información.
        //    Reusamos el método que creamos para esto.
        if (profilePanel != null) {
            profilePanel.refreshView(updatedUser);
        }
    }
    
    @Override
    public void navigateToLogin(User user) {
        //SYSO de test
        System.out.println("Se registro el usuario correctamente " + user.getName());
        
        //Accedo al metodo show panel y le paso el nombre del panel home        
        this.showPanel("LOGIN_PANEL");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents




    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
