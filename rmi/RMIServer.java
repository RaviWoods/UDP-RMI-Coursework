
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
import java.lang.Boolean;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

  	private int totalSent = -1;
  	private boolean[] receivedMessages;
  	private int totalRecieved = -1;
  	public int end = 0;
	public RMIServer() throws RemoteException {
		super();
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

	    if (totalRecieved == -1){
	      totalSent = msg.totalMessages;
	      receivedMessages = new boolean[totalSent];
	      totalRecieved = 0;
	    }
	    
	    receivedMessages[msg.messageNum] = true;
	    totalRecieved++;

		if(totalRecieved == totalSent && totalRecieved != -1){
			finish();
		}

	}

	public void exitConnection(boolean success) throws RemoteException {
		if(success) {
			end = 1;
		} else {
			end = 2;
		}
	}
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	    }

		try {
			RMIServerI server = new RMIServer();
	        rebindServer("RMIServerI", server);
	        System.out.println("Server ready...");
	        do {
	        	if(end == 1) {
	        		System.out.println("Exit Success");
					System.exit(0);
	        	} else if (end == 2) {
	        		System.out.println("Exit Failure");
					System.exit(-1);
	        	}
	        } while(end == 0);
	    } catch (Exception e) {
	        System.err.println("RMIServerI exception:");
	        e.printStackTrace();
	        System.exit(-1);
	    }
	}

	protected static void rebindServer(String name, RMIServerI server) {
		
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
		      System.out.print(lostMessages[i] + ",\t");
		    }

		    if (i%12 == 11) {
		        System.out.println("");  
		    }
		  }
		}
	}
		
}

