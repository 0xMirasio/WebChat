package com.agent.chat;

import java.io.*;

class FileOperation
{ 
    BufferedReader reader = null;
    private String profile = ".cache/profile.private";
    private String username;
    private String email;
    private String password;
    private String signature;

    public void readFile() throws Exception  {
        try {
            this.reader= new BufferedReader(new FileReader(profile));

            this.username = reader.readLine();
            this.email = reader.readLine();
            this.password = reader.readLine();
            this.signature = reader.readLine();
            reader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erreur d'ouverture du fichier " + profile);
        }
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
        try {
            PrintWriter writer = new PrintWriter(profile);
            writer.println(username);
            writer.println(email);
            writer.println(password.hashCode());
            writer.println(Integer.toString(password.hashCode()).hashCode()); // signature du hash => a envoyer au mySQL
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
