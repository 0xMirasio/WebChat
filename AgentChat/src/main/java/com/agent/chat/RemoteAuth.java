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
    private final int MAX_TIME = 5000;
    
    public RemoteAuth(String username) {
        this.username = username;
    }
    
    public void getUserConnected() {
        String message = "hello-remote:"+this.username;
        try {
            int resp = sendPOST("http://localhost:8080/agentchatext/subscribe", message);
            System.out.println("[DEBUG] POST Response Code : " + resp);
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(5000); // wait for 5s
            System.exit(0);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int sendPOST(String url, String param) throws IOException {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(param);
        out.flush();
        out.close();

        int responseCode = con.getResponseCode();
        return responseCode;
            
    }
    
    public String sendGET(String url) throws IOException {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        
        String resp = con.getResponseMessage();
        return resp;
        
            
    }
   
    
    public void getUserWaiting() {
        
        try {
             String output = sendGET("http://localhost:8080/agentchatext/getinfo");
             System.out.println(output);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
}
    

