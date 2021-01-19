package com.agent.chat;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Base64;

/*
Cette classe possède des méthodes pour traiter avec les fichiers stockée dans .cache/
*/
class FileOperation
{ 
    
    public FileOperation() {
        try {
            initNetworkComponents(); // on initialise les composants réseaux
 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    BufferedReader reader = null;
    private final String profile = ".cache/profile.private";
    private final String userlist = ".cache/userlist";
    private final String sessionfile = ".cache/sessions";
    private final String networkfile = ".cache/networkConfig";
    private String email;
    private String username;
    private List<String> IPC;
    private String password;
    private String profileimagepath;
    private String ip;
    private String broadcast;
    private String base_com_port = "6000";
    private String com_port = "5000";
    private String isRemote;
    private List<String> session = new ArrayList<String>();
    private final Util util = new Util();

    /*
    Lis le fichier profile.private et sauvegarde les paramètres associés.
    */
    public void readFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(profile));
        this.username = reader.readLine();
        this.email = reader.readLine();
        this.password = reader.readLine();
        this.profileimagepath = reader.readLine();
        this.isRemote = reader.readLine();
        reader.close();
    }
    
    /*
    Lis le fichier networkConfig et sauvegarde les paramètres associés.
    */
    public void initNetworkComponents() throws Exception  {
        this.reader= new BufferedReader(new FileReader(networkfile));
        this.ip = reader.readLine();
        this.broadcast = reader.readLine();
        this.base_com_port = reader.readLine();
        this.com_port = reader.readLine();
        reader.close();
    }
    
    /*
    Lis le fichier userlist et sauvegarde les paramètres associés.
    */
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
    
    /*
    Une injection de commande critique est possible dans cette fonction,
    elle doit être reformatée si utilisation pro. 
    
    Un chemin de fichier est envoyé en paramètre, puis la fonction sauvegarde la base64 du fichier dans download/temp
    puis le contenu est lu et renvoyé sous forme de String.
    */
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
     
    /*
    Lis le fichier sessions et sauvegarde les paramètres associés.
    */
     public void readSessionFile() throws Exception  {
        this.reader= new BufferedReader(new FileReader(sessionfile));
        String temp = reader.readLine();
        this.session = util.transform2Session(temp);
        reader.close();
    }
     
     /*
    Vérifie si le profil est okay. Si un des paramètres obligatoire est manquant , faux est renvoyé.
    */
    public boolean checkIsProfileOkay() throws Exception {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            return false;
        }

        return (!(this.username == null || this.password == null || this.email == null));

    }

    /*
    Retourne le nom d'utilisateur
    */
    public String getUsername() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.username;
    }
    /*
    Retourne l'email
    */
    public String getEmail() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.email;
    }
    
    /*
    Retourne le chemin de l'image de profil
    */
    public String getPath() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.profileimagepath;
    }
    /*
    Retourne l'état de la machine (reseau local ou internet)
    */
    public String getStateNetwork() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.isRemote;
    }
    
    
    /*
    Retourne l'adresse de la machine
    */
    public String getIp() {
        return this.ip;
    }
    
    /*
    Retourne l'adresse de broadcast
    */
    public String getBroadcast() {
        return this.broadcast;
    }
    
    /*
    Retourne le port pour la communication pour l'authentication (BASE_COM_PORT)
    */
    public String Get_base_com_port() {
        return this.base_com_port;
    }
    
     /*
    Retourne le port pour la communication des utilisateurs (COM_PORT)
    */
    public String Get_com_port() {
        return this.com_port;
    }
    
     /*
    Retourne le mot de passe de l'utilisateur (hashé)
    */
    public String getPassword() {
        try { 
            readFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.password;
    }
    
    /*
    Retourne la liste des sessions sauvegardées (couple source:destinataire:sessionid)
    */
    
    public List<String> getSessions() {
        try { 
            readSessionFile(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.session;
    }

    /*
    Crée un nouveau profil dans .cache/profile.private (username, mot de passe, email, chemin vers l'image de profil)
    */
    public void createNewProfile(String username,String password,String email, String profileimagepath, String isRemote) throws Exception {
        PrintWriter writer = new PrintWriter(profile);
        writer.println(username);
        writer.println(email);
        writer.println(password.hashCode());
        writer.println(profileimagepath);
        writer.println(isRemote);
        writer.close();
    }
    
    /*
    Sauvegarde une liste d'utilisateur dans .cache/userlist
    */
    public void saveUser(List<String> IPC) throws Exception {
        PrintWriter writer = new PrintWriter(userlist);
        writer.println(IPC);
        writer.close();
    }
    
    /*
    Cette méthode est vulnérable a une injection de commande critique, un attaquant peux faire une man in the middle, 
    modifier le paquet et donc data, et ainsi faire une injection de code.
    Cette fonction doit être reformatée. 
    
    Cette fonction attend en paramètre des données (base64) et les décode pour le sauvegarder dans le fichier (nom donné en paramètre)
    */
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
    
    Cette fonction sauvegarde une config réseau dans .cache/NetworkConfig
    (adresse source, adresse broadcast, base_com_port , com_port)
    */
    public void registerNetworkSetting(String ip, String broadcast, String bcp, String cp) throws Exception {
        
        PrintWriter writer = new PrintWriter(networkfile);
        writer.println(ip);
        writer.println(broadcast);
        writer.println(bcp);
        writer.println(cp);
        writer.close();
       
    }
    
    /*
    Sauvegarde une liste de sessions dans .cache/sessions
    Avant de sauvegarder les sessions envoyées en paramètres, la fonction recupère les anciennes sessions stockées 
    dans le fichier et les ajoute à celle sauvegardées.
    */
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
    
    /*
    Retourne la liste des utilisateurs authentifiés.
    */
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
