package com.agent.chat;
import java.util.Scanner;

public class App 
{
    static FileOperation filework;
    static private String username;
    static private String email;
    static private String password;
    static Scanner input = new Scanner(System.in);

    public static int test = 64; // debug JUNIT test
    
    public static void main( String[] args )
    {
        System.out.println("Starting AgentChat-Dev v0.1");
        start();
    }

    public static void start() {
        filework = new FileOperation();
        boolean res = false;
        try {
            res = filework.checkIsProfileOkay(); // check invalid profile
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (res) {
            createProfile(); // create a new profile
        }
        
        username = filework.getUsername();
        System.out.println("[INFO] Profile - OK. Welcome : " +username);

        //demander password au demarrage et le verifier
        askPassword();
        // démarage client
        System.out.println("[INFO] Connecting to network...");
        Client client = new Client();
        client.start();

        
    }

    public static void askPassword() {
        Security sec = new Security();
        boolean res;
        System.out.println("[INPUT] Welcome to AgentChatNetwork, Enter password Account > ");
        password = input.nextLine();
        try {
            res = sec.verifyPassword(password);
        }
        catch (Exception e) {
            e.printStackTrace();
            res= false;
            System.exit(-1);
        }
        if (!res) {
            System.out.println("[CRITICAL] Bad password : exiting\n");
            System.exit(-2);
        }

    }

    public static void createProfile() {
        System.out.println("[WARNING] Invalid profile or inexistent profile : Creating New profile...");
        System.out.println("\n[INPUT] Username > ");
        username = input.nextLine();
        System.out.println("\n[INPUT] Email > ");
        email = input.nextLine();
        System.out.println("\n[INPUT] Password > ");
        password = input.nextLine();
        System.out.println("[NEW] New Profile added");
        try {
            filework.setNewProfile(username,email,password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}