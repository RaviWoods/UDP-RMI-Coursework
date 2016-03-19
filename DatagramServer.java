import java.net.* ;

/**
 *  A simple datagram server
 *  Shows how to send and receive UDP packets in Java
 *
 *  @author  P. Tellenbach, http://www.heimetli.ch
 *  @version V1.01
 */

 
public class DatagramServer
{

  private final static int MAXPACKETLENGTH = 6 ;
   public static void main( String args[] )
   {
      
      // Check the arguments
      if( args.length != 1 )
      {
         System.out.println( "usage: DatagramServer port" ) ;
         return ;
      }

      try
      {
        int packetNo = 1;
        int totalPacketNo = 0;
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

            String[] parts = data.split("/");
            String part2 = parts[1];
            System.out.println("totalPacketNo = " + part2);
            totalPacketNo = Integer.parseInt(part2);
            //System.out.println("packetNo = " + part1 + ", totalNo = " + part2);
            packetNo++;
          }  
          System.out.println("Messages sent: " + totalPacketNo);
          System.out.println("Messages recieved: " + packetNo);
          System.out.println("Messages lost: " + (totalPacketNo-packetNo));
      }
      catch( Exception e )
      {
          System.out.println( e ) ;
          //System.out.println("Messages sent: " + totalPacketNo);
          //System.out.println("Messages recieved: " + packetNo);
          //System.out.println("Messages lost: " + (totalPacketNo-packetNo));
          
      }
  }
}
