/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Popup;

import Database.Database;
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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AJI-PC
 */
public class PopItemProject extends javax.swing.JDialog {

    Database dbsetting;
    String driver, database, user, pass;
    String no, noseri, produk, merk, model, sizenya,nobukti;
    String data[] = new String[6];
    Maintenance main;
    Project project;
    
    public PopItemProject(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.yellow);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        tabelCustomer.setModel(tblModel);
        Tabel(tabelCustomer, new int[]{100,150,100,100,100,100});
        tabelCustomer.setAutoCreateRowSorter(true);
        tampilItem();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        tabelCustomer.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        tabelCustomer.getTableHeader().setBackground(Color.decode("#ffbf80"));
        nobukti = Project.getNoBukti();
        System.err.println("rownya "+row);
        tabelCustomer.remove(row);
    }
    
    private void tampilItem(){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from ItemProject where NoBukti is null or NoBukti = ''"; 
            ResultSet rs = state.executeQuery(query);
            if(rs.next()==false){
                JOptionPane.showMessageDialog(null,"Tidak ada data","Info",JOptionPane.INFORMATION_MESSAGE);
            }else{
            while(rs.next()){
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                
                tblModel.addRow(data);
            }
            
                
            }
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
            String query = "Select * from ItemProject where "+ cmb +" like '%"+ key +"%', where Nobukti is null"; 
            ResultSet rs = state.executeQuery(query);
            System.err.println(query);
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
        jLabel2 = new javax.swing.JLabel();
        cmbCari = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelCustomer = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 153));

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        jLabel1.setBackground(new java.awt.Color(255, 255, 153));
        jLabel1.setFont(new java.awt.Font("Rockwell", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Item Project");

        jPanel1.setBackground(new java.awt.Color(255, 204, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setForeground(new java.awt.Color(204, 51, 0));
        jLabel2.setText("Cari berdasarkan");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 14, -1, -1));

        cmbCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Seri", "Produk", "Merk" }));
        jPanel1.add(cmbCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 9, 75, -1));

        jLabel3.setForeground(new java.awt.Color(204, 51, 0));
        jLabel3.setText("Keyword");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 14, -1, -1));

        txtKeyword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKeywordActionPerformed(evt);
            }
        });
        jPanel1.add(txtKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 10, 150, -1));

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        jPanel1.add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 6, -1, -1));

        tabelCustomer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabelCustomer.setForeground(new java.awt.Color(0, 0, 0));
        tabelCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nomor", "No Seri", "Produk", "Merk", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelCustomerMouseClicked(evt);
            }
        });
        tabelCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelCustomerKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabelCustomer);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
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

    private void tabelCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelCustomerMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            getData();
            dispose();
            
        }
    }//GEN-LAST:event_tabelCustomerMouseClicked

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        Object selectedItem = cmbCari.getSelectedItem();
        if (selectedItem != null){
            String selectedItemStr = selectedItem.toString();
            searchKode(selectedItemStr, txtKeyword.getText());       
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void tabelCustomerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelCustomerKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            getData();
            dispose();
        }
        
    }//GEN-LAST:event_tabelCustomerKeyPressed

    JTextField txtkode, txtnama;
    int row = 0;
    String tempNoseri;
    public void getData() {
        row     = tabelCustomer.getSelectedRow();
        no      = tblModel.getValueAt(row, 0).toString();
        noseri  = tblModel.getValueAt(row, 1).toString();    
        produk  = tblModel.getValueAt(row, 2).toString();   
        merk    = tblModel.getValueAt(row, 3).toString();   
        model   = tblModel.getValueAt(row, 4).toString();   
        sizenya = tblModel.getValueAt(row, 5).toString();
        //tempNoseri = noseri;
        
        System.out.println("get no seri getdata "+noseri);
        this.dispose();
        
    }

    public String getNo() {
        return no;
    }

    public String getNoseri() {
        System.out.println("get no seri get "+noseri);
        return noseri;
    }
    
    public String getProduk() {
        return produk;
    }

    public String getMerk() {
        return merk;
    }
    
    public String getModel() {
        return model;
    }

    public String getSizenya() {
        return sizenya;
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
            java.util.logging.Logger.getLogger(PopItemProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopItemProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopItemProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopItemProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                
                PopItemProject dialog = new PopItemProject(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCari;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelCustomer;
    private javax.swing.JTextField txtKeyword;
    // End of variables declaration//GEN-END:variables

    

    
}
