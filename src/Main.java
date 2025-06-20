import Config.ConnectionDB;
import Controller.UserController;
import DAO.UserDao;
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
            UserService userService = new UserServiceImpl(userDao);
            UserController userController = new UserController(userService);
            
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Frame().setVisible(true);
                }
        }); 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }





    }
}