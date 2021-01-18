import java.io.IOException;
import java.io.PrintWriter;

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
import java.util.ArrayList;
import java.util.List; 

public class GetInfo extends HttpServlet {

	public String login;
	StringBuffer sb = new StringBuffer();
	MyUtil list = new MyUtil();
	public List<String> loginlist = new ArrayList<String>();

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			String message = "Transmission de variables : OK ! ";
			request.setAttribute( "test", message );

			this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse
	  response) throws ServletException, IOException { 
		
		loginlist = list.getLoginList();
		sb.append(loginlist);
		response.getOutputStream().print(sb.toString());
		sb.delete(0, sb.length());
	
		}
	 

}
