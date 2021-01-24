/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemoteAuth extends Thread {
    private String username;
    private final int MAX_TIME = 5000;
    private final String OS = System.getProperty("os.name").toLowerCase();
    protected List<String> IPC = new ArrayList<String>();
    
    private final Util util = new Util();
    //private final String SERVER = "http://82.165.59.142/";
    private final String SERVER = "192.168.56.1";
    private final FileOperation filework = new FileOperation();
    
    public RemoteAuth(String username) {
        this.username = username;
    }
    
    public RemoteAuth() {
        
    }
    
    public void getUserConnected() {
        String value = this.username;
        String parameter = "name";
        try {
            System.out.println ("[INFO] Sending POST Request : " + value);
            sendPOST("http://"+ SERVER + ":8080/agentchatext/subscribe", this.username, parameter);
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(5000); // wait for 5s
            String all = sendGET("http://"+ SERVER + ":8080/agentchatext/getinfo");
            sendPOST("http://"+ SERVER + ":8080/agentchatext/notify", "null", "response"); // vide le buffer distant
 
            String response = util.getParameter(all, "response_local");
            if (!(response == null)) {
                if (response.contains("REJECT")) {
                    System.out.println("[CRITICAL] Your username is already choosen by another user ! Pls change your username in .cache/profile.private");
                    System.exit(0);
                }
                else {
                    System.out.println("[INFO] Authentification OK ");
                    this.IPC = util.transform2IPC(response.split(">")[1]);
                    System.out.println("[INFO] IPC : " + this.IPC);
                    filework.saveUser(this.IPC);
                }
            }

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
             String all = sendGET("http://"+ SERVER + ":8080/agentchatext/getinfo");
             String name = util.getParameter(all, "name");
             if (!(name == null)) {
                 if (!(name.contains("null")))
                 {
                    System.out.println("[INFO] /getInfo GET :" + name);
                    sendPOST("http://"+ SERVER + ":8080/agentchatext/subscribe", "null", "name"); // on vide le buffer distant
                    if (name.contains(this.username)) { //si le username est déja prit par la machine du réseau local
                        sendPOST("http://"+ SERVER + ":8080/agentchatext/notify", "REJECT", "response");
                    }
                    else {
                        this.IPC = filework.getuser();
                        this.IPC.add(name+"-REMOTE USER");
                        filework.saveUser(this.IPC);
                        sendPOST("http://"+ SERVER + ":8080/agentchatext/notify", "OK>"+this.IPC, "response");
                        
                    }
                } 
             }
                         
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
}
    

