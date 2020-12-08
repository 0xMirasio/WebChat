package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;
import java.time.Duration;

public class Network extends Thread {

    public String address;
    public String broadcast;
    public String username;
    public List<String> IPC = new ArrayList<String>();
    private int BASE_PORT = 6000;
    private int taille = 1024;
    DatagramSocket socket = null;
    DatagramPacket paquet = null;

    public Network (String username) {
        this.username = username;
    }

    public Network (String username, String address, List<String> IPC) { 
        this.username = username;
        this.address = address;

        this.IPC = IPC;

    }

    /* this method is called when a new client appear and want to auth on the network. He gather the list of all user connected on network.
    If he is the first user, he self auth */
    public void getUserConnected() {
 
        try {
            enumerationInterface();
            String message = "hello-1c"+":"+this.username+":"+this.address+":[]:0"; // initialisation, IPC = [] et nbUser = 0
            System.out.println("[INFO] - Broadcasting (10S MAX DELAY): "+ message);
            broadcast(message, broadcast);
            // démarrage écoute des réponses
            long start = System.currentTimeMillis();
            Network main= new Network(this.username, this.address, this.IPC);        
            main.start();              
            while(true){

                long end = System.currentTimeMillis();
                long duration = end - start;
                if(duration >(long) 5*1000){
                    this.IPC = main.IPC;
                    main.socket.close();
                    main.interrupt();
                    break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }

        if (this.IPC.size() == 0) {
            this.IPC.add(this.username+"-"+this.address);
        }
        System.out.println("[INFO] IPC Network (debug=0) : "+ IPC);

        
    }

    /* this Method is called when a client is authenticated and want to respond to new client on the network */
    public void prepare(List <String> IPC) {
        try {
            System.out.println("[DEBUG] IPC (prepare) = " + IPC);
            enumerationInterface();
            Network main = new Network(this.username, this.address, this.IPC);     
            main.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* 
    Main method when a Network thread is started.
    Test of p1broadcast/p2broadcast, which is the auth method (p1broadcast is when a new client connect to network and broadcast)
    p2broadcast is the reply to this client
     */
    public void run() {
        String donnees;
        try {
                donnees = receiveClient();
                TraitementPaquet(donnees);
            }
        catch (Exception e) {
                //e.printStackTrace();
        }
            
    }

    /*
    Send a message to Destination:Port
     */
    public void sendMessage(String message, String Destination, int Port) throws Exception {
        

        message = message + ":" + this.username + ":" + this.address + ":" + this.IPC + ":" + this.IPC.size();
        System.out.println("[INFO] Sending : " + message + " on port :" + Port);
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(Destination), Port);
        byte[] buf=message.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);
        socket.close();

    }

    /* 
    Set up a listener on port BASEPORT (6000) and wait for data
    */
    public String receiveClient() throws Exception { 

        System.out.println("[INFO] - Waiting for new client to connect on network");
        String donnees = null;
        byte buffer[] = new byte[taille];
        this.socket = new DatagramSocket(this.BASE_PORT); 
        boolean notConnected = false;
        while(!notConnected){
            paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet);
            notConnected = true;
            System.out.println("[DEBUG]"+paquet.getAddress());
            taille = paquet.getLength();
            donnees = new String(paquet.getData(),0, taille);
            System.out.println("[DEBUG] Donnée recu : " + donnees);

        }

        this.socket.close();
        return donnees;
    }

    /* 
    This method take data in argument and look on it and then take actions.
    Actions : analyse a broadcast message auth query, analyse a response to broadcast auth query
    */ 

    public void TraitementPaquet(String donnees) {

        System.out.println(donnees);
        String[] donnees_s = null;
        donnees_s = donnees.split(":", 5);
        String senderUsername = donnees_s[1];
        String senderAddress = donnees_s[2];
        String SenderIPC = donnees_s[3];
        int nbUser = Integer.parseInt(donnees_s[4]);
        Util util = new Util();
        // debut traitement des données paquet
        System.out.println("[DEBUG] Donnée en traitement = " + donnees);
        if (donnees.contains("hello-1c")) { // un nouveau utilisateur essaye de savoir qui est authentifié
            String message = "hello-1b"; // on lui répond avec notre nom + IP
            this.IPC.add(senderUsername+"-"+senderAddress);
            System.out.println("[DEBUG] - senderUsername, IPC => " + senderUsername + ":" + this.IPC );
            boolean rep = util.checkUsername(senderUsername, IPC);
            if (rep) {
                System.out.println("[INFO] Sending info to : " + senderUsername);
                System.out.println("[INFO] Updating userList - adding " + senderUsername);
                try {
                    sendMessage(message+"/userOK",  senderAddress, BASE_PORT);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // si son username est déja prit
            else {
                try {
                    sendMessage(message+"/userNOK", senderAddress, BASE_PORT);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[INFO] IPC Network : "+IPC);
            Network main = new Network(this.username, this.address, this.IPC);     
            main.start();
        }
        if (donnees.contains("hello-1b/userOK")) { // un utilisateur authentifié a répondu au broadcast de découverte
            this.IPC = util.transform2IPC(SenderIPC, nbUser); // le nouveau IPC est celui fourni par les utilisateurs authentifié.
            System.out.println("[INFO] Updating userList - NewIPC = " + this.IPC);

        }
        if (donnees.contains("hello-1b/userNOK")) { // un utilisateur authentifié a répondu au broadcast de découverte
            
           // TODO : changeUsername
           System.out.println("[debug] - i'm here");
        }
    
    }

    /*
    this method is taking 3 argument : a message, an address and send it (address = broadcast) on port
    BASE_PORT (6000)
    */
    public void broadcast(String broadcastMessage, String address) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(address), this.BASE_PORT);
        byte[] buf= broadcastMessage.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);
        socket.close();
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
                /*
                if (inetAddress.toString().contains("172")) { // rezo type docker
                     this.broadcast = "172.17.255.255";
                     this.address = inetAddress.getHostAddress();
                }
                */
            }
        }
        System.out.println("[INFO] Logged on internal Network with IP : "+ this.address);
        System.out.println("[INFO] using Broadcast : "+ this.broadcast); 
        // TODO : améliorez, en attendant on est sur du docker 

    }

    
   

}