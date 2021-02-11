package com.agent.chat;

import java.io.*;
import java.net.*;

/*
Cette classe agit en tant que client TCP
Elle se connecte à un socket serveur (adresse,port) et génère un socket client. 
2 threads sont actifs, ce qui permet de recevoir et envoyer en même temps.
 */
public class SessionClient {

    private String destination;
    private final Util util = new Util();
    private BufferedReader in;
    private PrintWriter out;
    private final FileOperation filework = new FileOperation();
    private final int BASE_COM_PORT = Integer.parseInt(filework.Get_com_port());

    private String destIPServlet = "192.168.56.1";
    private final int BASE_COM_PORT_REMOTE = 7000;
    private int PORT=0;

    public void startChatSession(String dest, int sessionId) {

        this.destination = dest;

        try {
            if (!this.destination.contains(destIPServlet)) {
                // génération socket client
                this.PORT = BASE_COM_PORT + util.getPort(this.destination);
                System.out.println("Connecting on > " + (PORT) + " and Dest > " + this.destination);
            } else {
                this.PORT = BASE_COM_PORT_REMOTE;
                System.out.println("Connecting on > " + (PORT) + " and Dest > " + this.destination);   
            }
            
            Socket clientSocket = new Socket(this.destination, (this.PORT));

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("hello-tcp:" + filework.getUsername() + ":" + sessionId);
            out.flush();
            SessionGui.setSendMessage(null);
            SessionGui.setReceiveMessage(null);

            //thread d'envoi
            Thread envoyer = new Thread(new Runnable() {
                String msg = null;

                @Override
                public void run() {

                    while (true) {

                        msg = SessionGui.Sendmessage; // on recupère les message a envoyer dans le buffer de sessionGui
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
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

            // thread reception
            Thread recevoir = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println("[DEBUG] Receved > " + msg);
                            SessionGui.setReceiveMessage(msg);
                            msg = in.readLine();
                        }
                        SessionGui.setReceiveMessage(" ****DISCONNECTED****"); // si serveur deconnecté
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
