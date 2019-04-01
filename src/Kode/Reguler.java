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
    
    String[] kolom  = new String[15];
    String[] isi    = new String[15];
    
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
        return kolom;
    }
    
    
    public String[] Data(){
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
             if(!tek2.isEmpty()){
                teknis2 = tek2.substring(0, tek2.indexOf(" : "));
             }
         }else{
             teknis1 = "Blank";
             teknis2 = "Blank";
         }
       
        isi[0] = lblNoBukti.getText();
        isi[1] = datetime("dd/MMM/yyyy HH:mm:ss");
        isi[2] = kdCust;
        isi[3] = nmCust;
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
        isi[14] = status;
  
        
        
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
        Tabel(tabelKode, new int[]{150,100,100,100,100,100,100,100,100,100,100});
        setCellsAlignment();
        //sortTable();
        tampilKode();
        
        
        comboCabang();
        kodeCab = "J01";
        comboMitra();
        //comboSDH();
        //cmbTeknisi1.removeAllItems();
        //cmbTeknisi2.removeAllItems();
        comboTeknisi();
        

        btnCariCustomer.getFocusListeners();
        getCurrentDate();
     ;
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
    
    public void tampilKode(){
        
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoBukti, TglBukti, NmCust, KdCab, Lokasi, Outlet, KdMitra, KdTeknisi, KdTeknisi2, PIC, Cancel from HOrder order by TglBukti desc"; 
            System.err.println("query tampil = "+query);
            ResultSet rs = state.executeQuery(query);
            
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
            String query = "Select NoBukti, TglBukti, NmCust, KdCab, Lokasi, Outlet, KdMitra, KdTeknisi, KdTeknisi2, PIC, Cancel from HOrder where "+ namakolom +" like '%"+ key +"%'";
            System.err.println("search "+query);
            ResultSet rs = state.executeQuery(query);
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
        cmbCabang.setSelectedItem("J01 : Jakarta");
        cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        cmbSDH.setSelectedItem("MTR1 : SUROSO");
        cmbTeknisi2.setSelectedItem("");
        txtKeyword.setText("");
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnUbah.setEnabled(false);
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
    
    
    
    private void cancel(){
        String noBuktinya = lblNoBukti.getText().toString().trim();
        if (JOptionPane.showConfirmDialog(null, "Yakin akan membatalkan project ini?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            status = "1";
            query.Edit(Kolom(), Data(), "HOrder", "NoBukti",noBuktinya );
            generateNoBukti();
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
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
        new String[] {"Nomor Bukti","Tanggal", "Nama Customer", "Cabang",  "Lokasi", "Outlet", "Mitra", "Teknisi1", "Teknisi2", "Admin",  "Status"}
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
        jLabel3 = new javax.swing.JLabel();
        btnCariCustomer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbCabang = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cmbMitra = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        txtTelp = new javax.swing.JTextField();
        lblNoBukti = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtUP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtLokasi = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtOutlet = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbTeknisi1 = new javax.swing.JComboBox<>();
        cmbTeknisi2 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbSDH = new javax.swing.JComboBox<>();
        btnUbah = new javax.swing.JButton();
        btnBatal1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKode = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtKeyword = new javax.swing.JTextField();
        cmbCari = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btnCari = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 153, 153));
        setTitle("Project");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kproject.png"))); // NOI18N

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

        jLabel3.setForeground(new java.awt.Color(204, 255, 255));
        jLabel3.setText("Customer");

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

        jLabel4.setForeground(new java.awt.Color(204, 255, 255));
        jLabel4.setText("Cabang");

        jLabel6.setForeground(new java.awt.Color(204, 255, 255));
        jLabel6.setText("Nomor Bukti :");

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

        jLabel9.setForeground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Mitra");

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

        jLabel12.setForeground(new java.awt.Color(204, 255, 255));
        jLabel12.setText("UP");

        txtCustomer.setMaximumSize(new java.awt.Dimension(100, 26));
        txtCustomer.setMinimumSize(new java.awt.Dimension(100, 24));

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

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/hapus.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setEnabled(false);
        btnBatal.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
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

        lblNoBukti.setForeground(new java.awt.Color(204, 255, 255));
        lblNoBukti.setText("-");

        jLabel13.setForeground(new java.awt.Color(204, 255, 255));
        jLabel13.setText("Telepon");

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

        jLabel7.setForeground(new java.awt.Color(204, 255, 255));
        jLabel7.setText("Lokasi");

        txtLokasi.setMaximumSize(new java.awt.Dimension(100, 26));
        txtLokasi.setMinimumSize(new java.awt.Dimension(100, 24));
        txtLokasi.setNextFocusableComponent(txtOutlet);

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

        jLabel11.setForeground(new java.awt.Color(204, 255, 255));
        jLabel11.setText("Teknisi 1");

        cmbTeknisi1.setMaximumSize(new java.awt.Dimension(33, 26));
        cmbTeknisi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTeknisi1ActionPerformed(evt);
            }
        });

        cmbTeknisi2.setMaximumSize(new java.awt.Dimension(33, 26));
        cmbTeknisi2.setNextFocusableComponent(btnSimpan);
        cmbTeknisi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTeknisi2ActionPerformed(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(204, 255, 255));
        jLabel19.setText("Teknisi 2");

        jLabel10.setForeground(new java.awt.Color(204, 255, 255));
        jLabel10.setText("SDH");

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

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/edit.png"))); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setEnabled(false);
        btnUbah.setPreferredSize(new java.awt.Dimension(100, 36));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnBatal1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnBatal1.setText("Clear");
        btnBatal1.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatal1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addGap(46, 46, 46))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txtOutlet, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtLokasi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel12))
                                    .addGap(33, 33, 33)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel7)
                            .addComponent(jLabel14)
                            .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cmbTeknisi2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(cmbTeknisi1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel9)
                                                    .addComponent(jLabel10))
                                                .addGap(40, 40, 40)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cmbSDH, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(157, 157, 157)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBatal1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel10)))
                            .addComponent(cmbSDH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)
                                .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbTeknisi1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOutlet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(cmbTeknisi2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
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
        tabelKode.setNextFocusableComponent(txtLokasi);
        tabelKode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKodeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelKode);

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtKeyword.setNextFocusableComponent(btnCari);
        txtKeyword.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtKeywordInputMethodTextChanged(evt);
            }
        });

        cmbCari.setForeground(new java.awt.Color(0, 102, 102));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Bukti", "Customer", "Kode Cabang" }));
        cmbCari.setNextFocusableComponent(txtKeyword);

        jLabel5.setForeground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("Cari :");

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

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefresh)
                .addGap(61, 61, 61))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh))
                .addGap(10, 10, 10))
        );

        jLabel1.setBackground(new java.awt.Color(0, 153, 153));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reguler");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(480, 480, 480)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnKeluar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    String kodeCust, namaCust;
    private void btnCariCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariCustomerActionPerformed
        
        tampilPopCust();
    }//GEN-LAST:event_btnCariCustomerActionPerformed
    
    String[] sim = new String[7];
    private void tabelKodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKodeMouseClicked
        if(evt.getClickCount()==2){
            getData();
            btnBatal.setEnabled(true);
            btnSimpan.setEnabled(false);
            btnUbah.setEnabled(true);
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
                query.Input_Detil(Kolom(), Data(), "HOrder");
                generateNoBukti();
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
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
        for (int i = rowCount - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
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
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
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

    int row = 0;
    String kdnya;
    public void getData() {
        row = tabelKode.getSelectedRow();
        System.err.println("row = "+row);
        kdnya = tblModel.getValueAt(row, 0).toString().trim();
        lblNoBukti.setText(kdnya);
        System.err.println("kodenya = "+kdnya);
        tampil(kdnya);
    }
    
    public void tampil(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoBukti, KdCust, NmCust, UP, Telp, KdCab, Lokasi, Outlet, KdSDH, KdMitra, KdTeknisi, KdTeknisi2 from HOrder where NoBukti = '" + kode + "'"; 
            System.err.println("query tampil = "+query);
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                lblNoBukti.setText(rs.getString(1));
                txtCustomer.setText(rs.getString(2)+" : "+rs.getString(3));
                txtUP.setText(rs.getString(4));
                txtTelp.setText(rs.getString(5));
                tampilCabang(rs.getString(6));
                txtLokasi.setText(rs.getString(7));
                txtOutlet.setText(rs.getString(8));
                tampilSDH(rs.getString(9));
                tampilMitra(rs.getString(10));
                tampilTeknisi1(rs.getString(11));
                tampilTeknisi2(rs.getString(12));
                
                
              
            }
            
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex.getStackTrace());
            ex.printStackTrace();
	}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnBatal1;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCariCustomer;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNoBukti;
    private javax.swing.JTable tabelKode;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtLokasi;
    private javax.swing.JTextField txtOutlet;
    private javax.swing.JTextField txtTelp;
    private javax.swing.JTextField txtUP;
    // End of variables declaration//GEN-END:variables
}
