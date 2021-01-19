/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteAuth extends Thread {
    private String username;
    
    public RemoteAuth(String username) {
        this.username = username;
    }
    
    public void getUserConnected() {
        String message = "hello-remote:"+this.username;
        try {
            int resp = sendPOST("http://localhost:8080/agentchatext/subscribe", message);
            System.out.println("[DEBUG] POST Response Code : " + resp);
            Thread.sleep(3000);
            System.exit(0);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int sendPOST(String url, String login) throws IOException {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(login);
        out.flush();
        out.close();

        int responseCode = con.getResponseCode();
        return responseCode;
            
    }
}
