package com.agent.chat;

import java.io.*;

class FileOperation
{ 
    static FileOperation filework;
    BufferedReader reader = null;
    private String profile = ".cache/profile.private";
    private String email;
    private String username;
    private String password;
    private String profileimagepath;


    public void readFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(profile));
        this.username = reader.readLine();
        this.email = reader.readLine();
        this.password = reader.readLine();
        this.profileimagepath = reader.readLine();
        reader.close();
    }

    public boolean checkIsProfileOkay() throws Exception {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return (this.username == null || this.password == null || this.email == null);

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

    public String getPassword() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.password;
    }


    public void createNewProfile(String username,String password,String email, String profileimagepath) throws Exception {
        PrintWriter writer = new PrintWriter(profile);
        writer.println(username);
        writer.println(email);
        writer.println(password.hashCode());
        writer.println(profileimagepath);
        writer.close();
    }



    
    
}
