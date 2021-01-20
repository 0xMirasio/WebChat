import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.ServletContext;

public class Subscribe extends HttpServlet {
	private final long serialVersionUID = 1L;
        private final Util util = new Util();
        private String name = null;  

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try { 
                util.setResponse(response, "ALL OK");
                System.out.println("[INFO] GET /subscribe");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
                
            System.out.println("[INFO] POST /subscribe");
            try {
                name = request.getParameter("name");
                util.setResponse(response, name);
                GetInfo.name = name;
            
            }
            catch (Exception e) {
                e.printStackTrace();
            }            
            
        }
	 

}
