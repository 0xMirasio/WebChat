package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;


public class SessionServ extends Thread {
    
    /*
    C'est le serveur TCP. Il se bind sur BASE_COM_PORT et attend une connexion, puis démarre un chat avec elle
    avant de démarrer les sockets, 
    */

    private final int BASE_COM_PORT = 5000;
    private BufferedReader in;
    private PrintWriter out;
    private final FileOperation filework = new FileOperation();
    private final Util util = new Util();
    private String username = null;
    
    public void prepare() {
        
        try {
           
            System.out.println("Binding on : >" + (BASE_COM_PORT + util.getPort(util.getSourceAddress())));
            ServerSocket serveurSocket = new ServerSocket((BASE_COM_PORT + util.getPort(util.getSourceAddress())));
            Socket clientSocket = serveurSocket.accept();
            System.out.println("New connexion accepted\n");
            SessionGui.setSendMessage(null);
            SessionGui.setReceiveMessage(null);
            
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
            
            this.username = filework.getUsername();
            
            SessionGui session = new SessionGui(this.username);
            session.setVisible(true);
            session.pack();
            session.setLocationRelativeTo(null);
                     
            Thread envoi= new Thread(new Runnable() {
                String msg = null;
                @Override
                public void run() {
                    while(true){
                        
                        msg = SessionGui.Sendmessage;
                        try {
                            Thread.sleep(50);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!(msg == null)) {
                            System.out.println("[DEBUG] Sending > " + msg);
                            out.println(msg);
                            out.flush();
                            SessionGui.setSendMessage(null);
                        }
                    }
                }
            });
            envoi.start();
        
            Thread recevoir= new Thread(new Runnable() {
                String msg ;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        
                        while(msg!=null){
                            System.out.println("[DEBUG] Receved > " + msg);
                            SessionGui.setReceiveMessage(msg);
                            msg = in.readLine();
                        }
                        System.out.println("Client déconecté");
                        out.close();
                        clientSocket.close();
                        serveurSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            recevoir.start();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
