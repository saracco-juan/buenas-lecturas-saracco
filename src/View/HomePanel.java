/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.BookController;
import Controller.UserController;
import Model.Book;
import Model.User;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan
 */
public class HomePanel extends javax.swing.JPanel {

   //Dependencias con el main frame y el user controller
   private Frame mainFrame;
   private UserController userController;
   private BookController bookController;
   private User currentUser;
   
   //Constructor del panel
    public HomePanel(Frame mainFrame, UserController userController, BookController bookController) {
        
        initComponents();
        
        this.mainFrame = mainFrame;
        this.userController = userController;
        this.bookController = bookController;
        
        // --- INICIALIZACIÓN DEL MODELO DE LA LISTA ---
        // Crea un modelo de lista que contendrá objetos Book.
        DefaultListModel<Book> initialModel = new DefaultListModel<>();
        
        // Asigna este modelo a tu JList.
        resultsList.setModel(initialModel);
        
        searchButon.addActionListener( new java.awt.event.ActionListener() {
           
            @Override
            public void actionPerformed(ActionEvent ae) {
                handleSearchClick();
            }
            
        });
        
        // Habilitar/deshabilitar el botón según la selección en la lista
        resultsList.addListSelectionListener(e -> {
            // Se habilita el botón solo si algo está seleccionado
            addToWhishlistButon.setEnabled(!resultsList.isSelectionEmpty());
        });
     
        // Acción para el botón "Añadir a Quiero Leer"
        addToWhishlistButon.addActionListener(e -> {
            
            System.out.println("1. Clic en 'Añadir a Quiero Leer' detectado.");
            
            // Obtenemos el libro seleccionado de la lista
            Book selectedBook = resultsList.getSelectedValue();
            
            // Verifiquemos si los objetos son nulos
            System.out.println("   - Libro seleccionado: " + (selectedBook != null ? selectedBook.getName() : "null"));
            System.out.println("   - Usuario actual: " + (currentUser != null ? currentUser.getName() : "null"));
    
            if (selectedBook != null && currentUser != null) {
                // Llamamos al controlador de usuario para que haga el trabajo
                System.out.println("   - Llamando al controlador...");
                userController.addBookToWhishlist(currentUser, selectedBook);
            }else{
                System.err.println("   - ¡ERROR! El libro o el usuario es null. No se puede continuar.");
            }
        });
        
        profileButon.addActionListener(e -> {
        mainFrame.showPanel("PROFILE_PANEL");
    });
        
    }
    
    //Seteo el usuario que esta utilizando la aplicacion
    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Opcional: puedes actualizar una etiqueta para que diga "Bienvenido, Juan"
        // lblWelcome.setText("Bienvenido, " + this.currentUser.getName());
    }
    
    private void handleSearchClick(){
        
        System.out.println("1. Clic en el botón de búsqueda detectado.");
        String query = buscarLibro.getText();
        System.out.println("   - Término de búsqueda: '" + query + "'"); // <-- AÑADE ESTO

        bookController.handleSearch(query);
    }
    
     public void showErrorMessage(String message) {
        // El propio panel muestra el popup de error.
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
     
     // ... dentro de la clase HomePanel ...


  public void updateResultsList(List<Book> books) {
    System.out.println("7. Dentro de updateResultsList. Actualizando el modelo con " + books.size() + " libros.");

    // CREAMOS UN MODELO NUEVO CADA VEZ. Es más simple y seguro.
    DefaultListModel<Book> newModel = new DefaultListModel<>();

    if (books != null && !books.isEmpty()) {
        // Llenamos el nuevo modelo con los libros.
        for (Book book : books) {
            newModel.addElement(book);
        }
    } else {
        // Para manejar el caso sin resultados, podríamos añadir un mensaje,
        // pero como el modelo es de <Book>, no podemos. Lo dejamos vacío.
        // Opcional: podrías mostrar un JLabel aparte con el mensaje "No se encontraron resultados".
    }

    // ASIGNAMOS EL NUEVO MODELO al JList.
    // Esto notificará automáticamente al JList que debe redibujarse.
    resultsList.setModel(newModel);
    
    // Opcional, como "seguro de vida" para forzar el repintado.
    // resultsList.revalidate();
    // resultsList.repaint();

    System.out.println("8. Modelo asignado al JList.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        buscarLibro = new javax.swing.JTextField();
        searchButon = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultsList = new javax.swing.JList<>();
        addToWhishlistButon = new javax.swing.JButton();
        profileButon = new javax.swing.JButton();
        loggoutButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 204, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Bienvenido a Buenas Lecturas");

        buscarLibro.setBackground(new java.awt.Color(255, 243, 232));
        buscarLibro.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        buscarLibro.setText("Buscar libro o autor");
        buscarLibro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buscarLibroMouseClicked(evt);
            }
        });

        searchButon.setBackground(new java.awt.Color(255, 243, 232));
        searchButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        searchButon.setText("Buscar");

        resultsList.setBackground(new java.awt.Color(255, 243, 232));
        resultsList.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jScrollPane1.setViewportView(resultsList);

        addToWhishlistButon.setBackground(new java.awt.Color(255, 243, 232));
        addToWhishlistButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        addToWhishlistButon.setText("Añadir a quiero leer");
        addToWhishlistButon.setEnabled(false);

        profileButon.setBackground(new java.awt.Color(255, 243, 232));
        profileButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        profileButon.setText("Ver mi perfil");

        loggoutButton.setBackground(new java.awt.Color(255, 243, 232));
        loggoutButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        loggoutButton.setText("Cerrar Sesión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(buscarLibro, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addToWhishlistButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(profileButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loggoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addToWhishlistButon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profileButon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loggoutButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buscarLibroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarLibroMouseClicked
        this.buscarLibro.setText("");
    }//GEN-LAST:event_buscarLibroMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToWhishlistButon;
    private javax.swing.JTextField buscarLibro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loggoutButton;
    private javax.swing.JButton profileButon;
    private javax.swing.JList<Book> resultsList;
    private javax.swing.JButton searchButon;
    // End of variables declaration//GEN-END:variables
}
