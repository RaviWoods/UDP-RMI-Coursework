package udp;

import java.io.IOException;
import java.net.*;
import common.MessageInfo;

public class UDPClient {

   private DatagramSocket sendSoc;

   public static void main(String[] args) {
      InetAddress serverAddr = null;
      int         recvPort;
      int         countTo;

      // Get the parameters
      if (args.length < 3) {
         System.err.print("Args: ");
         System.err.println("server name/IP, recv port, msg count");
         System.exit(-1);
      }

      try {
         serverAddr = InetAddress.getByName(args[0]);
      } catch (UnknownHostException e) {
         System.out.print("Bad server address in UDPClient, " + args[0]);
         System.out.print(" caused an unknown host exception " + e);
         System.out.println(" - Client");
         System.exit(-1);
      }
      recvPort = Integer.parseInt(args[1]);
      countTo = Integer.parseInt(args[2]);
      UDPClient client = new UDPClient();
      client.testLoop(serverAddr, recvPort, countTo);
      return;
   }

   public UDPClient() {
      try {
         sendSoc = new DatagramSocket();
      } catch (SocketException e) {
         System.out.println("Couldn't initialise socket - Client");
         e.printStackTrace();
         System.exit(-1);
      }
   }

   private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
      for (int i = 0; i < countTo; i++) {
         MessageInfo msg = new MessageInfo(countTo,i);
         this.send(msg.toString(),serverAddr,recvPort);
      }
      System.out.println(countTo + " Messages sent");
   }

   private void send(String payload, InetAddress destAddr, int destPort) {

      byte[] pktData = payload.getBytes();
      int payloadSize = pktData.length;
      DatagramPacket pkt;
      pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);

      try {
          sendSoc.send(pkt) ;
      } catch (IOException e) {
         System.out.println("Couldn't send packet - Client");
         e.printStackTrace();
         System.exit(-1);
      }
   }
}
