package com.agent.chat;

import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Thread {

    
    private final String username;
    private boolean MODE;
    private final int BASE_COM_PORT = 5000;
    private final Util util = new Util();
    private final FileOperation filework = new FileOperation();
    private final String sourceAddress = util.getSourceAddress();
    private String destAdress = null;
    private int sessionId;
    private ServerSocket sockS = null;


    public Client(String username, ServerSocket sockS,  boolean MODE) {
        this.sockS = sockS;
        this.username = username;
        this.MODE = MODE;
    }
    
     public Client(String username, String destAdress ,int sessionId,  boolean MODE) {
        this.username = username;
        this.MODE = MODE;
        this.sessionId = sessionId;
        this.destAdress = destAdress;
    }
    
    public void run() {
        System.out.println("[DEBUG] - Thread lance");
        if (MODE) {
            System.out.println("[DEBUG] - Source address : " + this.sourceAddress);
            System.out.println("[INFO] Starting SessionServ session on port : " + (BASE_COM_PORT + util.getPort(this.sourceAddress)));
            SessionServ session = new SessionServ();
            System.out.println("Binding on : >" + (BASE_COM_PORT + util.getPort(util.getSourceAddress())));
            try {
                Socket clientSocket = this.sockS.accept();
                System.out.println("New connexion accepted\n");
                Client client = new Client(this.username,this.sockS, true); 
                client.start();
                session.prepare(clientSocket);
                
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        else {
            System.out.println("[DEBUG] - Source address : " + this.sourceAddress);
            System.out.println("[INFO] Starting SessionClient session on port : " + (BASE_COM_PORT + util.getPort(this.destAdress)) +  " and Destination adress > " + destAdress);
            SessionClient sessionClient = new SessionClient();
            sessionClient.startChatSession(destAdress, sessionId);
        }
    }

    /* On possède 2 classes : SessionClient et SessionServ. 
    SessionClient initie une connexion à une adresse:port et chat avec elle
    SessionServeur bind un socket à port et attend une connexion puis chat avec la connexion
    Ici, on lance un thread client avec le MODE true, c'est à dire on lance une sessionServeur (attente de connexion)
    Parallélement, si l'user choisit un destinataire dans l'UI , on lance un nouveau thread client en MODE false, on 
    initie la connexion avec la destination choisit. */
    
    //TODO : actuellement si A -> B , B est occupé et ne peux pas communiquer avec d'autre personne
    // Pour palier à ca : Lorsque A -> B , B initie un nouveau port et le dit à A : on va parler sur le port COM_BASE_PORT + X
    // A->B est donc maintenant sur COM_BASE_PORT + X et B peux se bind a nouveau sur 5000
    // à chaque nouveau client, la machine discute avec l'autre et bouge son socket vers un socket supérieur.
    // Afin de libérer celui de base qui sert à discuter avec des nouveaux users.


}

