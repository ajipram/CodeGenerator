/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import Database.ConnectionHelper;
import Database.Database;

import Kode.Maintenance;
import Kode.Project;
import Kode.Reguler;
import Master.InputExcel;
import Master.MasterAdmin;
import Master.MasterItem;
import Model.Model_MenuUtama;
import java.awt.Color;
import java.sql.Connection;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 *
 * @author AJI-PC
 */
public class MenuUtama extends javax.swing.JFrame {

    
    Register reg = new Register(new javax.swing.JFrame(),true);
    Model_MenuUtama modelUtama = new Model_MenuUtama();
    
    ConnectionHelper conn;
    Connection connect;
    static String id, namanya, nama;
    static String email, password, dataEmail, dataPass, dataNama;
    //static String nama;
    private static final Pattern EMAIL_ADDRESS = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" + "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+" );
    
    public MenuUtama() {
        
        initComponents();
        cekKoneksi();
        
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setIconnya();
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension desktopSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
        pnlLogin.setLocation((desktopSize.width - pnlLogin.getWidth())/2,(desktopSize.height- pnlLogin.getHeight())/2);
        setMenu(false);
        jLabel4.setSize(this.getSize());
        miLogout.setEnabled(false);
        
        txtEmail.setText("aji@aji.com");
        txtPAss.setText("1111");
    }
        
    public void setIconnya(){
        java.net.URL url = ClassLoader.getSystemResource("Image/logopit.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        this.setIconImage(img);
    }

    public void setTxtKoneksi(JLabel txtKoneksi) {
        this.txtKoneksi = txtKoneksi;
    }
    
    public void setMenu(Boolean enable){
        mMaster.setEnabled(enable);
        mKode.setEnabled(enable);
        mPanduan.setEnabled(enable);        
    }
    
    public void login(){
        miLogin.setEnabled(false);
        miLogout.setEnabled(true);
        miRegister.setEnabled(false);
    }
    
    public void setNamanya(String namanya){
        this.nama = namanya;
        txtWelcome.setText("Welcome "+nama);
    }

    public static String getNama() {
        return nama;
    }

    public static String getId() {
        return id;
    }

    public static String getDataEmail() {
        return dataEmail;
    }
    
    public void refresh(){
        dekstop.removeAll();
        dekstop.setVisible(false);
        dekstop.setVisible(true);
        dekstop.add(jLabel4);
    }
    
    public static boolean validasiEmail(String emailStr) {
        Matcher matcher = EMAIL_ADDRESS.matcher(emailStr);
        return matcher.find();
    }
    
    private void validasi(String email){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement state = con.createStatement();
            String query = "Select * from MAdmin where Email = '"+email+"'"; 
            ResultSet rs = state.executeQuery(query);
            while(rs.next()){
                id = rs.getString(1);
                nama = rs.getString(2);
                dataEmail = rs.getString(3);
                dataPass = rs.getString(4);
                modelUtama.setNama(nama);
            }
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
	}    
    }
    
    public void validLogin(){
        email = txtEmail.getText();
        password  = txtPAss.getText();
        validasi(email);

        if(email.equals("") || password.equals("")){
            JOptionPane.showMessageDialog(null,"Data tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(validasiEmail(email)== false){
            JOptionPane.showMessageDialog(null,"Format email salah","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(!email.equals(dataEmail) && password.equals(dataPass)){
            JOptionPane.showMessageDialog(null,"Email salah","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(email.equals(dataEmail) && !password.equals(dataPass)){
            JOptionPane.showMessageDialog(null,"Password salah","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(!email.equals(dataEmail) && !password.equals(dataPass)){
            JOptionPane.showMessageDialog(null,"Data tidak ditemukan","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{

            login();
            setMenu(true);
            setNamanya(nama);
            modelUtama.setNama(nama);
            pnlLogin.hide();
            //JOptionPane.showMessageDialog(null,"Login Berhasil","Info", JOptionPane.INFORMATION_MESSAGE);
            txtEmail.setText("");
            txtPAss.setText("");
        }
    }
    
    public void validLupa(){
        email = txtEmail.getText();
        validasi(email);
        if(txtEmail.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Email tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else if(validasiEmail(email)== false){
            JOptionPane.showMessageDialog(null,"Format email salah","Error", JOptionPane.INFORMATION_MESSAGE);
        }if(!email.equals(dataEmail)){
            JOptionPane.showMessageDialog(null,"Email tidak ditemukan","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            refresh();
            if(!dekstop.isAncestorOf(mUbahPass)){
                mUbahPass = new UbahPassword();
                dekstop.add(mUbahPass);
                try{
                    mUbahPass.setSelected(true);
                    mUbahPass.setVisible(true);
                    Dimension desktopSize = dekstop.getSize();
                    Dimension jInternalFrameSize = mUbahPass.getSize();
                    mUbahPass.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height))/2);

                }catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
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

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtWelcome = new javax.swing.JLabel();
        txtKoneksi = new javax.swing.JLabel();
        pnlLogin = new javax.swing.JPanel();
        btnLupa = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPAss = new javax.swing.JTextField();
        btnRegister = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        dekstop = new javax.swing.JDesktopPane();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mHome = new javax.swing.JMenu();
        miLogin = new javax.swing.JMenuItem();
        miLogout = new javax.swing.JMenuItem();
        miRegister = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miExit = new javax.swing.JMenuItem();
        mMaster = new javax.swing.JMenu();
        miMAdmin = new javax.swing.JMenuItem();
        miInputExel = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        mKode = new javax.swing.JMenu();
        miMaintenance = new javax.swing.JMenuItem();
        miPorject = new javax.swing.JMenuItem();
        miReguler = new javax.swing.JMenuItem();
        miCustom = new javax.swing.JMenuItem();
        mPanduan = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        miTentang = new javax.swing.JMenuItem();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PIT Kode Generator");
        setBackground(new java.awt.Color(0, 204, 204));
        setForeground(new java.awt.Color(0, 204, 204));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logopit.png"))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/bannerpit.png"))); // NOI18N

        txtWelcome.setForeground(new java.awt.Color(0, 102, 204));
        txtWelcome.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtWelcome.setText("Welcome");
        txtWelcome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtWelcomeMouseClicked(evt);
            }
        });

        txtKoneksi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtKoneksi.setText("Koneksi");
        txtKoneksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKoneksiMouseClicked(evt);
            }
        });

        pnlLogin.setBackground(new java.awt.Color(255, 255, 204));

        btnLupa.setText("Lupa Password");
        btnLupa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLupaActionPerformed(evt);
            }
        });
        btnLupa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLupaKeyPressed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setText("Email");

        txtEmail.setBackground(new java.awt.Color(51, 51, 51));
        txtEmail.setForeground(new java.awt.Color(204, 204, 204));
        txtEmail.setCaretColor(new java.awt.Color(0, 204, 102));
        txtEmail.setNextFocusableComponent(txtPAss);
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 102, 51));
        jLabel7.setText("Password");

        txtPAss.setBackground(new java.awt.Color(51, 51, 51));
        txtPAss.setForeground(new java.awt.Color(204, 204, 204));
        txtPAss.setCaretColor(new java.awt.Color(0, 153, 153));
        txtPAss.setNextFocusableComponent(btnLogin);
        txtPAss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPAssActionPerformed(evt);
            }
        });
        txtPAss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPAssKeyPressed(evt);
            }
        });

        btnRegister.setText("Register");
        btnRegister.setNextFocusableComponent(btnLupa);
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        btnRegister.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnRegisterKeyPressed(evt);
            }
        });

        btnLogin.setText("Login");
        btnLogin.setNextFocusableComponent(btnRegister);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginKeyPressed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 102, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/login.png"))); // NOI18N
        jLabel5.setText("Login");

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addComponent(txtPAss, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLupa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegister)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLogin)
                            .addComponent(btnRegister))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txtPAss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnLupa)))
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(129, 129, 129))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKoneksi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtWelcome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKoneksi))
                    .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        dekstop.setBackground(new java.awt.Color(204, 255, 255));
        dekstop.setOpaque(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/wallpaper.jpg"))); // NOI18N
        jLabel4.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        dekstop.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout dekstopLayout = new javax.swing.GroupLayout(dekstop);
        dekstop.setLayout(dekstopLayout);
        dekstopLayout.setHorizontalGroup(
            dekstopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dekstopLayout.setVerticalGroup(
            dekstopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenuBar1.setBackground(new java.awt.Color(0, 102, 102));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(298, 40));

        mHome.setBackground(new java.awt.Color(0, 102, 102));
        mHome.setForeground(new java.awt.Color(255, 255, 255));
        mHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnhome.png"))); // NOI18N
        mHome.setText("Home");
        mHome.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        mHome.setNextFocusableComponent(mMaster);

        miLogin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        miLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        miLogin.setText("Log In");
        miLogin.setNextFocusableComponent(miRegister);
        miLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLoginActionPerformed(evt);
            }
        });
        mHome.add(miLogin);

        miLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        miLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/hapus.png"))); // NOI18N
        miLogout.setText("Log Out");
        miLogout.setNextFocusableComponent(miRegister);
        miLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogoutActionPerformed(evt);
            }
        });
        mHome.add(miLogout);

        miRegister.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        miRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/register.png"))); // NOI18N
        miRegister.setText("Register");
        miRegister.setNextFocusableComponent(miExit);
        miRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRegisterActionPerformed(evt);
            }
        });
        mHome.add(miRegister);
        mHome.add(jSeparator1);

        miExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        miExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/keluar.png"))); // NOI18N
        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        mHome.add(miExit);

        jMenuBar1.add(mHome);

        mMaster.setBackground(new java.awt.Color(0, 102, 102));
        mMaster.setForeground(new java.awt.Color(255, 255, 255));
        mMaster.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnmaster.png"))); // NOI18N
        mMaster.setText("Master");
        mMaster.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        mMaster.setNextFocusableComponent(mKode);

        miMAdmin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        miMAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/madmin.png"))); // NOI18N
        miMAdmin.setText("Master Admin");
        miMAdmin.setNextFocusableComponent(mKode);
        miMAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMAdminActionPerformed(evt);
            }
        });
        mMaster.add(miMAdmin);

        miInputExel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        miInputExel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/copy.png"))); // NOI18N
        miInputExel.setText("Item Project");
        miInputExel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInputExelActionPerformed(evt);
            }
        });
        mMaster.add(miInputExel);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/tambah.png"))); // NOI18N
        jMenuItem1.setText("Input Excel");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mMaster.add(jMenuItem1);

        jMenuBar1.add(mMaster);

        mKode.setBackground(new java.awt.Color(0, 102, 102));
        mKode.setForeground(new java.awt.Color(255, 255, 255));
        mKode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnkode.png"))); // NOI18N
        mKode.setText("Kode");
        mKode.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        mKode.setNextFocusableComponent(mPanduan);

        miMaintenance.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        miMaintenance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kmaintenance.png"))); // NOI18N
        miMaintenance.setText("Maintenance");
        miMaintenance.setNextFocusableComponent(miPorject);
        miMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMaintenanceActionPerformed(evt);
            }
        });
        mKode.add(miMaintenance);

        miPorject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        miPorject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kproject.png"))); // NOI18N
        miPorject.setText("Project");
        miPorject.setNextFocusableComponent(miReguler);
        miPorject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPorjectActionPerformed(evt);
            }
        });
        mKode.add(miPorject);

        miReguler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        miReguler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kemergency.png"))); // NOI18N
        miReguler.setText("Reguler");
        miReguler.setNextFocusableComponent(mPanduan);
        miReguler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRegulerActionPerformed(evt);
            }
        });
        mKode.add(miReguler);

        miCustom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        miCustom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/generate.png"))); // NOI18N
        miCustom.setText("Custom");
        miCustom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCustomActionPerformed(evt);
            }
        });
        mKode.add(miCustom);

        jMenuBar1.add(mKode);

        mPanduan.setBackground(new java.awt.Color(0, 102, 102));
        mPanduan.setForeground(new java.awt.Color(255, 255, 255));
        mPanduan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnpanduan.png"))); // NOI18N
        mPanduan.setText("Panduan");
        mPanduan.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        mPanduan.setNextFocusableComponent(mHome);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/panduan.png"))); // NOI18N
        jMenuItem9.setText("Panduan");
        jMenuItem9.setNextFocusableComponent(miTentang);
        mPanduan.add(jMenuItem9);

        miTentang.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        miTentang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/about.png"))); // NOI18N
        miTentang.setText("Tentang");
        miTentang.setNextFocusableComponent(mHome);
        mPanduan.add(miTentang);

        jMenuBar1.add(mPanduan);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dekstop)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dekstop))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    JInternalFrame mMaintenance;
    private void miMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMaintenanceActionPerformed
        refresh();
        if(!dekstop.isAncestorOf(mMaintenance)){
            mMaintenance= new Maintenance();
            dekstop.add(mMaintenance);
            try{
                mMaintenance.setSelected(true);
                mMaintenance.setVisible(true);
                mMaintenance.setSize(850, 640);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mMaintenance.getSize();
                mMaintenance.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height))/2);
            
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miMaintenanceActionPerformed

    private void miLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLoginActionPerformed
        pnlLogin.show();
        refresh();
        
    }//GEN-LAST:event_miLoginActionPerformed

    private void txtKoneksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKoneksiMouseClicked
        cekKoneksi();
    }//GEN-LAST:event_txtKoneksiMouseClicked

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        
        if (JOptionPane.showConfirmDialog(null, "Anda akan menutup aplikasi?", "Peringatan",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        } 
    }//GEN-LAST:event_miExitActionPerformed

    
    private void miLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLogoutActionPerformed
        setMenu(false);
        miLogin.setEnabled(true);
        miLogout.setEnabled(false);
        miRegister.setEnabled(true);
        pnlLogin.show();
        refresh();
        
    }//GEN-LAST:event_miLogoutActionPerformed

    private void miRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRegisterActionPerformed
        reg.setVisible(true);
    }//GEN-LAST:event_miRegisterActionPerformed

    private void txtWelcomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtWelcomeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWelcomeMouseClicked

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtPAssActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPAssActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPAssActionPerformed
   
    
    
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        validLogin();     
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed

        reg.setVisible(true);
    }//GEN-LAST:event_btnRegisterActionPerformed
    
    JInternalFrame mProject;
    private void miPorjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPorjectActionPerformed
       refresh();
        if(!dekstop.isAncestorOf(mProject)){
            mProject = new Project();
            dekstop.add(mProject);
            try{
                mProject.setSelected(true);
                mProject.setVisible(true);
                mProject.setSize(1000, 650);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mProject.getSize();
                mProject.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height))/2);
            
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miPorjectActionPerformed
    JInternalFrame mEmergency;
    private void miRegulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRegulerActionPerformed
        refresh();
        if(!dekstop.isAncestorOf(mEmergency)){
            mEmergency = new Reguler();
            dekstop.add(mEmergency);
            try{
                mEmergency.setSelected(true);
                mEmergency.setVisible(true);
                mEmergency.setSize(1000, 660);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mEmergency.getSize();
                mEmergency.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height))/2);
            
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miRegulerActionPerformed
    
    JInternalFrame mUbahPass;
    private void btnLupaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLupaActionPerformed
        validLupa();
    }//GEN-LAST:event_btnLupaActionPerformed
    JInternalFrame mMAdmin;
    private void miMAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMAdminActionPerformed
        refresh();
        if(!dekstop.isAncestorOf(mMAdmin)){
            mMAdmin = new MasterAdmin();
            dekstop.add(mMAdmin);
            try{
                mMAdmin.setSelected(true);
                mMAdmin.setVisible(true);
                mMAdmin.setSize(800, 600);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mMAdmin.getSize();
                mMAdmin.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height)-20)/2);

            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miMAdminActionPerformed

    private void btnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            validLogin();
        }
    }//GEN-LAST:event_btnLoginKeyPressed

    private void btnRegisterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRegisterKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            reg.setVisible(true);
        }
    }//GEN-LAST:event_btnRegisterKeyPressed

    private void btnLupaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLupaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            validLupa();
        }
    }//GEN-LAST:event_btnLupaKeyPressed

    private void txtPAssKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPAssKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            validLogin();
        }
        
    }//GEN-LAST:event_txtPAssKeyPressed

    private void miCustomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCustomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miCustomActionPerformed
    JInternalFrame mItemProject;
    private void miInputExelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInputExelActionPerformed
        // TODO add your handling code here:
        refresh();
        if(!dekstop.isAncestorOf(mItemProject)){
            mItemProject = new MasterItem();
            dekstop.add(mItemProject);
            try{
                mItemProject.setSelected(true);
                mItemProject.setVisible(true);
                mItemProject.setSize(800, 600);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mItemProject.getSize();
                mItemProject.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height)-20)/2);

            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miInputExelActionPerformed
    JInternalFrame mInput;
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        refresh();
        if(!dekstop.isAncestorOf(mInput)){
            mInput = new InputExcel();
            dekstop.add(mInput);
            try{
                mInput.setSelected(true);
                mInput.setVisible(true);
                mInput.setSize(800, 600);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mInput.getSize();
                mInput.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height)-20)/2);

            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuUtama().setVisible(true);
            }
        });
    }
    
    Database dbsetting;
    String driver, database, user, pass;
    private void cekKoneksi() {
        dbsetting = new Database();
        driver    = dbsetting.SettingPanel("DBDriver");
        database  = dbsetting.SettingPanel("DBServer");
        user      = dbsetting.SettingPanel("DBUsername");
        pass      = dbsetting.SettingPanel("DBPassword");
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(database, user, pass);
            if(!con.isClosed()){
                txtKoneksi.setText("Online");
                txtKoneksi.setForeground(Color.GREEN);
                con.close();
            }
            
        }catch(SQLException e){
            txtKoneksi.setText("Offline");
            txtKoneksi.setForeground(Color.RED);
            System.err.println("SQL Error"+e.getMessage());
        }catch(ClassNotFoundException e){
            txtKoneksi.setText("Offline");
            txtKoneksi.setForeground(Color.RED);
            System.err.println("Class Error"+e.getMessage());
        }catch(Exception e){
            txtKoneksi.setText("Offline");
            txtKoneksi.setForeground(Color.RED);
            System.err.println("Exception Error"+e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLupa;
    private javax.swing.JButton btnRegister;
    private javax.swing.JDesktopPane dekstop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenu mHome;
    private javax.swing.JMenu mKode;
    private javax.swing.JMenu mMaster;
    private javax.swing.JMenu mPanduan;
    private javax.swing.JMenuItem miCustom;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miInputExel;
    private javax.swing.JMenuItem miLogin;
    private javax.swing.JMenuItem miLogout;
    private javax.swing.JMenuItem miMAdmin;
    private javax.swing.JMenuItem miMaintenance;
    private javax.swing.JMenuItem miPorject;
    private javax.swing.JMenuItem miRegister;
    private javax.swing.JMenuItem miReguler;
    private javax.swing.JMenuItem miTentang;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JLabel txtKoneksi;
    private javax.swing.JTextField txtPAss;
    private javax.swing.JLabel txtWelcome;
    // End of variables declaration//GEN-END:variables

    
}
