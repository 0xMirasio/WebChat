package com.agent.chat;

public class App 
{
    static FileOperation filework;
    private String username;
    
    public static void main( String[] args )
    {
        System.out.println("[INFO] Starting AgentChat... - Developed by Poncetta Thibault & Youssef Amari");
        App app = new App();
        app.start();
    }

    public void start() {
        filework = new FileOperation();
        
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