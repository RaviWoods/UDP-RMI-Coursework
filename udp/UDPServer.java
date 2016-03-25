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
    boolean open = true;

    do {
      try {
        pac = new DatagramPacket(pacData, MAXPACKETLENGTH);
        recvSoc.setSoTimeout(30000) ;
        recvSoc.receive(pac);
        String data = new String(pac.getData()).trim();
        processMessage(data);
      } catch (IOException e) {
        open = false;
      }
    } while (open && totalSent != totalRecieved);

  }

  public void processMessage(String data) {
    MessageInfo msg;
    try {
      msg = new MessageInfo(data); 
    } catch (Exception e) {
      System.out.println("Couldn't convert data to MessageInfo - Server");
      return;
    }
    
    
    if (totalRecieved == -1 ){
      totalSent = msg.totalMessages;
      receivedMessages = new boolean[totalSent];
      totalRecieved = 0;
    }
    
    receivedMessages[msg.messageNum] = true;
    totalRecieved++;

  }

  public void finish() {
    if (totalRecieved == -1 ){
      System.out.println("No messages recieved!");
      return;
    }
    int totalLost = 0;
    int [] lostMessages = new int[totalSent];
    for(int i = 0; i < totalSent; i++) {
      if(!receivedMessages[i]) {
        lostMessages[totalLost] = i;
        totalLost++;
      }
    }
    System.out.println("Messages sent: " + totalSent);
    System.out.println("Messages recieved: " + totalRecieved);
    System.out.println("Messages lost: " + (totalSent-totalRecieved));
    if (totalLost != 0) {
      System.out.println("Lost Messages are: ");
      for(int i = 0; i < totalLost; i++) {
        if(i == (totalLost-1)) {
          System.out.println(lostMessages[i] + ".");  
        } else {
          System.out.print(lostMessages[i] + ", ");
        }

        if (i%18 == 17) {
        }
            System.out.println("");  
        }
   
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
      System.err.println("Arguments required: recv port - Server");
      System.exit(-1);
    }
    recvPort = Integer.parseInt(args[0]);
    UDPServer server = new UDPServer(recvPort);
    server.run();
    server.finish();
    return;
  }

}
