package com.agent.chat;
import java.util.*;

public class Util {
    
    public boolean checkUsername(String username, List<String> IPC) {
        for (int i=0; i<IPC.size(); i++) {
            String[] IPC_s = null;
            IPC_s = IPC.get(i).split("-", 2);
            if (IPC_s[0].equals(username)) {
                return true;
            }
        }
        return false;
        
    }

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
}