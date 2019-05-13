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
import Popup.PopItemProject;
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
public class Project extends javax.swing.JInternalFrame {
    int nomor;
    JLabel jl;
    
    Database dbsetting;
    String driver, database, user, pass;
    String no, kode, nama, jenis = "PRJ", status = "Aktif", date, tahun, bulan;
    String kdCust, nmCust, telp, kdCab, nmCab, kdAdm, nmAdm;
    Boolean dikunjungi;
    static String noBukti,cust,mitra, wilayah, outlet;
    
    String data[] = new String[12];
    String dataItem[] = new String[6];
    Fungsi_Query query = new Fungsi_Query();
    String dataItemTampil[] = new String[6];
    
    String[] kolom  = new String[18];
    String[] isi    = new String[18];
    int jumlah = 0;
    PopCustomer popCustomer = new PopCustomer(new javax.swing.JFrame(), true);
    PopItemProject popItemProject = new PopItemProject(new javax.swing.JFrame(), true);
    
    public String[] Kolom(){
        kolom[0] = "No_Bukti";
        kolom[1] = "Tanggal";
        kolom[2] = "Nm_Project";
        kolom[3] = "Kd_Cust";
        kolom[4] = "Nm_Cust";
        kolom[5] = "UP";
        kolom[6] = "Telp_Cust";
        kolom[7] = "Kelompok";
        kolom[8] = "No_Ref";
        kolom[9] = "Tgl_Ref";
        kolom[10] = "Kd_Cabang";
        kolom[11] = "Nm_Cabang";
        kolom[12] = "Kd_Mitra";
        kolom[13] = "Nm_Mitra";
        kolom[14] = "Quantity";
        kolom[15] = "Kd_Admin";
        kolom[16] = "Nm_Admin";
        kolom[17] = "Statusnya";
        return kolom;
    }
    
    
    public String[] Data(){
        String cust = txtCustomer.getText();
        String kode = cust.substring(0, cust.indexOf(" : "));
        String nama = cust.substring(cust.indexOf(" : "));
        String namafix = nama.substring(3);
        
        String strCab = String.valueOf(cmbCabang.getSelectedItem());
        String strMit = String.valueOf(cmbMitra.getSelectedItem());
        System.out.println("Mitra = "+cmbCabang.getSelectedItem());
        
        isi[0] = lblNoBukti.getText();
        isi[1] = lblTglBukti.getText();
        isi[2] = txtNamaProject.getText();
        isi[3] = kode;
        isi[4] = namafix;
        isi[5] = txtUP.getText();
        isi[6] = txtTelp.getText();
        isi[7] = String.valueOf(cmbKelompok.getSelectedItem());
        isi[8] = txtNoRef.getText();
        isi[9] = dateRef("dd MMM yyyy");
        isi[10] = strCab.substring(0, strCab.indexOf(" : "));
        isi[11] = strCab.substring(5);
        isi[12] = strMit.substring(0, strMit.indexOf(" : "));
        isi[13] = strMit.substring(strMit.lastIndexOf(" : ")+2);
        isi[14] = txtQuantity.getText();
        isi[15] = MenuUtama.getId();
        isi[16] = MenuUtama.getNama();
        isi[17] = status;
        
        return isi;
    }

    /**
     * Creates new form Setting
     */
    public Project() {
        initComponents();
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");

        tabelKode.setModel(tblModel);
        tabelKode.setAutoCreateRowSorter(true);
        Tabel(tabelKode, new int[]{150,100,100,100,100,100,100,100,100,100,100,100});
        tabelITem.setModel(tblModelItem);
        TabelItem(tabelITem, new int[]{50,100,100,100,70,70});
        //sortTable();
        getCurrentDate();
        txtTahun.setText(tahun);
        tampilKode();
        setCellsAlignment();
        comboCabang();
        comboMitra();
        generateNoBukti();
        btnCariCustomer.getFocusListeners();
        tglRef.setDate(Date.valueOf(LocalDate.now()));
        tabelKode.getTableHeader().setEnabled(false);
        tabelITem.getTableHeader().setEnabled(false);
        lblTglBukti.setText(datetime("dd MMM yyyy"));
    }
    
    private String datetime(String format){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
	LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }
    
    private String dateRef(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
	String date = sdf.format(tglRef.getDate());
        System.err.println("date "+date);
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
            String query = "SELECT MAX(SUBSTRING(No_Bukti, 20, 5) ) FROM Hproject"; 
            System.err.println(query);
            ResultSet rs = state.executeQuery(query);
            int max = 0;
            while(rs.next()){
                max = rs.getInt(1);
                  System.err.println(max);
            }
            max = max + 1;
            no = String.format("%04d", max);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
        System.out.println("nomor "+no);
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
        String nomor = String.valueOf(autonumber());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        lblNoBukti.setText(kdCust+"-"+date+nomor);           
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
        int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
        
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from Hproject where No_Bukti like '%"+ thn +"%'order by Kd_Cabang asc, Tanggal desc"; 
            ResultSet rs = state.executeQuery(query);
            System.err.println("query tampil project = "+query);
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(5);
                data[4] = rs.getString(8);
                data[5] = rs.getString(9);
                data[6] = rs.getString(10);
                data[7] = rs.getString(12);
                data[8] = rs.getString(14);
                data[9] = rs.getString(15);
                data[10] = rs.getString(17);
                data[11] = rs.getString(18);
                System.err.println("data 0 = "+rs.getString(1));
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
            namakolom = "No_Bukti";
        }else if(cmb.equals("Customer")){
            namakolom = "Nm_Cust";
        }else{
            namakolom = "Tanggal";
        }
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            tblModel.setRowCount(0);
            String query = "Select * from Hproject where "+ namakolom +" like '%"+ key +"%'"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(5);
                data[4] = rs.getString(8);
                data[5] = rs.getString(9);
                data[6] = rs.getString(10);
                data[7] = rs.getString(12);
                data[8] = rs.getString(14);
                data[9] = rs.getString(15);
                data[10] = rs.getString(17);
                data[11] = rs.getString(18);
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
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                kdCab = rs.getString(1);
                nmCab = rs.getString(2);
                cmbCabang.addItem(kdCab+" : "+nmCab);
            }
            cmbCabang.setSelectedItem("J01 : Jakarta");
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
            String query = "Select * from MMitra"; 
            ResultSet rs = state.executeQuery(query);
            String kodenya, namanya;
            while(rs.next()){
                kodenya = rs.getString(1);
                namanya = rs.getString(3);
                cmbMitra.addItem(kodenya+" : "+namanya);
            }
            cmbMitra.addItem("Blank");
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
            String query = "Select * from MMitra where Kode = '"+kode+"'"; 
            ResultSet rs = state.executeQuery(query);
            String kodenya = "", namanya = "";
            while(rs.next()){
                kodenya = rs.getString(1);
                namanya = rs.getString(3);
                //cmbMitra.addItem(kodenya+" : "+namanya);
            }
            //cmbMitra.addItem("Blank");
            cmbMitra.setSelectedItem(kodenya+" : "+namanya);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
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
    
    public void clear(){
        generateNoBukti();
        txtNamaProject.setText("");
        cmbKelompok.setSelectedItem("Reparasi");
        txtCustomer.setText("");
        txtUP.setText("");
        txtTelp.setText("");
        txtNoRef.setText("");
        //tglRef.setDate(Date.valueOf(datetime("dd-MMM-yyyy")));
        tglRef.setDate(Date.valueOf(LocalDate.now()));
        txtQuantity.setText("0");
        cmbCabang.setSelectedItem("J01 : Jakarta");
        cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        txtKeyword.setText("");
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
        btnCariCustomer.requestFocus();
        bersihTabel();
        
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
        Project.cust = cust;
    }
    
    private void bersihTabel(){
        int rowCount = tblModelItem.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                tblModelItem.removeRow(i);
        }
    }
    
    private void cancel(){
        String noBuktinya = lblNoBukti.getText().toString().trim();
        if (JOptionPane.showConfirmDialog(null, "Yakin akan membatalkan project ini?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            status = "Batal";
            query.Edit(Kolom(), Data(), "Hproject", "No_Bukti",noBuktinya );
            generateNoBukti();
                
                tampilKode();
            clear();
            
        } else{
            status = "Aktif";
            clear();
            generateNoBukti();
        }
    }
    
    int row = 0;
    String kdnya;
    private void getDataPrev(){
        
        row--;
        if(row > 0){
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya); 
            tabelKode.setRowSelectionInterval(row, row);
        }else{
            row = 0;
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
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
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya); 
            tabelKode.setRowSelectionInterval(row, row);
        }else{
            row = BindList().size() - 1;
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            tabelKode.setRowSelectionInterval(row,row);
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya);
            JOptionPane.showMessageDialog(null, "Data Terakhir");
         }
    }
    
    String kd;
    int pos;
    private void getDataNav(int row){
        kd = tblModel.getValueAt(row, 0).toString().trim();
        txtNav.setText(kd);
        tampil(kd);
    }
    
    public List<String> BindList(){
        String datanya;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoBukti from HProject where NoBukti like '%"+ thn +"%'order by TglBukti desc";
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
    
    private void updateItemProject(){
        String noseri, nobukti;
        int rowCount = tblModelItem.getRowCount();
        System.err.println("row "+rowCount);
        for (int i = rowCount - 1; i >= 0; i--) {
            noseri = tabelITem.getValueAt(i, 0).toString().trim();
            //nobukti = lblNoBukti.getText().toString().trim();
            //System.err.println("nobukti "+nobukti);
            query.EditSingle("NoBukti", lblNoBukti.getText(), "ItemProject", "NoUrut",noseri);
        }
        //for(int i = 0; i < tabelITem.getRowCount();i++){    
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
        new String[] {"Nomor Bukti","Tanggal","Nama Project", "Nama Customer", "Kelompok", "No Ref", "Tgl Ref", "Cabang",  "Mitra", "Quantity", "Admin",  "Status"}
        ){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false,false,false,false,false,false,false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }
    private javax.swing.table.DefaultTableModel tblModel = getDefaultTabelModel();
    
    
    private javax.swing.table.DefaultTableModel getDefaultTabelModelItem(){
        return new javax.swing.table.DefaultTableModel(
        new Object[][] {},
        new String[] {"Nomor","Nomor Seri", "Produk", "Merk", "Model", "Size"}
        ){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel tblModelItem = getDefaultTabelModelItem();
   
    private void TabelItem(javax.swing.JTable tb, int lebar[]){
        tb.setAutoResizeMode(tb.AUTO_RESIZE_OFF);
        int kolom=tb.getColumnCount();
        for(int i = 0;i<kolom; i++){
            javax.swing.table.TableColumn tbc = tb.getColumnModel().getColumn(i);
            tbc.setPreferredWidth(lebar[i]);
            tb.setRowHeight(17);
        }
    }
    

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
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtNamaProject = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtUP = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbMitra = new javax.swing.JComboBox<>();
        cmbCabang = new javax.swing.JComboBox<>();
        txtTelp = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cmbKelompok = new javax.swing.JComboBox<>();
        btnCariCustomer = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        tglRef = new com.toedter.calendar.JDateChooser();
        txtNoRef = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNoBukti = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblTglBukti = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnBatal1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelITem = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKode = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
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
        jButton1 = new javax.swing.JButton();
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

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setForeground(new java.awt.Color(204, 255, 255));
        jLabel7.setText("Nama Project");

        txtNamaProject.setMaximumSize(new java.awt.Dimension(100, 26));
        txtNamaProject.setMinimumSize(new java.awt.Dimension(100, 24));

        jLabel3.setForeground(new java.awt.Color(204, 255, 255));
        jLabel3.setText("Customer");

        txtCustomer.setMaximumSize(new java.awt.Dimension(100, 26));
        txtCustomer.setMinimumSize(new java.awt.Dimension(100, 24));

        jLabel12.setForeground(new java.awt.Color(204, 255, 255));
        jLabel12.setText("UP");

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

        jLabel13.setForeground(new java.awt.Color(204, 255, 255));
        jLabel13.setText("Telepon");

        jLabel4.setForeground(new java.awt.Color(204, 255, 255));
        jLabel4.setText("Cabang");

        jLabel9.setForeground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Mitra");

        cmbMitra.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbMitra.setMinimumSize(new java.awt.Dimension(100, 26));
        cmbMitra.setNextFocusableComponent(txtNoRef);
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
        });
        cmbCabang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCabangActionPerformed(evt);
            }
        });

        txtTelp.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTelp.setMinimumSize(new java.awt.Dimension(100, 24));
        txtTelp.setNextFocusableComponent(cmbCabang);
        txtTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelpKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelpKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaProject, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTelp, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .addComponent(txtUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNamaProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setForeground(new java.awt.Color(204, 255, 255));
        jLabel14.setText("Kelompok");

        cmbKelompok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reparasi", "Pengecatan", "Sparepart" }));

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

        jLabel15.setForeground(new java.awt.Color(204, 255, 255));
        jLabel15.setText("No Ref");

        jLabel16.setForeground(new java.awt.Color(204, 255, 255));
        jLabel16.setText("Tgl Ref");

        tglRef.setNextFocusableComponent(txtQuantity);
        tglRef.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tglRefMouseReleased(evt);
            }
        });

        txtNoRef.setMaximumSize(new java.awt.Dimension(100, 26));
        txtNoRef.setMinimumSize(new java.awt.Dimension(100, 24));
        txtNoRef.setNextFocusableComponent(tglRef);
        txtNoRef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRefKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoRefKeyTyped(evt);
            }
        });

        btnTambah.setForeground(new java.awt.Color(0, 153, 153));
        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/tambah.png"))); // NOI18N
        btnTambah.setText("Tambah Item");
        btnTambah.setMaximumSize(new java.awt.Dimension(90, 35));
        btnTambah.setMinimumSize(new java.awt.Dimension(90, 35));
        btnTambah.setNextFocusableComponent(txtUP);
        btnTambah.setPreferredSize(new java.awt.Dimension(90, 35));
        btnTambah.setSelected(true);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        btnTambah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTambahKeyPressed(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(204, 255, 255));
        jLabel17.setText("Quantity");

        txtQuantity.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtQuantity.setForeground(new java.awt.Color(255, 204, 102));
        txtQuantity.setText(" 0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(34, 34, 34)
                        .addComponent(cmbKelompok, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglRef, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNoRef, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKelompok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tglRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(9, 9, 9))
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 102));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setForeground(new java.awt.Color(204, 255, 255));
        jLabel6.setText("Nomor Bukti :");

        lblNoBukti.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblNoBukti.setForeground(new java.awt.Color(204, 255, 255));
        lblNoBukti.setText("-");

        jLabel18.setForeground(new java.awt.Color(204, 255, 255));
        jLabel18.setText("Tanggal Bukti :");

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
                .addComponent(lblNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel18)
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
                        .addComponent(jLabel18))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNoBukti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addGap(10, 10, 10))
        );

        jToolBar1.setBackground(new java.awt.Color(0, 102, 102));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

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
        jToolBar1.add(btnSimpan);

        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/edit.png"))); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setEnabled(false);
        btnUbah.setPreferredSize(new java.awt.Dimension(100, 36));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        jToolBar1.add(btnUbah);

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setEnabled(false);
        btnBatal.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBatal);

        btnBatal1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/clear.png"))); // NOI18N
        btnBatal1.setText("Clear");
        btnBatal1.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatal1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBatal1);

        tabelITem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabelITem.setForeground(new java.awt.Color(0, 102, 102));
        tabelITem.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelITem.setNextFocusableComponent(txtNamaProject);
        tabelITem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelITemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelITem);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addContainerGap())
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
        tabelKode.setNextFocusableComponent(txtNamaProject);
        tabelKode.setVerifyInputWhenFocusTarget(false);
        tabelKode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKodeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelKode);

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jToolBar2.setBackground(new java.awt.Color(0, 102, 102));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnNavFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_first.png"))); // NOI18N
        btnNavFirst.setEnabled(false);
        btnNavFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavFirstActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNavFirst);

        btnNavPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_prev.png"))); // NOI18N
        btnNavPrev.setEnabled(false);
        btnNavPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavPrevActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNavPrev);

        txtNav.setForeground(new java.awt.Color(0, 102, 102));
        jToolBar2.add(txtNav);

        btnNavNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_next.png"))); // NOI18N
        btnNavNext.setEnabled(false);
        btnNavNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavNextActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNavNext);

        btnNavLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_last.png"))); // NOI18N
        btnNavLast.setEnabled(false);
        btnNavLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavLastActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNavLast);

        jLabel8.setForeground(new java.awt.Color(204, 255, 255));
        jLabel8.setText("Bulan");
        jToolBar2.add(jLabel8);

        cmbBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember", " " }));
        cmbBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBulanActionPerformed(evt);
            }
        });
        jToolBar2.add(cmbBulan);

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
        jToolBar2.add(txtTahun);

        jLabel5.setForeground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("Cari :");
        jToolBar2.add(jLabel5);

        cmbCari.setForeground(new java.awt.Color(0, 102, 102));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Bukti", "Customer", "Tanggal" }));
        cmbCari.setNextFocusableComponent(txtKeyword);
        jToolBar2.add(cmbCari);

        txtKeyword.setNextFocusableComponent(btnCari);
        txtKeyword.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtKeywordInputMethodTextChanged(evt);
            }
        });
        jToolBar2.add(txtKeyword);

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
        jToolBar2.add(btnCari);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setBackground(new java.awt.Color(0, 153, 153));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Project");
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
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
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
        if(evt.getClickCount()==2){
            getData();
            getItem();
            btnBatal.setEnabled(true);
            btnUbah.setEnabled(false);
            btnSimpan.setEnabled(false);
            btnCariCustomer.setEnabled(false);
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
        }else if(txtNamaProject.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Nama Project tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNoRef.getText().equals("")){
            JOptionPane.showMessageDialog(null,"No Referensi tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(tglRef.getDate().equals("")){
            JOptionPane.showMessageDialog(null,"Pilih tanggal","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtQuantity.getText().equals("0")){
            JOptionPane.showMessageDialog(null,"Quantity tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
                generateNoBukti();
                query.Input_Detil(Kolom(), Data(), "Hproject");
                String getStatusInput = query.getStatusInput();
                if(getStatusInput.equals("Sukses")){
                    updateItemProject();
                    tampilKode();
                }else{
                    JOptionPane.showMessageDialog(null,"Simpan Gagal","Error", JOptionPane.INFORMATION_MESSAGE);
                }
                //generateNoBukti();
                
//                int rowCount = tblModel.getRowCount();
//                for (int i = rowCount - 1; i >= 0; i--) {
//                    tblModel.removeRow(i);
//                }
                
                
            }catch(Exception e){
                System.err.println("errorr"+e.getMessage());
            }
        }
        clear();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void cmbMitraItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMitraItemStateChanged
       
        
    }//GEN-LAST:event_cmbMitraItemStateChanged

    private void cmbCabangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCabangActionPerformed
        //generateNoBukti();
    }//GEN-LAST:event_cmbCabangActionPerformed

    private void cmbMitraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMitraActionPerformed
        
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

    private void txtNoRefKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRefKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoRefKeyPressed

    private void txtNoRefKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRefKeyTyped
    
    }//GEN-LAST:event_txtNoRefKeyTyped

    private void tglRefMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tglRefMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tglRefMouseReleased

    private void cmbCabangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCabangMouseClicked
        
    }//GEN-LAST:event_cmbCabangMouseClicked

    private void cmbCabangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCabangItemStateChanged
        //generateNoBukti();
    }//GEN-LAST:event_cmbCabangItemStateChanged

    private void cmbCabangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbCabangMouseExited
        //generateNoBukti();
    }//GEN-LAST:event_cmbCabangMouseExited

    private void btnSimpanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseEntered
        generateNoBukti();
    }//GEN-LAST:event_btnSimpanMouseEntered

    private void cmbMitraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbMitraMouseEntered
        generateNoBukti();
    }//GEN-LAST:event_cmbMitraMouseEntered

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        generateNoBukti();
//        int rowCount = tblModel.getRowCount();
//        for (int i = rowCount - 1; i >= 0; i--) {
//            tblModel.removeRow(i);
//        }
        tampilKode();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        popItemProject.setVisible(true);
        noBukti = lblNoBukti.getText();
        System.out.println("get no seri project "+popItemProject.getNoseri());
                int nokod = 0;
                if(popItemProject.getNo()== null){
                    System.err.println("nokod kosong");
                }else{
                nokod = Integer.parseInt(popItemProject.getNo());
                }
                String nokode = String.format("%02d", nokod);
                String noserinya = popItemProject.getNoseri();
                System.err.println("noserinya "+noserinya);
                List<String> list = new ArrayList<String>();
                dataItem[0] = nokode;
                dataItem[1] = popItemProject.getNoseri();
                dataItem[2] = popItemProject.getProduk();
                dataItem[3] = popItemProject.getMerk();
                dataItem[4] = popItemProject.getModel();
                dataItem[5] = popItemProject.getSizenya();
                for(int i = 0; i < tabelITem.getRowCount();i++){
                    if(tabelITem.getRowCount() > 0){
                        list.add(tabelITem.getValueAt(i, 1).toString().trim());
                        System.err.println("list "+list.toString());
                    }else{
                        System.err.println("list kosong"+list.toString());
                    }  
                }
                
                if(list.contains(noserinya)){
                    JOptionPane.showMessageDialog(null,"Item sudah diinput","Error", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    tblModelItem.addRow(dataItem);
                    jumlah++;
                    txtQuantity.setText(String.valueOf(jumlah));
                }
               
        System.out.println("add row"+dataItem.toString());
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnTambahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTambahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambahKeyPressed

    private void tabelITemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelITemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelITemMouseClicked

    private void cmbBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBulanActionPerformed
        // TODO add your handling code here:
//        int rowCount = tblModel.getRowCount();
//                for (int i = rowCount - 1; i >= 0; i--) {
//                    tblModel.removeRow(i);
//        }
        tampilKode();
    }//GEN-LAST:event_cmbBulanActionPerformed

    private void txtTahunInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtTahunInputMethodTextChanged
//        int rowCount = tblModel.getRowCount();
//                for (int i = rowCount - 1; i >= 0; i--) {
//                    tblModel.removeRow(i);
//                }
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
//        int rowCount = tblModel.getRowCount();
//                for (int i = rowCount - 1; i >= 0; i--) {
//                    tblModel.removeRow(i);
//                }
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

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtCustomer.getText().equals("null : null")){
            JOptionPane.showMessageDialog(null,"Pilih Customer","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtQuantity.getText().equals("0")){
            JOptionPane.showMessageDialog(null,"Pilih Item","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNamaProject.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Nama  project tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNoRef.getText().equals("")){
            JOptionPane.showMessageDialog(null,"No Referensi tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{

                query = new Fungsi_Query();
                query.Edit(Kolom(), Data(), "HProject", "No_Bukti", lblNoBukti.getText());
                updateItemProject();
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

    
    String kodenya;
    public void getData() {
        row = tabelKode.getSelectedRow();
        System.err.println("row = "+row);
        kodenya = tblModel.getValueAt(row, 0).toString().trim();
        lblNoBukti.setText(kodenya);
        System.err.println("kodenya = "+kodenya);
        tampil(kodenya);
    }
    
    
    public void getItem(){
        
        String nobuk = lblNoBukti.getText().toString().trim();
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from ItemProject where NoBukti = '" + nobuk + "'"; 
            System.err.println("data 11215555   "+query);
            ResultSet rs = state.executeQuery(query);
            int rowCount = tblModelItem.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModelItem.removeRow(i);
                }
            while(rs.next()){
                
//                int nokod = 0;
//                popItemProject.getData();
//                if(popItemProject.getNo()== null){
//                    System.err.println("nokod kosong");
//                }else{
//                nokod = Integer.parseInt(popItemProject.getNo());
//                System.err.println("nokod "+nokod);
//                }
//                String nokode = String.format("%02d", nokod);
//                String noserinya = popItemProject.getNoseri();
//                System.err.println("noserinya "+noserinya);
                List<String> list = new ArrayList<String>();
                dataItemTampil[0] = rs.getString(1);
                dataItemTampil[1] = rs.getString(2);
                dataItemTampil[2] = rs.getString(3);
                dataItemTampil[3] = rs.getString(4);
                dataItemTampil[4] = rs.getString(5);
                dataItemTampil[5] = rs.getString(6);
                System.err.println("data 11215555"+rs.getString(2));
                for(int i = 0; i < tabelITem.getRowCount();i++){
                    if(tabelITem.getRowCount() > 0){
                        list.add(tabelITem.getValueAt(i, 1).toString().trim());
                        System.err.println("list "+list.toString());
                    }else{
                        System.err.println("list kosong"+list.toString());
                    }  
                }
                tblModelItem.addRow(dataItemTampil);
            }
            
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex.getStackTrace());
            ex.printStackTrace();
	}
    }
    
    public void tampil(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from Hproject where No_Bukti = '" + kode + "'"; 
            System.err.println("query tampil "+query);
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                lblNoBukti.setText(rs.getString(1));
                lblTglBukti.setText(rs.getString(2));
                txtNamaProject.setText(rs.getString(3));
                txtCustomer.setText(rs.getString(4)+" : "+rs.getString(5));
                txtUP.setText(rs.getString(6));
                txtTelp.setText(rs.getString(7));
                cmbKelompok.setSelectedItem(rs.getString(8));
                txtNoRef.setText(rs.getString(9));
                //java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s").parse(rs.getString(10));
                tglRef.setDate(Date.valueOf(rs.getString(10)));
                txtQuantity.setText(rs.getString(15));
                cmbCabang.setSelectedItem(rs.getString(11)+" :"+rs.getString(12));
                System.err.println("kode cabang "+rs.getString(11));
                cmbMitra.setSelectedItem(rs.getString(13)+" :"+rs.getString(14));
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
    private javax.swing.JButton btnNavFirst;
    private javax.swing.JButton btnNavLast;
    private javax.swing.JButton btnNavNext;
    private javax.swing.JButton btnNavPrev;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbBulan;
    private javax.swing.JComboBox<String> cmbCabang;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbKelompok;
    private javax.swing.JComboBox<String> cmbMitra;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblNoBukti;
    private javax.swing.JLabel lblTglBukti;
    private javax.swing.JTable tabelITem;
    private javax.swing.JTable tabelKode;
    private com.toedter.calendar.JDateChooser tglRef;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtNamaProject;
    private javax.swing.JTextField txtNav;
    private javax.swing.JTextField txtNoRef;
    private javax.swing.JLabel txtQuantity;
    private javax.swing.JTextField txtTahun;
    private javax.swing.JTextField txtTelp;
    private javax.swing.JTextField txtUP;
    // End of variables declaration//GEN-END:variables
}
