package com.agent.chat;

public class Client extends Thread {

    
    public String username;
    private boolean MODE;

    public Client(String username) {
        this.username = username;
    }
    public Client(String username, boolean MODE) {
        this.username = username;
        this.MODE = MODE;
    }

    private Network net = new Network(this.username); 
    private int COM_BASE_PORT = 5000;
    private Util util = new Util();
    private String sourceAddress = util.getSourceAddress();
    private FileOperation filework = new FileOperation();



    public void run() {
        System.out.println("[DEBUG] - Thread lance");
        if (MODE) {
            System.out.println("[DEBUG] - Source address : " + this.sourceAddress);
            System.out.println("[INFO] Starting SessionServ session on port : " + (COM_BASE_PORT + util.getPort(this.sourceAddress)));
            SessionServ session = new SessionServ();
            session.prepare();
        }
        else {
            String destAdress = filework.getDestAdress();
            System.out.println("[DEBUG] - Source address : " + this.sourceAddress);
            System.out.println("[INFO] Starting SessionClient session on port : " + (COM_BASE_PORT + util.getPort(destAdress)) +  " and Destination adress > " + destAdress);
            SessionClient sessionClient = new SessionClient();
            sessionClient.startChatSession(destAdress);
        }
    }

    /* On possède 2 classes : SessionClient et SessionServ. 
    SessionClient initie une connexion à une adresse:port et chat avec elle
    SessionServeur bind un socket à port et attend une connexion puis chat avec la connexion
    Ici, on lance un thread client avec le MODE true, c'est à dire on lance une sessionServeur (attente de connexion)
    Parallélement, on boucle en while(true) et on attend un envoie de l'interface graphique (ici debug pour le moment) , 
    si le client a choisit une destination avec qui discuter, on lance un nouveau thread client en MODE false, on 
    initie la connexion avec la destination choisit. De plus on passe DEBUG_isSender->false afin de ne pas boucler a l'infini*/

    public void startClient() {

        Client client = new Client(this.username, true);
        client.start();
        while(true) {
            if (filework.isSender() && !this.sourceAddress.equals("172.17.0.3")) {
                Client client2 = new Client(this.username, false);
                client2.start();
                try {
                    filework.SetSenderState(false); 
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* 
        // pour l'instant on focus sur tcp
        System.out.println("[INFO] : Not Authenticated - Searching for others users\n");
        net.getUserConnected(); 
         if (net.IPC.size() == 1) {
            System.out.println("[INFO] : Authenticated - You are the only one on the network!\n");
            net.prepare(net.IPC);
        }
        else {
            System.out.println("[INFO] : Authenticated - Welcome on the network (Number of user: "+net.IPC.size() + ") !\n");
            net.prepare(net.IPC);
            while(true) {
                // TODO : en fonction de l'utilisateur que le GUI va renvoyer (choix user)
                // on va lancer une méthode chat.startSession(username1, addr1, username2, addr2)
                // + bdd history / session.old
            }
        }
        }
        */

    }


}