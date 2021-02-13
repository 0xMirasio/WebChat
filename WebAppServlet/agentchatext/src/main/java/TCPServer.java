import java.net.*;
import java.io.*;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class TCPServer extends Thread {

  final static int port = 6250;
  private Socket socket;
  String bool;
  String value="";


  public TCPServer(Socket socket) {
    this.socket = socket;
  }

  public TCPServer() {

  }

  public void active() {
    try {
      ServerSocket socketServeur = new ServerSocket(port);
      System.out.println("Lancement du serveur");
      this.bool = "true";
      while (true) {
        Socket socketClient = socketServeur.accept();
        TCPServer t = new TCPServer(socketClient);
        t.start();
        //socketServeur.close();
      }
    } catch (Exception e) {
      this.bool = "false";
      e.printStackTrace();
    }
  }

  public void run() {

    treatments();

  }

  public void treatments() {

    try {
        String message = "";
        InetAddress client = this.socket.getInetAddress();
        while(client==null){
          //System.out.println("None");
        }

        if(client!=null){
          this.value = "OK";
        }
        System.out.println("Connexion avec le client : " + client);

        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        PrintStream out = new PrintStream(this.socket.getOutputStream());
       
        while(true){
            message = in.readLine();
            System.out.println(message);
            if(message.equals("END")) break;
          }

          in.close();
          out.close();
          socket.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    
    }




  }

  public String validate(){

    return this.value;

  }


    
    
}
