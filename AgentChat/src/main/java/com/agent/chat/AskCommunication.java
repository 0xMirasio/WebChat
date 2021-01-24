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
    RemoteAuth remoteauth = new RemoteAuth();
    
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
            remoteauth.sendPOST("http://" + SERVER + ":8080/agentchatext/communicate", dName + ":" + sName, "askSession");
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(MAX_TIME); // wait for 5s
            //sendPOST("http://"+ SERVER + ":8080/agentchatext/notify", "null", "response"); // buffer become empty 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
