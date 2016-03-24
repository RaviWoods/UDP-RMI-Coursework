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
			serverURL = new String("rmi://" + args[0] + "/RMIServer");
			registry = LocateRegistry.getRegistry(args[0],2000);
			iRMIServer = (RMIServerI) Naming.lookup(serverURL);
			for (int i=0; i<numMessages; i++) {
					MessageInfo msg = null;
					try {
						msg = new MessageInfo(numMessages, i);
					}
					catch (Exception e) {
						System.out.println("1st");
						System.out.println("i = " + i);
						System.out.println(e);
					}

					try {
						iRMIServer.receiveMessage(msg);
					}
					catch (Exception e) {
						System.out.println("2nd");
						System.out.println("i = " + i);
						System.out.println(e);
					}
					
				}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		



	}
}
