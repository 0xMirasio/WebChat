import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  

public class Communicate extends HttpServlet {
    private final long serialVersionUID = 1L;
        public static String asker;
        public static String asked;
        private String names = null; 

        
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
                
        try {
            names = request.getParameter("askSession");
            System.out.println("[INFO] POST /communicate > askSession=" + names );

            asker = names.split(":")[1];
            asked = names.split(":")[0];

            System.out.println("[INFO] POST /communicate > asker=" + asker );
            System.out.println("[INFO] POST /communicate > asked=" + asked );
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        
            
    }

    public String getAsker() {
        return asker;
        } 

    public String getAsked(){
        return asked;
    }
	 

}