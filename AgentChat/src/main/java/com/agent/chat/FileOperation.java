package com.agent.chat;

import java.io.*;

class FileOperation
{ 
    static FileOperation filework;
    BufferedReader reader = null;
    private String profile = ".cache/profile.private";
    private String network = ".cache/network.ini";
    private String username;
    private String email;
    private String password;
    private String signature;

    private String mask;//Masque de (sous) réseau
    private int port; //Numéro de port

    public void readFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(profile));
        this.username = reader.readLine();
        this.email = reader.readLine();
        this.password = reader.readLine();
        this.signature = reader.readLine();
        reader.close();
    }

    public boolean checkIsProfileOkay() throws Exception {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return (this.username == null || this.password == null || this.email == null || this.signature == null);

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

    public String getPassword() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.password;
    }

    public String getSignature() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.signature;
    }



    public void setNewProfile(String username,String email,String password) throws Exception {
        PrintWriter writer = new PrintWriter(profile);
        writer.println(username);
        writer.println(email);
        writer.println(password.hashCode());
        writer.println(Integer.toString(password.hashCode()).hashCode()); // signature du hash => a envoyer au mySQL
        writer.close();
    }

    public void readFileNetwork() throws Exception  {
        this.reader= new BufferedReader(new FileReader(network));
        this.mask = reader.readLine();
        this.port = Integer.parseInt(reader.readLine());
        reader.close();
    }

    public String GetNeworkMask() throws Exception  {
            try { 
                readFileNetwork();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return this.mask;
    }

    public int GetPort() throws Exception {
        try { 
            readFileNetwork();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.port;
    }
}
