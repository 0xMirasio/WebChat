package com.agent.chat;

import java.util.*;

public class Client {

    
    public String username;

    public Client(String username) {
        this.username = username;
    }

    Network net; 

    public void start() {
        System.out.println("[INFO] Opening client socket");
        net= new Network(this.username);

        System.out.println("[INFO] : Not Authenticated - Searching for others users\n");
        net.getUserConnected(); 
         if (net.IPC.size() == 1) {
            System.out.println("[INFO] : Authenticated - You are the only one on the network!\n");
            net.prepare(net.IPC);

        }
        else {
            System.out.println("[INFO] : Authenticated - Welcome on the network (Number of user: "+net.IPC.size() + ") !\n");
            net.prepare(net.IPC);

        }
        }

}

