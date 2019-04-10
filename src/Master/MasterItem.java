/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master;

import Database.Database;
import Database.Fungsi_Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author AJI-PC
 */
public class MasterItem extends javax.swing.JInternalFrame {

    
    int row = 0;
    
    Database dbsetting;
    String driver, database, user, pass;
    String no, kode, nama;
    String kdCust, nmCust, telp, kdCab, nmCab, kdWil, nmWil, kdAdm, nmAdm;
    Boolean dikunjungi;
    Float notelp;
    
    String data[] = new String[6];
    Fungsi_Query query = new Fungsi_Query();
    
    String[] kolom  = new String[6];
    String[] isi    = new String[6];
    
    public String[] Kolom(){
        kolom[0] = "NoUrut";
        kolom[1] = "NoSeri";
        kolom[2] = "Produk";
        kolom[3] = "Merk";
        kolom[4] = "Tipe";
        kolom[5] = "size";
     
        return kolom;
    }
    
    public String[] Data(){
        
        isi[0] = txtNourut.getText();
        isi[1] = txtNoseri.getText();
        isi[2] = txtProduk.getText();
        isi[3] = txtMerk.getText();
        isi[4] = txtModel.getText();
        isi[5] = txtSize.getText();
        return isi;
    }
    
    
    public MasterItem() {
        initComponents();
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        
        tabel.setModel(tblModel);
        tabel.setAutoCreateRowSorter(true);
        Tabel(tabel, new int[]{100,150,100,100,100,100});
        sortTable();
        tampilTabel();
        setCellsAlignment();
        
        txtNourut.setText(autonumber());
        
    }
    
    private void sortTable(){
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabel.getModel());
        tabel.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        int columnIndexToSort = 0;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }
 
    public String autonumber(){
        System.err.println("auto ");
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select MAX(NoUrut) from ItemProject"; 
            ResultSet rs = state.executeQuery(query);
            int max = 0;
            while(rs.next()){
                max = rs.getInt(1);    
            }
            System.err.println("maxxxxx "+max);
            max=max+1;
            no = String.format("%02d", max);
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
            tabel.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
    
    private void tampilTabel(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from ItemProject"; 
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                int nokod = Integer.parseInt(rs.getString(1));
                String nokode = String.format("%02d", nokod);
                data[0] = nokode;
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
    
    String namakolom;
    private void search(String cmb, String key){
        
        if(cmb.equals("No Seri")){
            namakolom = "NoSeri";
        }else if (cmb.equals("Produk")){
            namakolom = "Produk";
        }else{
            namakolom = "Merk";
        }
        
        try{
            
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            tblModel.setRowCount(0);
            String query = "Select * from MAdmin where "+ namakolom +" like '%"+ key +"%'"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                int nokod = Integer.parseInt(rs.getString(1));
                String nokode = String.format("%02d", nokod);
                data[0] = nokode;
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
    
    public void clear(){
        txtNoseri.setText("");
        txtProduk.setText("");
        txtMerk.setText("");
        txtModel.setText("");
        txtSize.setText("");
        txtKeyword.setText("");
        txtNav.setText("");
        btnSimpan.setEnabled(true);
        btnBatal.setEnabled(true);
        btnHapus.setEnabled(false);
        btnUbah.setEnabled(false);
        btnNavFirst.setEnabled(false);
        btnNavPrev.setEnabled(false);
        btnNavNext.setEnabled(false);
        btnNavLast.setEnabled(false);
        txtNourut.setText(autonumber());
    }
    
    String kodenya;
    public void getData() {
        row = tabel.getSelectedRow();
        kodenya = tblModel.getValueAt(tabel.convertRowIndexToModel( row),0).toString();
        txtNourut.setText(kodenya);
        tampil(kodenya);
        
    }
    
    public void tampil(String kode){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from ItemProject where NoUrut = '" + kode + "'";
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                txtNoseri.setText(rs.getString(2));
                txtProduk.setText(rs.getString(3));
                txtMerk  .setText(rs.getString(4));
                txtModel .setText(rs.getString(5));
                txtSize  .setText(rs.getString(6));  
            }
            
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    String kd;
    int pos;
    String kdnya;
    private void getDataNav(int row){
        kd = tblModel.getValueAt(row, 0).toString().trim();
        txtNav.setText(kd);
        tampil(kd);
    }
    
    private void getDataPrev(){
        
        row--;
        if(row > 0){
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya); 
            tabel.setRowSelectionInterval(row, row);
        }else{
            row = 0;
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya);
            JOptionPane.showMessageDialog(null, "Data Pertama");
            tabel.setRowSelectionInterval(row, row);
         }
        
    }
    
    private void getDataNext(){
        
        row++;
        if(row < BindList().size()){
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya); 
            tabel.setRowSelectionInterval(row, row);
        }else{
            row = BindList().size() - 1;
            kdnya = tblModel.getValueAt(row, 0).toString().trim();
            tabel.setRowSelectionInterval(row,row);
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            tampil(kdnya);
            JOptionPane.showMessageDialog(null, "Data Terakhir");
         }
    }
    
    public List<String> BindList(){
        String datanya;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoUrut from HOrder order by NoUrut asc";
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtKeyword = new javax.swing.JTextField();
        cmbCari = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        btnCari = new javax.swing.JButton();
        btnNavFirst = new javax.swing.JButton();
        btnNavPrev = new javax.swing.JButton();
        txtNav = new javax.swing.JTextField();
        btnNavNext = new javax.swing.JButton();
        btnNavLast = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNourut = new javax.swing.JTextField();
        txtNoseri = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSize = new javax.swing.JTextField();
        txtMerk = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtProduk = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();

        setBackground(new java.awt.Color(153, 204, 255));
        setTitle("Item Project");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kproject.png"))); // NOI18N

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(153, 204, 255));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Item Project");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(153, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtKeyword.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtKeywordInputMethodTextChanged(evt);
            }
        });

        cmbCari.setForeground(new java.awt.Color(51, 51, 51));
        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Seri", "Produk", "Merk" }));
        cmbCari.setToolTipText("");

        jLabel8.setForeground(new java.awt.Color(0, 51, 204));
        jLabel8.setText("Cari :");

        btnCari.setForeground(new java.awt.Color(51, 51, 51));
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

        btnNavFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_first.png"))); // NOI18N
        btnNavFirst.setEnabled(false);
        btnNavFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavFirstActionPerformed(evt);
            }
        });

        btnNavPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_prev.png"))); // NOI18N
        btnNavPrev.setEnabled(false);
        btnNavPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavPrevActionPerformed(evt);
            }
        });

        txtNav.setForeground(new java.awt.Color(0, 102, 102));

        btnNavNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_next.png"))); // NOI18N
        btnNavNext.setEnabled(false);
        btnNavNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavNextActionPerformed(evt);
            }
        });

        btnNavLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/nav_last.png"))); // NOI18N
        btnNavLast.setEnabled(false);
        btnNavLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNavLastActionPerformed(evt);
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
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnRefresh))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnNavFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnNavPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNav, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNavNext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNavLast, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNavLast, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNavFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNavPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNavNext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("No Urut");

        txtNourut.setForeground(new java.awt.Color(0, 0, 0));
        txtNourut.setEnabled(false);

        jLabel3.setForeground(new java.awt.Color(0, 51, 204));
        jLabel3.setText("Nomor Seri");

        jLabel4.setForeground(new java.awt.Color(0, 51, 204));
        jLabel4.setText("Merk");

        jLabel5.setForeground(new java.awt.Color(0, 51, 204));
        jLabel5.setText("Size");

        jLabel6.setForeground(new java.awt.Color(0, 51, 204));
        jLabel6.setText("Produk");

        jLabel7.setForeground(new java.awt.Color(0, 51, 204));
        jLabel7.setText("Model");

        jLabel9.setForeground(new java.awt.Color(0, 51, 204));
        jLabel9.setText("Liter");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNourut, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(txtNoseri)
                    .addComponent(txtProduk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9))
                            .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMerk, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtMerk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNourut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNoseri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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

        btnUbah.setForeground(new java.awt.Color(0, 153, 153));
        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/edit.png"))); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setEnabled(false);
        btnUbah.setPreferredSize(new java.awt.Dimension(100, 36));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setPreferredSize(new java.awt.Dimension(100, 36));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        tabel.setBackground(new java.awt.Color(153, 204, 255));
        tabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKeywordInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtKeywordInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeywordInputMethodTextChanged

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        Object selectedItem = cmbCari.getSelectedItem();
        if (selectedItem != null){
            String selectedItemStr = selectedItem.toString();
            search(selectedItemStr, txtKeyword.getText());
        }
        btnBatal.setEnabled(true);
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Yakin akan menghapus?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        try{
            query.Hapus("ItemProject", "NoUrut", txtNourut.getText());
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

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        query.Edit(Kolom(), Data(), "ItemProject", "NoUrut", txtNourut.getText());
        int rowCount = tblModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
        tampilTabel();
        clear();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        
        if(txtNoseri.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Nomor Seri tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtSize.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Size tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtMerk.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Merk tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(txtModel.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Model tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try{
                query.Input_Detil(Kolom(), Data(), "ItemProject");
                int rowCount = tblModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    tblModel.removeRow(i);
                }
                tampilTabel();
                clear();
                
            }catch(Exception e){
                System.err.println("errorr"+e.getMessage());
            }
        }

    }//GEN-LAST:event_btnSimpanActionPerformed

    private void tabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMouseClicked
        if(evt.getClickCount()==1){
            getData();
            btnHapus.setEnabled(true);
            btnBatal.setEnabled(true);
            btnSimpan.setEnabled(false);
            btnUbah.setEnabled(true);
            btnNavFirst.setEnabled(true);
            btnNavPrev.setEnabled(true);
            btnNavNext.setEnabled(true);
            btnNavLast.setEnabled(true);
        }
    }//GEN-LAST:event_tabelMouseClicked

    private void btnNavFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavFirstActionPerformed
        // TODO add your handling code here:
        pos = 0;
        getDataNav(pos);
        System.err.println("pos first = "+pos);
        tabel.setRowSelectionInterval(0, 0);
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
        tabel.setRowSelectionInterval(pos, pos);
    }//GEN-LAST:event_btnNavLastActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
 
        int rowCount = tblModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
        tampilTabel();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnNavFirst;
    private javax.swing.JButton btnNavLast;
    private javax.swing.JButton btnNavNext;
    private javax.swing.JButton btnNavPrev;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTable tabel;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtMerk;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtNav;
    private javax.swing.JTextField txtNoseri;
    private javax.swing.JTextField txtNourut;
    private javax.swing.JTextField txtProduk;
    private javax.swing.JTextField txtSize;
    // End of variables declaration//GEN-END:variables

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
        new String[] {"No Urut", "Nomor Seri", "Produk", "Merk", "Model", "Size"}){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel tblModel = getDefaultTabelModel();
    

}
