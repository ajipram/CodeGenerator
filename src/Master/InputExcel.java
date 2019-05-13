/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master;

import Database.Database;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author AJI-PC
 */
public class InputExcel extends javax.swing.JInternalFrame {
    
    Database dbsetting;
    String driver, database, user, pass;
    int row = 0;
    String no, kode, nama;
    String noseri,produk,merk,tipe,size;
    
     String data[] = new String[7];
    
    public InputExcel() {
        initComponents();
        
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        
        tabel.setModel(tblModel);
        tabel.setAutoCreateRowSorter(true);
        Tabel(tabel, new int[]{100,150,100,100,100,100,100});
        sortTable();
        tampilTabel();
        setCellsAlignment();
        
        
    }
    
    public static Vector read(String fileName)    {
        Vector cellVectorHolder = new Vector();
        try{
            FileInputStream myInput = new FileInputStream(fileName);
            //POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while(rowIter.hasNext()){
                XSSFRow myRow = (XSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                //Vector cellStoreVector=new Vector();
                List list = new ArrayList();
                while(cellIter.hasNext()){
                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    list.add(myCell);
                }
                cellVectorHolder.addElement(list);
            }
            myWorkBook.close();
            myInput.close();
        }catch (Exception e){e.printStackTrace(); }
        return cellVectorHolder;
    }
    private void saveToDatabase(Vector dataHolder) {

        for(Iterator iterator = dataHolder.iterator();iterator.hasNext();) {
            List list = (List) iterator.next();
            noseri = list.get(0).toString().trim();
            produk = list.get(1).toString().trim();
            merk = list.get(2).toString().trim();
            tipe = list.get(3).toString().trim();
            size = list.get(4).toString().trim();
            if(checkExists(noseri) == true){
                 JOptionPane.showMessageDialog(null,"Nomor seri sudah terdaftar","Error", JOptionPane.INFORMATION_MESSAGE);

            }else{

            try {
                Class.forName(driver);
                Connection con = DriverManager.getConnection(database, user, pass);
                System.out.println("connection made...");
//                PreparedStatement stmt=con.prepareStatement("INSERT INTO ItemProject(NoUrut,NoSeri,Produk,Merk,Tipe,Size,NoBukti) VALUES(?,?,?,?,?,?,?)");
//                stmt.setString(1, autonumber());
//                stmt.setString(2, noseri);
//                stmt.setString(3, produk);
//                stmt.setString(4, merk);
//                stmt.setString(5, tipe);
//                stmt.setString(6, size);
//                stmt.setString(7, "");
//                stmt.executeUpdate();
                
                String sql = "INSERT INTO ItemProject(NoUrut,NoSeri,Produk,Merk,Tipe,Size,NoBukti) VALUES("+
                        autonumber()+",'"+noseri+"','"+produk+"','"+merk+"','"+tipe+"','"+size+"','')";
                System.err.println("sql "+sql);
                PreparedStatement stmt=con.prepareStatement(sql);
                stmt.executeUpdate();
                
                System.out.println("Data is inserted");
                stmt.close();
                con.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            }
        }
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
            int rowCount = tblModel.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                tblModel.removeRow(i);
            }
            while(rs.next()){
                int nokod2 = Integer.parseInt(rs.getString(1));
                String nokode2 = String.format("%02d", nokod2);

                data[0] = nokode2;
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                data[6] = rs.getString(7);

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
            String query = "Select * from ItemProject where "+ namakolom +" like '%"+ key +"%'"; 
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
                data[6] = rs.getString(7);
                tblModel.addRow(data);
            }
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}
    }
    
    String kodenya;
    public void getData() {
        row = tabel.getSelectedRow();
        kodenya = tblModel.getValueAt(tabel.convertRowIndexToModel( row),1).toString();
        txtNav.setText(kodenya);
  
        
    }
    
    public void clear(){
       
        txtKeyword.setText("");
        txtNav.setText("");
        
        btnNavFirst.setEnabled(false);
        btnNavPrev.setEnabled(false);
        btnNavNext.setEnabled(false);
        btnNavLast.setEnabled(false);
    
    }
    
   
    
    String kd;
    int pos;
    String kdnya;
    private void getDataNav(int row){
        kd = tblModel.getValueAt(row, 1).toString().trim();
        txtNav.setText(kd);
    
    }
    
    private void getDataPrev(){
        
        row--;
        if(row > 0){
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
           
            tabel.setRowSelectionInterval(row, row);
        }else{
            row = 0;
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
          
            JOptionPane.showMessageDialog(null, "Data Pertama");
            tabel.setRowSelectionInterval(row, row);
         }
        
    }
    
    private void getDataNext(){
        
        row++;
        if(row < BindList().size()){
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
            
            tabel.setRowSelectionInterval(row, row);
        }else{
            row = BindList().size() - 1;
            kdnya = tblModel.getValueAt(row, 1).toString().trim();
            tabel.setRowSelectionInterval(row,row);
            txtNav.setText(kdnya);
            System.err.println("kodenya = "+kdnya);
   
            JOptionPane.showMessageDialog(null, "Data Terakhir");
         }
    }
    
    public List<String> BindList(){
        String datanya;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select NoUrut from ItemProject order by NoUrut asc";
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
    
    public boolean checkExists(String serial) {
        ResultSet rs;
        int x = 0;
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();

                String query = "Select * from ItemProject where NoSeri = '"+serial+"'";
                rs = state.executeQuery(query);
                if(rs.next()){
                    x = 1;
                }else{
                    x = 0;
                }

        } catch (Exception ex) {
             System.err.println(ex.getMessage());
        }
       
        return x > 0 ? true : false;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();
        btnKeluar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnAmbil = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblFile = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 255, 204));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Input Data ");

        jPanel3.setBackground(new java.awt.Color(153, 255, 153));
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

        tabel.setBackground(new java.awt.Color(204, 255, 204));
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

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/keluar.png"))); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.setOpaque(false);
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 36));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAmbil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/panduan.png"))); // NOI18N
        btnAmbil.setText("Ambil Data");
        btnAmbil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAmbilActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 102, 51));
        jLabel2.setText("File :");

        lblFile.setForeground(new java.awt.Color(0, 102, 51));

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/tambah.png"))); // NOI18N
        btnTambah.setText("Tambahkan Data");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAmbil)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnTambah)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTambah, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAmbil)
                        .addComponent(jLabel2)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 171, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        
        if(f.length > 0){
            System.out.println(file);
            String fileName=file;
            Vector dataHolder=read(fileName);
            saveToDatabase(dataHolder);
            JOptionPane.showMessageDialog(null, "File "+file+" telah dikirim","Info",JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Gagal ditambahkan","Info",JOptionPane.INFORMATION_MESSAGE);
        }
        tampilTabel();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtKeywordInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtKeywordInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeywordInputMethodTextChanged

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        Object selectedItem = cmbCari.getSelectedItem();
        if (selectedItem != null){
            String selectedItemStr = selectedItem.toString();
            search(selectedItemStr, txtKeyword.getText());
        }
    
    }//GEN-LAST:event_btnCariActionPerformed

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

//        int rowCount = tblModel.getRowCount();
//        for (int i = rowCount - 1; i >= 0; i--) {
//            tblModel.removeRow(i);
//        }
        tampilTabel();
        txtKeyword.setText("");
        txtNav.setText("");
        btnNavFirst.setEnabled(false);
        btnNavLast.setEnabled(false);
        btnNavNext.setEnabled(false);
        btnNavPrev.setEnabled(false);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void tabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMouseClicked
        getData();
        btnNavFirst.setEnabled(true);
        btnNavLast.setEnabled(true);
        btnNavNext.setEnabled(true);
        btnNavPrev.setEnabled(true);
    }//GEN-LAST:event_tabelMouseClicked
    String file;
    File[] f;
    private void btnAmbilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAmbilActionPerformed
        // TODO add your handling code here:
        FileDialog fd = new FileDialog(new JFrame());
        fd.setVisible(true);
        f = fd.getFiles();
        file = fd.getFiles()[0].getAbsolutePath();
        lblFile.setText(file);
    }//GEN-LAST:event_btnAmbilActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAmbil;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnNavFirst;
    private javax.swing.JButton btnNavLast;
    private javax.swing.JButton btnNavNext;
    private javax.swing.JButton btnNavPrev;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cmbCari;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFile;
    private javax.swing.JTable tabel;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtNav;
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
        new String[] {"No Urut", "Nomor Seri", "Produk", "Merk", "Model", "Size", "No Bukti"}){
            boolean[] canEdit = new boolean[]{
                false,false,false,false,false,false,false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel tblModel = getDefaultTabelModel();
}
