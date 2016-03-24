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
         System.err.println("Arguments required: server name/IP, recv port, message count");
         System.exit(-1);
      }

      try {
         serverAddr = InetAddress.getByName(args[0]);
      } catch (UnknownHostException e) {
         System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
         System.exit(-1);
      }
      recvPort = Integer.parseInt(args[1]);
      countTo = Integer.parseInt(args[2]);
      UDPClient client = new UDPClient();
      client.testLoop(serverAddr, recvPort, countTo);
   }

   public UDPClient() {
      try {
         sendSoc = new DatagramSocket();
      } catch (SocketException e) {
         System.out.println("Couldn't initialise socket - Client");
         e.printStackTrace();
      }
   }

   private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
      for (int i = 1; i <= countTo; i++) {
         MessageInfo msg = new MessageInfo(countTo,i);
         this.send(msg.toString(),serverAddr,recvPort);
      }
      System.out.println(countTo + "Messages sent");
   }

   private void send(String payload, InetAddress destAddr, int destPort) {

      byte[] pktData = payload.getBytes();
      int payloadSize = pktData.length;
      
      try {
         DatagramPacket pkt = new DatagramPacket(payload, payloadSize, destAddr, destPort);
      } catch (SocketException e) {
         System.out.println("Couldn't setup packet - Client");
         e.printStackTrace();
      }

      try {
          sendSoc.send(pkt) ;
      } catch (IOException e) {
         System.out.println("Couldn't send packet - Client");
         e.printStackTrace();
      }
   }
}
