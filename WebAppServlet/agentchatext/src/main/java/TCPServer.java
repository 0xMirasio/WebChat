import java.net.*;
import java.io.*;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

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
          try{
            FileWriter myWriter = new FileWriter("C:\\Users\\youssf\\Desktop\\Test.txt");
            myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

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
