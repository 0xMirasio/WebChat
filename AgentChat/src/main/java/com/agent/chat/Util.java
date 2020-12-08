package com.agent.chat;
import java.util.*;

public class Util {
    
    public boolean checkUsername(String username, List<String> IPC) {
        if (IPC.contains(username)) {
            System.out.println("[INFO] double user detected, sending rename request");
            return true;
        }
        return false;
    }

    public List<String> transform2IPC(String listIPC, int nbUser) {
        List<String> IPC = new ArrayList<String>();
        String[] IPC_s = null;
        // listIPC = "['user:IP','user2:ip',...]"
        IPC_s = listIPC.split("[", 2); // " 'user:ip', 'user2:ip']"
        String IPC_temp = IPC_s[1];
        IPC_temp = IPC_temp.split("]",2)[0]; // " 'user:ip', 'user2:ip'"
        IPC_s = IPC_temp.split(",", nbUser); // [user:ip, user2:ip]
        for (String value : IPC_s) {
            IPC.add(value);
        }
        System.out.println("[debug] IPC=" + IPC);
        return IPC;
    }
}