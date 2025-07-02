package View;

import Controller.BookController;
import Controller.UserController;
import Model.Book;
import Model.User;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.JFrame;

public class Frame extends JFrame implements AuthView, HomeView{

    //El card layout es el que se encarga de manejar las vistas
    private final CardLayout cardLayout;
    
    //Usuario logeado
    private User loggedInUser;
    
    //paneles
    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final HomePanel homePanel;
    private final ProfilePanel profilePanel;
    private final ReviewsPanel reviewsPanel;

    //Controladores
    private final BookController bookController;
    private final UserController userController;

    public Frame(UserController userController, BookController bookController) {
           
        //Guardo las referencias a los controladores
        this.userController = userController;
        this.bookController = bookController;

        initComponents();

        //Configuro el Jframe
        setTitle("Buenas Lecturas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Centra la ventana

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

        //Lo primero que muestro es el login panel
         cardLayout.show(getContentPane(), "LOGIN_PANEL");
    }
    
    public void showPanel(String panelName){
        cardLayout.show(getContentPane(), panelName);
    }
    
 
    public void showReviewsPanel(User user) {
        if (reviewsPanel != null) {
            
            reviewsPanel.displayReviews(user);
            
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
        //test
        //System.out.println("Navegando a la pantalla principal para el usuario: " + user.getName());
        
        //Guardo el usuario en el frame
         this.loggedInUser = user;
         
        //Notifico a los controladores el usuario activo
        this.userController.setLoggedInUser(this.loggedInUser);
        this.bookController.setLoggedInUser(this.loggedInUser);
    
         //Le seto el user a los paneles
         homePanel.setCurrentUser(this.loggedInUser);
         profilePanel.setCurrentUser(this.loggedInUser);
        
        //Accedo al metodo show panel y le paso el nombre del panel home
        this.showPanel("HOME_PANEL");
    }
    
    @Override
    public void displaySearchResults(List<Book> books) {
       //El Frame recibe la orden y se la pasa al panel correcto
        if (homePanel != null) {
            homePanel.updateResultsList(books);
        }
    }
    
    @Override
    public void refreshProfileView(User updatedUser) {
        //System.out.println("Frame: orden para refrescar el perfil recibida");

        //Updateo el user
        this.loggedInUser = updatedUser;
        
        //Le digo al ProfilePanel que se reenderice con la nueva info
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
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        setLocation(new java.awt.Point(0, 0));
        setName("Buenas Lecturas"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1050, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(1050, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
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
