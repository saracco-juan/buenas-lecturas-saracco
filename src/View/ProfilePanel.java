/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.BookController;
import Model.Book;
import Model.User;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan
 */
public class ProfilePanel extends javax.swing.JPanel {

    private final Frame mainFrame;
    private User currentUser;
    private BookController bookController;

    
    public ProfilePanel(Frame mainFrame) {
        initComponents();
        initListeners();
        this.mainFrame = mainFrame;
    }

    private void initListeners() {

        //No encontre la property para estos eventos
        
        wishlistList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            Book selected = wishlistList.getSelectedValue();

            // Limpio la otra lista
            readlistList.clearSelection();

            // Habilito o deshabilito el botón según haya selección
            markAsReadButon.setEnabled(selected != null);

            // Desactivo el botón de reseña si algo se seleccionó acá
            addReviewButton.setEnabled(false);
            }
        });

        readlistList.addListSelectionListener(e -> {
        
        if (!e.getValueIsAdjusting()) {
      
            Book selected = readlistList.getSelectedValue();
            // Limpio la otra lista
            wishlistList.clearSelection();

            // Habilito o deshabilito el botón de reseña
            addReviewButton.setEnabled(selected != null);

            // Desactivo el botón de marcar como leído
            markAsReadButon.setEnabled(false);
            }
        });

        //Se inicializan disabled
        markAsReadButon.setEnabled(false);
        addReviewButton.setEnabled(false);
 
    }
    
    private void handleReviewAction() {
        Book selectedBook = readlistList.getSelectedValue();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro de la lista 'Leídos'.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ReviewDialog reviewDialog = new ReviewDialog(mainFrame, selectedBook, bookController);
        reviewDialog.setVisible(true);
    }
    
    private void handleDeleteBook() {
        Book selectedBook = null;
        String listType = null;

        if (wishlistList.getSelectedValue() != null) {
            selectedBook = wishlistList.getSelectedValue();
            listType = "WANT_TO_READ";
        } 
        else if (readlistList.getSelectedValue() != null) {
            selectedBook = readlistList.getSelectedValue();
            listType = "READ";
        }

        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro de alguna de las listas para eliminar.", "Ningún libro seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que quieres eliminar \"" + selectedBook.getName() + "\" de tu lista?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (bookController != null) {
                bookController.removeBookFromList(selectedBook, listType);
            } else {
                System.err.println("Error: BookController no ha sido inicializado en ProfilePanel.");
                JOptionPane.showMessageDialog(this, "Error de configuración de la aplicación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshView(User updatedUser) {
        this.currentUser = updatedUser;
        updateProfileDisplay();
    }
    
    public void setController(BookController bookController) {
        this.bookController = bookController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        userName.setText("Perfil de: " + user.getName());
        updateProfileDisplay();
    }

    private void updateProfileDisplay() {
        if (currentUser == null) {
            userName.setText("Perfil de: Invitado");
            updateBookList(wishlistList, null);
            updateBookList(readlistList, null);
            return;
        }

        // Actualiza la etiqueta con el nombre del usuario
        userName.setText("Perfil de: " + currentUser.getName());

        // Actualiza la lista "Quiero Leer"
        updateBookList(wishlistList, currentUser.getWantToRead());

        // Actualiza la lista "Leídos"
        updateBookList(readlistList, currentUser.getReadBooks());
        
    }
    
    private void updateBookList(javax.swing.JList<Book> listComponent, List<Book> books) {
        DefaultListModel<Book> model = new DefaultListModel<>();
        if (books != null && !books.isEmpty()) {
            for (Book book : books) {
                model.addElement(book);
            }
        }
        listComponent.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        wishlistList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        readlistList = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        homeButon = new javax.swing.JButton();
        deleteButon = new javax.swing.JButton();
        markAsReadButon = new javax.swing.JButton();
        addReviewButton = new javax.swing.JButton();
        viewReviewsButton = new javax.swing.JButton();
        viewSuggestionsButton = new javax.swing.JButton();
        loggoutButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 204, 153));

        userName.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        userName.setText("user name here");

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setText("Lista de Quiero leer");

        wishlistList.setBackground(new java.awt.Color(255, 243, 232));
        wishlistList.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        wishlistList.setPreferredSize(null);
        jScrollPane1.setViewportView(wishlistList);

        jLabel2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel2.setText("Lista de leidos");

        readlistList.setBackground(new java.awt.Color(255, 243, 232));
        readlistList.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        readlistList.setName(""); // NOI18N
        readlistList.setPreferredSize(null);
        jScrollPane2.setViewportView(readlistList);

        jLabel3.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel3.setText("Lista de sugerencias");

        jList3.setBackground(new java.awt.Color(255, 243, 232));
        jList3.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jScrollPane3.setViewportView(jList3);

        homeButon.setBackground(new java.awt.Color(255, 243, 232));
        homeButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        homeButon.setText("Inicio");
        homeButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButonActionPerformed(evt);
            }
        });

        deleteButon.setBackground(new java.awt.Color(255, 243, 232));
        deleteButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        deleteButon.setText("Eliminar seleccionado");
        deleteButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButonActionPerformed(evt);
            }
        });

        markAsReadButon.setBackground(new java.awt.Color(255, 243, 232));
        markAsReadButon.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        markAsReadButon.setText("Marcar como Leído");
        markAsReadButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markAsReadButonActionPerformed(evt);
            }
        });

        addReviewButton.setBackground(new java.awt.Color(255, 243, 232));
        addReviewButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        addReviewButton.setText("Añadir Reseña");
        addReviewButton.setEnabled(false);
        addReviewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addReviewButtonActionPerformed(evt);
            }
        });

        viewReviewsButton.setBackground(new java.awt.Color(255, 243, 232));
        viewReviewsButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        viewReviewsButton.setText("Ver Reseñas");
        viewReviewsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReviewsButtonActionPerformed(evt);
            }
        });

        viewSuggestionsButton.setBackground(new java.awt.Color(255, 243, 232));
        viewSuggestionsButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        viewSuggestionsButton.setText("Ver Sugerencias");
        viewSuggestionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSuggestionsButtonActionPerformed(evt);
            }
        });

        loggoutButton.setBackground(new java.awt.Color(255, 243, 232));
        loggoutButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        loggoutButton.setText("Cerrar Sesión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewReviewsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewSuggestionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(homeButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(markAsReadButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addReviewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(loggoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(markAsReadButon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addReviewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewReviewsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewSuggestionsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(homeButon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loggoutButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void markAsReadButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markAsReadButonActionPerformed
        Book selectedBook = wishlistList.getSelectedValue();

            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro de la lista 'Quiero Leer'.",
                    "Ningún libro seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (bookController != null) {
                bookController.moveBookToReadList(selectedBook);
            }
    }//GEN-LAST:event_markAsReadButonActionPerformed

    private void deleteButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButonActionPerformed
        handleDeleteBook();
    }//GEN-LAST:event_deleteButonActionPerformed

    private void addReviewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addReviewButtonActionPerformed
        handleReviewAction();
    }//GEN-LAST:event_addReviewButtonActionPerformed

    private void viewReviewsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReviewsButtonActionPerformed
        if (currentUser != null) {
                mainFrame.showReviewsPanel(currentUser);
            }
    }//GEN-LAST:event_viewReviewsButtonActionPerformed

    private void viewSuggestionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSuggestionsButtonActionPerformed
        JOptionPane.showMessageDialog(this, "Metodo en construccion",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
    }//GEN-LAST:event_viewSuggestionsButtonActionPerformed

    private void homeButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButonActionPerformed
        mainFrame.showPanel("HOME_PANEL");
    }//GEN-LAST:event_homeButonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addReviewButton;
    private javax.swing.JButton deleteButon;
    private javax.swing.JButton homeButon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<Book> jList3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton loggoutButton;
    private javax.swing.JButton markAsReadButon;
    private javax.swing.JList<Book> readlistList;
    private javax.swing.JLabel userName;
    private javax.swing.JButton viewReviewsButton;
    private javax.swing.JButton viewSuggestionsButton;
    private javax.swing.JList<Book> wishlistList;
    // End of variables declaration//GEN-END:variables
}
