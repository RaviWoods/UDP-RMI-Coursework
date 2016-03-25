package udp;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.lang.Boolean;
import common.MessageInfo;
 
public class UDPServer {

  private final static int MAXPACKETLENGTH = 50;
  private DatagramSocket recvSoc;
  private int totalSent = -1;
  private boolean[] receivedMessages;
  private int totalRecieved = -1;

  private void run() {
  //Function to recieve and process messages

    byte[]      pacData = new byte[MAXPACKETLENGTH];
    DatagramPacket  pac = null;
    boolean open = true;

    // Keep recieving messages until timeout ends, or when all messages recieved
    do {
      try {

        // Setup recieving packet, with buffer
        pac = new DatagramPacket(pacData, MAXPACKETLENGTH);

        // Set timeout on Socket, and recieve packet
        recvSoc.setSoTimeout(30000) ;
        recvSoc.receive(pac);

        // Process recieved data
        String data = new String(pac.getData()).trim();
        processMessage(data);
      } catch (IOException e) {
        // Boolean changes when timeout finishes
        open = false;
      }
    } while (open && totalSent != totalRecieved);
  }

  public void processMessage(String data) {
  // Function to process incoming data

    // Cast string to MessageInfo
    MessageInfo msg = null;
    try {
      msg = new MessageInfo(data); 
    } catch (Exception e) {
      System.out.println("Couldn't convert data to MessageInfo - Server");
      System.exit(-1);
    }
    
    // Setup if first message
    if (totalRecieved == -1 ){
      totalSent = msg.totalMessages;
      receivedMessages = new boolean[totalSent];
      totalRecieved = 0;
    }
    
    // Log each message
    receivedMessages[msg.messageNum] = true;
    totalRecieved++;

  }

  public void finish() {
  // Function to print stats at end of transmissio

    // Print error if no messages recieved
    if (totalRecieved == -1 ){
      System.out.println("No messages recieved!");
      return;
    }

    int totalLost = 0;
    int [] lostMessages = new int[totalSent];

    // Get indexes for lost messages into an array
    for(int i = 0; i < totalSent; i++) {
      if(!receivedMessages[i]) {
        lostMessages[totalLost] = i;
        totalLost++;
      }
    }

    // Print summary stats
    System.out.println("Messages sent: " + totalSent);
    System.out.println("Messages recieved: " + totalRecieved);
    System.out.println("Messages lost: " + (totalSent-totalRecieved));

    // Pretty print Lost message numbers
    if (totalLost != 0) {
      System.out.println("Lost Messages are: ");
      for(int i = 0; i < totalLost; i++) {
        if(i == (totalLost-1)) {
          System.out.println(lostMessages[i] + ".");  
        } else {
          System.out.print(lostMessages[i] + ",\t");
        }

        if (i%12 == 11) {
            System.out.println("");  
        }
      }
    }
  }


  public UDPServer(int rp) {
  // Setup Socket on initialisation

    try {
      recvSoc = new DatagramSocket(rp);
    } catch (SocketException e) {
      System.out.println("Couldn't initialise socket - Server");
      e.printStackTrace();
      System.exit(-1);
    }
    System.out.println("Server ready...");
  }

  public static void main(String args[]) {
    int recvPort;
    if (args.length < 1) {
      System.err.println("Arguments required: recv port");
      System.exit(-1);
    }
    recvPort = Integer.parseInt(args[0]);

    // Setup and Run Server
    UDPServer server = new UDPServer(recvPort);
    server.run();

    // Print out stats on completion
    server.finish();
    return;
  }

}
