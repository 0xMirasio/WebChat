package com.agent.chat;
import java.util.*;

public class Util {
   
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
        // pour l'instant on ne travaille que avec des adresse docker
        String[] temp = address.split("\\.", 4);
        INDEX = -2 + Integer.parseInt(temp[3]); // 172.17.0.2 = 1ere adresse docker => BASE_PORT = 1ere adresse = 5998
        System.out.println(INDEX);
        return INDEX;
    }
}