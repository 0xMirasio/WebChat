/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;


import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
/**
 *
 * @author Youssef
 */
public class HttpURLConnectionSubscribe {


    public void sendPOST(String url, String login) throws IOException {
        
        
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");

		// For POST only - START
		con.setDoOutput(true);
		DataOutputStream os = new DataOutputStream(con.getOutputStream());
		os.writeBytes(login);
		os.flush();
		os.close();
                //For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success

                        
                        System.out.println("POST request worked");
                        
		} else {
			System.out.println("POST request did not worked");
		}
	}
    
}