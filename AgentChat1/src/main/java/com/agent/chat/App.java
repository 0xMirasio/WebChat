package com.agent.chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App 
{
    static FileOperation filework;
    private String username;
    Util util = new Util();
    
    HttpURLConnectionSubscribe sub = new HttpURLConnectionSubscribe();
    HttpURLConnectionGetInfo get = new HttpURLConnectionGetInfo();
    
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
            
            
            
           /* else{
                try {
                    //Beaucoup trop tôt !!!
                get.receivePOST("http://localhost:8080/agentchatext/getinfo");
                
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            }*/
          
            LoginGui lgui = new LoginGui();
            String[] args = null;
            lgui.main(args);
            
            if(!(util.getSourceAddress().contains("192.168.56"))){
                
            this.username = filework.getUsername();
            //requête post
            
            try {
                sub.sendPOST("http://localhost:8080/agentchatext/subscribe",this.username);
                
                
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    }

}