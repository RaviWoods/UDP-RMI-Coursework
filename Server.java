/*

http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html


http://mrbool.com/how-to-create-rmi-client-and-server-to-invoke-remove-method-of-rmi-server-in-java/28320


This RMI stuff can be really messy !

To run on the same machine, do the following:


1. Open 3 windows (one for rmiregistry, one for the server, one for the client).
Make sure that you are in the SAME directory in each window,
e.g.

cd $HOME

We'll call the windows R,S and C

2. In window R
rmiregistry

It will just sit there, running the registry

3. In window S
javac Hello.java
javac Server.java

java Server
java -Djava.rmi.server.hostname=127.0.0.1 Server

It should say this:

before getRegistry
unbind failed - probably not a problem:java.rmi.NotBoundException: Hello
Server ready

or (if you're running it again without restarting the rmiregistry):
before getRegistry
Server ready


and then just sit, waiting.


4. In window C

javac Client.java
java Client 127.0.0.1

It should say:
response: Hello, world!




To run on different machines, do the same, except:

. windows R and S will be on the "server" machine; window C will be on the "client" machine

. the "server" machine must either have all ports open (ugh - a security risk !)
or be doing ssh port forwarding (ugh !)
 
. when you run the server, you must specify the public ip address of the machine it's running on.
So, if the ip is 52.18.133.131, you'd do this:

java -Djava.rmi.server.hostname=52.18.133.131 Server


. the client must specify that same ip address, like this:
java Client 52.18.133.131


*/


import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
        
public class Server implements Hello {
        
    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            Hello stub = (Hello)UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
			System.err.println("before getRegistry");
            Registry registry = LocateRegistry.getRegistry();
            
            try {
            	registry.unbind("Hello");
			} catch (Exception e) {
				System.err.println("unbind failed - probably not a problem:" + e.toString() + "/unbind_failed");
			}

            try {
                registry.bind("Hello", stub);
            } catch (Exception e) {
                System.err.println("BIND FAILED" + e.toString());
            }
            
            System.err.println("Server ready");
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            //e.printStackTrace();
        }
    }
}
