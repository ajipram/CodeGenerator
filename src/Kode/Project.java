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
import java.awt.Image;
import static java.awt.Label.CENTER;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javax.swing.JLabel;

import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
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
    String no, kode, nama, jenis = "PRJ";
    String kdCust, nmCust, telp, kdCab, nmCab, kdWil, nmWil, kdAdm, nmAdm;
    Boolean dikunjungi;
    Float notelp;
    
    String data[] = new String[12];
    Fungsi_Query query = new Fungsi_Query();
    
    String[] kolom  = new String[17];
    String[] isi    = new String[17];
    
    PopCustomer popCustomer = new PopCustomer(new javax.swing.JFrame(), true);
    
    public String[] Kolom(){
        kolom[0] = "No_Kode";
        kolom[1] = "No_Bukti";
        kolom[2] = "Kd_Cust";
        kolom[3] = "Nm_Cust";
        kolom[4] = "Telp_Cust";
        kolom[5] = "Kd_Cabang";
        kolom[6] = "Nm_Cab";
        kolom[7] = "Kd_Wilayah";
        kolom[8] = "Nm_Wilayah";
        kolom[9] = "Outlet";
        kolom[10] = "Teknisi1";
        kolom[11] = "Teknisi2";
        kolom[12] = "Kd_Admin";
        kolom[13] = "Nm_Admin";
        kolom[14] = "Tanggal";
        kolom[15] = "Kd_Mitra";
        kolom[16] = "Nm_Mitra";
        return kolom;
    }
    
    public String[] Data(){
        String strCab = String.valueOf(cmbCabang.getSelectedItem());
        String strWil = String.valueOf(cmbWilayah.getSelectedItem());
        String strMit = String.valueOf(cmbMitra.getSelectedItem());
        
        isi[0] = txtNoKode.getText();
        isi[1] = txtNoBukti.getText();
        isi[2] = kdCust;
        isi[3] = nmCust;
        isi[4] = txtTelp.getText();
        isi[5] = strCab.substring(0, strCab.indexOf(" : "));
        isi[6] = strCab.substring(5);
        isi[7] = strWil.substring(0, strWil.indexOf(" : "));
        isi[8] = strWil.substring(strWil.lastIndexOf(" : ")+2);
        isi[9] = txtOutlet.getText();
        isi[10] = txtTeknisi1.getText();
        isi[11] = txtTeknisi2.getText();
        isi[12] = MenuUtama.getId();
        isi[13] = MenuUtama.getNama();
        isi[14] = lblTanggal.getText();
        isi[15] = strMit.substring(0, strMit.indexOf(" : "));
        isi[16] = strMit.substring(strMit.lastIndexOf(" : ")+2);
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
        

        lblTanggal.setText(datetime("dd/MMM/yyyy"));
        tabelKode.setModel(tblModel);
        tabelKode.setAutoCreateRowSorter(true);
        Tabel(tabelKode, new int[]{50,150,100,100,100,100,100,100,100,100,100,100});
        sortTable();
        
        tampilKode();
        setCellsAlignment();
        txtNoKode.setText(autonumber());
        comboCabang();
        comboWilayah();
        comboMitra();
    }
    
    private String datetime(String format){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
	LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
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
            
            String query = "Select MAX(No_Kode) from HProject"; 
            ResultSet rs = state.executeQuery(query);
            int max = 0;
            while(rs.next()){
                max = rs.getInt(1);    
            }
            max=max+1;
            no = String.format("%04d", max);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
        return no;
    }
    
    public void setCellsAlignment()
    {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for (int columnIndex = 0; columnIndex <tblModel.getColumnCount(); columnIndex++){
            tabelKode.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
    
    private void tampilKode(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from HProject"; 
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                int nokod = Integer.parseInt(rs.getString(1));
                String nokode = String.format("%04d", nokod);
                data[0] = nokode;
                data[1] = rs.getString(2);
                data[2] = rs.getString(4);
                data[3] = rs.getString(5);
                data[4] = rs.getString(7);
                data[5] = rs.getString(9);
                data[6] = rs.getString(10);
                data[7] = rs.getString(17);
                data[8] = rs.getString(11);
                data[9] = rs.getString(12);
                data[10] = rs.getString(14);
                data[11] = rs.getString(15);
                
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
        }else{
            namakolom = "Tanggal";
        }
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            tblModel.setRowCount(0);
            String query = "Select * from KDMaintenance where "+ namakolom +" like '%"+ key +"%'"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                int nokod = Integer.parseInt(rs.getString(1));
                String nokode = String.format("%04d", nokod);
                data[0] = nokode;
                data[1] = rs.getString(2);
                data[2] = rs.getString(4);
                data[3] = rs.getString(5);
                data[4] = rs.getString(7);
                data[5] = rs.getString(9);
                data[6] = rs.getString(10);
                data[7] = rs.getString(17);
                data[8] = rs.getString(11);
                data[9] = rs.getString(12);
                data[10] = rs.getString(14);
                data[11] = rs.getString(15);
                
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
    
    private void comboWilayah(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from MWilayah"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                kdWil = rs.getString(1);
                nmWil = rs.getString(2);
                cmbWilayah.addItem(kdWil+" : "+nmWil);
            }
            cmbWilayah.setSelectedItem("JKT : Jakarta");
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
            cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    public void clear(){
        txtNoKode.setText(autonumber());
        txtCustomer.setText("");
        txtTelp.setText("");
        txtNoBukti.setText("");
        cmbCabang.setSelectedItem("J01 : Jakarta");
        cmbWilayah.setSelectedItem("JKT : Jakarta");
        cmbMitra.setSelectedItem("WS : WORKSHOP TAMBUN");
        txtOutlet.setText("");
        txtTeknisi1.setText("");
        txtTeknisi2.setText("");
        txtKeyword.setText("");
        btnSimpan.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBatal.setEnabled(false);
        btnGenerate.setEnabled(true);
        btnCariCustomer.requestFocus();
    }
 
  
    public void getTelp(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Telp from MCustomer where Kode = '"+ kode +"'"; 
            ResultSet rs = state.executeQuery(query);
            System.err.println(query);
            //ResultSetMetaData rsmd = rs.getMetaData(); int columnsNumber = rsmd.getColumnCount();
          
            while(rs.next()){
                if(rs.getString(1).equals(" ") || rs.getString(1).equals("") || rs.getString(1) == null) {
                    telp = "Tidak ada data";
                    txtTelp.setText(telp);
                    
                }else{
                    telp = rs.getString(1); 
                    txtTelp.setText(telp);
                    
                }
            }
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
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
        new String[] {"Kode", "Nomor Bukti", "Nama Customer", "Telp", "Cabang", "Wilayah", "Outlet", "Mitra", "Teknisi 1", "Teknisi 2", "Admin", "Tanggal"}
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
        jLabel2 = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKode = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtKeyword = new javax.swing.JTextField();
        cmbCari = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btnCari = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnCariCustomer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNoKode = new javax.swing.JTextField();
        cmbCabang = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtOutlet = new javax.swing.JTextField();
        cmbWilayah = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cmbMitra = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtTeknisi1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTeknisi2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTelp = new javax.swing.JTextField();
        txtCustomer = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnGenerate = new javax.swing.JButton();
        txtNoBukti = new javax.swing.JTextField();
        btnCopy = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setTitle("Kode Maintenance");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kmaintenance.png"))); // NOI18N

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel2.setText("Tanggal :");

        lblTanggal.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTanggal.setText("tgl");

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
        tabelKode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKodeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelKode);

        jLabel1.setBackground(new java.awt.Color(0, 153, 153));
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Kode Project");
        jLabel1.setOpaque(true);

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtKeyword.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtKeywordInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        cmbCari.setForeground(new java.awt.Color(0, 102, 102));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Bukti", "Tanggal" }));

        jLabel5.setForeground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("Cari :");

        btnCari.setForeground(new java.awt.Color(0, 153, 153));
        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/cari.png"))); // NOI18N
        btnCari.setText("Cari");
        btnCari.setMaximumSize(new java.awt.Dimension(85, 35));
        btnCari.setMinimumSize(new java.awt.Dimension(85, 35));
        btnCari.setPreferredSize(new java.awt.Dimension(85, 35));
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setForeground(new java.awt.Color(204, 255, 255));
        jLabel3.setText("Customer");

        btnCariCustomer.setForeground(new java.awt.Color(0, 153, 153));
        btnCariCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/cari.png"))); // NOI18N
        btnCariCustomer.setText("Cari Customer");
        btnCariCustomer.setMaximumSize(new java.awt.Dimension(90, 35));
        btnCariCustomer.setMinimumSize(new java.awt.Dimension(90, 35));
        btnCariCustomer.setPreferredSize(new java.awt.Dimension(90, 35));
        btnCariCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariCustomerActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(204, 255, 255));
        jLabel4.setText("Cabang");

        jLabel6.setForeground(new java.awt.Color(204, 255, 255));
        jLabel6.setText("Nomor Kode");

        txtNoKode.setForeground(new java.awt.Color(0, 102, 102));
        txtNoKode.setEnabled(false);

        cmbCabang.setPreferredSize(null);

        jLabel7.setForeground(new java.awt.Color(204, 255, 255));
        jLabel7.setText("Wilayah");

        jLabel8.setForeground(new java.awt.Color(204, 255, 255));
        jLabel8.setText("Outlet");

        txtOutlet.setMaximumSize(new java.awt.Dimension(100, 26));
        txtOutlet.setMinimumSize(new java.awt.Dimension(100, 26));
        txtOutlet.setPreferredSize(new java.awt.Dimension(100, 26));

        cmbWilayah.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbWilayah.setMinimumSize(new java.awt.Dimension(100, 26));
        cmbWilayah.setPreferredSize(new java.awt.Dimension(100, 26));

        jLabel9.setForeground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Mitra");

        cmbMitra.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbMitra.setMinimumSize(new java.awt.Dimension(100, 26));
        cmbMitra.setPreferredSize(new java.awt.Dimension(100, 26));

        jLabel10.setForeground(new java.awt.Color(204, 255, 255));
        jLabel10.setText("Teknisi 1");

        txtTeknisi1.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTeknisi1.setMinimumSize(new java.awt.Dimension(100, 26));
        txtTeknisi1.setPreferredSize(new java.awt.Dimension(100, 26));

        jLabel11.setForeground(new java.awt.Color(204, 255, 255));
        jLabel11.setText("Teknisi 2");

        txtTeknisi2.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTeknisi2.setMinimumSize(new java.awt.Dimension(100, 26));
        txtTeknisi2.setPreferredSize(new java.awt.Dimension(100, 26));

        jLabel12.setForeground(new java.awt.Color(204, 255, 255));
        jLabel12.setText("Telp");

        txtTelp.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTelp.setMinimumSize(new java.awt.Dimension(100, 26));
        txtTelp.setPreferredSize(new java.awt.Dimension(100, 26));

        txtCustomer.setMaximumSize(new java.awt.Dimension(100, 26));
        txtCustomer.setMinimumSize(new java.awt.Dimension(100, 26));
        txtCustomer.setPreferredSize(new java.awt.Dimension(100, 26));

        btnSimpan.setForeground(new java.awt.Color(0, 153, 153));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setEnabled(false);
        btnSimpan.setMaximumSize(new java.awt.Dimension(110, 36));
        btnSimpan.setPreferredSize(new java.awt.Dimension(110, 36));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnHapus.setForeground(new java.awt.Color(0, 153, 153));
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/delete.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.setEnabled(false);
        btnHapus.setPreferredSize(new java.awt.Dimension(100, 36));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/keluar.png"))); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 36));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnGenerate.setForeground(new java.awt.Color(0, 153, 153));
        btnGenerate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/generate.png"))); // NOI18N
        btnGenerate.setText("Generate Nomor Bukti");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        txtNoBukti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoBuktiActionPerformed(evt);
            }
        });

        btnCopy.setForeground(new java.awt.Color(0, 153, 153));
        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/copy.png"))); // NOI18N
        btnCopy.setText("Copy");
        btnCopy.setMaximumSize(new java.awt.Dimension(90, 36));
        btnCopy.setMinimumSize(new java.awt.Dimension(90, 36));
        btnCopy.setPreferredSize(new java.awt.Dimension(90, 36));
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGenerate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerate)
                    .addComponent(txtNoBukti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel8)
                                        .addGap(6, 6, 6))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNoKode, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel12)
                                        .addGap(12, 12, 12))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtOutlet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbMitra, 0, 167, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel10))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTeknisi1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTeknisi2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNoKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOutlet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel4)
                            .addComponent(cmbCabang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMitra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(cmbWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTeknisi1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTeknisi2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 67, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    String kodeCust, namaCust;    
    String[] sim = new String[7];
    private void tabelKodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKodeMouseClicked
        if(evt.getClickCount()==2){
            getData();
            btnHapus.setEnabled(true);
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

    private void btnCariCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariCustomerActionPerformed

        popCustomer.setVisible(true);
        kdCust = popCustomer.getKode();
        nmCust = popCustomer.getNama();
        txtCustomer.setText(kdCust+" : "+nmCust);
        getTelp(kdCust);
        System.err.println(kdCust);
    }//GEN-LAST:event_btnCariCustomerActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtNoKode.equals("")){
            JOptionPane.showMessageDialog(null,"Nomor Kode tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtCustomer.equals("")){
            JOptionPane.showMessageDialog(null,"Customer tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNoBukti.equals("")){
            JOptionPane.showMessageDialog(null,"Generate kode terlebih dahulu","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
                lblTanggal.setText(datetime("dd/MMM/yyyy HH:mm:ss"));
                query.Input_Detil(Kolom(), Data(), "HEmergency");

                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
                tampilKode();
                lblTanggal.setText(datetime("dd/MMM/yyyy"));
            }catch(Exception e){
                System.err.println("errorr"+e.getMessage());
            }
        }
        clear();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Yakin akan menghapus?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        try{
            query.Hapus("HEmergency", "No_Kode", txtNoKode.getText());
            tblModel.removeRow(row);
            clear();
        }catch(Exception e){
            System.err.println("errorr"+e.getMessage());
        }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clear();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // TODO add your handling code here:
        if(txtCustomer.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Customer tidak boleh kosong", "Perhatian", JOptionPane.INFORMATION_MESSAGE);
        }else{

            String nomor = String.valueOf(autonumber());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            txtNoBukti.setText(kdCust+"-"+kdCab+"-"+jenis+"-"+date+nomor);
            btnSimpan.setEnabled(true);
            btnBatal.setEnabled(true);
        }
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void txtNoBuktiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoBuktiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoBuktiActionPerformed

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed

        StringSelection stringSelection = new StringSelection(txtNoBukti.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_btnCopyActionPerformed

    int row = 0;
    String kodenya;
    public void getData() {
        row = tabelKode.getSelectedRow();
        kodenya = tblModel.getValueAt(tabelKode.convertRowIndexToModel( row),0).toString();
        txtNoKode.setText(kodenya);
        tampil(kodenya);
        btnGenerate.setEnabled(false);
    }
    
    public void tampil(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from HProject where No_Kode = '" + kode + "'"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                txtCustomer.setText(rs.getString(3)+" : "+rs.getString(4));
                txtTelp.setText(rs.getString(5));
                cmbCabang.setSelectedItem(rs.getString(6)+" :"+rs.getString(7));
                cmbWilayah.setSelectedItem(rs.getString(8)+" :"+rs.getString(9));
                txtOutlet.setText(rs.getString(10));
                cmbMitra.setSelectedItem(rs.getString(16)+" :"+rs.getString(17));
                txtTeknisi1.setText(rs.getString(11));
                txtTeknisi2.setText(rs.getString(12));
            }
            
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCariCustomer;
    private javax.swing.JButton btnCopy;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbCabang;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbMitra;
    private javax.swing.JComboBox<String> cmbWilayah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JTable tabelKode;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtNoBukti;
    private javax.swing.JTextField txtNoKode;
    private javax.swing.JTextField txtOutlet;
    private javax.swing.JTextField txtTeknisi1;
    private javax.swing.JTextField txtTeknisi2;
    private javax.swing.JTextField txtTelp;
    // End of variables declaration//GEN-END:variables
}
