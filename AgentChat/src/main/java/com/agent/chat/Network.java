package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/*
Cette classe est utilisée pour la partie réseau. (broadcast et authentification des utilisateurs)
*/
public class Network extends Thread {

     
    protected List<String> IPC = new ArrayList<String>();
    private int taille = 1024;
    private final int MAX_C = 20;
    private final long MAX_TIME_BROADCAST =  1000;
    private DatagramSocket socket = null;
    private DatagramPacket paquet = null;
    private int INDEX = 0;
    private final FileOperation filework = new FileOperation();
    private final int BASE_PORT = Integer.parseInt(filework.Get_base_com_port());
    private final String broadcast = filework.getBroadcast();
    private String address = filework.getIp();
    private String username;
    private final Util util = new Util();

    public Network (String username) {
        this.username = username;
    }

    public Network (String username, String address, List<String> IPC, int INDEX) { 
        this.username = username;
        this.address = address;
        this.INDEX = INDEX;
        this.IPC = IPC;

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
            String message = "hello-1c"+":"+this.username+":"+this.address+":[]"; // initialisation, IPC = []
            JOptionPane.showMessageDialog(null, "Broadcasting With a Maximum Delay of " + MAX_TIME_BROADCAST +  "ms on the network with IP addresss :  " + this.address + " and with broadcast " + this.broadcast,"Information", 1);
            broadcast(message, broadcast); // cette méthode broadcast le message sur le port BASE_COM_PORT
            // démarrage écoute des réponses
            long start = System.currentTimeMillis(); // début chrono
            Network main[] = new Network[MAX_C];
            for (int i = 0 ; i< MAX_C ; i++) { // on lance MAX_C thread
                main[i]= new Network(this.username, this.address, this.IPC, (this.INDEX+ i));        
                main[i].start();    
            }
                      
            while(true){

                long end = System.currentTimeMillis(); // actualisation chrono
                long duration = end - start; 
                if(duration > MAX_TIME_BROADCAST){ // si la durée excede le temps de broadcast max, on annule 
                    this.IPC = main[0].IPC; 
                    /* il recupere l'ipc de chaque thread, or si on ne prend pas le plus gros (car certain thread 
                     n'ont rien recu et donc IPC.size() = 0
                     Donc pour eviter de recuperer un ipc vide alors que l'ipc peut etre rempli, on prend le premier IPC
                      puis on teste la taille, et on recupère le plus gros
                    */ 
                    for (int i = 0 ; i< MAX_C ; i++) {
                        if (main[i].IPC.size() > this.IPC.size()) {
                            this.IPC = main[i].IPC;
                        }
                        main[i].socket.close(); // on ferme les sockets des threads
                        main[i].interrupt(); // puis on les détruits
                    }
                    break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }

        if (this.IPC.size() == 0) { // si personne a répondu au broadcast
            this.IPC.add(this.username+"-"+this.address); // on s'ajoute alors
            try {
                filework.saveUser(this.IPC); // IPC.Size=1
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
             try {
                filework.saveUser(this.IPC); // sinon on a IPC.Size > 1
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        
    }

    /* this Method is called when a client is authenticated and want to respond to new client on the network */
    public void prepare(List <String> IPC) {
        try {
            Network main = new Network(this.username, this.address, this.IPC);     
            main.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* 
    Main method when a Network thread is started.
     */
    public void run() {
        String donnees;
        try {
                donnees = receiveClient(); // reveice one client connexion, based on BASE_COM_PORT+INDEX
                TraitementPaquet(donnees); // analyse a received packet
            }
        catch (Exception e) {
                //e.printStackTrace();
        }
            
    }

    /*
    Send a message to Destination:Port
     */
    public void sendMessage(String message, String Destination, int Port) throws Exception {
        
        message = message + ":" + this.username + ":" + this.address + ":" + this.IPC;
        System.out.println("[INFO] Sending : " + message + " on port :" + Port);
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(Destination), Port);
        byte[] buf=message.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);
        socket.close();

    }

    /* 
    Set up a listener on port BASEPORT (6000 + INDEX) and wait for data
    */
    public String receiveClient() throws Exception { 

        String donnees = null;
        byte buffer[] = new byte[taille];
        this.socket = new DatagramSocket((this.BASE_PORT+ INDEX)); 
        boolean notConnected = false;
        while(!notConnected){
            paquet = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquet);
            notConnected = true;
            System.out.println("[INFO] Incoming Data from > "+paquet.getAddress());
            taille = paquet.getLength();
            donnees = new String(paquet.getData(),0, taille);

        }

        this.socket.close();
        return donnees;
    }

    /*
    This method generate a random int value between [min,max]
    */
    public int getRandomArbitrary(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    /* 
    This method take data in argument and look on it and then take actions.
    Actions : analyse a broadcast message auth query, analyse a response to broadcast auth query
    */ 

    public void TraitementPaquet(String donnees) {

        String[] donnees_s = null;
        donnees_s = donnees.split(":", 4);
        String senderUsername = donnees_s[1];
        String senderAddress = donnees_s[2];
        String SenderIPC = donnees_s[3];
        // debut traitement des données paquet
        if (donnees.contains("hello-1c")) { // un nouveau utilisateur essaye de savoir qui est authentifié
            String message = "hello-1b"; // on lui répond avec notre nom + IP
            boolean rep = this.username.equals(senderUsername);
            if (!rep) {
                this.IPC.add(senderUsername+"-"+senderAddress);
                try {
                    filework.saveUser(this.IPC);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("[INFO] Updating userList - NewIPC = " + this.IPC);
                try {
                    Thread.sleep(getRandomArbitrary(500, 900)); // Cela permet de temporiser un peu, problème de perte de paquet UDP si tout est envoyé d'un coup
                    this.INDEX = util.getPort(this.address); 
                    // en gros si addresse = 172.17.0.2 => INDEX = 0 
                    // 172.17.0.2 => INDEX=1
                    // cela permet de distribuer les replies sur les différents sockets de l'emmeteur du broadcast en fct de l'adresse
                    sendMessage(message+"/userOK",  senderAddress, BASE_PORT + INDEX);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // si son username est déja prit
            else {
                try {
                    Thread.sleep(getRandomArbitrary(500, 900));
                    this.INDEX = util.getPort(this.address); 
                    sendMessage(message+"/userNOK", senderAddress, BASE_PORT + INDEX);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[INFO] IPC Network : "+IPC);
            Network main = new Network(this.username, this.address, this.IPC);     
            main.start();
            this.interrupt();
        }
        if (donnees.contains("hello-1b/userOK")) { // un utilisateur authentifié a répondu au broadcast de découverte et signale que le pseudo est OK
            this.IPC = util.transform2IPC(SenderIPC); // le nouveau IPC est celui fourni par les utilisateurs authentifié.
            System.out.println("[INFO] Updating userList - NewIPC = " + this.IPC);

        }
        if (donnees.contains("hello-1b/userNOK")) { // un utilisateur authentifié a répondu au broadcast de découverte et signale que le pseudo déja utilisé
            
            System.out.println("[CRITICAL] - Username Aleady Taken, Pls Change your username in GUI Login or in .cache/profile.private");
            System.exit(-2);
        }
    
    }

    /*
    this method is taking 3 argument : a message, an address and send it (address = broadcast) on port
    BASE_PORT )
    */
    public void broadcast(String broadcastMessage, String address) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName(address), this.BASE_PORT);
        byte[] buf= broadcastMessage.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        socket.send(packet);
        socket.close();
    }



    
   

}