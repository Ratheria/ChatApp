

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.*;

public class ChatServer
{
	private static final int PORT = 8029;
	private static final int MAX_CONNECTIONS = 2;
	private static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private static HashMap<String, Connection> clientConnections;
	//TODO keep array handy?
	private static ServerSocket sock = null;
	private static boolean serverRunning = true;
	private static int connections = 0;
	//TODO server GUI?
	
	public static void main(String[] args) throws IOException 
	{
		clientConnections = new HashMap<String, Connection>();
		try 
		{
			sock = new ServerSocket(PORT);
			do
			{
				for(String currentKey : clientConnections.keySet())
				{
					if(clientConnections.get(currentKey) == null)
					{	clientConnections.remove(currentKey);	}
				}
				connections = clientConnections.size();
				Connection latestConnection = new Connection(sock.accept());
				//TODO parse dealio object for key
				String key = " " + connections;
				//System.out.print(connections + " ");
				if(connections < MAX_CONNECTIONS)
				{
					//if(!clientConnections.containsKey(key))
					//{
						clientConnections.put(key, latestConnection);
						EXECUTOR.execute(latestConnection);
						//System.out.println("connected");
					//}
					//else
					//{
						//TODO username taken dealio
					//}
				}
				else
				{
					//TODO server full dealio
					//status is -1
					//System.out.println("too many users");
				}
				//TODO remember to terminate client from client end
			}
			while(serverRunning);
		}
		catch (IOException ioe) { }
		finally 
		{
			if (sock != null)
			{	sock.close();	}
		}
	}
}
