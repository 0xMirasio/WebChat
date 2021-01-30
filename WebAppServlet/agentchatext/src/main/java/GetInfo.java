import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetInfo extends HttpServlet {

        private static final long serialVersionUID = 1L;
        private String name=null;
        private String response_local  =null;
        
        
        private String all;
        private final Util util = new Util();
               
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

              try {
                  all = util.getAllParams(response); 
                  util.setResponse(response, all);
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
