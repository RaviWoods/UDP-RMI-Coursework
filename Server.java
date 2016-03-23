/*

http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html

javac Hello.java
javac Client.java
javac Server.java


rmiregistry
java Server

java Client 127.0.0.1



http://mrbool.com/how-to-create-rmi-client-and-server-to-invoke-remove-method-of-rmi-server-in-java/28320



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
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            //e.printStackTrace();
        }
    }
}
