package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;

/*
classe utilitaire contenant des méthodes appelées par d'autres classes.
 */
public class Util {
   
    private String address;
    private String broadcast;
    private final String myDriver = "com.mysql.cj.jdbc.Driver";
    private final String myUrl = "jdbc:mysql://82.165.59.142:3306/webchat";
    private Connection conn = null;

          
    // sauvegarde un couple id_session/message dans la base de données distantes
    public void saveUserMessage(int idSession, String message) throws Exception {
        
      Class.forName(myDriver);
      this.conn = DriverManager.getConnection(myUrl, "webchat", "webchat");
          
      String query = " insert into message_history (sessionid, message)" + " values (?, ?)";
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      System.out.println("[INFO] Saving into DB : (" + idSession  + "," + message + ")");
      preparedStmt.setInt (1, idSession);
      preparedStmt.setString (2, message);
  
      preparedStmt.execute();

    }
    
    // recupère des messages associées à un id de session.
    public String getOldMessage(int sessionID) throws Exception {
       
      String msg="";
      Class.forName(myDriver);
      this.conn = DriverManager.getConnection(myUrl, "webchat", "webchat"); // identifiant valide uniquement pour mySQL
      
      String query = "select message from message_history where sessionid=" + sessionID;
      System.out.println("[INFO] Executing Query into DB : " + query);
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String message = rs.getString("message");
        System.out.println("[DEBUG] Retreived message from DB :"+message+ "\n");
        msg += message + "\n";
      }
      return msg;
    }
            
    // retourne une liste, paramètre: un string  qui sera transformé en liste (liste transformé en String avec toString())
    // format : ['Mirasio:192.168.54.2','Youssef:192.168.54.3','...']
    public List<String> transform2IPC(String listIPC) {
        List<String> IPC = new ArrayList<String>();
        String[] IPC_s = null;
        IPC_s = listIPC.split("\\[", 2); // " 'user', 'user2']"
        String IPC_temp = IPC_s[1];
        IPC_temp = IPC_temp.split("]",2)[0]; // " 'user', 'user2'"
        IPC_s = IPC_temp.split(",", 999); // [user, user2]
        for (String value : IPC_s) {
            IPC.add(value);
        }
        return IPC;
    }
    
    
    // pareil, mais avec des sessions
    // format : ['Mirasio:Youssef:5454', '...']
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

    // retourne un INDEX
    //paramètre : une adresse. (en fonction de l'adresse, un index est renvoyé, cela permet en fonction de l'ip de se connecter a certains sockets)
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

    // retourne l'adresse Ip local
    public String getSourceAddress() {
        try {
            enumerationInterface();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.address;
    }

    // retourne le broadcast local
    public String getBroadcastAddress() {
        try {
            enumerationInterface();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.broadcast;
    }
    
    // retourne la valeur du paramètre demandé (format : param1=value1&param2=value2&...
    public String getParameter(String data, String param) {
        String out=null;
        String[] temp = data.split("&");
        for (String params : temp) {
            if (params.contains(param)) {
                out = params.split("=")[1];
            } 
        }
        return out;
    }

    /*
    this method is gathering all network interface and set up the global variable this.broadcast & this.address 
    */
    public void enumerationInterface() throws IOException{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        int skip=0;
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (skip == 1) {
                    continue;
                }
                else {
                    if (inetAddress == null || inetAddress.toString().contains(":") || inetAddress.toString().contains("127")) {
                        continue;
                    }
                    if (inetAddress.isLinkLocalAddress()) { // reseau local windows
                        this.broadcast = "169.254.255.255";
                        this.address = inetAddress.getHostAddress();
                    }

                    if (inetAddress.toString().contains("172.17.0.")) { // reseau type docker
                         this.broadcast = "172.17.255.255";
                         this.address = inetAddress.getHostAddress();
                    }

                    if (inetAddress.toString().contains("192.168.56.")) { // reseau local Vbox
                         this.broadcast = "192.168.56.255";
                         this.address = inetAddress.getHostAddress();
                         skip=1;
                    }
                    else {
                        this.address = inetAddress.getHostAddress(); // not from know network, so it's a adress for internet purpose
                    }
                }
                
                
            }
        }
    }
}