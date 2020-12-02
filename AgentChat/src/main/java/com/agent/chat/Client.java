package com.agent.chat;

import java.util.*;

public class Client {

    
    public String username;

    public Client(String username) {
        this.username = username;
    }

    Network net; 
    FileOperation filework;
    boolean debug = false;
    boolean isConnected = false;

    public void start() {
        System.out.println("[INFO] Opening client socket");

        // todo BROADCAST : qui est la sur le reseau => Reponse OK +  (bonus : signature de la liste)?
        // signature liste => savoir si tout le monde a bien la meme liste et pas de conflit
        // recuperation dans une liste user_connected 
        // choisir une personne user_connecter aléatoirement
        // donne moi ta liste
        // verif(pseudo ) n'apppartient pas a la liste de
        // si c'est le cas => il s'ajoute a la liste et broadcast la nouvelle liste a tt le monde
        // si psuedo deja utilisé => il change de pseudo (actualisation du profile) + ajout dans la liste + broadcast a tt le monde
        // struct liste : username / IP_LOCAL
        // ouvrir sessions chat avec username => temriné partie reseau => on passe a l'interface graphique

        // sauvegarde des messages : bdd distante => table 1:signature : username | signature
        // table2 : history : username | horodatage | message | sessionID => afin de retrouver la sessionId de l'ancienne sessio
        // au démarrage d'une nouvelle, on sauvegarde dans .cache/session.old => username1:username2:sessionId

        // nouvelle session => append a session.old (pas ecraser les ancienne sessions car impossibilité de retrouver les msg)
        // bonus : (IG : effacer l'historique des messages, se renommer , changer de mot de passer)

        // pour debugger
        filework = new FileOperation();
        net= new Network(this.username);
        try {
            debug = filework.GetDebugMode();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (debug) {
            isConnected = true;
        }

        if (!isConnected) {
            System.out.println("[INFO] : Not Authenticated - Searching for others users\n");
            net.getUserConnected(); 
            System.out.println("[INFO] IPC Client (debug=0) : " + net.getIPC());
           
        }
        else {
            System.out.println("[INFO] : Authenticated - Receiving new connexions\n");
            net.prepare();
            System.out.println("[INFO] IPC Client (debug=1) : "+net.getIPC());
        }
        
    }

}

