/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.border.Border;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author Youssef
 */
public class SessionGui extends javax.swing.JFrame {

    /**
     * Creates new form SessionGui
     */
    private int xMouse;
    private int yMouse;

    private String input_Sender;
    private Timer timer;

    private String sender;
    private String destName;
    private String destIP;
    public static String Sendmessage;
    public static String Receivemessage;
    private final FileOperation filework = new FileOperation();
    private final Util util = new Util();
    private List<String> sessions = new ArrayList<String>();
    private final SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private int sessionId;
    private String old_message = null;
    

    
    public SessionGui() {
        initComponents();
    }
    
    // ce constructor est appellé quand une machine B contact A.
    public SessionGui(String sender) {
        this.sender = sender;
        initComponents(); 
        
        // Toute les 0.01 secondes, on récupère les messages réçu qui sont stockées dans les fichiers buffers.
        timer = new Timer(10, new java.awt.event.ActionListener() 
        {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt ) {
            String message = SessionGui.Receivemessage;
            if (!(message == null)) {
                 System.out.println("Printing " + message);
                 actualiseView(message);
                 SessionGui.setReceiveMessage(null); // vide le buffer
            }
            
        }});
                
        timer.start(); 
    }
    
    // quand A décide de contacter B, ce constructeur est appelé.
    public SessionGui(String sender, String destName, String destIP, int sessionId) {
        this.sender = sender;
        this.destName = destName;
        this.destIP = destIP;
        this.sessionId = sessionId;
        initComponents();
              
        jLabel2.setText("SessionChat : " + this.destName);
        
        this.sessions = filework.getSessions();
        for (String session : this.sessions) {
                   String temp2[] = session.split(":",3);
                   int sessionID = Integer.parseInt(temp2[2]);
                   try {
                       this.destName = this.destName.split(" ")[1];
                   }
                   catch (Exception e) {
                       this.destName = this.destName.split(" ")[0];
                   }                   
                   String couple = this.sender + ":" + this.destName;
                   System.out.println("[DEBUG] Couple = " + couple);
                   if (session.contains(couple)) {
                       try {
                            System.out.println("Consulting DB for SessionID = "+ sessionID);
                            String oldtemp = util.getOldMessage(sessionID);
                            if (!(oldtemp == null || oldtemp.equals(""))) {
                                    System.out.println("[DEBUG] OLD_MSG =  "+ oldtemp);
                                    this.old_message = jText_AreaMessage.getText() + "\n" + oldtemp;
                                    jText_AreaMessage.setText(this.old_message + "\n");
                            }    
                       }
                       catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
        }
        for (String session : this.sessions) {
                   String temp2[] = session.split(":",3);
                   int sessionID = Integer.parseInt(temp2[2]);
                   try {
                       this.destName = this.destName.split(" ")[1];
                   }
                   catch (Exception e) {
                       this.destName = this.destName.split(" ")[0];
                   }                   
                   String couple = this.destName + ":" + this.sender;
                   System.out.println("[DEBUG] Couple = " + couple);
                   if (session.contains(couple)) {
                       try {
                            System.out.println("Consulting DB for SessionID = "+ sessionID);
                            String oldtemp = util.getOldMessage(sessionID);
                            if (!(oldtemp == null || oldtemp.equals(""))) {
                                    System.out.println("[DEBUG] OLD_MSG =  "+ oldtemp);
                                    this.old_message = jText_AreaMessage.getText() + "\n" + oldtemp;
                                    jText_AreaMessage.setText(this.old_message + "\n");
                            }    
                       }
                       catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
        }
        System.out.println(old_message);
        jText_AreaMessage.setText(old_message + "\n");
        
        
        
        // Toute les 0.01 secondes, on récupère les messages réçu qui sont stockées dans les fichiers buffers.
        timer = new Timer(10, new java.awt.event.ActionListener() 
        {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt ) {
            String message = SessionGui.Receivemessage;
            if (!(message == null)) {
                 actualiseView(message);
                 SessionGui.setReceiveMessage(null); // vide le buffer
            }
                            
        }});    
        timer.start(); 
        // démarrage session TCP

        Client client = new Client(this.sender , this.destIP, this.sessionId,  false);
        client.start();
    }
    
    public static void setSendMessage(String sendMsg) {
        SessionGui.Sendmessage = sendMsg;
    }
     
    
    public static void setReceiveMessage(String RMsg) {
        SessionGui.Receivemessage = RMsg;
    }
    
    private void actualiseView(String message) {
            
           if (message.contains("hello-tcp")) {
               String temp[] = message.split(":",3);
               this.destName = temp[1];
               
               this.sessions = filework.getSessions();
               for (String session : this.sessions) {
                   String temp2[] = session.split(":",3);
                   int sessionID = Integer.parseInt(temp2[2]);
                   
                   String couple = this.destName + ":" + this.sender;
                   System.out.println("[DEBUG] Couple = " + couple);
                   if (session.contains(couple)) {
                       try {
                            old_message = util.getOldMessage(sessionID);
                            System.out.println("[DEBUG] OLD_MSG =  "+ old_message);
                            if (!(old_message == null || old_message.equals(""))) {
                                    old_message = jText_AreaMessage.getText() + "\n" + old_message;
                                    jText_AreaMessage.setText(old_message + "\n");
                            }    
                       }
                       catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               }
               
               for (String session : this.sessions) {
                   String temp2[] = session.split(":",3);
                   int sessionID = Integer.parseInt(temp2[2]);
                   
                   String couple = this.sender + ":" + this.destName;
                   System.out.println("[DEBUG] Couple = " + couple);
                   if (session.contains(couple)) {
                       try {
                            old_message = util.getOldMessage(sessionID);
                            System.out.println("[DEBUG] OLD_MSG =  "+ old_message);
                            if (!(old_message == null || old_message.equals(""))) {
                                    old_message = jText_AreaMessage.getText() + "\n" + old_message;
                                    jText_AreaMessage.setText(old_message + "\n");
                            }    
                       }
                       catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               }
               
               
               this.sessionId = Integer.parseInt(temp[2]);
               System.out.println("[debug] SessionId : " + this.sessionId);
               jLabel2.setText("Session chat : " + this.destName);
               System.out.println("[DEBUG] : DestNAme : " + this.destName);
               try {
                   filework.saveChatSession(this.destName, this.sender, this.sessionId);

               }
               catch (Exception e) {
                    e.printStackTrace();
               }
                                       
           }
           else {
               System.out.println("Received : " + message);
               String temp[] = message.split(":", 2);
               String msg = temp[0];
               String sourceMsg = temp[1];
               Date date = new Date();
               System.out.println("Sender :" + this.destName);
               System.out.println("Local : " + this.sender);
               System.out.println("theorical : " + sourceMsg);
               // TODO : fix this bug. Isn't working
               //if (sourceMsg.equals(this.sender)) {
                    String toSend = s.format(date) + " : " + this.destName + " > " + msg;
                    jText_AreaMessage.setText(jText_AreaMessage.getText() + "\n" + toSend);
                    jTextField_Message.setText("");
               //}
               
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

        jLabel1 = new javax.swing.JLabel();
        jButton_Reset = new javax.swing.JButton();
        jButton_Send = new javax.swing.JButton();
        jButton_Upload = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jText_AreaMessage = new javax.swing.JTextArea();
        jLabel_minimize = new javax.swing.JLabel();
        jLabel_close = new javax.swing.JLabel();
        jTextField_Message = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        jLabel1.setText("Enter Message");

        jButton_Reset.setText("Reset Chat");
        jButton_Reset.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton_Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ResetActionPerformed(evt);
            }
        });

        jButton_Send.setText("Send");
        jButton_Send.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SendActionPerformed(evt);
            }
        });

        jButton_Upload.setText("Upload");
        jButton_Upload.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton_Upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_UploadActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setFocusable(false);

        jText_AreaMessage.setColumns(2);
        jText_AreaMessage.setLineWrap(true);
        jText_AreaMessage.setRows(5);
        jText_AreaMessage.setFocusable(false);
        jScrollPane1.setViewportView(jText_AreaMessage);

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

        jTextField_Message.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_MessageActionPerformed(evt);
            }
        });
        jTextField_Message.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_MessageKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel_minimize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_close, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton_Send)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_Upload)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_Reset))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField_Message, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(20, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_minimize)
                            .addComponent(jLabel_close)
                            .addComponent(jLabel2))
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField_Message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 41, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_Upload)
                            .addComponent(jButton_Send)
                            .addComponent(jButton_Reset))))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_minimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizeMouseClicked

        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel_minimizeMouseClicked

    private void jLabel_minimizeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizeMouseEntered

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);

        //On applique les bords à jLabel_minimize.
        jLabel_minimize.setBorder(jlabel_border);
        jLabel_minimize.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_minimizeMouseEntered

    private void jLabel_minimizeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizeMouseExited

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);

        //On applique les bords à jLabel_minimize.
        jLabel_minimize.setBorder(jlabel_border);
        jLabel_minimize.setForeground(Color.BLACK);
    }//GEN-LAST:event_jLabel_minimizeMouseExited

    private void jLabel_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_closeMouseClicked

        this.dispose();
    }//GEN-LAST:event_jLabel_closeMouseClicked

    private void jLabel_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_closeMouseEntered

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
        jLabel_close.setBorder(jlabel_border);
        jLabel_close.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_closeMouseEntered

    private void jLabel_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_closeMouseExited

        Border jlabel_border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        jLabel_close.setBorder(jlabel_border);
        jLabel_close.setForeground(Color.BLACK);
    }//GEN-LAST:event_jLabel_closeMouseExited

    private void jButton_UploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_UploadActionPerformed

        //Selectionner un fichier
        String path = null;
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        //Extension de fichier
        //FileNameExtensionFilter extension = new FileNameExtensionFilter()
        int filestate = chooser.showSaveDialog(null);

        //Vérifie si l'user selectionne un fichier
        if (filestate == JFileChooser.APPROVE_OPTION) {

            File selectedFile = chooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
    }//GEN-LAST:event_jButton_UploadActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged

        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_formMouseDragged

    private void jButton_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SendActionPerformed
        Date date = new Date();
        
        input_Sender = jTextField_Message.getText();
        if (!input_Sender.equals("") || !(input_Sender == null)) {
            
            SessionGui.setSendMessage(input_Sender+":"+this.sender);
            String message = s.format(date) + " : " + this.sender + " > " + input_Sender;
            try {
                this.util.saveUserMessage(this.sessionId, message);
                }
            catch (Exception e) {
                e.printStackTrace();
            }
            jText_AreaMessage.setText(jText_AreaMessage.getText() + "\n" + message);
            jTextField_Message.setText("");
        }


    }//GEN-LAST:event_jButton_SendActionPerformed

    private void jTextField_MessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_MessageKeyPressed
        Date date = new Date();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            input_Sender = jTextField_Message.getText();

            if (!input_Sender.equals("") || !(input_Sender == null)) {
                
                SessionGui.setSendMessage(input_Sender+":"+this.sender);
                String message = s.format(date) + " : " + this.sender + " > " + input_Sender;
                try {
                    this.util.saveUserMessage(this.sessionId, message);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                jText_AreaMessage.setText(jText_AreaMessage.getText() + "\n" + message);
                jTextField_Message.setText("");
        }
        }
    }//GEN-LAST:event_jTextField_MessageKeyPressed

    private void jTextField_MessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_MessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_MessageActionPerformed

    private void jButton_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ResetActionPerformed

        jText_AreaMessage.setText("");
                
    }//GEN-LAST:event_jButton_ResetActionPerformed

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
            java.util.logging.Logger.getLogger(SessionGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SessionGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SessionGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SessionGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SessionGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Reset;
    private javax.swing.JButton jButton_Send;
    private javax.swing.JButton jButton_Upload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel_close;
    private javax.swing.JLabel jLabel_minimize;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField_Message;
    private javax.swing.JTextArea jText_AreaMessage;
    // End of variables declaration//GEN-END:variables
}
