/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import Database.ConnectionHelper;
import Database.Database;
import Kode.Emergency;
import Kode.Maintenance;
import Kode.Project;
import Master.MasterAdmin;
import Model.Model_MenuUtama;
import static codegenerator.Login.nama;
import static codegenerator.LupaPassword.email;
import java.awt.Color;
import java.sql.Connection;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
    Login log = new Login(new javax.swing.JFrame(),true);
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
        
        miLogout.setEnabled(false);
        
        File file = new File(".");
        for(String fileNames : file.list()) System.out.println(fileNames);
    
    }
    
    
    
    public void setIconnya(){
        java.net.URL url = ClassLoader.getSystemResource("Image/logopit.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        this.setIconImage(img);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

    public JLabel getTxtKoneksi() {
        return txtKoneksi;
    }

    public void setTxtKoneksi(JLabel txtKoneksi) {
        this.txtKoneksi = txtKoneksi;
    }

    public JDesktopPane getDekstop() {
        return dekstop;
    }

    public void setDekstop(JDesktopPane dekstop) {
        this.dekstop = dekstop;
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
        namanya = log.getDataNama();
        System.err.println("namanya "+namanya);
        txtWelcome.setText("Welcome : "+namanya);
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
                System.err.println("data email "+dataEmail);
            } 
            MenuUtama.nama = nama;
            System.err.println("data nama = "+nama);
            rs.close();
            state.close();
            con.close();
        }catch(Exception ex){
            System.out.println("tidak ada koneksi "+ex);
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
        dekstop = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtWelcome = new javax.swing.JLabel();
        txtKoneksi = new javax.swing.JLabel();
        pnlLogin = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPAss = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblKlik = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mHome = new javax.swing.JMenu();
        miLogin = new javax.swing.JMenuItem();
        miLogout = new javax.swing.JMenuItem();
        miRegister = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miExit = new javax.swing.JMenuItem();
        mMaster = new javax.swing.JMenu();
        miMAdmin = new javax.swing.JMenuItem();
        miMTeknisi = new javax.swing.JMenuItem();
        miMitra = new javax.swing.JMenuItem();
        mKode = new javax.swing.JMenu();
        miMaintenance = new javax.swing.JMenuItem();
        miPorject = new javax.swing.JMenuItem();
        miEmergency = new javax.swing.JMenuItem();
        mPanduan = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PIT Kode Generator");
        setBackground(new java.awt.Color(0, 204, 204));
        setForeground(new java.awt.Color(0, 204, 204));

        dekstop.setBackground(new java.awt.Color(204, 255, 255));
        dekstop.setOpaque(false);

        javax.swing.GroupLayout dekstopLayout = new javax.swing.GroupLayout(dekstop);
        dekstop.setLayout(dekstopLayout);
        dekstopLayout.setHorizontalGroup(
            dekstopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 811, Short.MAX_VALUE)
        );
        dekstopLayout.setVerticalGroup(
            dekstopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
        );

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 393, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKoneksi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtWelcome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKoneksi)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlLogin.setBackground(new java.awt.Color(255, 255, 204));

        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setText("Email");

        txtEmail.setBackground(new java.awt.Color(51, 51, 51));
        txtEmail.setForeground(new java.awt.Color(204, 204, 204));
        txtEmail.setCaretColor(new java.awt.Color(0, 204, 102));
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
        txtPAss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPAssActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Lupa password? ");

        lblKlik.setForeground(new java.awt.Color(0, 51, 153));
        lblKlik.setText("Klik disini");
        lblKlik.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblKlikMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblKlikMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblKlikMouseExited(evt);
            }
        });

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 204));
        jLabel8.setForeground(new java.awt.Color(0, 102, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Login");
        jLabel8.setOpaque(true);

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLoginLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblKlik))
                            .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLoginLayout.createSequentialGroup()
                                    .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtPAss, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(67, 67, 67))
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPAss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnRegister))
                .addGap(22, 22, 22)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblKlik))
                .addGap(17, 17, 17))
        );

        jMenuBar1.setBackground(new java.awt.Color(0, 102, 102));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(298, 40));

        mHome.setBackground(new java.awt.Color(0, 102, 102));
        mHome.setForeground(new java.awt.Color(255, 255, 255));
        mHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnhome.png"))); // NOI18N
        mHome.setText("Home");
        mHome.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        miLogin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        miLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/ok.png"))); // NOI18N
        miLogin.setText("Log In");
        miLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLoginActionPerformed(evt);
            }
        });
        mHome.add(miLogin);

        miLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        miLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/hapus.png"))); // NOI18N
        miLogout.setText("Log Out");
        miLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogoutActionPerformed(evt);
            }
        });
        mHome.add(miLogout);

        miRegister.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        miRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/register.png"))); // NOI18N
        miRegister.setText("Register");
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

        miMAdmin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        miMAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/madmin.png"))); // NOI18N
        miMAdmin.setText("Master Admin");
        miMAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMAdminActionPerformed(evt);
            }
        });
        mMaster.add(miMAdmin);

        miMTeknisi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        miMTeknisi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mteknisi.png"))); // NOI18N
        miMTeknisi.setText("Master Teknisi");
        mMaster.add(miMTeknisi);

        miMitra.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        miMitra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mmitra.png"))); // NOI18N
        miMitra.setText("Master Mitra");
        mMaster.add(miMitra);

        jMenuBar1.add(mMaster);

        mKode.setBackground(new java.awt.Color(0, 102, 102));
        mKode.setForeground(new java.awt.Color(255, 255, 255));
        mKode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnkode.png"))); // NOI18N
        mKode.setText("Kode");
        mKode.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        miMaintenance.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        miMaintenance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kmaintenance.png"))); // NOI18N
        miMaintenance.setText("Maintenance");
        miMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMaintenanceActionPerformed(evt);
            }
        });
        mKode.add(miMaintenance);

        miPorject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        miPorject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kproject.png"))); // NOI18N
        miPorject.setText("Project");
        miPorject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPorjectActionPerformed(evt);
            }
        });
        mKode.add(miPorject);

        miEmergency.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        miEmergency.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/kemergency.png"))); // NOI18N
        miEmergency.setText("Emergency");
        miEmergency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEmergencyActionPerformed(evt);
            }
        });
        mKode.add(miEmergency);

        jMenuBar1.add(mKode);

        mPanduan.setBackground(new java.awt.Color(0, 102, 102));
        mPanduan.setForeground(new java.awt.Color(255, 255, 255));
        mPanduan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/mnpanduan.png"))); // NOI18N
        mPanduan.setText("Panduan");
        mPanduan.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/panduan.png"))); // NOI18N
        jMenuItem9.setText("Panduan");
        mPanduan.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/about.png"))); // NOI18N
        jMenuItem10.setText("Tentang");
        mPanduan.add(jMenuItem10);

        jMenuBar1.add(mPanduan);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dekstop)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(130, 130, 130)
                    .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(189, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dekstop))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(67, 67, 67)
                    .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(57, Short.MAX_VALUE)))
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

    private void lblKlikMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKlikMouseClicked
        
        if(txtEmail.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Email tidak boleh kosong","Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
        JInternalFrame lupa = new LupaPassword();
        refresh();
        if(!dekstop.isAncestorOf(lupa)){
          
            dekstop.add(lupa);
            try{
                
                email = txtEmail.getText();
                dataEmail = email;
                 System.err.println("data emailnya "+dataEmail);               
                System.err.println("emailnya "+email);
                lupa.setSelected(true);
                lupa.setVisible(true);
                //refresh();
                //mMAdmin.setSize(800, 600);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = lupa.getSize();
                lupa.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height)-20)/2);
            
            }catch (Exception e){
                System.err.println(e.getMessage());
            }     
        }
        }
        
    }//GEN-LAST:event_lblKlikMouseClicked

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed

        email = txtEmail.getText();
        password  = txtPAss.getText();
        
        validasi(email);

        System.err.println("data email = "+email);
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
            //MenuUtama mu = new MenuUtama();
            setMenu(true);
            setNamanya(nama);
            modelUtama.setNama(nama);
            pnlLogin.hide();

            System.err.println("namnya kok erorr mulu"+nama);
            JOptionPane.showMessageDialog(null,"Login Berhasil","Error", JOptionPane.INFORMATION_MESSAGE);
        }
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
                mProject.setSize(850, 640);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mProject.getSize();
                mProject.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height))/2);
            
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miPorjectActionPerformed
    JInternalFrame mEmergency;
    private void miEmergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEmergencyActionPerformed
        refresh();
        if(!dekstop.isAncestorOf(mEmergency)){
            mEmergency = new Emergency();
            dekstop.add(mEmergency);
            try{
                mEmergency.setSelected(true);
                mEmergency.setVisible(true);
                mEmergency.setSize(850, 640);
                Dimension desktopSize = dekstop.getSize();
                Dimension jInternalFrameSize = mEmergency.getSize();
                mEmergency.setLocation((desktopSize.width - jInternalFrameSize.width)/2,((desktopSize.height- jInternalFrameSize.height))/2);
            
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_miEmergencyActionPerformed

    private void lblKlikMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKlikMouseEntered
        lblKlik.setSize(60, 20);
    }//GEN-LAST:event_lblKlikMouseEntered

    private void lblKlikMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKlikMouseExited
        lblKlik.setSize(54, 16);
    }//GEN-LAST:event_lblKlikMouseExited

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
    private javax.swing.JButton btnRegister;
    private javax.swing.JDesktopPane dekstop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lblKlik;
    private javax.swing.JMenu mHome;
    private javax.swing.JMenu mKode;
    private javax.swing.JMenu mMaster;
    private javax.swing.JMenu mPanduan;
    private javax.swing.JMenuItem miEmergency;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miLogin;
    private javax.swing.JMenuItem miLogout;
    private javax.swing.JMenuItem miMAdmin;
    private javax.swing.JMenuItem miMTeknisi;
    private javax.swing.JMenuItem miMaintenance;
    private javax.swing.JMenuItem miMitra;
    private javax.swing.JMenuItem miPorject;
    private javax.swing.JMenuItem miRegister;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JLabel txtKoneksi;
    private javax.swing.JTextField txtPAss;
    private javax.swing.JLabel txtWelcome;
    // End of variables declaration//GEN-END:variables

    
}
