package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;


public class SessionServ extends Thread {
    
    /*
    C'est le serveur TCP. Il se bind sur BASE_COM_PORT et attend une connexion, puis démarre un chat avec elle
    avant de démarrer les sockets, 
    */

    private int BASE_COM_PORT = 5000;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc=new Scanner(System.in);
    
    public void prepare() {
        
        try {
            Util util = new Util();
            System.out.println("Binding on : >" + (BASE_COM_PORT + util.getPort(util.getSourceAddress())));
            ServerSocket serveurSocket = new ServerSocket((BASE_COM_PORT + util.getPort(util.getSourceAddress())));
            Socket clientSocket = serveurSocket.accept();
            System.out.println("New connexion accepted\n");

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
           
            Thread envoi= new Thread(new Runnable() {
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
            envoi.start();
        
            Thread recevoir= new Thread(new Runnable() {
                String msg ;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        //tant que le client est connecté
                        while(msg!=null){
                        System.out.println("Client : "+msg);
                        msg = in.readLine();
                        }
                        //sortir de la boucle si le client a déconecté
                        System.out.println("Client déconecté");
                        //fermer le flux et la session socket
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
