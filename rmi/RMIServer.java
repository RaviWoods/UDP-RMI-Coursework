
package rmi;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

  	private int totalSent = -1;
  	private boolean[] receivedMessages;
  	private int totalRecieved = -1;
	public RMIServer() throws RemoteException {
		super();
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {
		// Function run Client-Side when message sent

		// Setup on first message
	    if (totalRecieved == -1){
	      totalSent = msg.totalMessages;
	      receivedMessages = new boolean[totalSent];
	      totalRecieved = 0;
	    }
	    
	    // Log each message in the array
	    receivedMessages[msg.messageNum] = true;
	    totalRecieved++;

	    // If this is the last expected packet, print out results
		if(msg.messageNum = msg.totalMessages - 1){
			finish();
		}

	}
	public static void main(String[] args) {
		
		// Set up Security Policy
		if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	    }

		try {
			// Create server and bind to registry
			RMIServerI server = new RMIServer();
	        rebindServer("RMIServerI", server);
	        System.out.println("Server ready...");
	    } catch (Exception e) {
	        System.err.println("RMIServerI exception:");
	        e.printStackTrace();
	        System.exit(-1);
	    }
	}

	protected static void rebindServer(String name, RMIServerI server) {
		// Function for binding Server and Registry

        try {
        	Registry registry = LocateRegistry.createRegistry(2000);
			registry.rebind(name, server);
		} catch (RemoteException e) {
			System.out.println("Couldn't bind registry - Server");
			e.printStackTrace();
			System.exit(-1);
		}	
	}
		
	public void finish() {
		// Function to print stats at end of transmission

		int totalLost = 0;
		int [] lostMessages = new int[totalSent];

		// Get indexes for lost messages into an array
		for(int i = 0; i < totalSent; i++) {
		  if(!receivedMessages[i]) {
		    lostMessages[totalLost] = i;
		    totalLost++;
		  }
		}

		// Print summary stats
		System.out.println("Messages sent: " + totalSent);
		System.out.println("Messages recieved: " + totalRecieved);
		System.out.println("Messages lost: " + (totalSent-totalRecieved));

		// Pretty print Lost message numbers
		if (totalLost != 0) {
		  System.out.println("Lost Messages are: ");
		  for(int i = 0; i < totalLost; i++) {
		    if(i == (totalLost-1)) {
		      System.out.println(lostMessages[i] + ".");  
		    } else {
		      System.out.print(lostMessages[i] + ",\t");
		    }

		    if (i%12 == 11) {
		        System.out.println("");  
		    }
		  }
		}
	}
		
}

