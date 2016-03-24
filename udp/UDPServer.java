package udp;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import common.MessageInfo;
 
public class UDPServer {

  private final static int MAXPACKETLENGTH = 50;
  private DatagramSocket recvSoc;
  private int totalSent = -1;
  private int[] receivedMessages;
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

      socket.receive(pac);
      processMessage(pac.getData);
    } catch (IOException e) {
      processMessage(pac.getData);
    }
  }

  public void processMessage(String data) {

    MessageInfo msg = new MessageInfo(data);
    
    if (totalRecieved == -1 ){
      totalSent = msg.totalMessages;
      receivedMessages = new bool[totalSent];
      totalRecieved = 0;
    }
    
    receivedMessages[msg.messageNum] = true;
    totalRecieved++;

  }

  public void finish() {
    int totalLost = 0;
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



















////////////////////////////////////
{

  private final static int MAXPACKETLENGTH = 7 ;
   public static void main( String args[] )
   {
      int packetNo = 1;
      int totalPacketNo = 0;
      // Check the arguments
      if( args.length != 1 )
      {
         System.out.println( "usage: DatagramServer port" ) ;
         return ;
      }

      try
      {

         // Convert the argument to ensure that is it valid
        int port = Integer.parseInt( args[0] ) ;
        // Construct the socket
        DatagramSocket socket = new DatagramSocket( port ) ;
        socket.setSoTimeout( 60000 ) ;
        System.out.println( "The server is ready..." ) ;

        
        int buf = 2*MAXPACKETLENGTH + 1;
          while(packetNo != totalPacketNo) { 
            // Create a packet

            DatagramPacket packet = new DatagramPacket( new byte[buf], buf ) ;

            // Receive a packet (blocking)
            socket.receive( packet ) ;

            // Print the packet
            //System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()) ) ;
            String data = new String(packet.getData());

            Pattern p = Pattern.compile("([0-9]+)/([0-9]+)");
            Matcher m = p.matcher(data);
            if (m.find())
            { 
              totalPacketNo = Integer.parseInt(m.group(2));
            }

            
            packetNo++;
            System.out.println("packetNo = " + packetNo + ", totalNo = " + totalPacketNo);
          }  

          System.out.println("Messages sent: " + totalPacketNo);
          System.out.println("Messages recieved: " + packetNo);
          System.out.println("Messages lost: " + (totalPacketNo-packetNo));
      }
      catch( Exception e )
      {
          System.out.println( e ) ;
          System.out.println("Messages sent: " + totalPacketNo);
          System.out.println("Messages recieved: " + packetNo);
          System.out.println("Messages lost: " + (totalPacketNo-packetNo));
          
      }
  }
}
