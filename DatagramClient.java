import java.net.* ;

/**
 *  A simple datagram client
 *  Shows how to send and receive UDP packets in Java
 *
 *  @author  P. Tellenbach,  http://www.heimetli.ch
 *  @version V1.00
 */
 
/*

javac DatagramClient.java
javac DatagramServer.java

java DatagramServer 7777
java DatagramClient localhost 7777


*/


 
 
public class DatagramClient
{
   private final static int PACKETSIZE = 20000000 ;

   public static void main( String args[] )
   {
      // Check the arguments
      if( args.length != 2 )
      {
         System.out.println( "usage: java DatagramClient host port" ) ;
         return ;
      }

      DatagramSocket socket = null ;

      try
      {
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName( args[0] ) ;
         int port         = Integer.parseInt( args[1] ) ;

         // Construct the socket
         socket = new DatagramSocket() ;

         // Construct the datagram packet
         DatagramPacket packet = null;
         // Send it
         for (int i = 0; i < PACKETSIZE; i++) {
            byte [] data = ("Packet " + i).getBytes() ;
            packet = new DatagramPacket( data, data.length, host, port ) ;
            socket.send( packet ) ;
         }


         while(true) {
            // Set a receive timeout, 2000 milliseconds
            socket.setSoTimeout( 2000 ) ;
            byte [] maxSize = ("Packet " + PACKETSIZE).getBytes() ;
            // Prepare the packet for receive
            packet.setData(maxSize) ;

            // Wait for a response from the server

            socket.receive(packet);

            // Print the response
            System.out.println( new String(packet.getData()) ) ;
         }


      }
      catch( Exception e )
      {
         System.out.println( e ) ;
      }
      finally
      {
         if( socket != null )
            socket.close() ;
      }
   }
}
