import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  


public class Subscribe extends HttpServlet {
	private final long serialVersionUID = 1L;
        private final Util util = new Util();
        private String name = null;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try { 
                util.setResponse(response, "ALL OK");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
                       
            try {
                name= util.returnParameter(request);
                util.setResponse(response, name);
            }
            catch (Exception e) {
                e.printStackTrace();
            }            
            
        }
	 

}
