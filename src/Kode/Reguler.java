/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kode;

import Database.ConnectionHelper;
import Database.Database;
import Database.Fungsi_Query;
import Popup.PopCustomer;
import Popup.PopDetailReguler;
import codegenerator.MenuUtama;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import static java.awt.Label.CENTER;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javax.swing.JLabel;

import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author AJI-PC
 */
public class Reguler extends javax.swing.JInternalFrame {
    int nomor;
    JLabel jl;
    
    Database dbsetting;
    String driver, database, user, pass;
    String tek1, tek2, teknis1, teknis2;
    String no, kode, nama, status = "0", date, tahun, bulan;
    String kdCust, nmCust, telp, kdCab, nmCab, kdAdm, nmAdm, kodeCab="J01";
    Boolean dikunjungi;
    static String noBukti,cust,mitra, wilayah, outlet;
    String kodetek, kodenya, kodecabnya, namanya, datenya, kodemitranya;
    
    
    String data[] = new String[11];
    Fungsi_Query query = new Fungsi_Query();
    
    String[] kolom  = new String[19];
    String[] isi    = new String[19];
    
    PopCustomer popCustomer = new PopCustomer(new javax.swing.JFrame(), true);
    PopDetailReguler popDetail = new PopDetailReguler(new javax.swing.JFrame(), true);
    
    public String[] Kolom(){
        kolom[0] = "NoBukti";
        kolom[1] = "TglBukti";
        kolom[2] = "KdCust";
        kolom[3] = "NmCust";
        kolom[4] = "UP";
        kolom[5] = "Telp";
        kolom[6] = "KdCab";
        kolom[7] = "Lokasi";
        kolom[8] = "Outlet";
        kolom[9] = "KdSDH";
        kolom[10] = "KdMitra";
        kolom[11] = "KdTeknisi";
        kolom[12] = "KdTeknisi2";
        kolom[13] = "PIC";
        kolom[14] = "Cancel";
        kolom[15] = "Dikunjungi";
        kolom[16] = "Approval";
        kolom[17] = "Selesai";
        kolom[18] = "WO";
        
        
        return kolom;
    }
    
    
    public String[] Data(){
        
        String cust = txtCustomer.getText();
        String kode = cust.substring(0, cust.indexOf(" : "));
        String nama = cust.substring(cust.indexOf(" : "));
        String namafix = nama.substring(3);
        
        String strCab = String.valueOf(cmbCabang.getSelectedItem());
        String strSDH = String.valueOf(cmbSDH.getSelectedItem());
        String strMit = String.valueOf(cmbMitra.getSelectedItem());
        System.out.println("Mitra = "+strMit);
        
        String kodeMitra = strMit.substring(0, 2);
        System.out.println("kodenya "+kodeMitra);
         if(kodeMitra.equals("WS")){
             tek1 = cmbTeknisi1.getSelectedItem().toString();
             teknis1 = tek1.substring(0, tek1.indexOf(" : "));
             System.out.println("teknis "+teknis1);
             tek2 = cmbTeknisi2.getSelectedItem().toString();
             if(!tek2.isEmpty() || !tek2.equals("Pilih Teknisi")){
                teknis2 = tek2.substring(0, tek2.indexOf(" : "));
             }else{
                teknis2 = ""; 
             }
         }else{
             teknis1 = "";
             teknis2 = "";
         }
         
         
        isi[0] = lblNoBukti.getText();
        isi[1] = datetime("MM/dd/yyyy");
        isi[2] = kode;
        isi[3] = namafix;
        isi[4] = txtUP.getText();
        isi[5] = txtTelp.getText();
        isi[6] = strCab.substring(0, strCab.indexOf(" : "));
        isi[7] = txtLokasi.getText();
        isi[8] = txtOutlet.getText();
        isi[9] = strSDH.substring(0, strSDH.indexOf(" : "));
        isi[10] = strMit.substring(0, strMit.indexOf(" : "));
        isi[11] = teknis1;
        isi[12] = teknis2;
        isi[13] = MenuUtama.getNama();
        isi[14] = "0";
        isi[15] = "0";
        isi[16] = "0";
        isi[17] = "0";
        isi[18] = "0";

        return isi;
    }

    /**
     * Creates new form Setting
     */
    public Reguler() {
        initComponents();
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");

        tabelKode.setModel(tblModel);
        tabelKode.setAutoCreateRowSorter(true);
        Tabel(tabelKode, new int[]{100,100,100,250,100,100,100,100,100,100,100});
        setCellsAlignment();
        sortTable();
        tabelKode.getTableHeader().setEnabled(false);
        
        
        comboCabang();
        kodeCab = "J01";
        comboMitra();
        //comboSDH();
        //cmbTeknisi1.removeAllItems();
        //cmbTeknisi2.removeAllItems();
        comboTeknisi();
        

        btnCariCustomer.getFocusListeners();
        getCurrentDate();
        lblTglBukti.setText(datetime("dd MMM yyyy"));
        txtTahun.setText(tahun);
        tampilKode();
        
     
        datenya = tahun+bulan;
        System.out.println("datenya = "+datenya);
        
        //searchKode("No Bukti", datenya);
        generateNoBukti();
        
        
    }
    
    
    
    private void getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDateTime now = LocalDateTime.now();
        date = dtf.format(now);
        tahun = date.substring(0,4);
        bulan = date.substring(4,6);
        
        switch(bulan){
            case "01":
                cmbBulan.setSelectedItem("Januari");
                break;
            case "02":
                cmbBulan.setSelectedItem("Februari");
                break;
            case "03":
                cmbBulan.setSelectedItem("Maret");
                break;
            case "04":
                cmbBulan.setSelectedItem("April");
                break;
            case "05":
                cmbBulan.setSelectedItem("Mei");
                break;
            case "06":
                cmbBulan.setSelectedItem("Juni");
                break;
            case "07":
                cmbBulan.setSelectedItem("Juli");
                break;
            case "08":
                cmbBulan.setSelectedItem("Agustus");
                break;
            case "09":
                cmbBulan.setSelectedItem("September");
                break;
            case "10":
                cmbBulan.setSelectedItem("Oktober");
                break;
            case "11":
                cmbBulan.setSelectedItem("November");
                break;
            case "12":
                cmbBulan.setSelectedItem("Desember");
                break;       
        }
    }
    
    private String datetime(String format){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
	LocalDateTime now = LocalDateTime.now();
        date = dtf.format(now);
        return date;
    }

    
    private void sortTable(){
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabelKode.getModel());
        tabelKode.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        int columnIndexToSort = 0;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }
    
    private void clearTable(){
        while(tblModel.getRowCount() > 0){
            tblModel.removeRow(0);
        }
    }
    
    public String autonumber(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "SELECT MAX(SUBSTRING(NoBukti, 12, 3) ) FROM HOrder where KdCust = '"+kdCust+"' and datepart(yy, TglBukti)= "+tahun+" and datepart (mm, TglBukti)= "+bulan; 
            System.err.println("query autonumber"+query);
            ResultSet rs = state.executeQuery(query);
            int max = 0;
            while(rs.next()){
                max = rs.getInt(1);
                  System.err.println(max);
            }
            max = max + 1;
            no = String.format("%03d", max);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
        return no;
    }
    
    public void setCellsAlignment(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for (int columnIndex = 0; columnIndex <tblModel.getColumnCount(); columnIndex++){
            tabelKode.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
    
    private void generateNoBukti(){
        
        String getCab = cmbCabang.getSelectedItem().toString().trim();
        kdCab = getCab.substring(0, getCab.indexOf(" : "));
        String nomor = String.valueOf(autonumber());
        lblNoBukti.setText(kdCust+"-"+tahun+bulan+nomor);           
    }
    
    String getBulan, getTahun, bln, thn;
    public void tampilKode(){
        
        
        getBulan = cmbBulan.getSelectedItem().toString().trim();
        getTahun = txtTahun.getText();
        switch(getBulan){
            case "Januari" :
                bln = "01";
                break;
            case "Februari" :
                bln = "02";
                break;
            case "Maret" :
                bln = "03";
                break;
            case "April" :
                bln = "04";
                break;
            case "Mei" :
                bln = "05";
                break;
            case "Juni" :
                bln = "06";
                break;
            case "Juli" :
                bln = "07";
                break;
            case "Agustus" :
                bln = "08";
                break;
            case "September" :
                bln = "09";
                break;
            case "Oktober" :
                bln = "10";
                break;
            case "November" :
                bln = "11";
                break;
            case "Desember" :
                bln = "12";
                break;
        }
        thn = getTahun+bln;
        System.err.println("tahun "+thn);
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select KdCab, NoBukti, TglBukti, NmCust, Lokasi, Outlet, KdMitra, KdTeknisi, KdTeknisi2, PIC, Cancel from HOrder where NoBukti like '%"+ thn +"%'order by KdCab asc, TglBukti desc"; 
            System.err.println("query tampil = "+query);
            ResultSet rs = state.executeQuery(query);
            clearTable();
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                data[6] = rs.getString(7);
                data[7] = rs.getString(8);
                data[8] = rs.getString(9);
                data[9] = rs.getString(10);
                data[10] = rs.getString(11);
   /*             
                System.err.println("date1 = "+rs.getString(3));
                DateTimeFormatter f = new DateTimeFormatterBuilder().toFormatter();
                DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
                //Date localDate = Date.valueOf(rs.getString(3));
                TemporalAccessor accessor = f.withResolverStyle(ResolverStyle.SMART).parse(rs.getString(3));
                String dateString = FOMATTER.format(accessor);
                System.err.println("date2 = "+dateString);
                data[2] = dateString;
     */           
                tblModel.addRow(data);    
            }
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    String namakolom;
    private void searchKode(String cmb, String key){
        if(cmb.equals("No Bukti")){
            namakolom = "NoBukti";
        }else if(cmb.equals("Customer")){
            namakolom = "NmCust";
        }else{
            namakolom = "KdCab";
        }
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            tblModel.setRowCount(0);
            String query = "Select KdCab, NoBukti, TglBukti, NmCust, Lokasi, Outlet, KdMitra, KdTeknisi, KdTeknisi2, PIC, Cancel from HOrder where "+ namakolom +" like '%"+ key +"%'";
            System.err.println("search "+query);
            ResultSet rs = state.executeQuery(query);
            clearTable();
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                data[6] = rs.getString(7);
                data[7] = rs.getString(8);
                data[8] = rs.getString(9);
                data[9] = rs.getString(10);
                data[10] = rs.getString(11);
                tblModel.addRow(data);
            }
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void comboCabang(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from MCabang";
            System.err.println("query cabang = "+query);
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                kdCab = rs.getString(1);
                nmCab = rs.getString(2);
                cmbCabang.addItem(kdCab+" : "+nmCab);
            }
            cmbCabang.setSelectedItem("J01 : Jakarta");
            System.err.println("cabang = "+kdCab);
            kdCab = "J01";
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void tampilCabang(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from MCabang where KdCab = '"+kode+"'";
            System.err.println("query cabang = "+query);
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                kdCab = rs.getString(1);
                nmCab = rs.getString(2);
                //cmbCabang.addItem(kdCab+" : "+nmCab);
            }
            cmbCabang.setSelectedItem(kdCab+" : "+nmCab);
            System.err.println("cabang = "+kdCab);
            kdCab = "J01";
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    
    
    private void comboMitra(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, Nama from MMitra"; 
            System.err.println("query mitra = "+query);
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                kodenya = rs.getString(1);
                namanya = rs.getString(2);
                cmbMitra.addItem(kodenya+" : "+namanya);
                
            }
            cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void tampilMitra(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, Nama from MMitra where Kode = '"+kode+"'"; 
            System.err.println("query mitra = "+query);
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                kodenya = rs.getString(1);
                namanya = rs.getString(2);
                cmbMitra.addItem(kodenya+" : "+namanya);
                System.out.println("kodenya combo "+kodenya);
            }
            cmbMitra.setSelectedItem(kodenya+" : "+namanya);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void comboTeknisi(){
        try{
            cmbTeknisi1.removeAllItems();
            cmbTeknisi2.removeAllItems();
            cmbTeknisi1.addItem("Pilih Teknisi");
            cmbTeknisi2.addItem("Pilih Teknisi");
            System.err.println("remove all item");
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, KdCab, Nama from HTeknisi where KdCab = '"+kodeCab+"' order by Kode";
            System.err.println("query teknisi = "+query);
            ResultSet rs = state.executeQuery(query);
            String namanya;
            
            while(rs.next()){
                kodetek = rs.getString(1);
                namanya = rs.getString(3);
                cmbTeknisi1.addItem(kodetek+" : "+namanya);
                cmbTeknisi2.addItem(kodetek+" : "+namanya);
            }
            cmbTeknisi2.setSelectedItem("");
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void tampilTeknisi1(String kode){
        try{
            
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, KdCab, Nama from HTeknisi where KdCab = '"+kodeCab+"' and Kode = '"+kode+"' order by Kode";
            ResultSet rs = state.executeQuery(query);
            String namanya="";
            
            while(rs.next()){
                kodetek = rs.getString(1);
                namanya = rs.getString(3);
                
                
            }
            cmbTeknisi1.setSelectedItem(kodetek+" : "+namanya);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void tampilTeknisi2(String kode){
        try{
            
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, KdCab, Nama from HTeknisi where KdCab = '"+kodeCab+"' and Kode = '"+kode+"' order by Kode";
            ResultSet rs = state.executeQuery(query);
            String namanya="";
            
            while(rs.next()){
                kodetek = rs.getString(1);
                namanya = rs.getString(3);
                
                
            }
            cmbTeknisi2.setSelectedItem(kodetek+" : "+namanya);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void comboSDH(){
        try{
            cmbSDH.removeAllItems();
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "SELECT KODE, NAMA, KDCAB FROM MPETUGAS WHERE KDCAB='"+kodeCab+"' ORDER BY KODE"; 
            System.err.println("query SDH = "+query);
            ResultSet rs = state.executeQuery(query);
            String kodenya, namanya;
            while(rs.next()){
                kodenya = rs.getString(1);
                namanya = rs.getString(2);
                cmbSDH.addItem(kodenya+" : "+namanya);
            }
            cmbSDH.setSelectedItem("MTR1 : SUROSO");
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void tampilSDH(String kode){
        try{
            
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "SELECT KODE, NAMA, KDCAB FROM MPETUGAS WHERE KDCAB='"+kodeCab+"' and Kode = '"+kode+"' ORDER BY KODE"; 
            System.err.println("query SDH = "+query);
            ResultSet rs = state.executeQuery(query);
            String kodenya="", namanya="";
            while(rs.next()){
                kodenya = rs.getString(1);
                namanya = rs.getString(2);
            }
            cmbSDH.setSelectedItem(kodenya+" : "+namanya);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    public void clear(){
        generateNoBukti();
        txtLokasi.setText("");
        txtOutlet.setText("");
        txtCustomer.setText("");
        txtUP.setText("");
        txtTelp.setText(""); 
        cmbTeknisi1.setSelectedItem("Pilih Teknisi");
        cmbTeknisi2.setSelectedItem("Pilih Teknisi");
        cmbCabang.setSelectedItem("J01 : Jakarta");
        cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        cmbSDH.setSelectedItem("MTR1 : SUROSO");
        cmbTeknisi2.setSelectedItem("");
        txtKeyword.setText("");
        txtNav.setText("");
        cmbBulan.setSelectedItem(bulan);
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnUbah.setEnabled(false);
        btnCariCustomer.setEnabled(true);
        btnNavFirst.setEnabled(false);
        btnNavPrev.setEnabled(false);
        btnNavNext.setEnabled(false);
        btnNavLast.setEnabled(false);
        btnCariCustomer.requestFocus();
      
    }
    
    private void tampilPopCust(){
        popCustomer.setVisible(true);
        kdCust = popCustomer.getKode();
        nmCust = popCustomer.getNama();
        txtCustomer.setText(kdCust+" : "+nmCust);
        System.err.println(kdCust);
        btnSimpan.setEnabled(true);
        generateNoBukti();
    }
     
    
    public static String getNoBukti() {
      System.err.println("noBukti2 = "+noBukti);
        return noBukti;
    }
    
    public static String getCust() {
      System.err.println("cust2 = "+cust);
        return cust;
    }

    public static void setCust(String cust) {
        Reguler.cust = cust;
    }
    
    public List<String> BindList(){
        String datanya;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoBukti from HOrder where NoBukti like '%"+ thn +"%'order by TglBukti desc";
            ResultSet rs = state.executeQuery(query);
            List<String> list = new ArrayList<String>();
            while(rs.next()){
                datanya = rs.getString(1);
                
                list.add(datanya );
            }
            return list;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    private void cancel(){
        String noBuktinya = lblNoBukti.getText().toString().trim();
        if (JOptionPane.showConfirmDialog(null, "Yakin akan membatalkan project ini?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            status = "1";
            query.Edit(Kolom(), Data(), "HOrder", "NoBukti",noBuktinya );
            generateNoBukti();
                //int rowCount = tblModel.getRowCount();
                //for (int i = rowCount - 1; i >= 0; i--) {
                    ///tblModel.removeRow(i);
                //}
                tampilKode();
            clear();
            
        } else{
            status = "0";
            clear();
            generateNoBukti();
        }
    }
    
    private void Tabel(javax.swing.JTable tb, int lebar[]){
        tb.setAutoResizeMode(tb.AUTO_RESIZE_OFF);
        int kolom=tb.getColumnCount();
        for(int i = 0;i<kolom; i++){
            javax.swing.table.TableColumn tbc = tb.getColumnModel().getColumn(i);
            tbc.setPreferredWidth(lebar[i]);
            tb.setRowHeight(17);
        }
    }
    
    private javax.swing.table.DefaultTableModel getDefaultTabelModel(){
        return new javax.swing.table.DefaultTableModel(
        new Object[][] {},
        new String[] {"Cabang", "Nomor Bukti","Tanggal", "Nama Customer",  "Lokasi", "Outlet", "Mitra", "Teknisi1", "Teknisi2", "Admin",  "Status"}
        ){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false,false,false,false,false,false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel tblModel = getDefaultTabelModel();
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnKeluar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnCariCustomer = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cmbTeknisi2 = new javax.swing.JComboBox<>();
        cmbTeknisi1 = new javax.swing.JComboBox<>();
        cmbMitra = new javax.swing.JComboBox<>();
        cmbSDH = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtOutlet = new javax.swing.JTextField();
        txtLokasi = new javax.swing.JTextField();
        cmbCabang = new javax.swing.JComboBox<>();
        txtTelp = new javax.swing.JTextField();
        txtUP = new javax.swing.JTextField();
        txtCustomer = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNoBukti = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblTglBukti = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnBatal1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKode = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNavFirst = new javax.swing.JButton();
        btnNavPrev = new javax.swing.JButton();
        txtNav = new javax.swing.JTextField();
        btnNavNext = new javax.swing.JButton();
        btnNavLast = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cmbBulan = new javax.swing.JComboBox<>();
        txtTahun = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbCari = new javax.swing.JComboBox<>();
        txtKeyword = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setTitle("Reguler");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kemergency.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1000, 700));
        setPreferredSize(new java.awt.Dimension(1000, 689));

        jPanel1.setLayout(new java.awt.BorderLayout());

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/keluar.png"))); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.setOpaque(false);
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 36));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnCariCustomer.setForeground(new java.awt.Color(0, 153, 153));
        btnCariCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/cari.png"))); // NOI18N
        btnCariCustomer.setText("Cari Customer");
        btnCariCustomer.setMaximumSize(new java.awt.Dimension(90, 35));
        btnCariCustomer.setMinimumSize(new java.awt.Dimension(90, 35));
        btnCariCustomer.setNextFocusableComponent(txtUP);
        btnCariCustomer.setPreferredSize(new java.awt.Dimension(90, 35));
        btnCariCustomer.setSelected(true);
        btnCariCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariCustomerActionPerformed(evt);
            }
        });
        btnCariCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCariCustomerKeyPressed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setForeground(new java.awt.Color(204, 255, 255));
        jLabel10.setText("SDH");

        jLabel9.setForeground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Mitra");

        jLabel11.setForeground(new java.awt.Color(204, 255, 255));
        jLabel11.setText("Teknisi 1");

        jLabel19.setForeground(new java.awt.Color(204, 255, 255));
        jLabel19.setText("Teknisi 2");

        cmbTeknisi2.setMaximumSize(new java.awt.Dimension(33, 26));
        cmbTeknisi2.setNextFocusableComponent(btnSimpan);
        cmbTeknisi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTeknisi2ActionPerformed(evt);
            }
        });

        cmbTeknisi1.setMaximumSize(new java.awt.Dimension(33, 26));
        cmbTeknisi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTeknisi1ActionPerformed(evt);
            }
        });

        cmbMitra.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbMitra.setMinimumSize(new java.awt.Dimension(100, 26));
        cmbMitra.setPreferredSize(new java.awt.Dimension(100, 24));
        cmbMitra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMitraItemStateChanged(evt);
            }
        });
        cmbMitra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cmbMitraMouseEntered(evt);
            }
        });
        cmbMitra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMitraActionPerformed(evt);
            }
        });

        cmbSDH.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbSDH.setMinimumSize(new java.awt.Dimension(100, 26));
        cmbSDH.setNextFocusableComponent(cmbMitra);
        cmbSDH.setPreferredSize(new java.awt.Dimension(100, 24));
        cmbSDH.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSDHItemStateChanged(evt);
            }
        });
        cmbSDH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cmbSDHMouseEntered(evt);
            }
        });
        cmbSDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSDHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11))
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cmbTeknisi1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbSDH, javax.swing.GroupLayout.Alignment.LEADING, 0, 280, Short.MAX_VALUE)
                    .addComponent(cmbMitra, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTeknisi2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(cmbSDH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cmbTeknisi1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cmbTeknisi2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setForeground(new java.awt.Color(204, 255, 255));
        jLabel3.setText("Customer");

        jLabel12.setForeground(new java.awt.Color(204, 255, 255));
        jLabel12.setText("UP");

        jLabel13.setForeground(new java.awt.Color(204, 255, 255));
        jLabel13.setText("Telepon");

        jLabel4.setForeground(new java.awt.Color(204, 255, 255));
        jLabel4.setText("Cabang");

        jLabel7.setForeground(new java.awt.Color(204, 255, 255));
        jLabel7.setText("Lokasi");

        jLabel14.setForeground(new java.awt.Color(204, 255, 255));
        jLabel14.setText("Outlet");

        txtOutlet.setMaximumSize(new java.awt.Dimension(100, 26));
        txtOutlet.setMinimumSize(new java.awt.Dimension(100, 24));
        txtOutlet.setNextFocusableComponent(cmbSDH);
        txtOutlet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOutletKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOutletKeyTyped(evt);
            }
        });

        txtLokasi.setMaximumSize(new java.awt.Dimension(100, 26));
        txtLokasi.setMinimumSize(new java.awt.Dimension(100, 24));
        txtLokasi.setNextFocusableComponent(txtOutlet);

        cmbCabang.setMinimumSize(new java.awt.Dimension(33, 24));
        cmbCabang.setNextFocusableComponent(cmbMitra);
        cmbCabang.setPreferredSize(null);
        cmbCabang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCabangItemStateChanged(evt);
            }
        });
        cmbCabang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbCabangMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cmbCabangMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmbCabangMouseReleased(evt);
            }
        });
        cmbCabang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCabangActionPerformed(evt);
            }
        });

        txtTelp.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTelp.setMinimumSize(new java.awt.Dimension(100, 24));
        txtTelp.setNextFocusableComponent(cmbCabang);
        txtTelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelpActionPerformed(evt);
            }
        });
        txtTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelpKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelpKeyTyped(evt);
            }
        });

        txtUP.setMaximumSize(new java.awt.Dimension(100, 26));
        txtUP.setMinimumSize(new java.awt.Dimension(100, 24));
        txtUP.setNextFocusableComponent(txtTelp);
        txtUP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUPKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUPKeyTyped(evt);
            }
        });

        txtCustomer.setMaximumSize(new java.awt.Dimension(100, 26));
        txtCustomer.setMinimumSize(new java.awt.Dimension(100, 24));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(46, 46, 46))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtOutlet, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtLokasi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel12))
                            .addGap(33, 33, 33)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel7)
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOutlet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 31, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 102));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setForeground(new java.awt.Color(204, 255, 255));
        jLabel6.setText("Nomor Bukti :");

        lblNoBukti.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblNoBukti.setForeground(new java.awt.Color(204, 255, 255));
        lblNoBukti.setText("-");

        jLabel15.setForeground(new java.awt.Color(204, 255, 255));
        jLabel15.setText("Tanggal Bukti :");

        lblTglBukti.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblTglBukti.setForeground(new java.awt.Color(204, 255, 255));
        lblTglBukti.setText("-");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTglBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTglBukti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNoBukti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addGap(10, 10, 10))
        );

        jToolBar2.setBackground(new java.awt.Color(0, 102, 102));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnSimpan.setForeground(new java.awt.Color(0, 153, 153));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setEnabled(false);
        btnSimpan.setMaximumSize(new java.awt.Dimension(110, 36));
        btnSimpan.setNextFocusableComponent(btnBatal);
        btnSimpan.setPreferredSize(new java.awt.Dimension(110, 36));
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSimpanMouseEntered(evt);
            }
        });
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSimpan);

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/edit.png"))); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setEnabled(false);
        btnUbah.setPreferredSize(new java.awt.Dimension(100, 36));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        jToolBar2.add(btnUbah);

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/hapus.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setEnabled(false);
        btnBatal.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jToolBar2.add(btnBatal);

        btnBatal1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/clear.png"))); // NOI18N
        btnBatal1.setText("Clear");
        btnBatal1.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatal1ActionPerformed(evt);
            }
        });
        jToolBar2.add(btnBatal1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(135, 135, 135)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        tabelKode.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabelKode.setForeground(new java.awt.Color(0, 102, 102));
        tabelKode.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nomor", "Nomor Bukti", "Tanggal", "Admin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelKode.setCellSelectionEnabled(true);
        tabelKode.setNextFocusableComponent(txtLokasi);
        tabelKode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKodeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelKode);

        jLabel1.setBackground(new java.awt.Color(0, 153, 153));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reguler");
        jLabel1.setOpaque(true);

        jToolBar1.setBackground(new java.awt.Color(0, 102, 102));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnNavFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_first.png"))); // NOI18N
        btnNavFirst.setEnabled(false);
        btnNavFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavFirstActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNavFirst);

        btnNavPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_prev.png"))); // NOI18N
        btnNavPrev.setEnabled(false);
        btnNavPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavPrevActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNavPrev);

        txtNav.setForeground(new java.awt.Color(0, 102, 102));
        jToolBar1.add(txtNav);

        btnNavNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_next.png"))); // NOI18N
        btnNavNext.setEnabled(false);
        btnNavNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavNextActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNavNext);

        btnNavLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_last.png"))); // NOI18N
        btnNavLast.setEnabled(false);
        btnNavLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavLastActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNavLast);

        jLabel8.setForeground(new java.awt.Color(204, 255, 255));
        jLabel8.setText("Bulan");
        jToolBar1.add(jLabel8);

        cmbBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember", " " }));
        cmbBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBulanActionPerformed(evt);
            }
        });
        jToolBar1.add(cmbBulan);

        txtTahun.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtTahunInputMethodTextChanged(evt);
            }
        });
        txtTahun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTahunKeyTyped(evt);
            }
        });
        jToolBar1.add(txtTahun);

        jLabel5.setForeground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("Cari :");
        jToolBar1.add(jLabel5);

        cmbCari.setForeground(new java.awt.Color(0, 102, 102));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Bukti", "Customer", "Kode Cabang" }));
        cmbCari.setNextFocusableComponent(txtKeyword);
        jToolBar1.add(cmbCari);

        txtKeyword.setNextFocusableComponent(btnCari);
        txtKeyword.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtKeywordInputMethodTextChanged(evt);
            }
        });
        jToolBar1.add(txtKeyword);

        btnCari.setForeground(new java.awt.Color(0, 153, 153));
        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/cari.png"))); // NOI18N
        btnCari.setText("Cari");
        btnCari.setMaximumSize(new java.awt.Dimension(85, 35));
        btnCari.setMinimumSize(new java.awt.Dimension(85, 35));
        btnCari.setNextFocusableComponent(tabelKode);
        btnCari.setPreferredSize(new java.awt.Dimension(85, 35));
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCari);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRefresh);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(843, 843, 843))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    String kodeCust, namaCust;
    private void btnCariCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariCustomerActionPerformed
        
        tampilPopCust();
    }//GEN-LAST:event_btnCariCustomerActionPerformed
    
    String[] sim = new String[7];
    private void tabelKodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKodeMouseClicked
        if(evt.getClickCount()==1){
            getData();
            btnBatal.setEnabled(true);
            btnSimpan.setEnabled(false);
            btnUbah.setEnabled(true);
            btnCariCustomer.setEnabled(false);
            btnNavFirst.setEnabled(true);
            btnNavPrev.setEnabled(true);
            btnNavNext.setEnabled(true);
            btnNavLast.setEnabled(true);
        }
    }//GEN-LAST:event_tabelKodeMouseClicked

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        Object selectedItem = cmbCari.getSelectedItem();
        if (selectedItem != null){
            String selectedItemStr = selectedItem.toString();
            searchKode(selectedItemStr, txtKeyword.getText());
        }
        btnBatal.setEnabled(true);
        
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtKeywordInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtKeywordInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_txtKeywordInputMethodTextChanged

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        cancel();
        
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtCustomer.getText().equals("null : null")){
            JOptionPane.showMessageDialog(null,"Pilih Customer","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtCustomer.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Customer tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtLokasi.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Lokasi tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtOutlet.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Outlet tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
                generateNoBukti();
                System.out.println("input detail");
                query = new Fungsi_Query();
                //Date tgl = Date.valueOf("20190101");
                //System.err.println("tglnya "+tgl);
                query.Input_DetilDate(Kolom(), Data(), "HOrder");
                generateNoBukti();
                //int rowCount = tblModel.getRowCount();
                //for (int i = rowCount - 1; i >= 0; i--) {
                    //tblModel.removeRow(i);
                //}
                tampilKode();
            }catch(Exception e){
                System.err.println("errorr = "+e.fillInStackTrace());
            }
        }
        clear();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void cmbMitraItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMitraItemStateChanged
       
        
    }//GEN-LAST:event_cmbMitraItemStateChanged

    private void cmbCabangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCabangActionPerformed
        String combonya = cmbCabang.getSelectedItem().toString().trim();
        if(combonya.equals("J01 : Jakarta")){
            cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        }else if(combonya.equals("J02 : Jawa Barat")){
            cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        }else if(combonya.equals("D01 : Denpasar")){
            cmbMitra.setSelectedItem("WS3 : WORKSHOP BALI");
        }else if(combonya.equals("S01 : Surabaya")){
            cmbMitra.setSelectedItem("WS2 : WORKSHOP SURABAYA");
        }else if(combonya.equals("S02 : Sumatera")){
            cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        }else if(combonya.equals("Y01 : Yogyakarta")){
            cmbMitra.setSelectedItem("WS4 : WORKSHOP YOGYAKARTA");
        }
        
    }//GEN-LAST:event_cmbCabangActionPerformed

    private void cmbMitraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMitraActionPerformed
        String kodemitra = cmbMitra.getSelectedItem().toString().trim();
        String kodemitranya = kodemitra.substring(0,2);
        if(kodemitranya.equals("WS")){
           
           cmbTeknisi1.setEnabled(true);
           cmbTeknisi2.setEnabled(true);
           
        }else{
           cmbTeknisi1.setSelectedItem("");
           cmbTeknisi1.setEnabled(false);
           cmbTeknisi2.setEnabled(false);
        }
    }//GEN-LAST:event_cmbMitraActionPerformed

    private void btnCariCustomerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCariCustomerKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilPopCust();
        }
    }//GEN-LAST:event_btnCariCustomerKeyPressed

    private void txtTelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelpKeyPressed
        
    }//GEN-LAST:event_txtTelpKeyPressed

    private void txtTelpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelpKeyTyped
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c)||(c == evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c == evt.VK_PERIOD))){
            getToolkit().beep ();
            evt.consume();
        }else if(txtTelp.getText().length() > 12){
            getToolkit().beep ();
            evt.consume ();
        }
    }//GEN-LAST:event_txtTelpKeyTyped

    private void txtUPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUPKeyPressed

    private void txtUPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUPKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUPKeyTyped

    private void txtOutletKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOutletKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOutletKeyPressed

    private void txtOutletKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOutletKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOutletKeyTyped

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        generateNoBukti();
        int rowCount = tblModel.getRowCount();
        //for (int i = rowCount - 1; i >= 0; i--) {
            //tblModel.removeRow(i);
        //}
        tampilKode();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void cmbCabangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCabangMouseClicked
        
    }//GEN-LAST:event_cmbCabangMouseClicked

    private void cmbCabangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCabangItemStateChanged
        String kodeCabang = cmbCabang.getSelectedItem().toString().trim();
        kodeCab = kodeCabang.substring(0, 3);
        System.out.println("kodeCab"+kodeCab);
        comboTeknisi();
        comboSDH();  
    }//GEN-LAST:event_cmbCabangItemStateChanged

    private void cmbCabangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCabangMouseExited
        //generateNoBukti();
    }//GEN-LAST:event_cmbCabangMouseExited

    private void btnSimpanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseEntered
        //generateNoBukti();
    }//GEN-LAST:event_btnSimpanMouseEntered

    private void cmbMitraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbMitraMouseEntered
        //generateNoBukti();
    }//GEN-LAST:event_cmbMitraMouseEntered

    private void cmbTeknisi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTeknisi1ActionPerformed
       
    
    }//GEN-LAST:event_cmbTeknisi1ActionPerformed

    private void cmbTeknisi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTeknisi2ActionPerformed
        
    }//GEN-LAST:event_cmbTeknisi2ActionPerformed

    private void cmbCabangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCabangMouseReleased
              
    }//GEN-LAST:event_cmbCabangMouseReleased

    private void cmbSDHItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSDHItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSDHItemStateChanged

    private void cmbSDHMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbSDHMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSDHMouseEntered

    private void cmbSDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSDHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSDHActionPerformed

    private void txtTelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelpActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtCustomer.getText().equals("null : null")){
            JOptionPane.showMessageDialog(null,"Pilih Customer","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtCustomer.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Customer tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtLokasi.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Lokasi tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtOutlet.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Outlet tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
            
              
                query = new Fungsi_Query();
                query.Edit(Kolom(), Data(), "HOrder", "NoBukti", lblNoBukti.getText());
                generateNoBukti();
                int rowCount = tblModel.getRowCount();
                //for (int i = rowCount - 1; i >= 0; i--) {
                    //tblModel.removeRow(i);
                //}
                tampilKode();
            }catch(Exception e){
                System.err.println("errorr = "+e.fillInStackTrace());
            }
        }
        clear();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnBatal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatal1ActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnBatal1ActionPerformed

    private void cmbBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBulanActionPerformed
        // TODO add your handling code here:
        tampilKode();
    }//GEN-LAST:event_cmbBulanActionPerformed

    private void txtTahunInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtTahunInputMethodTextChanged
         tampilKode();
    }//GEN-LAST:event_txtTahunInputMethodTextChanged

    private void txtTahunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTahunKeyTyped
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c)||(c == evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c == evt.VK_PERIOD))){
            getToolkit().beep ();
            evt.consume();
        }else if(txtTahun.getText().length() > 3){
            getToolkit().beep ();
            evt.consume ();
        }
        tampilKode();
    }//GEN-LAST:event_txtTahunKeyTyped

    private void btnNavFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavFirstActionPerformed
        // TODO add your handling code here:
        pos = 0;
        getDataNav(pos);
        System.err.println("pos first = "+pos);
        tabelKode.setRowSelectionInterval(0, 0);
    }//GEN-LAST:event_btnNavFirstActionPerformed

    private void btnNavPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavPrevActionPerformed

        getDataPrev();
    }//GEN-LAST:event_btnNavPrevActionPerformed

    private void btnNavNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavNextActionPerformed
        getDataNext();
    }//GEN-LAST:event_btnNavNextActionPerformed

    private void btnNavLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavLastActionPerformed
        pos = BindList().size() - 1;
            getDataNav(pos);
            System.err.println("pos last = "+pos);
            tabelKode.setRowSelectionInterval(pos, pos);
    }//GEN-LAST:event_btnNavLastActionPerformed

    int row = 0;
    String kdnya;
    public void getData() {
        row = tabelKode.getSelectedRow();
        System.err.println("row = "+row);
        kdnya = tblModel.getValueAt(row, 1).toString().trim();
        lblNoBukti.setText(kdnya);
        txtNav.setText(kdnya);
        System.err.println("kodenya = "+kdnya);
        tampil(kdnya);
    }
    
    String kd;
    int pos;
    private void getDataNav(int row){
        kd = tblModel.getValueAt(row, 1).toString().trim();
        txtNav.setText(kd);
        tampil(kd);
    }
    
    public void tampil(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoBukti, Tglbukti, KdCust, NmCust, UP, Telp, KdCab, Lokasi, Outlet, KdSDH, KdMitra, KdTeknisi, KdTeknisi2 from HOrder where NoBukti = '" + kode + "'"; 
            System.err.println("query tampil = "+query);
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                lblNoBukti.setText(rs.getString(1));
                //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");       
                lblTglBukti.setText(rs.getString(2));
                txtCustomer.setText(rs.getString(3)+" : "+rs.getString(4));
                txtUP.setText(rs.getString(5));
                txtTelp.setText(rs.getString(6));
                tampilCabang(rs.getString(7));
                txtLokasi.setText(rs.getString(8));
                txtOutlet.setText(rs.getString(9));
                tampilSDH(rs.getString(10));
                tampilMitra(rs.getString(11));
                tampilTeknisi1(rs.getString(12));
                tampilTeknisi2(rs.getString(13));
                
                
              
            }
            
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex.getStackTrace());
            ex.printStackTrace();
	}
    }
    
    private void getDataPrev(){
        
        row--;
        if(row > 0){
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya); 
            tabelKode.setRowSelectionInterval(row, row);
        }else{
            row = 0;
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya);
            JOptionPane.showMessageDialog(null, "Data Pertama");
            tabelKode.setRowSelectionInterval(row, row);
         }
        
    }
    
    private void getDataNext(){
        
        row++;
        if(row < BindList().size()){
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya); 
            tabelKode.setRowSelectionInterval(row, row);
        }else{
            row = BindList().size() - 1;
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            tabelKode.setRowSelectionInterval(row,row);
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya);
            JOptionPane.showMessageDialog(null, "Data Terakhir");
         }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnBatal1;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCariCustomer;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnNavFirst;
    private javax.swing.JButton btnNavLast;
    private javax.swing.JButton btnNavNext;
    private javax.swing.JButton btnNavPrev;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbBulan;
    private javax.swing.JComboBox<String> cmbCabang;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbMitra;
    private javax.swing.JComboBox<String> cmbSDH;
    private javax.swing.JComboBox<String> cmbTeknisi1;
    private javax.swing.JComboBox<String> cmbTeknisi2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblNoBukti;
    private javax.swing.JLabel lblTglBukti;
    private javax.swing.JTable tabelKode;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtLokasi;
    private javax.swing.JTextField txtNav;
    private javax.swing.JTextField txtOutlet;
    private javax.swing.JTextField txtTahun;
    private javax.swing.JTextField txtTelp;
    private javax.swing.JTextField txtUP;
    // End of variables declaration//GEN-END:variables
}
