package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;

public class Network implements Runnable {
    /* 
        used to gather all IP connected on the network and store it in ArrayList IPConnected
    */
    FileOperation filework = new FileOperation();
    public String networkMask;
    public int port;
    private static DatagramSocket socket = null;


    public List<String> getUserConnected() {
 
        List<String> IPConnected = new ArrayList<String>();
        IPConnected.add("0"); 
        try {
            networkMask = filework.GetNeworkMask();
            port = filework.GetPort();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("[CRITICAL] Cannot Open network.ini ! (Quitting...)");
            System.exit(-3);
        }
        try {
            broadcast("hello-1c", InetAddress.getByName(networkMask));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CRITICAL] Cannot Request authentificated Users list! (Quitting...)");
            System.exit(-4);
        }
        // démarrage écoute des réponses
        Thread receive = new Thread(new Network());        
        receive.start();
        return IPConnected;
        
    }

    // thread utilisé pour le receive ou le send
    public void run() {
        System.out.println("thread lancé");
        // send = 0 => on est en mode reveive
        // send = 1 => on est en mode sender
        boolean send = true;
        if (send) {
            sendMessage("hello", "127.0.0.1");
        }
        else {
            recMessage();
        }
    }

    // reseau local, on utilise UDP pour send un message d'informations
    public void sendMessage(String message, String Destination) {
        try {
            socket = new DatagramSocket();
            socket.connect(InetAddress.getByName(Destination), this.port);
        } catch (Exception e) {
            System.err.println("Connection failed. " + e.getMessage());
        }
        
        byte[] buf=message.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        try{
            socket.send(packet);
        } catch(Exception e){
            System.err.println("Sending failed. " + e.getMessage());
        }
        
    }

    public void recMessage() {
        // TODO        
    }

    public void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        /*
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, this.port);
        socket.send(packet);
        socket.close();
        */

        try {
            socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("169.254.255.255"), 8027);
        } catch (Exception e) {
            System.err.println("Connection failed. " + e.getMessage());
        }
        
        byte[] buf= broadcastMessage.getBytes();
        DatagramPacket packet= new DatagramPacket(buf, buf.length);
        try{
            socket.send(packet);
        } catch(Exception e){
            System.err.println("Sending failed. " + e.getMessage());
        }

    }
}