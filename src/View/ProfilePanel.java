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

    private Frame mainFrame;
    private User currentUser;
    private BookController bookController;

    
    public ProfilePanel(Frame mainFrame) {
        initComponents();
        initListeners();
        
        this.mainFrame = mainFrame;
    }
    
    // 3. MÉTODO PARA INICIALIZAR LOS LISTENERS DE LOS BOTONES
    private void initListeners() {
    // --- TUS LISTENERS EXISTENTES ---
   markAsReadButon.addActionListener(e -> {
    // 1. Obtener el libro seleccionado de la lista "Quiero Leer"
    Book selectedBook = wishlistList.getSelectedValue();

    // 2. Validar que se haya seleccionado un libro
    if (selectedBook == null) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro de la lista 'Quiero Leer'.", "Ningún libro seleccionado", JOptionPane.WARNING_MESSAGE);
        return; // Detiene la ejecución si no hay nada seleccionado
    }
    
    // 3. Llamar al método del controlador para mover el libro
    if (bookController != null) {
        bookController.moveBookToReadList(selectedBook);
    }
});
    deleteButon.addActionListener(e -> handleDeleteBook());
    homeButon.addActionListener(e -> mainFrame.showPanel("HOME_PANEL"));

    // --- LÓGICA DE LOS BOTONES DE RESEÑA ---
    
    // Acción para el botón "Añadir Reseña"
    addReviewButton.addActionListener(e -> handleReviewAction()); // Suponiendo que tu botón se llama addReviewButton

    // Acción para el nuevo botón "Ver Reseñas"
    viewReviewsButton.addActionListener(e -> {
        if (currentUser != null) {
            mainFrame.showReviewsPanel(currentUser);
            
        }
    });

    // --- LÓGICA DE SELECCIÓN DE LISTAS ---
    
    // Cuando se selecciona algo en "Quiero Leer"
    wishlistList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            boolean isSelected = wishlistList.getSelectedIndex() != -1;
            markAsReadButon.setEnabled(isSelected);
            if (isSelected) {
                readlistList.clearSelection();
                addReviewButton.setEnabled(false); // Desactivar botón de reseña
            }
        }
    });

    // Cuando se selecciona algo en "Leídos"
    readlistList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            boolean isSelected = readlistList.getSelectedIndex() != -1;
            addReviewButton.setEnabled(isSelected); // Activar botón de reseña
            if (isSelected) {
                wishlistList.clearSelection();
                markAsReadButon.setEnabled(false); // Desactivar botón de mover
            }
        }
    });

    // --- ESTADO INICIAL DE LOS BOTONES ---
    markAsReadButon.setEnabled(false);
    addReviewButton.setEnabled(false); // El botón de reseña empieza desactivado
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
    
        // 4. LÓGICA DEL EVENTO DE ELIMINACIÓN
    private void handleDeleteBook() {
        Book selectedBook = null;
        String listType = null;

        // Comprobamos primero si hay algo seleccionado en la lista "Quiero Leer"
        if (wishlistList.getSelectedValue() != null) {
            selectedBook = wishlistList.getSelectedValue();
            listType = "WANT_TO_READ";
        } 
        // Si no, comprobamos la lista "Leídos"
        else if (readlistList.getSelectedValue() != null) {
            selectedBook = readlistList.getSelectedValue();
            listType = "READ";
        }

        // Si después de comprobar ambas listas, no hay nada seleccionado, mostramos un error.
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro de alguna de las listas para eliminar.", "Ningún libro seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pedimos confirmación al usuario
        int choice = JOptionPane.showConfirmDialog(this, 
            "¿Estás seguro de que quieres eliminar \"" + selectedBook.getName() + "\" de tu lista?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // Llamamos al controlador con los datos correctos
            if (bookController != null) {
                bookController.removeBookFromList(selectedBook, listType);
            } else {
                System.err.println("Error: BookController no ha sido inicializado en ProfilePanel.");
                JOptionPane.showMessageDialog(this, "Error de configuración de la aplicación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshView(User updatedUser) {
    // Paso A: Actualiza la variable interna del panel con los nuevos datos.
    this.currentUser = updatedUser;
    
    // Paso B: Llama a tu método privado existente para que redibuje todo.
    updateProfileDisplay();
    }
    
    // El Frame usará este método para darnos una instancia del controlador.
    public void setController(BookController bookController) {
        this.bookController = bookController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Actualiza la UI con los datos del usuario
        userName.setText("Perfil de: " + user.getName());
        updateProfileDisplay();
    }
    
     /**
     * Método centralizado para actualizar todos los componentes del perfil.
     */
    private void updateProfileDisplay() {
        if (currentUser == null) {
            // Caso de seguridad por si algo falla.
            userName.setText("Perfil de: Invitado");
            // Limpiar listas si es necesario
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
        
        // (En el futuro) Actualiza la lista de "Sugerencias"
        // ...
    }
    
    /**
     * Un método de utilidad reutilizable para poblar cualquier JList de libros.
     * @param listComponent El JList que se va a actualizar.
     * @param books La lista de libros a mostrar.
     */
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

        userName.setText("user name here");

        jLabel1.setText("Lista de Quiero leer");

        jScrollPane1.setViewportView(wishlistList);

        jLabel2.setText("Lista de leidos");

        jScrollPane2.setViewportView(readlistList);

        jLabel3.setText("Lista de sugerencias");

        jScrollPane3.setViewportView(jList3);

        homeButon.setText("Inicio");

        deleteButon.setText("Eliminar seleccionado");

        markAsReadButon.setText("Marcar como Leído");

        addReviewButton.setText("Añadir Reseña");
        addReviewButton.setEnabled(false);

        viewReviewsButton.setText("Ver Reseñas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(48, 48, 48)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(homeButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(markAsReadButon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addReviewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(viewReviewsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(189, 189, 189))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(markAsReadButon)
                                .addGap(4, 4, 4)
                                .addComponent(deleteButon)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addReviewButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewReviewsButton))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(homeButon))
                .addContainerGap(215, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


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
    private javax.swing.JButton markAsReadButon;
    private javax.swing.JList<Book> readlistList;
    private javax.swing.JLabel userName;
    private javax.swing.JButton viewReviewsButton;
    private javax.swing.JList<Book> wishlistList;
    // End of variables declaration//GEN-END:variables
}
