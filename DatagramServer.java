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
         // Convert the argument to ensure that is it valid
        int port = Integer.parseInt( args[0] ) ;
        // Construct the socket
        DatagramSocket socket = new DatagramSocket( port ) ;

        System.out.println( "The server is ready..." ) ;

        int packetNo = 1;
        int totalPacketNo = 0;
        int buf = 2*MAXPACKETLENGTH + 1;
          while(packetNo != 200000) { 
            // Create a packet

            DatagramPacket packet = new DatagramPacket( new byte[buf], buf ) ;

            // Receive a packet (blocking)
            socket.receive( packet ) ;

            // Print the packet
            //System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()) ) ;
            String string = new String(packet.getData());
            System.out.println(string);
            //String[] parts = string.split("/");
            //String part1 = parts[0]; // 004
            //System.out.println("part 1 = " + part1);
            //String part2 = parts[1]; // 034556
            // System.out.println("part 2 = " + part2);
            //System.out.println("packetNo = " + part1 + ", totalNo = " + part2);
            packetNo++
        }  
     }
     catch( Exception e )
     {
        System.out.println( e ) ;
     }
  }
}
