import Config.ConnectionDB;
import Controller.BookController;
import Controller.UserController;
import DAO.AuthorDAO;
import DAO.BookDAO;
import DAO.ReviewDAO;
import DAO.UserDao;
import Service.AuthorService;
import Service.BookService;
import Service.UserService;
import Service.UserServiceImpl;
import View.Frame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args){

        System.out.println("Hello and welcome!");

        try {
            ConnectionDB instance = ConnectionDB.getInstance();

            Connection connection = instance.getConnection();

            System.out.println("Connected to database.");

            UserDao userDao = new UserDao(connection);
            BookDAO bookDAO = new BookDAO();
            ReviewDAO reviewDAO = new ReviewDAO(connection);

            BookService bookService = new BookService(bookDAO, userDao, reviewDAO);
            UserService userService = new UserServiceImpl(userDao, bookService);

            BookController bookController = new BookController(bookService);
            UserController userController = new UserController(userService);
            
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {

                    Frame mainFrame = new Frame(userController, bookController);

                    userController.setView(mainFrame);
                    bookController.setView(mainFrame);

                    mainFrame.setVisible(true);
                }
        }); 
        }catch (Exception e){
            System.err.println("Error fatal al inicializar la base de datos. La aplicación no puede continuar.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos. Verifique la configuración.\nError: " + e.getMessage(),
                    "Error Crítico",
                    JOptionPane.ERROR_MESSAGE);
        }





    }
}