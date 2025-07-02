package View;

import Model.Book;
import Model.Review;
import java.awt.Component;
import javax.swing.*;


public class BookReview extends JPanel implements ListCellRenderer<Book> {


    public BookReview() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Book> list, Book book, int index, boolean isSelected, boolean cellHasFocus) {
        //Metodo que va a renderizar las reseñas

        //Seteo el texto que se va a mostrar
        titleLabel.setText("<html><b>" + book.getName() + "</b><br>" + book.getAuthorName() + "</html>");

        //Obtengo reseña del libro y la asigno en un objeto review
        Review review = book.getReview();

        if (review != null) {
            //Si hay reseña la muestro
            ratingLabel.setText(generateStars(review.getRating()));
            commentArea.setText("“" + review.getComment() + "”");
            commentArea.setVisible(true);
        } else {
            // Si no hay reseña oculto los componentes
            ratingLabel.setText("");
            commentArea.setText("");
            commentArea.setVisible(false);
        }

        //Con esto manejo los colores de la seleccion
        if (isSelected) {
            setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
        }

        return this;
    }

    private String generateStars(int rating)  {
        //Metodo para mostrar el puntaje con formato de estrella

        StringBuilder stars = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars.append('\u2605'); //Estrella llena
            } else {
                stars.append('\u2606'); //Estrella vacia
            }
        }

        return stars.toString();
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        ratingLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        commentArea = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(255, 204, 153));

        titleLabel.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        titleLabel.setText("nombre del autor");

        ratingLabel.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        ratingLabel.setText("estrellas");

        commentArea.setColumns(20);
        commentArea.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        commentArea.setLineWrap(true);
        commentArea.setRows(5);
        commentArea.setWrapStyleWord(true);
        commentArea.setFocusable(false);
        jScrollPane1.setViewportView(commentArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ratingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(ratingLabel))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea commentArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel ratingLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables


}
