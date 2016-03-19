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
   private final static int PACKETSIZE = 500 ;

   public static void main( String args[] )
   {
      // Check the arguments
      if( args.length != 3 )
      {
         System.out.println( "usage: java DatagramClient host port packetNo" ) ;
         return ;
      }

      DatagramSocket socket = null ;
      int x = 0;
      try
      {
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName( args[0] ) ;
         int port         = Integer.parseInt( args[1] ) ;
         int packetNo     = Integer.parseInt( args [2] );
         // Construct the socket
         socket = new DatagramSocket() ;

         // Construct the datagram packet
         DatagramPacket packet = null;
         // Send it
         for (int i = 1; i <= packetNo; i++) {
            byte [] data = (i + "/" + packetNo).getBytes() ;
            System.out.println("packetNo = " + packetNo);
            System.out.println(i + "/" + packetNo);
            packet = new DatagramPacket( data, data.length, host, port ) ;
            socket.send( packet ) ;
         }


      }
      catch( Exception e )
      {
         System.out.println("x = " + x) ;
         System.out.println( e ) ;
      }
      finally
      {
         if( socket != null )
            socket.close() ;
      }
   }
}
