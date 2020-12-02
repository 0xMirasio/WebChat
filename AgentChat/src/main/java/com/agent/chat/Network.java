package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;

public class Network implements Runnable {
    /* 
        used to gather all IP connected on the network and store it in ArrayList IPConnected
    */
    public String broadcastAdress;
    public int port = 6000;
    private boolean sendState;
    private static DatagramSocket socket = null;

    public void setSendState(boolean state) {
        this.sendState = state;
    }

    public boolean getSendState() {
       return this.sendState;
    }

    public List<String> getUserConnected() {
 
        List<String> IPConnected = new ArrayList<String>();
        IPConnected.add("0"); 
        try {
            broadcastAdress =  enumerationInterface();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            broadcast("hello-1c", broadcastAdress);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CRITICAL] Cannot Request authentificated Users list! (Quitting...)");
            System.exit(-4);
        }
        // démarrage écoute des réponses
        sendState = false; // on passe en mode reception , on att les réponses
        Thread receive = new Thread(new Network());        
        receive.start();
        return IPConnected;
        
    }

    public void prepare() {
        sendState = false; // on passe en mode reception , on att les réponses
        Thread receive = new Thread(new Network());        
        receive.start();
    }
 

    // thread utilisé pour le receive ou le send
    public void run() {
        // send = 0 => on est en mode reveive
        // send = 1 => on est en mode sender
        if (getSendState()) {
            try {
                sendMessage("hello", "127.0.0.1");
            }
            catch (Exception e)  {
                e.printStackTrace();
            }
            
        }
        else {
            try {
                recMessage();
            }
            catch (Exception e)  {
                e.printStackTrace();
            }
        }
    }

    // reseau local, on utilise UDP pour send un message d'informations
    public void sendMessage(String message, String Destination) throws Exception {
        
        socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(Destination), this.port);
        byte[] buf=message.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);

    }


    public void recMessage() throws Exception {

        System.out.println("[INFO] Waiting for response\n");
        int taille = 1024;
        byte buffer[] = new byte[taille];
        DatagramSocket socket = new DatagramSocket(this.port);
        boolean notConnected = false;
        while(!notConnected){
            DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet);
            notConnected = true;
            System.out.println("\n"+paquet.getAddress());
            taille = paquet.getLength();
            String donnees = new String(paquet.getData(),0, taille);
            System.out.println("Donnees reçues = "+donnees);
        }

        // on a recu un message de X, on veux recevoir les autres message donc on relance le thread
        sendState = false; // on passe en mode reception , on att les réponses
        Thread receive = new Thread(new Network());        
        receive.start();
   
    }

    public void broadcast(String broadcastMessage, String address) throws IOException {
        socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(address), this.port);
        byte[] buf= broadcastMessage.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);
    }

    public String enumerationInterface() throws IOException{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        String broadcast = null;
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress == null || inetAddress.toString().contains(":") || inetAddress.toString().contains("127")) {
                    continue;
                }
                if (inetAddress.isLinkLocalAddress()) {
                    System.out.println("[INFO] Detected network interface\n");
                    System.out.println("[INFO] InetAddress >" + inetAddress.getHostAddress());
                    System.out.println("[INFO] Broadcast > 169.254.255.255");
                    // TODO : améliorer la fonction
                    broadcast = "169.254.255.255";
                }
                
            
            }
        }
        return broadcast;
    }
   

}