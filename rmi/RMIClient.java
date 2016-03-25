package rmi;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.MessageInfo;


public class RMIClient {

	public static void main(String[] args) {

		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int countTo = Integer.parseInt(args[1]);
		RMIServerI iRMIServer = null;
		if (System.getSecurityManager() == null) {
	    	System.setSecurityManager(new SecurityManager());
	    }
	        
	    try {
			Registry registry = LocateRegistry.getRegistry(args[0],2000);
			RMIServerI server = (RMIServerI) registry.lookup("RMIServerI");
            for (int i = 0; i < countTo; i++){
            	MessageInfo message = new MessageInfo(countTo, i);
            	server.receiveMessage(message);
            }
            System.out.println(countTo + " Messages Sent");
            server.exitConnection(true);
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Exception - Client");
            e.printStackTrace();
            System.exit(-1);
        }
	}    	
}
