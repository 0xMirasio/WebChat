package com.agent.chat;
import java.util.*;
import java.io.*;
import java.net.*;

public class Util {
   
    private String address;
    private String broadcast;

    public List<String> transform2IPC(String listIPC, int nbUser) {
        List<String> IPC = new ArrayList<String>();
        String[] IPC_s = null;
        // listIPC = "['user-IP','user2-ip',...]"
        IPC_s = listIPC.split("\\[", 2); // " 'user:ip', 'user2:ip']"
        String IPC_temp = IPC_s[1];
        IPC_temp = IPC_temp.split("]",2)[0]; // " 'user:ip', 'user2:ip'"
        IPC_s = IPC_temp.split(",", nbUser); // [user:ip, user2:ip]
        for (String value : IPC_s) {
            IPC.add(value);
        }
        return IPC;
    }

    public int getPort(String address) {
        int INDEX=0;
        // address = X.Y.Z.VAL
        if (address.contains("172.17.0.")) { // adresse docker
            String[] temp = address.split("\\.", 4);
            INDEX = -2 + Integer.parseInt(temp[3]); // 172.17.0.2 = 1ere adresse docker 
            System.out.println(INDEX);
            return INDEX;
        }
        if (address.contains("169.254.206.")) { // adresse reseau local windows
            String[] temp = address.split("\\.", 4);
            INDEX = -98 + Integer.parseInt(temp[3]); // 169.254.206.98 = 1ere adresse 
            System.out.println(INDEX);
            return INDEX;
        }
        return 0;
    }

    public String getSourceAddress() {
        try {
            enumerationInterface();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.address;
    }

    public String getBroadcastAddress() {
        try {
            enumerationInterface();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.broadcast;
    }

    /*
    this method is gathering all network interface and set up the global variable this.broadcast & this.address 
    */
    public void enumerationInterface() throws IOException{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress == null || inetAddress.toString().contains(":") || inetAddress.toString().contains("127")) {
                    continue;
                }
                if (inetAddress.isLinkLocalAddress()) { // rseau local windows
                    this.broadcast = "169.254.255.255";
                    this.address = inetAddress.getHostAddress();
                }
                
                if (inetAddress.toString().contains("172.17.0.")) { // rezo type docker
                     this.broadcast = "172.17.255.255";
                     this.address = inetAddress.getHostAddress();
                }
                
            }
        }
        // TODO : améliorer le calcul du broadcast, detection du reseau IP
    }
}