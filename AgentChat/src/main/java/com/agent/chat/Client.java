package com.agent.chat;

import java.util.*;

public class Client {

    
    public String username;

    public Client(String username) {
        this.username = username;
    }

    Network net; 
    boolean isConnected = false;

    public void start() {
        System.out.println("[INFO] Opening client socket");
        net= new Network(this.username);

        if (!isConnected) {
            System.out.println("[INFO] : Not Authenticated - Searching for others users\n");
            net.getUserConnected(); 
            System.out.println("[INFO] IPC Client (debug=0) : " + net.IPC);
            if (net.IPC.size() == 1) {
                System.out.println("[INFO] : Authenticated - You are the only one on the network!\n");
                net.prepare();

            }
           
           
        }
        else {
            System.out.println("[INFO] : Authenticated - Receiving new connexions\n");
            net.prepare();
            System.out.println("[INFO] IPC Client (debug=1) : "+net.IPC);
        }
        
    }

}

