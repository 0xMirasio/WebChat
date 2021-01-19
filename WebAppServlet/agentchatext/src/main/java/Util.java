
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

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
    
    public String returnParameter(HttpServletRequest request) throws Exception {
        DataInputStream inputStream = new DataInputStream(request.getInputStream());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
            
        while (inputStream.available() > 0) {
            byte b = inputStream.readByte();
            output.write(b);
        }
        byte[] out = output.toByteArray();
        String name = new String(out);
        return name;
    }
    
    public void setResponse(HttpServletResponse response, String message) throws Exception {
        response.setContentType("text/html");//setting the content type  
        PrintWriter pw=response.getWriter();//get the stream to write the data  
        
        pw.println("<html><body>");  
        pw.println("<h3>" + message+ " </h3>");  
        pw.println("</body></html>");  
        
        pw.close();
    }
}