package com.agent.chat;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Base64;

class FileOperation
{ 
    
    public FileOperation() {
        try {
            initNetworkComponents();
 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static FileOperation filework;
    BufferedReader reader = null;
    private final String profile = ".cache/profile.private";
    private final String userlist = ".cache/userlist";
    private final String sessionfile = ".cache/sessions";
    private final String networkfile = ".cache/networkConfig";
    
    private String email;
    private String username;
    private String receivemessage;
    private String sendmessage;
    private List<String> IPC;
    private String password;
    private String profileimagepath;
    private String ip;
    private String broadcast;
    private String base_com_port = "6000";
    private String com_port = "5000";
    private List<String> session = new ArrayList<String>();
    private Util util = new Util();

    public void readFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(profile));
        this.username = reader.readLine();
        this.email = reader.readLine();
        this.password = reader.readLine();
        this.profileimagepath = reader.readLine();
        reader.close();
    }
    
    public void initNetworkComponents() throws Exception  {
        this.reader= new BufferedReader(new FileReader(networkfile));
        this.ip = reader.readLine();
        this.broadcast = reader.readLine();
        this.base_com_port = reader.readLine();
        this.com_port = reader.readLine();
        reader.close();
    }
    
     public void readUserFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(userlist));
        String temp = reader.readLine();
        if (temp != null) {
            this.IPC = util.transform2IPC(temp);
        }
        else {
            this.IPC = null;
        }
        reader.close();
    }
     
     public String getFileFormatedData(String path) throws Exception {
         
       
        String command = "base64 " + "'"+ path + "'" + " > download/temp";
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", command);
        builder.start();
        String data= "";
        try {
                File myObj = new File("download/temp");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    data += myReader.nextLine();
                }
                myReader.close();
        } catch (FileNotFoundException e) {
                    e.printStackTrace();
        }
         return data;
     }
     
     public void readSessionFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(sessionfile));
        String temp = reader.readLine();
        this.session = util.transform2Session(temp);
        reader.close();
    }
     

    public boolean checkIsProfileOkay() throws Exception {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            return false;
        }

        return (!(this.username == null || this.password == null || this.email == null));

    }

    public String getUsername() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.username;
    }
    
    public String getPath() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.profileimagepath;
    }
    
    public String getIp() {
        return this.ip;
    }
    public String getBroadcast() {
        return this.broadcast;
    }
    
    public String Get_base_com_port() {
        return this.base_com_port;
    }
    
    public String Get_com_port() {
        return this.com_port;
    }
    
    
    

    public String getPassword() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.password;
    }
    
    public List<String> getSessions() {
        try { 
            readSessionFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.session;
    }


    public void createNewProfile(String username,String password,String email, String profileimagepath) throws Exception {
        PrintWriter writer = new PrintWriter(profile);
        writer.println(username);
        writer.println(email);
        writer.println(password.hashCode());
        writer.println(profileimagepath);
        writer.close();
    }
    
    public void saveUser(List<String> IPC) throws Exception {
        PrintWriter writer = new PrintWriter(userlist);
        writer.println(IPC);
        writer.close();
    }
    
    public void saveFile(String data, String nameFile) throws Exception {
        String command = "echo " + "'"+ data +"'" + "| base64 -d > download/"+"'"+nameFile+"'";
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", command);
        builder.start();
    }
    
    /*
    Default setting for network COM: 
    BASE_COM_PORT = 6000
    COM_PORT =5000
    */
    public void registerNetworkSetting(String ip, String broadcast, String bcp, String cp) throws Exception {
        
        PrintWriter writer = new PrintWriter(networkfile);
        writer.println(ip);
        writer.println(broadcast);
        writer.println(bcp);
        writer.println(cp);
        writer.close();
       
    }
    
    public void saveChatSession(String xsource, String xsender, int sessionId) throws Exception {
        this.reader= new BufferedReader(new FileReader(sessionfile));
        String temp = reader.readLine();
        if (!(temp == null || temp.equals(""))) {
            this.session = util.transform2Session(temp);
        }
        
        reader.close();
        String toAdd = xsource + ":" + xsender + ":" + sessionId;
        this.session.add(toAdd);
        PrintWriter writer = new PrintWriter(sessionfile);
        writer.println(this.session);
        writer.close();
    }
    
    public List<String> getuser() throws Exception {
        try { 
            readUserFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.IPC;
    }
        
   
}
