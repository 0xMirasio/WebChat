package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Util {
   
    private String address;
    private String broadcast;
    private final String myDriver = "com.mysql.cj.jdbc.Driver";
    private final String myUrl = "jdbc:mysql://82.165.59.142:3306/webchat";
    private Connection conn = null;

          
    public void saveUserMessage(int idSession, String message) throws Exception {
        
      Class.forName(myDriver);
      try {
            this.conn = DriverManager.getConnection(myUrl, "root", "15d7e2v142857143!"); // not root password for the vps :)
      }
      catch (Exception e) {
            e.printStackTrace();
      }
      
      String query = " insert into message_history (sessionid, message)" + " values (?, ?)";

      PreparedStatement preparedStmt = conn.prepareStatement(query);
      System.out.println("[INFO] Saving into DB : (" + idSession  + "," + message + ")");
      preparedStmt.setInt (1, idSession);
      preparedStmt.setString (2, message);
  
      preparedStmt.execute();

    }
    public List<String> transform2IPC(String listIPC) {
        List<String> IPC = new ArrayList<String>();
        String[] IPC_s = null;
        // listIPC = "['user-IP','user2-ip',...]"
        IPC_s = listIPC.split("\\[", 2); // " 'user-ip', 'user2-ip']"
        String IPC_temp = IPC_s[1];
        IPC_temp = IPC_temp.split("]",2)[0]; // " 'user-ip', 'user2-ip'"
        IPC_s = IPC_temp.split(",", 999); // [user-ip, user2-ip]
        for (String value : IPC_s) {
            IPC.add(value);
        }
        return IPC;
    }
    
    public List<String> transform2Session(String listSession) {
        List<String> sessions = new ArrayList<String>();
        String[] sec_s = null;
               
        sec_s = listSession.split("\\[", 2); 
        String temp = sec_s[1];
        temp = temp.split("]",2)[0]; 
        sec_s = temp.split(",", 999);
        for (String value : sec_s) {
            sessions.add(value);
        }
        return sessions;
    }

    public int getPort(String address) {
        int INDEX=0;
        // address = X.Y.Z.VAL
        if (address.contains("172.17.0.")) { // adresse docker
            String[] temp = address.split("\\.", 4);
            INDEX = -2 + Integer.parseInt(temp[3]); // 172.17.0.2 = 1ere adresse docker 
            System.out.println(INDEX);
            return INDEX;
        }
        if (address.contains("169.254.206.")) { // adresse reseau local windows
            String[] temp = address.split("\\.", 4);
            INDEX = -98 + Integer.parseInt(temp[3]); // 169.254.206.98 = 1ere adresse 
            System.out.println(INDEX);
            return INDEX;
        }
        
        if (address.contains("168.192.56.")) { // adresse reseau local virtualboxPrivate
            String[] temp = address.split("\\.", 4);
            INDEX = -102 + Integer.parseInt(temp[3]); // 168.192.56.102 = 1ere adresse vbox distribué DHCP
            System.out.println(INDEX);
            return INDEX;
        }
        
        return 0;
    }

    public String getSourceAddress() {
        try {
            enumerationInterface();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.address;
    }

    public String getBroadcastAddress() {
        try {
            enumerationInterface();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.broadcast;
    }

    /*
    this method is gathering all network interface and set up the global variable this.broadcast & this.address 
    */
    public void enumerationInterface() throws IOException{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress == null || inetAddress.toString().contains(":") || inetAddress.toString().contains("127")) {
                    continue;
                }
                if (inetAddress.isLinkLocalAddress()) { // rseau local windows
                    this.broadcast = "169.254.255.255";
                    this.address = inetAddress.getHostAddress();
                }
                
                if (inetAddress.toString().contains("172.17.0.")) { // rezo type docker
                     this.broadcast = "172.17.255.255";
                     this.address = inetAddress.getHostAddress();
                }
                
                if (inetAddress.toString().contains("192.168.56.")) { // rezo local Vbox
                     this.broadcast = "192.168.56.255";
                     this.address = inetAddress.getHostAddress();
                }
                
            }
        }
        // TODO : améliorer le calcul du broadcast, detection du reseau IP
    }
}