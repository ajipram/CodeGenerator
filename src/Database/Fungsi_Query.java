/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;



/**
 *
 * @author AJI-PC
 */
public class Fungsi_Query {
    
    Database dbsetting;
    String driver, database, user, pass;
    
    public Fungsi_Query(){
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
    }
 /*   
    public void Input_Detil(String[] kolom, String[] isi, String nmtable){
        try{
            String sql = "INSERT INTO " +nmtable+ " ( ";
            for(int i=0;i<kolom.length;i++){
                sql+=kolom[i];
                if(i<kolom.length-1)
                    sql+=",";
            
            }
            sql+=") values (";
            for(int i=0;i<kolom.length;i++){
                sql+=" '"+isi[i]+"' ";
                if(i<kolom.length-1)
                    sql+=",";
            }
            sql+=")";
            System.err.println("sql input "+sql);
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stt = con.createStatement();
            stt.executeUpdate(sql);
            stt.close();
            con.close();
            JOptionPane.showMessageDialog(null,"Data tersimpan","Info", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.INFORMATION_MESSAGE);
        }  
    }
 */   
    String statusInput = "";

    
    public void Input_Detil(String[] kolom, String[] isi, String nmtable){
        try{
            String sql = "INSERT INTO " +nmtable+ " ( ";
            for(int i=0;i<kolom.length;i++){
                sql+=kolom[i];
                if(i<kolom.length-1)
                    sql+=",";
            
            }
            sql+=") values (";
            for(int i=0;i<kolom.length;i++){
                sql+=" '"+isi[i]+"' ";
                if(i<kolom.length-1)
                    sql+=",";
            }
            sql+=")";

            System.err.println("sql input "+sql);
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stt = con.createStatement();
            stt.executeUpdate(sql);
            stt.close();
            con.close();
            setStatusInput("Sukses");
            JOptionPane.showMessageDialog(null,"Data tersimpan","Info", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            setStatusInput("Gagal");
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.INFORMATION_MESSAGE);
        }  
    }
    
    public void Input_DetilDate(String[] kolom, String[] isi, String nmtable){
        try{
            String sql = "INSERT INTO " +nmtable+ " ( ";
            for(int i=0;i<kolom.length;i++){
                sql+=kolom[i];
                if(i<kolom.length-1)
                    sql+=",";
            
            }
            sql+=") values (";
            for(int i=0;i<kolom.length;i++){
                sql+=" '"+isi[i]+"' ";
                if(i<kolom.length-1)
                    sql+=",";
            }
            sql+=")";
            
//            String sql = "INSERT INTO " +nmtable+ " ( ";
//            
//            for(int i=0;i<kolom.length;i++){
//                sql+=kolom[i];
//                if(i<kolom.length-1)
//                    sql+=",";
//            
//            }
//            sql+=") values (";
//            sql+=" '"+isi[0]+"' ";
//            sql+=",";
//            sql+=" '"+tglBukti+"' ";
//            sql+=",";
//            for(int i=2;i<kolom.length;i++){
//                sql+=" '"+isi[i]+"' ";
//                if(i<kolom.length-1)
//                    sql+=",";
//            }
//            sql+=")";
            System.err.println("sql input "+sql);
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stt = con.createStatement();
            stt.executeUpdate(sql);
            stt.close();
            con.close();
            setStatusInput("Sukses");
            JOptionPane.showMessageDialog(null,"Data tersimpan","Info", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            setStatusInput("Gagal");
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.INFORMATION_MESSAGE);
        }  
    }
    
    public String getStatusInput() {
        return statusInput;
    }

    public void setStatusInput(String statusInput) {
        this.statusInput = statusInput;
    }
    
    public void Edit(String[] kolom, String[] isi, String nmtable, String primarykey, String data){
        try{
            String sql = "Update "+nmtable+" set ";
            for(int i = 0;i<kolom.length;i++){
                sql+=kolom[i]+"= '"+isi[i]+"' ";
                if(i<kolom.length-1)
                    sql+=",";
            }
            sql+="Where "+primarykey+" = '"+data+"' ";
            System.err.println("sql = "+sql);
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stt = con.createStatement();
            stt.executeUpdate(sql);
            stt.close();
            con.close();
            JOptionPane.showMessageDialog(null,"Data berhasil diubah","Info", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void EditSingle(String kolom, String isi, String nmtable, String primarykey, String data){
        try{
            String sql = "Update "+nmtable+" set "+kolom+" = '"+isi+"' ";
            
            sql+="Where "+primarykey+" = '"+data+"' ";
            System.err.println("sql = "+sql);
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stt = con.createStatement();
            stt.executeUpdate(sql);
            stt.close();
            con.close();
            //JOptionPane.showMessageDialog(null,"Data berhasil diubah","Info", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void Hapus(String nmtable, String primarykey, String data){
        try{
            String sql = "Delete From "+nmtable+" Where "+primarykey+" = '"+data+"' ";
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stt = con.createStatement();
            stt.executeUpdate(sql);
            stt.close();
            con.close();
        }catch (Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    
}
