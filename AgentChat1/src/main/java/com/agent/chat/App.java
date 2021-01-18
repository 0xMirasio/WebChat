package com.agent.chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App 
{
    static FileOperation filework;
    private String username;
    Util address = new Util();
    
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
            
            if(!(address.getSourceAddress().contains("192.168.56"))){
                
            this.username = filework.getUsername();
            //requête post
            HttpURLConnectionSubscribe sub = new HttpURLConnectionSubscribe();
            try {
                sub.sendPOST("http://localhost:8080/agentchatext/subscribe",this.username);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
          
            LoginGui lgui = new LoginGui();
            String[] args = null;
            lgui.main(args);
        }
    }

}