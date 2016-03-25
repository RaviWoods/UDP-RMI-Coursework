/*
 * Created on 07-Sep-2004
 * @author bandara
 */
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

/**
 * @author bandara
 *
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private int[] lostMessages;
	private int totalLostMessages =0;
	private int totalSent = -1;
	public RMIServer() throws RemoteException {
		super();
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages == -1 ){
			totalSent = msg.totalMessages;
			receivedMessages = new int[totalSent];
			totalMessages = 0;
			lostMessages = new int[totalSent];
			
		}
		
		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		totalMessages += 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum + 1  == msg.totalMessages){
			summary();
		}

	}


	public static void main(String[] args) {
		//RMIServer rmis = null;
		 
		if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	        }
		try{
			String name = "RMIServerI";
			RMIServerI server = new RMIServer();
	        
	        rebindServer(name, server);
	        System.out.println("RMIServerI bound");
	    }catch (Exception e) {
	        System.err.println("RMIServerI exception:");
	        e.printStackTrace();
	    }
	}

	protected static void rebindServer(String name, RMIServerI server) {
		
        try {
        	Registry registry = LocateRegistry.createRegistry(2000);
			registry.rebind(name, server);
		} catch (RemoteException e) {
			System.out.println("Problem creating/binding to registry at port 2000");
			e.printStackTrace();
		}	
	}
		
	public void summary(){
		int i = 0;
		
		while( i < totalSent){
			if (receivedMessages[i] != 1 ){
					lostMessages[totalLostMessages] = i;
					totalLostMessages++;
			}
			i++;
		}
		
		lostMessages = Arrays.copyOf(lostMessages, totalLostMessages);
		
		System.out.println("Messages sent: " + totalSent);
		System.out.println("Messages received: " + totalMessages);
		if(totalLostMessages > 0){
		System.out.println("Missing Messages are: " + Arrays.toString(lostMessages));
		}
	}
		
}

