import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import java.io.PrintWriter;



public class Subscribe extends HttpServlet {

	public String login;
	//private final String profile = ".cache/profile.private";
	BufferedReader reader = null;

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			String message = "Transmission de variables : OK ! ";
			request.setAttribute( "test", message );

			this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse
	  response) throws ServletException, IOException { 

			//Récupération du login d'une machine de l'Internet
			MyUtil.getData(request);

			//Stockage dans le fichier pofile.private
			/*this.reader= new BufferedReader(new FileReader(profile));
			PrintWriter writer = new PrintWriter(profile);

			while(reader.readLine()!=""){
				writer.println("\n");
			  }*/
			

			
		}
	 

}
