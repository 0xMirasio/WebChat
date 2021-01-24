
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author titip
 */
public class Util {
    
    private final String paramfile = "param";

    public void setResponse(HttpServletResponse response, String message) throws Exception {
        response.setContentType("text/html");//setting the content type  
        PrintWriter pw=response.getWriter();//get the stream to write the data  
        
        pw.println("<html><body>");  
        pw.println("<h3>" + message+ " </h3>");  
        pw.println("</body></html>");  
        
        pw.close();
    }
    
    public String getAllParams() throws Exception {
        try {
             BufferedReader reader=  new BufferedReader(new FileReader(paramfile));
             reader.close();
        }
        catch (Exception e) {
            PrintWriter writer = new PrintWriter(paramfile);
            writer.println("testparam=2");
            writer.close();
        }
        BufferedReader reader=  new BufferedReader(new FileReader(paramfile));
        String params = reader.readLine();
        reader.close();
        return params;
    }
    
    public void saveParam(String value, String param) throws Exception {
        
        String newparam = "";
        try {
             BufferedReader reader=  new BufferedReader(new FileReader(paramfile));
             reader.close();
        }
        catch (Exception e) {
            PrintWriter writer = new PrintWriter(paramfile);
            writer.println("&");
            writer.close();
        }
        
        newparam = "";
        BufferedReader reader=  new BufferedReader(new FileReader(paramfile));
        String old = reader.readLine();
        reader.close();
              
        String[] temp = old.split("&");
        boolean isInside = false;
        for (String params : temp) {
                if (params.contains(param)) 
                {
                    isInside = true;
                }
        }
        if ((!isInside) && (temp.length == 1)) { 
            newparam += param+"=" + value + "&";
        }
        
        if ((!isInside) && (temp.length == 0)) { 
            newparam += param+"=" + value;
        }
        
        for (int i = 0 ; i< temp.length ; i++) {
            
                if (temp[i].contains(param)) 
                {
                    if (i == temp.length) {
                        
                        newparam += "&" + param+"=" + value;
                    }
                    else {
                        if (temp.length == 1) {
                            newparam += param+"=" + value;
                        }
                        else {
                            if (i == 0) {
                                newparam += param+"=" + value + "&";
                            }
                            else {
                                 newparam += "&" + param+"=" + value;
                            }
                           
                        }
                    }
                }
                else {
                    newparam += temp[i];
                }
        }
        
        PrintWriter writer = new PrintWriter(paramfile);
        writer.println(newparam);
        writer.close();
    }
    
    
 
}