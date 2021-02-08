import java.net.*;
import java.io.*;

public class TCPServer extends Thread {

  final static int port = 7000;
  private Socket socket;


  public TCPServer(Socket socket) {
    this.socket = socket;
  }

  public static void main(String[] args) {
    try {
      ServerSocket socketServeur = new ServerSocket(port);
      System.out.println("Lancement du serveur");
      while (true) {
        Socket socketClient = socketServeur.accept();
        TCPServer t = new TCPServer(socketClient);
        t.start();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void run() {

    treatments();

  }

  public void treatments() {

    try {
        String message = "";
  
        System.out.println("Connexion avec le client : " + socket.getInetAddress());
  
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());
        message = in.readLine();
        while(message!=""){

            System.out.println(message);
        }
        out.println("Bonjour " + message);
  
        socket.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    
    }



  }


    
    
}
