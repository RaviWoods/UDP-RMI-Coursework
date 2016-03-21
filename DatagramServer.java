import java.net.* ;
import java.lang.* ;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *  A simple datagram server
 *  Shows how to send and receive UDP packets in Java
 *
 *  @author  P. Tellenbach, http://www.heimetli.ch
 *  @version V1.01
 */

 
public class DatagramServer
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
