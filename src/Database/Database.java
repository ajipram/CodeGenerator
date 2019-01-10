/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author AJI-PC
 */
public class Database {
    
    public Properties mypanel, myLanguage;
    private String strNamaPanel;
    
    
    public Database(){
    }
    
    public String SettingPanel(String nmPanel){
        try{
            mypanel = new Properties();
            mypanel.load(new FileInputStream("lib/database.ini"));
            strNamaPanel = mypanel.getProperty(nmPanel);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"Tidak ada koneksi","Error", JOptionPane.INFORMATION_MESSAGE);
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return strNamaPanel;
    }
}
