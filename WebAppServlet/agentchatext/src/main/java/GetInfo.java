import java.io.IOException;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.StringTokenizer;

public class GetInfo extends HttpServlet {

        private static final long serialVersionUID = 1L;
        protected static String name;
        protected static String response_local;
        
        private String all = "name="+name+"&response_local="+ response_local; 
        private final Util util = new Util();

        
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              try {
                  System.out.println("GET / ; all =" + all);
                  util.setResponse(response, all);
              }
              catch (Exception e) {
                  e.printStackTrace();
              }
        }	 

}
