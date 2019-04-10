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
public class Maintenance extends javax.swing.JInternalFrame {
    int nomor;
    JLabel jl;
    
    Database dbsetting;
    String driver, database, user, pass;
    String no, kode, nama, jenis = "PRJ", status = "Aktif";
    String kdCust, nmCust, telp, kdCab, nmCab, kdAdm, nmAdm;
    Boolean dikunjungi;
    static String noBukti,cust,mitra, wilayah, outlet;
    
    String data[] = new String[12];
    Fungsi_Query query = new Fungsi_Query();
    
    String[] kolom  = new String[18];
    String[] isi    = new String[18];
    
    PopCustomer popCustomer = new PopCustomer(new javax.swing.JFrame(), true);
    
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
        String strCab = String.valueOf(cmbCabang.getSelectedItem());
        String strMit = String.valueOf(cmbMitra.getSelectedItem());
        System.out.println("Mitra = "+cmbCabang.getSelectedItem()); 
        
        
        isi[0] = lblNoBukti.getText();
        isi[1] = datetime("dd/MMM/yyyy HH:mm:ss");
        isi[2] = txtNamaProject.getText();
        isi[3] = kdCust;
        isi[4] = nmCust;
        isi[5] = txtUP.getText();
        isi[6] = txtTelp.getText();
        isi[7] = cmbKelompok.getSelectedItem().toString().trim();
        isi[8] = txtNoRef.getText();
        isi[9] = dateRef("dd-MMM-yyyy");
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
    public Maintenance() {
        initComponents();
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");

        tabelKode.setModel(tblModel);
        tabelKode.setAutoCreateRowSorter(true);
        Tabel(tabelKode, new int[]{150,100,100,100,100,100,100,100,100,100,100,100});
        sortTable();
        tampilKode();
        setCellsAlignment();
        comboCabang();
        comboMitra();
        generateNoBukti();
        btnCariCustomer.getFocusListeners();
        tglRef.setDate(Date.valueOf(LocalDate.now()));
        
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
            String query = "SELECT MAX(SUBSTRING(No_Bukti, 20, 5) ) FROM HMaintenance"; 
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        lblNoBukti.setText(kdCust+"-"+kdCab+"-"+jenis+"-"+date+nomor);           
    }
    
    public void tampilKode(){
        
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from HMaintenance"; 
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
            String query = "Select * from HMaintenance where "+ namakolom +" like '%"+ key +"%'"; 
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
        txtQuantity.setText("");
        cmbCabang.setSelectedItem("J01 : Jakarta");
        cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        txtKeyword.setText("");
        btnSimpan.setEnabled(false);
        btnBatal.setEnabled(false);
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
        Maintenance.cust = cust;
    }
    
    private void cancel(){
        String noBuktinya = lblNoBukti.getText().toString().trim();
        if (JOptionPane.showConfirmDialog(null, "Yakin akan membatalkan project ini?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            status = "Batal";
            query.Edit(Kolom(), Data(), "HMaintenance", "No_Bukti",noBuktinya );
            generateNoBukti();
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
                tampilKode();
            clear();
            
        } else{
            status = "Aktif";
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
        txtNamaProject = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtNoRef = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        tglRef = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        cmbKelompok = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKode = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtKeyword = new javax.swing.JTextField();
        cmbCari = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
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

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
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
        jLabel7.setText("Nama Project");

        txtNamaProject.setMaximumSize(new java.awt.Dimension(100, 26));
        txtNamaProject.setMinimumSize(new java.awt.Dimension(100, 24));

        jLabel14.setForeground(new java.awt.Color(204, 255, 255));
        jLabel14.setText("Kelompok");

        jLabel15.setForeground(new java.awt.Color(204, 255, 255));
        jLabel15.setText("No Ref");

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

        jLabel16.setForeground(new java.awt.Color(204, 255, 255));
        jLabel16.setText("Tgl Ref");

        tglRef.setNextFocusableComponent(txtQuantity);
        tglRef.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tglRefMouseReleased(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(204, 255, 255));
        jLabel17.setText("Quantity");

        txtQuantity.setMaximumSize(new java.awt.Dimension(100, 26));
        txtQuantity.setMinimumSize(new java.awt.Dimension(100, 24));
        txtQuantity.setNextFocusableComponent(btnSimpan);
        txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQuantityKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQuantityKeyTyped(evt);
            }
        });

        cmbKelompok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reparasi", "Pengecatan", "Sparepart" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNamaProject, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel12))
                                .addGap(33, 33, 33)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(cmbKelompok, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tglRef, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNoRef, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(137, 137, 137))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel9))
                                            .addGap(46, 46, 46))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmbCabang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbMitra, 0, 293, Short.MAX_VALUE)))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbKelompok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNamaProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNoRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(txtUP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tglRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)
                                .addComponent(jLabel16)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Bukti", "Customer", "Tanggal" }));
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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
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
                    .addComponent(jButton1))
                .addGap(10, 10, 10))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        }else if(txtQuantity.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Quantity tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
                generateNoBukti();
                query.Input_Detil(Kolom(), Data(), "HMaintenance");
                generateNoBukti();
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
                tampilKode();
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

    private void txtQuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityKeyPressed

    private void txtQuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyTyped
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c)||(c == evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c == evt.VK_PERIOD))){
            getToolkit().beep ();
            evt.consume();
        }else if(txtQuantity.getText().length() >= 4){
            getToolkit().beep ();
            evt.consume ();
        }
    }//GEN-LAST:event_txtQuantityKeyTyped

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
        int rowCount = tblModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
        tampilKode();
    }//GEN-LAST:event_jButton1ActionPerformed

    int row = 0;
    String kodenya;
    public void getData() {
        row = tabelKode.getSelectedRow();
        System.err.println("row = "+row);
        kodenya = tblModel.getValueAt(row, 0).toString().trim();
        lblNoBukti.setText(kodenya);
        System.err.println("kodenya = "+kodenya);
        tampil(kodenya);
    }
    
    public void tampil(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from HMaintenance where No_Bukti = '" + kode + "'"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                lblNoBukti.setText(rs.getString(1));
                txtNamaProject.setText(rs.getString(3));
                txtCustomer.setText(rs.getString(4)+" : "+rs.getString(5));
                txtUP.setText(rs.getString(6));
                txtTelp.setText(rs.getString(7));
                cmbKelompok.setSelectedItem(rs.getString(8));
                txtNoRef.setText(rs.getString(9));
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s").parse(rs.getString(10));
                tglRef.setDate(date);
                txtQuantity.setText(rs.getString(15));
                cmbCabang.setSelectedItem(rs.getString(11)+" :"+rs.getString(12));             
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
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCariCustomer;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
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
    private com.toedter.calendar.JDateChooser tglRef;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtNamaProject;
    private javax.swing.JTextField txtNoRef;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtTelp;
    private javax.swing.JTextField txtUP;
    // End of variables declaration//GEN-END:variables
}
