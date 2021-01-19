/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agent.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Youssef
 */
public class HttpURLConnectionGetInfo {
    
    public void receivePOST(String url) throws IOException {
        
        
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
                
                
                /*DataInputStream inputStream = new DataInputStream(con.getInputStream());
                byte[] array = inputStream.readAllBytes();
                String s = new String(array);*/

		// For POST only - START
		/*con.setDoOutput(true);
		DataOutputStream os = new DataOutputStream(con.getOutputStream());
		os.writeBytes(login);
		os.flush();
		os.close();*/
                //For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    
                        
                        System.out.println("POST response worked");
                        //System.out.println("List: "+inputStream);
                        
                        BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
                        
		} else {
			System.out.println("POST response did not worked");
		}
	}
    
}
