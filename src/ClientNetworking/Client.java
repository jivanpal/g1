// Usage:
//        java Client user-nickname port hostname
//
// After initializing and opening appropriate sockets, we start two
// client threads, one to send messages, and another one to get
// messages.
//
//
// Another limitation is that there is no provision to terminate when
// the server dies.
package ClientNetworking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
class Client {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {

    // Check correct usage:
    // Initialize information:
    int port = ClientVariables.PORT;
    String hostname = ClientVariables.HOSTNAME;

    // Open sockets:
    PrintStream toServer = null;
    BufferedReader fromServer = null;
    Socket server = null;

    //get a socket and the 2 streams
    try {
      server = new Socket(hostname, port);
      toServer = new PrintStream(server.getOutputStream());
      fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
    } 
    catch (UnknownHostException e) {
      System.err.println("Unknown host: " + hostname);
      System.exit(1); 
    } 
    catch (IOException e) {
      System.err.println("The server doesn't seem to be running " + e.getMessage());
      System.exit(1);
    }

    // ClientSender sender = new ClientSender(toServer);
    //ClientReceiver receiver = new ClientReceiver(fromServer);


    // Run them in parallel:
  //  sender.start();
  //  receiver.start();
    
    // Wait for them to end and close sockets.
    try {
   //   sender.join();
      toServer.close();
   //   receiver.join();
      fromServer.close();
      server.close();
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }

  }
}
