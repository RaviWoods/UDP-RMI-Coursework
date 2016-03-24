/*
 * Created on 07-Sep-2004
 * @author bandara
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager; // deprecated? use SecurityManger instead?

import common.MessageInfo;

/**
 * @author bandara
 *
 */
public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
	
		if (System.getSecurityManager()==null)
			System.setSecurityManager(new RMISecurityManager());
		// TO-DO: Bind to RMIServer
		try {
			String serverURL = null;
			Registry registry = null;
			try {
				serverURL = new String("rmi://" + args[0] + "/RMIServer");
			}
			catch (Exception e) {
			System.out.println("1");
			System.out.println(e);
			}
			try {
				  registry = LocateRegistry.getRegistry(args[0],1099);
			}
			catch (Exception e) {
			System.out.println("2");
			System.out.println(e);
			}
			try {
				iRMIServer = (RMIServerI) registry.lookup(serverURL);
			}
			catch (Exception e) {
			System.out.println("3");
			System.out.println(e);
			}
			try {
				for (int i=0; i<numMessages; i++) {
				MessageInfo msg = new MessageInfo(numMessages, i);
				iRMIServer.receiveMessage(msg);
				}
			}
			catch (Exception e) {
			System.out.println("4");
			System.out.println(e);
			}
			
          
			

		// TO-DO: Attempt to send messages the specified number of times
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		



	}
}
