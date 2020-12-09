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
        boolean res = false;
        try {
            res = filework.checkIsProfileOkay(); // check invalid profile
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (res) {
            RegisterGui rgui = new RegisterGui(); // create a new profile
            String[] args = null;
            rgui.main(args);
        }
        
        else {
            this.username = filework.getUsername();
            LoginGui lgui = new LoginGui();
            String[] args = null;
            lgui.main(args);
        }
    }

}