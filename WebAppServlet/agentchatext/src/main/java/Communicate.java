import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors  

public class Communicate extends HttpServlet {
    private static final long serialVersionUID = 1L;
        public static String asker;
        public static String asked;
        public String names; 
        Util util = new Util();
        String state;
        TCPServer server = new TCPServer();
        String validation;
 

        
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
                
        try {

            names = request.getParameter("askSession");
            System.out.println("[INFO] POST /communicate > askSession=" + names );

            asker = names.split(":")[1];
            asked = names.split(":")[0];

            System.out.println("[INFO] POST /communicate > asker=" + asker );
            System.out.println("[INFO] POST /communicate > asked=" + asked );

            util.saveParam(names, "askSession");

            if(!asker.equals("null") && !asked.equals("null")){

                server.active();
            }



            String val = server.validate();

            while(val.equals("")){

            }

            if(!val.equals("")){

                this.validation = "TRUE";
            }

            util.saveParam(names+":"+ this.validation, "validateSession");



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