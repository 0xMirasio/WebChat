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

/**
 *
 * @author Youssef
 */
public class AskCommunication extends Thread {

    private String username;
    private final int MAX_TIME = 5000;
    private final String OS = System.getProperty("os.name").toLowerCase();
    protected List<String> IPC = new ArrayList<String>();

    private final Util util = new Util();
    //private final String SERVER = "http://82.165.59.142/";
    private final String SERVER = "192.168.56.1";
    private final FileOperation filework = new FileOperation();

    String sourceName;
    String destName;
    
        public AskCommunication(){
        
        }

    public AskCommunication(String sourceName, String destName) {
        this.sourceName = sourceName;
        this.destName = destName;

    }

    public void askSession() {

        String sName = this.sourceName;
        String dName = this.destName;

        try {
            System.out.println("[INFO] Sending POST Request : " + dName + ":" + sName);
            sendPOST("http://" + SERVER + ":8080/agentchatext/communicate", dName + ":" + sName, "askSession");
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(MAX_TIME); // wait for 5s
            //sendPOST("http://"+ SERVER + ":8080/agentchatext/notify", "null", "response"); // buffer become empty 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendPOST(String url, String value, String parameter) throws Exception {

        String command = "curl -X POST " + url + " -d \"" + parameter + "=" + value + "\"";
        System.out.println("[INFO] Executing command : " + command);
        ProcessBuilder builder = new ProcessBuilder();

        if (OS.contains("win")) {
            builder.command("cmd.exe", "/c", command);
        } else {
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

}
