/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;

public class RemoteAuth extends Thread {
    private String username;
    private final int MAX_TIME = 5000;
    private final String OS = System.getProperty("os.name").toLowerCase();
    
    public RemoteAuth(String username) {
        this.username = username;
    }
    
    public void getUserConnected() {
        String value = this.username;
        String parameter = "name";
        try {
            System.out.println ("[INFO] Sending POST Request : " + value);
            sendPOST("http://82.165.59.142:8080/agentchatext/subscribe", this.username, parameter);
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(5000); // wait for 5s
            System.exit(0);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendPOST(String url, String value, String parameter) throws Exception {
        
        String command = "curl -X POST " + url + " -d \"" + parameter + "=" + value + "\"";
        System.out.println("[INFO] Executing command : " + command);
        ProcessBuilder builder = new ProcessBuilder();
        
        if (OS.contains("win")) {
            builder.command("cmd.exe", "/c", command);
        }
        else {
            builder.command("sh", "-c", command);
        }
       
        builder.start();
            
    }
    
    public String sendGET(String url) throws IOException {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");       
        int resp = con.getResponseCode();
        String response = "";
        if (resp == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
                        
			while ((inputLine = in.readLine()) != null) {
				response += inputLine;
			}            
			in.close();
        } else {
            System.out.println("[INFO] GET request FAIL");
        }
        
        response = response.split("<h3>")[1].split("</h3>")[0];       
        return response;
        
            
    }
   
    
    public void getUserWaiting() {
        
        try {
             String name = sendGET("http://82.165.59.142:8080/agentchatext/getinfo");
             if (!(name.contains("null")))
             {
                System.out.println("[INFO] /getInfo GET :" + name);
                sendPOST("http://82.165.59.142:8080/agentchatext/subscribe", "null", "name"); // on vide le buffer distant
                if (this.username.equals(name)) {
                    System.out.println("oh no");
                    System.exit(0);
                }
             }             
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
}
    

