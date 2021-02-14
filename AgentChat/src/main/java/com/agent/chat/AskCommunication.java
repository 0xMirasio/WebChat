/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.Timer;

/**
 *
 * @author Youssef
 */
public class AskCommunication extends Thread {

    private String username;
    private final int MAX_TIME = 5000;
    private final String OS = System.getProperty("os.name").toLowerCase();
    protected List<String> IPC = new ArrayList<String>();

    private final Util util = new Util();
    //private final String SERVER = "82.165.59.142";
    private final String SERVER = "192.168.56.1";
    //private final String SERVER = "localhost";
    private final FileOperation filework = new FileOperation();

    String sourceName;
    String destName;
    RemoteAuth remoteauth = new RemoteAuth();
    String destIPServlet = "192.168.56.1";
    MainGui maingui = new MainGui();
    private Timer timer;

    public AskCommunication(String sourceName, String destName) {
        this.sourceName = sourceName;
        this.destName = destName;

    }

    public void askSession() {

        String sName = this.sourceName;
        String dName = this.destName;

        try {
            System.out.println("[INFO] Sending POST Request : " + dName + ":" + sName);
            remoteauth.sendPOST("http://" + SERVER + ":8080/agentchatext/communicate", dName + ":" + sName, "askSession");
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(MAX_TIME); // wait for 5s

            String all = remoteauth.sendGET("http://" + SERVER + ":8080/agentchatext/getinfo");

            String responsep = util.getParameter(all, "validateSession");

            // Toute les 1 secondes, on regarde si il y'a de nouveau utilisateurs pour les ajouter à la liste
            timer = new Timer(1000, new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {

                    try {
                        String[] temp1 = responsep.split(":");

                        String value = temp1[2];
                        String futurDest1 = temp1[1];
                        String futurSender1 = temp1[0];

                        if (username.equals(futurDest1)) {

                            if (value.equals("TRUE")) {

                                SessionGui session = new SessionGui(futurSender1, futurDest1, destIPServlet, maingui.sessionid());
                                session.setVisible(true);
                                session.pack();
                                session.setLocationRelativeTo(null);

                            }
                        }

                    } catch (Exception e) {
                    }

                }
            });

            timer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void validateSession(String var) {

        String sName = this.sourceName;
        String dName = this.destName;

        try {

            System.out.println("[INFO] Sending POST Request : " + dName + ":" + sName + ":" + var);
            remoteauth.sendPOST("http://" + SERVER + ":8080/agentchatext/validatecom", dName + ":" + sName + ":" + var, "validateSession");
            System.out.println("[INFO] Waiting " + MAX_TIME + "ms before asking server response");
            Thread.sleep(MAX_TIME); // wait for 5s
            //String all = remoteauth.sendGET("http://" + SERVER + ":8080/agentchatext/getinfo");        
            //System.out.println(all);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
