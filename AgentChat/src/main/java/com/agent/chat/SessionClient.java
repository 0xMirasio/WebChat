package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;

public class SessionClient {

    private String destination;
    private int BASE_COM_PORT = 5000;
    Util util = new Util();
   //private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc = new Scanner(System.in);

    public void startChatSession(String dest) { 

        this.destination = dest;
    
        try {
            System.out.println("Connecting on > " + (BASE_COM_PORT + util.getPort(this.destination)) + " and Dest > " + this.destination);
            Socket clientSocket = new Socket(this.destination, (BASE_COM_PORT + util.getPort(this.destination)));
    
            //flux pour envoyer
            out = new PrintWriter(clientSocket.getOutputStream());
            //flux pour recevoir
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    
            Thread envoyer = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(true){
                    msg = sc.nextLine();
                    out.println(msg);
                    out.flush();
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
                        System.out.println("Serveur : "+msg);
                        msg = in.readLine();
                    }
                    System.out.println("Serveur déconecté");
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
