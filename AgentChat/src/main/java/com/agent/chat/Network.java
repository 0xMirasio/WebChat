package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;

public class Network implements Runnable {
    /* 
        used to gather all IP connected on the network and store it in ArrayList IPConnected
    */
    public int port = 6000;
    private boolean sendState;
    private static DatagramSocket socket = null;
    public String address;
    public String broadcast;
    public String username;
    public List<String> IPC = new ArrayList<String>();


    public Network (String username) {
        this.username = username;

    }
    
    public Network (String username, String address) { // deuxième constructeur requis, afin d'envoyer address au thread
        this.username = username;
        this.address = address;

    }

    public List<String> getIPC() {
        return this.IPC;
    }


    public void getUserConnected() {
 
        try {
            enumerationInterface();
            String message = "hello-1c"+":"+this.username+":"+this.address;
            System.out.println("[INFO] - Broadcasting : "+ message);
            broadcast(message, broadcast);
    
            // démarrage écoute des réponses
            sendState = false; // on passe en mode reception , on att les réponses
            Thread main = new Thread(new Network(this.username, this.address));        
            main.start();
            main.join();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
        
    }

    public void prepare() {
        try {
            enumerationInterface();
            System.out.println("Done!");
            System.out.println(this.address);
            sendState = false; // on passe en mode reception , on att les réponses
            Thread main = new Thread(new Network(this.username, this.address));        
            main.start();
            main.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
 

    // thread utilisé pour le receive ou le send
    public void run() {
        // send = 0 => on est en mode reveive
        if (!sendState) {
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
        
        message = message + ":" + this.username + ":" + this.address;
        System.out.println("[INFO] Sending : " + message);
        socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(Destination), this.port);
        byte[] buf=message.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);

    }


    public void recMessage() throws Exception { 

        System.out.println("[INFO] Waiting for response\n");
        String donnees = null;
        DatagramPacket paquet = null;
        String senderUsername = null;
        int taille = 1024;
        String[] donnees_s = null;
        byte buffer[] = new byte[taille];
        DatagramSocket socket = new DatagramSocket(this.port);
        boolean notConnected = false;
        while(!notConnected){
            paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet);
            notConnected = true;
            System.out.println("\n"+paquet.getAddress());
            taille = paquet.getLength();
            donnees = new String(paquet.getData(),0, taille);
            donnees_s = donnees.split(":", 3);
            senderUsername = donnees_s[1]; 
            //TODO : split : => X:username:IP => recup username
            System.out.println("Donnees reçues = "+donnees);
        }

        socket.close();

        if (donnees.contains("hello-1c")) { // un nouveau utilisateur essaye de savoir qui est authentifié
            String message = "hello-1cb"; // on lui répond avec notre nom + IP
            System.out.println("[INFO] Sending info to : " + senderUsername);
            System.out.println("[INFO] Updating userList - OK");
            IPC.add(donnees_s[1]+ ":"+ donnees_s[2]);
            System.out.println("[INFO] IPC Network (debug=1) : "+ getIPC());
            sendMessage(message, paquet.getAddress().getHostAddress());
        }

        if (donnees.contains("hello-1cb")) { // un utilisateur authentifié a répondu au broadcast de découverte
            
            System.out.println("[INFO] Updating userList - OK");
            IPC.add(donnees_s[1]+ ":"+ donnees_s[2]); // TODO : FIX IPC 
            System.out.println("[INFO] IPC Network (debug=0) : "+ getIPC());
        }
   
    }

    public void broadcast(String broadcastMessage, String address) throws IOException {
        socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(address), this.port);
        byte[] buf= broadcastMessage.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);
    }

    public void enumerationInterface() throws IOException{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress == null || inetAddress.toString().contains(":") || inetAddress.toString().contains("127")) {
                    continue;
                }
                if (inetAddress.isLinkLocalAddress()) {
                    System.out.println("[INFO] Detected network interface\n");
                    System.out.println("[INFO] Broadcast > 169.254.255.255");
                    // TODO : améliorer la fonction
                    this.broadcast = "169.254.255.255";
                    this.address = inetAddress.getHostAddress();
                }
                
            
            }
        }

    }

    
   

}