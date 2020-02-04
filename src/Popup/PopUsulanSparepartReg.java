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
import Kode.Reguler_Outstanding;
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
public class PopUsulanSparepartReg extends javax.swing.JDialog {

    Database dbsetting;
    String driver, database, user, pass;
    String kode, nama, status, bukti, serial, kdWil, nmWil;
    String data[] = new String[8];
    static String noBukti, mitra, wilayah, outlet;
    String no,Kode, Nomor, KodeUsulan, JnStock, Nama;
    int  quantity;
    //Reguler_Outstanding regout = new Reguler_Outstanding();
    Fungsi_Query query = new Fungsi_Query();
    String[] kolom  = new String[6];
    String[] isi    = new String[6];
    String[] kolom2  = new String[6];
    String[] isi2    = new String[6];
    String[] kolom3  = new String[4];
    String[] isi3    = new String[4];
    
    public String[] Kolom(){
        kolom[0] = "NoBukti";
        kolom[1] = "KdBarang";
        kolom[2] = "JnStock";
        kolom[3] = "NmBarang";
        kolom[4] = "Qty";
        kolom[5] = "Harga";

        return kolom;
    }
    
    public String[] Data(){
        isi[0] = lblNoBukti.getText();
        isi[1] = lblKodePNI.getText();
        isi[2] = lblJenisStock.getText();
        isi[3] = cmbKodePNI.getSelectedItem().toString().trim();
        isi[4] = lblJumlah.getText();
        isi[5] = txtHarga.getText();

        return isi; 
    }
    
    public String[] Kolom2(){
        kolom2[0] = "Nomor";
        kolom2[1] = "Kode";
        kolom2[2] = "Nama";
        kolom2[3] = "Jumlah";
        kolom2[4] = "Harga";
        kolom2[5] = "Verifikasi";

        return kolom2;
    }
    
    public String[] Data2(){
        isi2[0] = no;
        isi2[1] = lblNoBukti.getText();
        isi2[2] = cmbKodePNI.getSelectedItem().toString().trim();
        isi2[3] = lblJumlah.getText();
        isi2[4] = txtHarga.getText().toString();
        isi2[5] = "1";

        return isi2; 
    }
    
    public String[] Kolom3(){
        kolom3[0] = "Nama";
        kolom3[1] = "Jumlah";
        kolom3[2] = "Harga";
        kolom3[3] = "Verifikasi";

        return kolom3;
    }
    
    public String[] Data3(){
        isi3[0] = cmbKodePNI.getSelectedItem().toString().trim();
        isi3[1] = lblJumlah.getText();
        isi3[2] = txtHarga.getText().toString();
        isi3[3] = "1";

        return isi3; 
    }
    
    public PopUsulanSparepartReg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        dbsetting = new Database();
        driver    = "net.sourceforge.jtds.jdbc.Driver";
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        
        this.setSize(830, 700);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.yellow);
        System.err.println("mulai= popsparepart ");
        tabelData.setModel(tblModel);
        Tabel(tabelData, new int[]{80,100,150,150,70,100,70});
        tabelData.setAutoCreateRowSorter(true);
        
        
        noBukti = Reguler_Outstanding.getKdnya();
        lblNoBukti.setText(noBukti);
        System.err.println("nobukti = "+noBukti);
        clear();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        tabelData.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        tabelData.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
  
        tampilPNI();

        tabelData.getTableHeader().setBackground(Color.decode("#ffbf80"));
        tampilData(noBukti);
    }

    public static String getNoBukti() {
        return noBukti;
    }

    public static void setNoBukti(String noBukti) {
        PopUsulanSparepartReg.noBukti = noBukti;
    }

    public String autonumber(String nobukti){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select MAX(Nomor) from UsulanSparepart where Kode = '"+nobukti+"'"; 
            System.out.println("query max "+query);
            ResultSet rs = state.executeQuery(query);
            int max = 0;
            while(rs.next()){
                max = rs.getInt(1);  
                //max++;
                System.out.println("max "+max);
            }
            max=max+1;
            no = String.valueOf(max);
            System.out.println("no "+no);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
        return no;
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
            String query = "Select * from UsulanSparepart where Kode = '"+bukti+"' ORDER BY Nomor asc";
            System.out.println("querrry "+query);
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                tblModel.addRow(data);
            }
          
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }  
    
    private void tampilPNI(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, JnStock, Nama from MStock order by Nama asc";
            ResultSet rs = state.executeQuery(query);
            cmbKodePNI.addItem("Pilih PNI");
            while(rs.next()){
                Kode = rs.getString(1);
                JnStock = rs.getString(2);
                Nama = rs.getString(3);
                cmbKodePNI.addItem(Nama);
            }
            cmbKodePNI.setSelectedItem("Pilih PNI");
            lblKodePNI.setText("");
            lblJenisStock.setText("");
            btnSimpan.setEnabled(true);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void tampilKodePNI(String Nama){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, JnStock, Nama from MStock Where Nama = '"+Nama+"' order by Kode asc";
            ResultSet rs = state.executeQuery(query);
       
            while(rs.next()){
                Kode = rs.getString(1);
                JnStock = rs.getString(2);
                Nama = rs.getString(3);
           
            }
            lblKodePNI.setText(Kode);
            lblJenisStock.setText(JnStock);
          
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void searchPNI(String text){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select Kode, JnStock, Nama from MStock Where Nama like '%"+text+"%' order by Kode asc";
            System.err.println("query search PNI "+query);
            ResultSet rs = state.executeQuery(query);
       
            while(rs.next()){
                Kode = rs.getString(1);
                JnStock = rs.getString(2);
                Nama = rs.getString(3);
                cmbKodePNI.addItem(Nama);
            }
            cmbKodePNI.setSelectedItem(nama);
            lblKodePNI.setText(Kode);
            lblJenisStock.setText(JnStock);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    private void hapusItem(){
        tblModel.removeRow(row);
    }
    
    private void clear(){
        cmbKodePNI.setSelectedItem("Pilih PNI");
        lblKodePNI.setText("");
        lblJenisStock.setText("");
        lblNamaBarang.setText("");
        lblJumlah.setText("");
        txtHarga.setText("");
        //btnSimpan.setEnabled(false);
        
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
        new String[] {"Nomor", "Kode", "Nama", "Jumlah","Harga","Verifikasi"}
        ){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelData = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        cmbKodePNI = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblNoBukti = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblJenisStock = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblNamaBarang = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblKodePNI = new javax.swing.JLabel();
        btnBatal = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        lblJumlah = new javax.swing.JTextField();
        btnKeluar = new javax.swing.JButton();
        btnTambahSparepart = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 153));

        jPanel2.setBackground(new java.awt.Color(255, 255, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 153));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sparepart Usulan");

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

        jLabel8.setForeground(new java.awt.Color(204, 51, 0));
        jLabel8.setText(":");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 10, -1));

        jLabel6.setForeground(new java.awt.Color(204, 51, 0));
        jLabel6.setText("Jenis Stock");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel16.setForeground(new java.awt.Color(204, 51, 0));
        jLabel16.setText("Nama Barang");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel17.setForeground(new java.awt.Color(204, 51, 0));
        jLabel17.setText("Harga         Rp.");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 80, -1));

        btnSimpan.setForeground(new java.awt.Color(0, 153, 153));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setMaximumSize(new java.awt.Dimension(110, 36));
        btnSimpan.setPreferredSize(new java.awt.Dimension(110, 36));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jPanel4.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 160, -1));

        cmbKodePNI.setMaximumSize(new java.awt.Dimension(100, 26));
        cmbKodePNI.setMinimumSize(new java.awt.Dimension(100, 24));
        cmbKodePNI.setPreferredSize(new java.awt.Dimension(100, 26));
        cmbKodePNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKodePNIActionPerformed(evt);
            }
        });
        cmbKodePNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbKodePNIKeyTyped(evt);
            }
        });
        jPanel4.add(cmbKodePNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 330, -1));

        jLabel12.setForeground(new java.awt.Color(204, 51, 0));
        jLabel12.setText("Nama PNI");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 70, -1));

        jLabel18.setForeground(new java.awt.Color(204, 51, 0));
        jLabel18.setText("Nomor Bukti");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 70, -1));

        lblNoBukti.setForeground(new java.awt.Color(204, 51, 0));
        lblNoBukti.setText("-");
        jPanel4.add(lblNoBukti, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 230, -1));

        jLabel9.setForeground(new java.awt.Color(204, 51, 0));
        jLabel9.setText(":");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 10, -1));

        lblJenisStock.setForeground(new java.awt.Color(204, 51, 0));
        lblJenisStock.setText("-");
        jPanel4.add(lblJenisStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 280, -1));

        jLabel10.setForeground(new java.awt.Color(204, 51, 0));
        jLabel10.setText(":");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 10, -1));

        lblNamaBarang.setForeground(new java.awt.Color(204, 51, 0));
        lblNamaBarang.setText("-");
        jPanel4.add(lblNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 540, -1));

        jLabel19.setForeground(new java.awt.Color(204, 51, 0));
        jLabel19.setText("Jumlah");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        txtHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaKeyTyped(evt);
            }
        });
        jPanel4.add(txtHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 240, -1));

        jLabel7.setForeground(new java.awt.Color(204, 51, 0));
        jLabel7.setText("Kode PNI");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel11.setForeground(new java.awt.Color(204, 51, 0));
        jLabel11.setText(":");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 10, -1));

        lblKodePNI.setForeground(new java.awt.Color(204, 51, 0));
        lblKodePNI.setText("-");
        jPanel4.add(lblKodePNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 280, -1));

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/batal.png"))); // NOI18N
        btnBatal.setText("Clear");
        btnBatal.setEnabled(false);
        btnBatal.setPreferredSize(new java.awt.Dimension(100, 36));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanel4.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 170, -1));

        txtCari.setToolTipText("Kata Kunci");
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });
        jPanel4.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, 200, -1));

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
        jPanel4.add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 100, 30));

        lblJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lblJumlahKeyTyped(evt);
            }
        });
        jPanel4.add(lblJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 90, -1));

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/keluar.png"))); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.setOpaque(false);
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 36));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnTambahSparepart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/generate.png"))); // NOI18N
        btnTambahSparepart.setText("Tambah Sparepart");
        btnTambahSparepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahSparepartActionPerformed(evt);
            }
        });

        btnHapus.setForeground(new java.awt.Color(0, 153, 153));
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/delete.png"))); // NOI18N
        btnHapus.setText("Hapus Sparepart");
        btnHapus.setEnabled(false);
        btnHapus.setPreferredSize(new java.awt.Dimension(100, 36));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnTambahSparepart, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(32, 32, 32)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahSparepart)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void tabelDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            getData();
            //btnSimpan.setEnabled(false);
            btnBatal.setEnabled(true);
            btnHapus.setEnabled(true);
        }
    }//GEN-LAST:event_tabelDataMouseClicked

    private void tabelDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelDataKeyPressed
        
        
    }//GEN-LAST:event_tabelDataKeyPressed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(lblKodePNI.getText().toString().equals("")){
            JOptionPane.showMessageDialog(null,"Pilih PNI","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtHarga.getText().toString().equals("")){
            JOptionPane.showMessageDialog(null,"Isi Harga","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
                query.Input_Detil(Kolom(), Data(), "DOrder");
           
                query.updateUsulan(Kolom3(), Data3(), "UsulanSparepart", "Nomor", Nomor, "Kode", KodeUsulan);
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
                bukti = lblNoBukti.getText();
                tampilData(bukti);
                clear();
                hapusItem();
            }catch(NullPointerException e){
                e.printStackTrace();
                System.err.println("errorr3 "+e);
            }catch(Error er){
                System.err.println("errorr2 "+er.getMessage());
            }catch(Exception ex){
                ex.printStackTrace();
                System.err.println("errorr3 "+ex);
            }
        clear();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void cmbKodePNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKodePNIActionPerformed
        // TODO add your handling code here:
        String nama = cmbKodePNI.getSelectedItem().toString().trim();
        tampilKodePNI(nama);
        btnSimpan.setEnabled(true);
    }//GEN-LAST:event_cmbKodePNIActionPerformed

    private void txtHargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c)||(c == evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c == evt.VK_PERIOD))){
            getToolkit().beep ();
            evt.consume();
        }else if(txtHarga.getText().length() > 8){
            getToolkit().beep ();
            evt.consume ();
        }
    
    }//GEN-LAST:event_txtHargaKeyTyped

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
//        int count = tabelData.getRowCount();
//        if( count > 0){
//            JOptionPane.showMessageDialog(null,"Simpan Semua Data","Error", JOptionPane.INFORMATION_MESSAGE);
//        }else{
//            this.dispose();
//        }
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void cmbKodePNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKodePNIKeyTyped
        // TODO add your handling code here:
        searchPNI(cmbKodePNI.getSelectedItem().toString().trim());
    }//GEN-LAST:event_cmbKodePNIKeyTyped

    private void btnTambahSparepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahSparepartActionPerformed
        // TODO add your handling code here:
        if(cmbKodePNI.getSelectedItem().toString().equals("Pilih PNI")){
            JOptionPane.showMessageDialog(null,"Pilih PNI","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(lblJumlah.getText().toString().equals("") || lblJumlah.getText().toString().equals("0")){
            JOptionPane.showMessageDialog(null,"Isi Jumlah","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtHarga.getText().toString().equals("")){
            JOptionPane.showMessageDialog(null,"Isi Harga","Error", JOptionPane.INFORMATION_MESSAGE);
        }else {
            try{
                autonumber(lblNoBukti.getText().toString());
                query.Input_Detil(Kolom(), Data(), "DOrder");
                query.Input_Detil(Kolom2(), Data2(), "UsulanSparepart");
                //query.EditUsulanSparepart("Nama", cmbKodePNI.getSelectedItem().toString().trim(), "UsulanSparepart", "Nomor", Nomor, "Kode", KodeUsulan);
//                int rowCount = tblModel.getRowCount();
//                for (int i = rowCount - 1; i >= 0; i--) {
//                    tblModel.removeRow(i);
//                }
                bukti = lblNoBukti.getText();
                tampilData(bukti);
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
        clear();
        }
    }//GEN-LAST:event_btnTambahSparepartActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Yakin akan menghapus?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        try{
            query.HapusUsulanSparepart("UsulanSparepart", "Nomor",Nomor,"Kode", lblNoBukti.getText());
            tblModel.removeRow(row);
            clear();
        }catch(Exception e){
            System.err.println("errorr"+e.getMessage());
        }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void lblJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblJumlahKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c)||(c == evt.VK_BACK_SPACE) || (c == evt.VK_DELETE) || (c == evt.VK_PERIOD))){
            getToolkit().beep ();
            evt.consume();
        }else if(lblJumlah.getText().length() > 6){
            getToolkit().beep ();
            evt.consume ();
        }
    }//GEN-LAST:event_lblJumlahKeyTyped

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed

        searchPNI(txtCari.getText());
        btnBatal.setEnabled(true);
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        // TODO add your handling code here:
        searchPNI(txtCari.getText().toString().trim());
        System.out.println("txtcari "+txtCari.getText().toString().trim());
    }//GEN-LAST:event_txtCariKeyTyped

    JTextField txtkode, txtnama;
    int row = 0;
    public void getData() {
        row = tabelData.getSelectedRow();
        Nomor = tblModel.getValueAt(row, 0).toString().trim();
        KodeUsulan = tblModel.getValueAt(row, 1).toString().trim();
        lblNamaBarang.setText(tblModel.getValueAt(row, 2).toString().trim());
        lblJumlah.setText(tblModel.getValueAt(row, 3).toString().trim());
        
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
            java.util.logging.Logger.getLogger(PopUsulanSparepartReg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopUsulanSparepartReg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopUsulanSparepartReg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopUsulanSparepartReg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                
                PopUsulanSparepartReg dialog = new PopUsulanSparepartReg(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambahSparepart;
    private javax.swing.JComboBox<String> cmbKodePNI;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblJenisStock;
    private javax.swing.JTextField lblJumlah;
    private javax.swing.JLabel lblKodePNI;
    private javax.swing.JLabel lblNamaBarang;
    private javax.swing.JLabel lblNoBukti;
    private javax.swing.JTable tabelData;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtHarga;
    // End of variables declaration//GEN-END:variables

    

    

    
}
