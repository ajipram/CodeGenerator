/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author AJI-PC
 */

;

import codegenerator.MenuUtama;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JLabel;

public class ConnectionHelper {
    Database dbsetting;
    String driver, database, user, pass, userLogin;
    MenuUtama MU;
    JLabel textkon;
    
    
    public Connection connectionClass(){
        
        java.sql.Connection con = null;
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(database, user, pass);
            textkon.setText("Online");
            textkon.setForeground(Color.GREEN);
            MU.setTxtKoneksi(textkon);
        }catch(SQLException e){
            textkon.setText("Offline");
            textkon.setForeground(Color.RED);
            MU.setTxtKoneksi(textkon);
            System.err.println("SQL Error"+e.getMessage());
        }catch(ClassNotFoundException e){
            textkon.setText("Offline");
            textkon.setForeground(Color.RED);
            MU.setTxtKoneksi(textkon);
            System.err.println("Class Error"+e.getMessage());
        }catch(Exception e){
            textkon.setText("Offline");
            textkon.setForeground(Color.RED);
            MU.setTxtKoneksi(textkon);
            System.err.println("Exception Error"+e.getMessage());
        }
        return con;
    }
}
