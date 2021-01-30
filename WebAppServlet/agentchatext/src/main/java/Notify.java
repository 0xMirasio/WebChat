import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  

public class Notify extends HttpServlet {
	private final long serialVersionUID = 1L;
        private final Util util = new Util();
        private String response_local = null;  
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
                
            try {
                response_local = request.getParameter("response");
                util.saveParam(response_local, "response_local");
                util.logInfo("POST /notify " + response_local);
            
            }
            catch (Exception e) {
                  try {
                      String s = util.getCustomStackTrace(e);
                      util.logError(s);
                  }
                  catch (Exception a) {}
                }        
            
        }
	 

}
