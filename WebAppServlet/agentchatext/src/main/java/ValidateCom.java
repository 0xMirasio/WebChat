import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidateCom extends HttpServlet{
    private static final long serialVersionUID = 1L;
    public String validateNames;
    Util util = new Util(); 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
    
        try{
        validateNames = request.getParameter("validateSession");

        System.out.println("[INFO] POST /communicate > validateSession=" + validateNames );

        util.saveParam(validateNames, "validateSession");
        }
        catch (Exception e) {
            e.printStackTrace();
        } 

    }
    
}
