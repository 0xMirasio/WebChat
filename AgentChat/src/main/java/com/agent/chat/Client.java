package com.agent.chat;

import java.net.ServerSocket;
import java.net.Socket;

/* On possède 2 classes : SessionClient et SessionServ. 
    SessionClient initie une connexion à une adresse:port et chat avec elle
    SessionServeur bind un socket à port et attend une connexion puis chat avec la connexion
    Ici, on lance un thread client avec le MODE true, c'est à dire on lance une sessionServeur (attente de connexion)
    Parallélement, si l'user choisit un destinataire dans l'UI , on lance un nouveau thread client en MODE false, on 
    initie la connexion avec la destination choisit. 
    
    Lorsque A -> B, B génère un thread et libère le port 5000 en prenant un port plus haut aléatoire (gestion automatique système)
    Comme ça, si C-> B, alors B peut aussi répondre à C, sans être occupé.
 */
public class Client extends Thread {

    private final String username;
    private boolean MODE;
    private final Util util = new Util();
    private final FileOperation filework = new FileOperation();
    private final int BASE_COM_PORT = Integer.parseInt(filework.Get_com_port());
    private final String sourceAddress = util.getSourceAddress();
    private String destAdress = null;
    private int sessionId;
    private ServerSocket sockS = null;

    private String destIPServlet = "192.168.56.1";
    private final int BASE_COM_PORT_REMOTE = 6250;
    

    public Client(String username, ServerSocket sockS, boolean MODE) {
        this.sockS = sockS;
        this.username = username;
        this.MODE = MODE;
    }

    public Client(String username, String destAdress, int sessionId, boolean MODE) {
        this.username = username;
        this.MODE = MODE;
        this.sessionId = sessionId;
        this.destAdress = destAdress;
    }

    public void run() {
        if (MODE) {
            System.out.println("[INFO] Starting SessionServ session on port : " + (BASE_COM_PORT + util.getPort(this.sourceAddress)));
            SessionServ session = new SessionServ();
            System.out.println("[INFO] Binding on : >" + (BASE_COM_PORT + util.getPort(util.getSourceAddress())));
            try {
                Socket clientSocket = this.sockS.accept();
                Client client = new Client(this.username, this.sockS, true);
                client.start();
                session.prepare(clientSocket);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            if (!this.destAdress.contains(destIPServlet)) {

                System.out.println("[INFO] Starting SessionClient session on port : " + (BASE_COM_PORT + util.getPort(this.destAdress)) + " and Destination adress > " + destAdress);
                SessionClient sessionClient = new SessionClient();
                sessionClient.startChatSession(destAdress, sessionId);
                
            } else {

                System.out.println("[INFO] Starting SessionClient session on port : " + (BASE_COM_PORT_REMOTE) + " and Destination adress > " + destAdress);
                SessionClient sessionClient = new SessionClient();
                sessionClient.startChatSession(destAdress, sessionId);

            }

        }
    }

}
