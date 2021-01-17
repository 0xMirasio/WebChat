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



public class Subscribe extends HttpServlet {

	public String login;
	private final String profile = ".cache/profile.private";

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			System.out.println("bonjour22222!");

			String message = "Transmission de variables : OK ! ";
			request.setAttribute( "test", message );

			this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse
	  response) throws ServletException, IOException { 

			String login = MyUtil.getData(request);
			
		}
	 

}
