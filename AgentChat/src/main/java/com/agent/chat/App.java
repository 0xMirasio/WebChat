package com.agent.chat;

public class App 
{
    static FileOperation filework;
    private String username;
    public static int test = 64; // debug JUNIT test
    
    public static void main( String[] args )
    {
        System.out.println("Starting AgentChat-Dev v0.1");
        App app = new App();
        app.start();
    }

    public void start() {
        filework = new FileOperation();
        this.username = filework.getUsername();
        Client client = new Client(username);
        client.startClient();
    }

}