package com.agent.chat;

import java.util.*;

public class Client {

    
    public String username;

    public Client(String username) {
        this.username = username;
    }

    List<String> user = new ArrayList<String>();
    List<String> IPC = new ArrayList<String>();
    Network net = new Network();
    boolean isConnected = false;
    boolean debug = false;

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
        FileOperation filework = new FileOperation();
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
            IPC = net.getUserConnected(); 
            if (IPC == null) {
                // SI IPC = null => personne connecté, on s'ajoute donc tout seul a liste et on se prepare a de nouvelle connexion
                IPC.add(username);
                net.prepare();
            }
        }
        else {
            System.out.println("[INFO] : Authenticated - Receiving new connexions\n");
            net.prepare();
        }
        
    }

}

