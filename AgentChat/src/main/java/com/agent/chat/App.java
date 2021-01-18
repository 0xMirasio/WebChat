package com.agent.chat;

/*
Classe appelée au démarrage de l'application
Fonction : Vérifie si un profil utilisateur est déja existant et valide
Si oui : Lance LoginGui.java
Si non : Lance Register.java
*/
public class App 
{
    private final FileOperation filework = new FileOperation();
    private String username;
    
    public static void main( String[] args )
    {
        System.out.println("[INFO] Starting AgentChat... - Developed by Poncetta Thibault & Youssef Amari");
        App app = new App();
        app.start();
    }

    public void start() {        
        boolean res = false;
        try {
            filework.saveUser(null);
            res = filework.checkIsProfileOkay(); // check invalid profile
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!res) {
            RegisterGui rgui = new RegisterGui(); // create a new profile
            String[] args = null;
            rgui.main(args);
        }
        
        else {
            this.username = filework.getUsername();
            LoginGui lgui = new LoginGui();
            String[] args = null;
            lgui.main(args);
        }
    }

}