import java.io.IOException;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.StringTokenizer;

public class GetInfoCommunicate extends HttpServlet {

        private static final long serialVersionUID = 1L;
            Communicate comm = new Communicate(); 

        
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              try {

                new Util().setResponse(response, comm.getAsked());

              }
              catch (Exception e) {
                  e.printStackTrace();
              }
        }	 

}