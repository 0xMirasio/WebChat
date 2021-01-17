import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.StringTokenizer;
import java.io.BufferedInputStream;  
import java.io.DataInputStream;  


public class MyUtil {
    

    public static String getData(HttpServletRequest request)throws ServletException, IOException{

			DataInputStream inputStream=new DataInputStream(request.getInputStream());  
			System.out.println("Input Stream: "+inputStream);
			byte[] array = inputStream.readAllBytes();
			String s = new String(array);
            System.out.println("Data\n"+s);
            
            return s;

    }
}
