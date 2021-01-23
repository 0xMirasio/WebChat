/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;

import java.awt.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.ImageIcon;


/*
Fenêtre principale du projet. 
Elle peux lancer :
- la fênetre de configuration de profile (ModifyProfileGui.java)
- la fênetre de configuration de réseaux (NetworkConfigGui.java)
- la fênetre de chat (SessionGui.java)

La liste des utilisateurs est affichées , le client peux alors choisir une personne et lancer une session
de chat avec lui. 
 */
public class MainGui extends javax.swing.JFrame {

    /**
     * Creates new form Gui
     */
    //Variable qui va contenir le chemin du fichier à upload
    private String file_path = null;
    private int xMouse;
    private int yMouse;
    private final String pathLogo = "assets/logo.png";
    private Timer timer;
    public List<String> IPC = new ArrayList<String>();
    private final FileOperation filework = new FileOperation();
    private String username = filework.getUsername();
    private final RemoteAuth remote = new RemoteAuth(this.username);
    private Vector old = null;
    private Vector items = null;
    private final int BASE_COM_PORT = 5000;
    private final Util util = new Util();
    private ServerSocket serveurSocket = null;
    
    public MainGui() {
        initComponents();
        // on lance un client en mode SERVEUR (il écoute sur le port 5000)
        try {
            serveurSocket = new ServerSocket((BASE_COM_PORT + util.getPort(util.getSourceAddress())));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        Client client = new Client(this.username,serveurSocket, true); 
        client.start();
        
        String path = filework.getPath();
        ImageIcon picImage = new ImageIcon(path);
        if (picImage != null)
        {
            jLabel_image.setIcon(new ImageIcon(picImage.getImage().getScaledInstance(150, 150, 1)));
        }
        jLabel2.setText(this.username);
        picImage = new ImageIcon(pathLogo);
        if (picImage != null)
        {
            jLabel3.setIcon(new ImageIcon(picImage.getImage().getScaledInstance(200, 200, 1)));
        }
        jLabel2.setText(this.username);
        
        // Toute les 1 secondes, on regarde si il y'a de nouveau utilisateurs pour les ajouter à la liste
        timer = new Timer(1000, new java.awt.event.ActionListener() 
        {
        @Override
         public void actionPerformed(java.awt.event.ActionEvent evt ) {
            FileOperation filework = new FileOperation();
            try {
                IPC  = filework.getuser();
                if (!IPC.isEmpty()) {
                    items = new Vector(IPC);
                    if (items != null && old == null) {
                        old = new Vector(IPC);
                        jList1.setListData(items);
                    }
                    if (!(old.equals(items))) {
                       old = new Vector(IPC);
                       jList1.setListData(items);
                    }
                }
                
                remote.getUserWaiting();
                
                
            }
            catch (Exception e) {
            }
            
            
            
        }});
                
        timer.start();       
        
    }
    
    public MainGui(boolean isRemote) {
        initComponents();
        // on lance un client en mode SERVEUR (il écoute sur le port 5000)
        
        String path = filework.getPath();
        ImageIcon picImage = new ImageIcon(path);
        if (picImage != null)
        {
            jLabel_image.setIcon(new ImageIcon(picImage.getImage().getScaledInstance(150, 150, 1)));
        }
        jLabel2.setText(this.username);
        picImage = new ImageIcon(pathLogo);
        if (picImage != null)
        {
            jLabel3.setIcon(new ImageIcon(picImage.getImage().getScaledInstance(200, 200, 1)));
        }
        jLabel2.setText(this.username);
        
        // Toute les 1 secondes, on regarde si il y'a de nouveau utilisateurs pour les ajouter à la liste
        timer = new Timer(1000, new java.awt.event.ActionListener() 
        {
        @Override
         public void actionPerformed(java.awt.event.ActionEvent evt ) {
            FileOperation filework = new FileOperation();
            try {
                IPC  = filework.getuser();
                if (!IPC.isEmpty()) {
                    items = new Vector(IPC);
                    if (items != null && old == null) {
                        old = new Vector(IPC);
                        jList1.setListData(items);
                    }
                    if (!(old.equals(items))) {
                       old = new Vector(IPC);
                       jList1.setListData(items);
                    }
                }
                
                remote.getUserWaiting();
                
                
            }
            catch (Exception e) {
            }
            
            
            
        }});
                
        timer.start();       
        
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel_close = new javax.swing.JLabel();
        jLabel_minimize = new javax.swing.JLabel();
        jButton_Modify = new javax.swing.JButton();
        jLabel_image = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton_SelectUser = new javax.swing.JButton();
        jButton_StartSession = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 255, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 102));

        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel_close.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_close.setText(" X");
        jLabel_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_closeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel_closeMouseExited(evt);
            }
        });

        jLabel_minimize.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_minimize.setText(" -");
        jLabel_minimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_minimizeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel_minimizeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel_minimizeMouseExited(evt);
            }
        });

        jButton_Modify.setFont(new java.awt.Font("Linux Libertine G", 0, 18)); // NOI18N
        jButton_Modify.setText("Modify profile");
        jButton_Modify.setActionCommand("Modify Profile");
        jButton_Modify.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton_Modify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModifyActionPerformed(evt);
            }
        });

        jLabel_image.setBackground(new java.awt.Color(51, 0, 0));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel3");

        jButton1.setFont(new java.awt.Font("Linux Libertine G", 0, 18)); // NOI18N
        jButton1.setText("Network Configuration");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        jLabel5.setText("Welcome : ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton_Modify, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel_image, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))))
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_minimize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_close, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_close)
                            .addComponent(jLabel_minimize))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(74, 74, 74))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel_image, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton_Modify)
                                            .addComponent(jButton1))
                                        .addGap(6, 6, 6)))))))
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N

        jButton_SelectUser.setFont(new java.awt.Font("Linux Libertine G", 0, 18)); // NOI18N
        jButton_SelectUser.setText("Select User");
        jButton_SelectUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SelectUserActionPerformed(evt);
            }
        });

        jButton_StartSession.setFont(new java.awt.Font("Linux Libertine G", 0, 18)); // NOI18N
        jButton_StartSession.setText("Start a session");
        jButton_StartSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_StartSessionActionPerformed(evt);
            }
        });

        jLabel1.setText("User selected : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jButton_SelectUser, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jButton_StartSession, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_StartSession, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_SelectUser, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // fenêtre config profil
    private void jButton_ModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModifyActionPerformed

        ModifyProfileGui mp = new ModifyProfileGui();
        mp.setVisible(true);
        mp.pack();
        mp.setLocationRelativeTo(null);

    }//GEN-LAST:event_jButton_ModifyActionPerformed

    private void jLabel_minimizeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizeMouseExited

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);

        //On applique les bords à jLabel_minimize.
        jLabel_minimize.setBorder(jlabel_border);
        jLabel_minimize.setForeground(Color.BLACK);
    }//GEN-LAST:event_jLabel_minimizeMouseExited

    private void jLabel_minimizeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizeMouseEntered

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);

        //On applique les bords à jLabel_minimize.
        jLabel_minimize.setBorder(jlabel_border);
        jLabel_minimize.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_minimizeMouseEntered

    private void jLabel_minimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizeMouseClicked

        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel_minimizeMouseClicked

    private void jLabel_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_closeMouseExited

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        jLabel_close.setBorder(jlabel_border);
        jLabel_close.setForeground(Color.BLACK);
    }//GEN-LAST:event_jLabel_closeMouseExited

    private void jLabel_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_closeMouseEntered

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
        jLabel_close.setBorder(jlabel_border);
        jLabel_close.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_closeMouseEntered

    private void jLabel_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_closeMouseClicked

        this.dispose();
    }//GEN-LAST:event_jLabel_closeMouseClicked

    private void jButton_StartSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_StartSessionActionPerformed

        if (jLabel4.getText().equals("")) { // verifie que une personne est selectionée
            JOptionPane.showMessageDialog(null, "Please choose user", "Error Session", 2);
        } else {
            
            String destName;
            String destIP;
            
            String[] temp = jLabel4.getText().split("-"); // on recupère le nom et l'ip
            destName = temp[0];
            destIP = temp[1];
            
            int sessionId = (int) (Math.random() * 30000); // on genere une sessionId aléatoire
            try {
                destName = destName.split(" ")[1]; // petit bug de format, je ne sais pas d'ou ça vient
            }
            catch (Exception e) {
                destName = destName.split(" ")[0];
            }
                   
            try {
                filework.saveChatSession(this.username, destName, sessionId); // on sauvegarde le couple de session

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            // on lance la session de chat
            SessionGui session = new SessionGui(this.username, destName, destIP, sessionId);
            session.setVisible(true);
            session.pack();
            session.setLocationRelativeTo(null);
        }

    }//GEN-LAST:event_jButton_StartSessionActionPerformed

    private void jButton_SelectUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SelectUserActionPerformed

        jLabel4.setText(jList1.getSelectedValue()); // on recupère la personne séléctionnée

    }//GEN-LAST:event_jButton_SelectUserActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged

        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_formMouseDragged

    // fenêtre config réseau
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        NetworkConfigGui net = new NetworkConfigGui();
        net.setVisible(true);
        net.pack();
        net.setLocationRelativeTo(null);

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_Modify;
    private javax.swing.JButton jButton_SelectUser;
    private javax.swing.JButton jButton_StartSession;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel_close;
    public javax.swing.JLabel jLabel_image;
    private javax.swing.JLabel jLabel_minimize;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
