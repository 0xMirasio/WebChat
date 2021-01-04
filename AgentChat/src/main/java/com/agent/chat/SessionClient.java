package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;

public class SessionClient {

    private String destination;
    private final int BASE_COM_PORT = 5000;
    private final Util util = new Util();
    private BufferedReader in;
    private PrintWriter out;
    private final FileOperation filework = new FileOperation();
    
    public void startChatSession(String dest) { 

        this.destination = dest;
    
        try {
            System.out.println("Connecting on > " + (BASE_COM_PORT + util.getPort(this.destination)) + " and Dest > " + this.destination);
            Socket clientSocket = new Socket(this.destination, (BASE_COM_PORT + util.getPort(this.destination)));
            
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                  
            out.println("hello-tcp:" + filework.getUsername());
            out.flush();
            SessionGui.setSendMessage(null);
            SessionGui.setReceiveMessage(null);
            
            Thread envoyer = new Thread(new Runnable() {
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
            envoyer.start();
    
            Thread recevoir = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                try {
                    msg = in.readLine();
                    while(msg!=null){
                        System.out.println("[DEBUG] Receved > " + msg);
                        SessionGui.setReceiveMessage(msg);
                        msg = in.readLine();
                    }
                    SessionGui.setReceiveMessage(" ****DISCONNECTED****");
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            });
            recevoir.start();
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
