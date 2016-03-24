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
    byte[]      pacData = new byte[MAXPACKETLENGTH];
    DatagramPacket  pac = null;
    
    try {
      try {
        pac = new DatagramPacket(pacData, MAXPACKETLENGTH);
      } catch (SocketException e) {
         System.out.println("Couldn't setup packet - Server");
         e.printStackTrace();
      }

      recvSoc.setSoTimeout(30000) ;
      recvSoc.receive(pac);
      String data = new String(pac.getData());
      processMessage(data);
    } catch (IOException e) {
      System.out.println("Exceeded Timeout");
      return;
    }
  }

  public void processMessage(String data) {

    MessageInfo msg = new MessageInfo(data);
    
    if (totalRecieved == -1 ){
      totalSent = msg.totalMessages;
      receivedMessages = new boolean[totalSent];
      totalRecieved = 0;
    }
    
    receivedMessages[msg.messageNum] = true;
    totalRecieved++;

  }

  public void finish() {
    int totalLost = 0;
    int [] lostMessages = new int[totalSent];
    for(int i = 0; i < totalSent; i++) {
      if(!recievedMessages[i]) {
        lostMessages[totalLost] = i;
        totalLost++;
      }
    }
    System.out.println("Messages sent: " + totalSent);
    System.out.println("Messages recieved: " + totalRecieved);
    System.out.println("Messages lost: " + (totalSent-totalRecieved));
    System.out.println("Lost Messages are: ");
    for(int i = 0; i < totalLost; i++) {
      System.out.print(lostMessages[i] + ", ");
    }
    
  }


  public UDPServer(int rp) {
    try {
      recvSoc = new DatagramSocket(rp);
    } catch (SocketException e) {
      System.out.println("Couldn't initialise socket - Server");
      e.printStackTrace();
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
    UDPServer server = new UDPServer(recvPort);
    server.run();
    server.finish();
    return;
  }

}
