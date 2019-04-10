/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Popup;

import Database.Database;
import Database.Fungsi_Query;
import Kode.Maintenance;
import Kode.Project;
import codegenerator.MenuUtama;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AJI-PC
 */
public class PopDetailReguler extends javax.swing.JDialog {

    Database dbsetting;
    String driver, database, user, pass;
    String kode, nama, status, bukti, serial, kdWil, nmWil;
    String data[] = new String[8];
    static String noBukti, mitra, wilayah, outlet;
    int quantity;
    Maintenance main;
    Fungsi_Query query = new Fungsi_Query();
    String[] kolom  = new String[11];
    String[] isi    = new String[11];
    
    public String[] Kolom(){
        kolom[0] = "no_seri";
        kolom[1] = "no_bukti";
        kolom[2] = "kode_mitra";
        kolom[3] = "merk";
        kolom[4] = "tipe";
        kolom[5] = "wilayah";
        kolom[6] = "outlet";
        kolom[7] = "teknisi1";
        kolom[8] = "teknisi2";
        kolom[9] = "tanggal";
        kolom[10] = "statusnya";
     
        return kolom;
    }
    
    public String[] Data(){
        
        String strMit = String.valueOf(txtMitra.getText());
        
        isi[0] = txtNoSeri.getText();
        isi[1] = txtNoBukti.getText();
        isi[2] = strMit.substring(0, strMit.indexOf(" : "));
        isi[3] = txtMerk.getText();
        isi[4] = txtTipe.getText();
        isi[5] = cmbWilayah.getSelectedItem().toString().trim();
        isi[6] = txtOutlet.getText();
        isi[7] = txtTeknisi1.getText();
        isi[8] = txtTeknisi2.getText();
        isi[9] = txtTanggal.getText();
        isi[10] = cmbStatus.getSelectedItem().toString().trim();
        return isi;
        
    }
    
    public PopDetailReguler(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        
        this.setSize(800, 700);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.yellow);
        
        tabelData.setModel(tblModel);
        Tabel(tabelData, new int[]{150,150,150,150,100,100,100,100});
        tabelData.setAutoCreateRowSorter(true);
        comboWilayah();
        comboTeknisi();
        noBukti = getNoBukti();
        System.out.println("nobukti = "+noBukti);
        txtTeknisi1.setText("");
        txtTeknisi2.setText("");
        txtTanggal.setText(datetime("dd-MMM-yyyy"));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        tabelData.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
        tabelData.getTableHeader().setBackground(Color.decode("#ffbf80"));
    }

    
    public JLabel getTxtMitra() {
        return txtMitra;
    }

    public JLabel getTxtNoBukti() {
        return txtNoBukti;
    }

    public void setNoBukti(String noBukti) {
        PopDetailReguler.noBukti = noBukti;
    }

    public static String getNoBukti() {
        return noBukti;
    }
    
    

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public int getQuantity() {
        return quantity;
    }
    
    private void setQuantity(){
        
        try{
            String sql = "update HReguler set Quantity = '"+ maxnumber() +"' where No_Bukti = '"+noBukti+"'";
            System.err.println("sql = "+sql);
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
    
    int max = 0;
    public int maxnumber(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "SELECT MAX(Quantity) FROM HReguler where No_Bukti = '"+noBukti+"'"; 
            System.err.println(query);
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                max = rs.getInt(1);
                  System.err.println(max);
            }
            max = max + 1;
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
        System.err.println("max = "+max);
        return max;
    }
    
    public void setTeknisi(String mitranya){
        //String mitra = txtMitra.getText();
        System.err.println("mitra = "+mitranya);
        String left = mitranya.substring(0,2);
        if(left.equals("WS")){
            cmbTeknisi1.setEnabled(true);
            cmbTeknisi2.setEnabled(true);
            txtTeknisi1.setEnabled(false);
            txtTeknisi2.setEnabled(false);
        }else{
            txtTeknisi1.setEnabled(true);
            txtTeknisi2.setEnabled(true);
            cmbTeknisi1.setEnabled(false);
            cmbTeknisi2.setEnabled(false);
       }
       
    }
    
    
    
    public void tampilData(String bukti){
        int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from detailReguler where no_bukti = '"+bukti+"'";
            System.out.println("querrry "+query);
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(4);
                data[2] = rs.getString(5);
                data[3] = rs.getString(6);
                data[4] = rs.getString(7);
                data[5] = rs.getString(8);
                data[6] = rs.getString(9);
                data[7] = rs.getString(11);
                tblModel.addRow(data);
            }
            quantity = tblModel.getRowCount();
            txtQuantity.setText(String.valueOf(quantity));
            System.out.println("quantity = "+quantity);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void searchKode(String cmb, String key){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            tblModel.setRowCount(0);
            String query = "Select * from detailReguler where "+ cmb +" like '%"+ key +"%'"; 
            ResultSet rs = state.executeQuery(query);
            System.err.println(query);
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(4);
                data[2] = rs.getString(5);
                data[3] = rs.getString(6);
                data[4] = rs.getString(7);
                data[5] = rs.getString(8);
                data[6] = rs.getString(9);
                data[7] = rs.getString(11);
                tblModel.addRow(data);
            }
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
    
    private String datetime(String format){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
	LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }
    
    private void clear(){
        txtNoSeri.setText("");
        txtMerk.setText("");
        txtTipe.setText("");
        cmbStatus.setSelectedItem("Belum Dikerjakan");
        cmbWilayah.setSelectedItem("JKT : Jakarta");
        txtOutlet.setText("");
        cmbTeknisi1.setSelectedItem("Pilih Teknisi");
        cmbTeknisi2.setSelectedItem("Pilih Teknisi");
        txtTeknisi1.setText("");
        txtTeknisi2.setText("");
        btnUpdate.setEnabled(false);
        btnBatal.setEnabled(false);
        btnSimpan.setEnabled(true);
    }
    
    private void comboTeknisi(){
        cmbTeknisi1.addItem("Pilih Teknisi");
        cmbTeknisi1.setSelectedItem("Pilih Teknisi");
        cmbTeknisi2.addItem("Pilih Teknisi");
        cmbTeknisi2.setSelectedItem("Pilih Teknisi");
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from HTeknisi"; 
            ResultSet rs = state.executeQuery(query);
            String namanya;
            while(rs.next()){
                namanya = rs.getString(3);
                cmbTeknisi1.addItem(namanya);
                cmbTeknisi2.addItem(namanya);
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
        new String[] {"Nomor Seri", "Merk", "Tipe", "Wilayah", "Outlet", "Teknisi 1", "Teknisi 2", "Status"}
        ){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false,false,false
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cmbCari = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelData = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtNoSeri = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtMitra = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNoBukti = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMerk = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTipe = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnSimpan = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        cmbWilayah = new javax.swing.JComboBox<>();
        txtOutlet = new javax.swing.JTextField();
        btnBatal = new javax.swing.JButton();
        txtTeknisi1 = new javax.swing.JTextField();
        cmbTeknisi1 = new javax.swing.JComboBox<>();
        txtTeknisi2 = new javax.swing.JTextField();
        cmbTeknisi2 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 153));

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        jLabel1.setBackground(new java.awt.Color(255, 255, 153));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Detail Project");

        jPanel1.setBackground(new java.awt.Color(255, 204, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Seri", "Merk" }));
        cmbCari.setNextFocusableComponent(txtKeyword);
        jPanel1.add(cmbCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 9, 75, -1));

        jLabel3.setForeground(new java.awt.Color(204, 51, 0));
        jLabel3.setText("Keyword");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 14, -1, -1));

        txtKeyword.setNextFocusableComponent(btnCari);
        txtKeyword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKeywordActionPerformed(evt);
            }
        });
        jPanel1.add(txtKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 10, 150, -1));

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/cari.png"))); // NOI18N
        btnCari.setText("Cari");
        btnCari.setNextFocusableComponent(txtNoSeri);
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        jPanel1.add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 6, -1, -1));

        jLabel4.setForeground(new java.awt.Color(204, 51, 0));
        jLabel4.setText("Cari berdasarkan");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 14, -1, -1));

        txtQuantity.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txtQuantity.setForeground(new java.awt.Color(204, 51, 0));
        jPanel1.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, 40, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 51, 0));
        jLabel13.setText("Jumlah :");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, -1));

        tabelData.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabelData.setForeground(new java.awt.Color(0, 0, 0));
        tabelData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No Seri", "Merk", "Tipe", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataMouseClicked(evt);
            }
        });
        tabelData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelDataKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabelData);

        jPanel4.setBackground(new java.awt.Color(255, 204, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setForeground(new java.awt.Color(204, 51, 0));
        jLabel5.setText("Mitra");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        txtNoSeri.setNextFocusableComponent(txtMerk);
        txtNoSeri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoSeriActionPerformed(evt);
            }
        });
        jPanel4.add(txtNoSeri, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 250, -1));

        jLabel7.setForeground(new java.awt.Color(204, 51, 0));
        jLabel7.setText(":");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        txtMitra.setForeground(new java.awt.Color(204, 51, 0));
        jPanel4.add(txtMitra, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 190, -1));

        jLabel8.setForeground(new java.awt.Color(204, 51, 0));
        jLabel8.setText(":");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 10, -1));

        jLabel9.setForeground(new java.awt.Color(204, 51, 0));
        jLabel9.setText("Nomor Bukti");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 70, -1));

        txtNoBukti.setForeground(new java.awt.Color(204, 51, 0));
        jPanel4.add(txtNoBukti, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 190, -1));

        jLabel11.setForeground(new java.awt.Color(204, 51, 0));
        jLabel11.setText("Teknisi 1");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 60, -1));

        jLabel14.setForeground(new java.awt.Color(204, 51, 0));
        jLabel14.setText("Status");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, -1, -1));

        jLabel6.setForeground(new java.awt.Color(204, 51, 0));
        jLabel6.setText("Nomor Seri");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        txtMerk.setNextFocusableComponent(txtTipe);
        txtMerk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMerkActionPerformed(evt);
            }
        });
        jPanel4.add(txtMerk, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 250, -1));

        jLabel16.setForeground(new java.awt.Color(204, 51, 0));
        jLabel16.setText("Merk");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel17.setForeground(new java.awt.Color(204, 51, 0));
        jLabel17.setText("Tipe");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        txtTipe.setNextFocusableComponent(cmbStatus);
        txtTipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipeActionPerformed(evt);
            }
        });
        jPanel4.add(txtTipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 250, -1));

        jLabel19.setForeground(new java.awt.Color(204, 51, 0));
        jLabel19.setText("Teknisi 2");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Belum Dikerjakan", "Sedang Dikerjakan", "Selesai", "Batal" }));
        cmbStatus.setNextFocusableComponent(btnSimpan);
        jPanel4.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, 280, -1));

        btnSimpan.setForeground(new java.awt.Color(0, 153, 153));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setMaximumSize(new java.awt.Dimension(110, 36));
        btnSimpan.setNextFocusableComponent(btnUpdate);
        btnSimpan.setPreferredSize(new java.awt.Dimension(110, 36));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jPanel4.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/edit.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setEnabled(false);
        btnUpdate.setNextFocusableComponent(cmbCari);
        btnUpdate.setPreferredSize(new java.awt.Dimension(100, 36));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel4.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, -1, -1));

        cmbWilayah.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbWilayah.setMinimumSize(new java.awt.Dimension(100, 24));
        cmbWilayah.setPreferredSize(new java.awt.Dimension(100, 26));
        jPanel4.add(cmbWilayah, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 280, -1));

        txtOutlet.setMaximumSize(new java.awt.Dimension(100, 26));
        txtOutlet.setMinimumSize(new java.awt.Dimension(100, 24));
        jPanel4.add(txtOutlet, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 280, -1));

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setEnabled(false);
        btnBatal.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanel4.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 110, -1));

        txtTeknisi1.setEnabled(false);
        txtTeknisi1.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTeknisi1.setMinimumSize(new java.awt.Dimension(100, 24));
        txtTeknisi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTeknisi1ActionPerformed(evt);
            }
        });
        jPanel4.add(txtTeknisi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 140, -1));

        cmbTeknisi1.setMaximumSize(new java.awt.Dimension(33, 26));
        cmbTeknisi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTeknisi1ActionPerformed(evt);
            }
        });
        jPanel4.add(cmbTeknisi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, 130, -1));

        txtTeknisi2.setEnabled(false);
        txtTeknisi2.setMaximumSize(new java.awt.Dimension(100, 26));
        txtTeknisi2.setMinimumSize(new java.awt.Dimension(100, 24));
        jPanel4.add(txtTeknisi2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 140, -1));

        cmbTeknisi2.setMaximumSize(new java.awt.Dimension(33, 26));
        cmbTeknisi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTeknisi2ActionPerformed(evt);
            }
        });
        jPanel4.add(cmbTeknisi2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 130, -1));

        jLabel12.setForeground(new java.awt.Color(204, 51, 0));
        jLabel12.setText("Wilayah");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 50, -1));

        jLabel22.setForeground(new java.awt.Color(204, 51, 0));
        jLabel22.setText("Outlet");
        jPanel4.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, -1, -1));

        jLabel20.setForeground(new java.awt.Color(204, 51, 0));
        jLabel20.setText("Tanggal");

        jLabel21.setForeground(new java.awt.Color(204, 51, 0));
        jLabel21.setText(":");

        txtTanggal.setForeground(new java.awt.Color(204, 51, 0));
        txtTanggal.setText("-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel20))
                    .addComponent(txtTanggal))
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKeywordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKeywordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeywordActionPerformed

    private void tabelDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            getData();
            btnSimpan.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnBatal.setEnabled(true);
        }
    }//GEN-LAST:event_tabelDataMouseClicked

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        Object selectedItem = cmbCari.getSelectedItem();
        if (selectedItem != null){
            String selectedItemStr = selectedItem.toString();
            searchKode(selectedItemStr, txtKeyword.getText());       
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void tabelDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelDataKeyPressed
        
        
    }//GEN-LAST:event_tabelDataKeyPressed

    private void txtNoSeriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoSeriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoSeriActionPerformed

    private void txtMerkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMerkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMerkActionPerformed

    private void txtTipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipeActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(txtNoSeri.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Isi Nomor Seri","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtMerk.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Isi Merk","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtTipe.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Isi Tipe","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{

                query.Input_Detil(Kolom(), Data(), "detailReguler");
                setQuantity();
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
                bukti = txtNoBukti.getText();
                tampilData(bukti);
                Project prj = new Project();
                prj.tampilKode();
                clear();
            }catch(NullPointerException e){
                e.printStackTrace();
                System.err.println("errorr3 "+e);
            }catch(Error er){
                System.err.println("errorr2 "+er.getMessage());
            }catch(Exception ex){
                ex.printStackTrace();
                System.err.println("errorr3 "+ex);
            }
        }
        clear();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        //cancel();
        serial = txtNoSeri.getText();
        query.Edit(Kolom(), Data(), "detailReguler", "no_seri", serial);
        int rowCount = tblModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
        bukti = txtNoBukti.getText();
        tampilData(bukti);
        clear();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        //cancel();
        clear();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void cmbTeknisi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTeknisi1ActionPerformed
        String teknisi = cmbTeknisi1.getSelectedItem().toString().trim();
        txtTeknisi1.setText(teknisi);
    }//GEN-LAST:event_cmbTeknisi1ActionPerformed

    private void cmbTeknisi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTeknisi2ActionPerformed
        String teknisi = cmbTeknisi2.getSelectedItem().toString().trim();
        txtTeknisi2.setText(teknisi);
    }//GEN-LAST:event_cmbTeknisi2ActionPerformed

    private void txtTeknisi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTeknisi1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTeknisi1ActionPerformed

    JTextField txtkode, txtnama;
    int row = 0;
    public void getData() {
        row = tabelData.getSelectedRow();
        txtNoSeri.setText(tblModel.getValueAt(row, 0).toString().trim());
        txtMerk.setText(tblModel.getValueAt(row, 1).toString().trim());
        txtTipe.setText(tblModel.getValueAt(row, 2).toString().trim());
        cmbWilayah.setSelectedItem(tblModel.getValueAt(row, 3).toString().trim());
        txtOutlet.setText(tblModel.getValueAt(row, 4).toString().trim());
        txtTeknisi1.setText(tblModel.getValueAt(row, 5).toString().trim());
        txtTeknisi2.setText(tblModel.getValueAt(row, 6).toString().trim());
        cmbStatus.setSelectedItem(tblModel.getValueAt(row, 7).toString().trim());
        btnUpdate.setEnabled(true);
        
    }
    
    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PopDetailReguler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopDetailReguler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopDetailReguler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopDetailReguler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                
                PopDetailReguler dialog = new PopDetailReguler(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbTeknisi1;
    private javax.swing.JComboBox<String> cmbTeknisi2;
    private javax.swing.JComboBox<String> cmbWilayah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelData;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtMerk;
    private javax.swing.JLabel txtMitra;
    private javax.swing.JLabel txtNoBukti;
    private javax.swing.JTextField txtNoSeri;
    private javax.swing.JTextField txtOutlet;
    private javax.swing.JLabel txtQuantity;
    private javax.swing.JLabel txtTanggal;
    private javax.swing.JTextField txtTeknisi1;
    private javax.swing.JTextField txtTeknisi2;
    private javax.swing.JTextField txtTipe;
    // End of variables declaration//GEN-END:variables

    

    

    
}
