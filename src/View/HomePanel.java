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
import java.util.List;
import javax.swing.*;


public class HomePanel extends JPanel {

   //Dependencias con el main frame
   private final Frame mainFrame;

   //Dependencia a los controladores
   private final UserController userController;
   private final BookController bookController;

   //Seteo el usuario
   private User currentUser;
   
   //Constructor del panel
    public HomePanel(Frame mainFrame, UserController userController, BookController bookController) {
        
        initComponents();
        
        this.mainFrame = mainFrame;
        this.userController = userController;
        this.bookController = bookController;

        //Creo un modelo de lista que va a tener objetos de tipo Book
        DefaultListModel<Book> initialModel = new DefaultListModel<>();
        
        //Asigno el modelo a resultsList
        resultsList.setModel(initialModel);
        
        //Habilito o deshabilito el boton segun la selección en la lista
        resultsList.addListSelectionListener(e -> {

        //Habilito el botOn solo si algo esta seleccionado
        addToWhishlistButon.setEnabled(!resultsList.isSelectionEmpty());
        });
    }
    
    //Seteo el usuario que esta utilizando la aplicacion
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    private void handleSearchClick(){

        String query = buscarLibro.getText();

        bookController.handleSearch(query);
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
     
    public void updateResultsList(List<Book> books) {

      DefaultListModel<Book> newModel = new DefaultListModel<>();

        if (books != null && !books.isEmpty()) {
            for (Book book : books) {
              newModel.addElement(book);
            }
        }

        resultsList.setModel(newModel);
    }

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
        searchButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButonActionPerformed(evt);
            }
        });

        resultsList.setBackground(new java.awt.Color(255, 243, 232));
        resultsList.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jScrollPane1.setViewportView(resultsList);

        addToWhishlistButon.setBackground(new java.awt.Color(255, 243, 232));
        addToWhishlistButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        addToWhishlistButon.setText("Añadir a quiero leer");
        addToWhishlistButon.setEnabled(false);
        addToWhishlistButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToWhishlistButonActionPerformed(evt);
            }
        });

        profileButon.setBackground(new java.awt.Color(255, 243, 232));
        profileButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        profileButon.setText("Ver mi perfil");
        profileButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileButonActionPerformed(evt);
            }
        });

        loggoutButton.setBackground(new java.awt.Color(255, 243, 232));
        loggoutButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        loggoutButton.setText("Cerrar Sesión");
        loggoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buscarLibroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarLibroMouseClicked
        this.buscarLibro.setText("");
    }//GEN-LAST:event_buscarLibroMouseClicked

    private void searchButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButonActionPerformed
        handleSearchClick();
    }//GEN-LAST:event_searchButonActionPerformed

    private void addToWhishlistButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToWhishlistButonActionPerformed
        //System.out.println("Clic en boton "Añadir a Quiero" Leer detectado");
            
            //Obtengo el libro seleccionado de la lista
            Book selectedBook = resultsList.getSelectedValue();
            
            //Debug
            //System.out.println("Libro seleccionado: " + (selectedBook != null ? selectedBook.getName() : "null"));
            //System.out.println("Usuario actual: " + (currentUser != null ? currentUser.getName() : "null"));
    
            if (selectedBook != null && currentUser != null) {

                //Llamo al controlador de usuario
                userController.addBookToWhishlist(currentUser, selectedBook);
            }else{
                System.err.println(" ERROR: El libro o el usuario es null");
            }
    }//GEN-LAST:event_addToWhishlistButonActionPerformed

    private void profileButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileButonActionPerformed
        mainFrame.showPanel("PROFILE_PANEL");
    }//GEN-LAST:event_profileButonActionPerformed

    private void loggoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggoutButtonActionPerformed
        showErrorMessage("Metodo en construccion");
    }//GEN-LAST:event_loggoutButtonActionPerformed


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
