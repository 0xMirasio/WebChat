package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;
import java.time.Duration;

public class Network extends Thread {
   
    private boolean p1broadcast = false;
    private boolean p2broadcast = false;
    public String address;
    public String broadcast;
    public String username;
    public List<String> IPC = new ArrayList<String>();
    public int MAX_CONNEXION = 20;
    public int BASE_PORT = 6000;
    public int taille = 1024;
    public int INDEX = 0;
    DatagramSocket socket = null;
    DatagramPacket paquet = null;

    public void closeSocket() {
        this.socket.close();
    }

    public Network (String username) {
        this.username = username;
    }
    
    public Network (String username, String address) { 
        this.username = username;
        this.address = address;
    }

    public Network (String username, String address, boolean p1broadcast, int INDEX) {
        this.username = username;
        this.address = address;
        this.p1broadcast = p1broadcast;
        this.INDEX = INDEX;

    }

    public Network (String username, String address, boolean p1broadcast, boolean p2broadcast) { 
        this.username = username;
        this.address = address;
        this.p1broadcast = p1broadcast;
        this.p2broadcast = p2broadcast;

    }

    /* this method is called when a new client appear and want to auth on the network. He gather the list of all user connected on network.
    If he is the first user, he self auth */
    public void getUserConnected() {
 
        try {
            enumerationInterface();
            IPC.add(this.username + ":" + this.address);
            String message = "hello-1c"+":"+this.username+":"+this.address;
            System.out.println("[INFO] - Broadcasting (10S MAX DELAY): "+ message);
            broadcast(message, broadcast);
    
            // démarrage écoute des réponses
            long start = System.currentTimeMillis();
            Network main[] = new Network[MAX_CONNEXION];
            this.p1broadcast = true;
            for (int i=0 ; i <MAX_CONNEXION ; i++) {
                this.INDEX = i;
                main[i]= new Network(this.username, this.address, this.p1broadcast, this.INDEX);        
                main[i].start();              
            }

            while(true){

                long end = System.currentTimeMillis();
                long duration = end - start;
                if(duration >(long) 10*1000){
                    for (int i=0 ; i <MAX_CONNEXION ; i++) {
                        main[i].closeSocket(); // on a redefini la méthode stop pour fermer le socket du thread.
                        main[i].interrupt();                
                    }
                    break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }

        System.out.println("[INFO] IPC Network (debug=0) : "+ IPC);

        
    }

    /* this Method is called when a client is authenticated and want to respond to new client on the network */
    public void prepare() {
        try {
            enumerationInterface();
            this.p2broadcast = true;
            this.p1broadcast = false;   
            Network main = new Network(this.username, this.address, this.p1broadcast, this.p2broadcast);     
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
        if (p1broadcast) { // phase1, on prepare MAX_CONNEXIon socket
            try {
                donnees = receiveMultiClient();
                TraitementPaquet(donnees);
            }
            catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if (p2broadcast) { // phase2, les clients authentifiés vont répondre aux sockets
            try {
                donnees = receive1Client(); 
                TraitementPaquet(donnees);
            }
            catch (Exception e)  {
                e.printStackTrace();
            }
        }
            
    }

    /*
    Send a message to Destination:Port
     */
    public void sendMessage(String message, String Destination, int Port) throws Exception {
        

        message = message + ":" + this.username + ":" + this.address;
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
    public String receive1Client() throws Exception { 

        System.out.println("[INFO] - Waiting for new client to connect on network");
        String donnees = null;
        byte buffer[] = new byte[taille];
        DatagramSocket socket = new DatagramSocket(this.BASE_PORT); 
        boolean notConnected = false;
        while(!notConnected){
            paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet);
            notConnected = true;
            System.out.println("\n"+paquet.getAddress());
            taille = paquet.getLength();
            donnees = new String(paquet.getData(),0, taille);
            System.out.println("Donnees reçues = "+donnees);
        }

        socket.close();
        return donnees;
    }

    /* 
    This method take data in argument and look on it and then take actions.
    Actions : analyse a broadcast message auth query, analyse a response to broadcast auth query
    */ 

    public void TraitementPaquet(String donnees) {

        System.out.println(donnees);
        String senderUsername = null;
        String[] donnees_s = null;
        donnees_s = donnees.split(":", 3);
        senderUsername = donnees_s[1]; 
        if (donnees.contains("hello-1c")) { // un nouveau utilisateur essaye de savoir qui est authentifié
            String message = "hello-1b"; // on lui répond avec notre nom + IP
            System.out.println("[INFO] Sending info to : " + senderUsername);
            System.out.println("[INFO] Updating userList - OK");
            IPC.add(donnees_s[1]+ ":"+ donnees_s[2]);
            System.out.println("[INFO] IPC Network (debug=1) : "+IPC);

            for (int i=0; i< MAX_CONNEXION ; i++) {
                    try {
                        sendMessage(message, paquet.getAddress().getHostAddress(), (BASE_PORT + i));
                        System.out.println("[DEBUG] - Port2Send : " + (BASE_PORT + i));
                    }
                    catch (Exception e) {
                        System.out.println("pb here");
                        continue;

                    }
                    finally {
                        break;
                    }
            }
            
            this.p2broadcast = true;
            this.p1broadcast = false;
            Network main = new Network(this.username, this.address, this.p1broadcast, this.p2broadcast);     
            main.start();
        }

        if (donnees.contains("hello-1b")) { // un utilisateur authentifié a répondu au broadcast de découverte
            
            System.out.println("[INFO] Updating userList - Adding "+ donnees_s[1]+" - OK");
            IPC.add(donnees_s[1]+ ":"+ donnees_s[2]); // TODO : FIX IPC 
        }
    
    }
    /*
    This method is listening on a port BASE_PORT + INDEX, INDEX is given when startin the thread in network constructor
    It return data which is the result of the data captured.
    */

    public String receiveMultiClient() throws Exception { 

        String donnees = null;
        DatagramPacket paquet = null;
        int taille = 1024;
        String[] donnees_s = null;
        byte buffer[] = new byte[taille];
        try {
                socket = new DatagramSocket(this.BASE_PORT + INDEX); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        boolean notConnected = false;
        while(!notConnected){
            paquet = new DatagramPacket(buffer, buffer.length);
            System.out.println(this.BASE_PORT + INDEX);
            socket.receive(paquet);
            notConnected = true;
            System.out.println("\n"+paquet.getAddress());
            taille = paquet.getLength();
            donnees = new String(paquet.getData(),0, taille);
            System.out.println("Donnees reçues = "+donnees);
        }
        socket.close();
        System.out.println(donnees);
        return donnees;
        
   
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