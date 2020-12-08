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
        System.out.println("I WAS HERE BEFORE CRASHING1");
        String[] IPC_s = null;
        System.out.println("I WAS HERE BEFORE CRASHING2");
        // listIPC = "['user-IP','user2-ip',...]"
        IPC_s = listIPC.split("[", 2); // " 'user:ip', 'user2:ip']"
        System.out.println("I WAS HERE BEFORE CRASHING3");
        String IPC_temp = IPC_s[1];
        System.out.println("I WAS HERE BEFORE CRASHING4");
        System.out.println(IPC_temp);
        System.out.println("I WAS HERE BEFORE CRASHING5");
        IPC_temp = IPC_temp.split("]",2)[0]; // " 'user:ip', 'user2:ip'"
        System.out.println("I WAS HERE BEFORE CRASHING6");
        System.out.println(IPC_temp);
        System.out.println("I WAS HERE BEFORE CRASHING7");
        IPC_s = IPC_temp.split(",", nbUser); // [user:ip, user2:ip]
        System.out.println("I WAS HERE BEFORE CRASHING8");
        for (String value : IPC_s) {
            System.out.println("I WAS HERE BEFORE CRASHING9");
            System.out.println(value);
            IPC.add(value);
        }
        System.out.println("[debug] IPC=" + IPC);
        return IPC;
    }
}