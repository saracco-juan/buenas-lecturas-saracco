import Config.ConnectionDB;

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
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }





    }
}