/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatServer
{
	public static final int PORT = 8029;
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	public static final int MAX_CONNECTIONS = 2;
	public static boolean serverRunning = true;
	public static Connection[] clientConnections;
	public static HashMap<Integer, String> userMap;
	private static ServerSocket sock = null;
	
	//TODO server GUI?
	
	public static void main(String[] args) throws IOException 
	{
		clientConnections = new Connection[MAX_CONNECTIONS];
		userMap = new HashMap<Integer, String>();
		try 
		{
			sock = new ServerSocket(PORT);
			EXECUTOR.execute(new UpdateConnections());
			while(serverRunning)
			{
				Connection latestConnection = new Connection(sock.accept());
				int firstNull = -1;
		        for (int i = 0; i < MAX_CONNECTIONS; i++) 
		        {
		        	if (clientConnections[i] == null) 
		        	{
		        		//System.out.println("Connected");
		        		firstNull = i;
		        		clientConnections[i] = latestConnection;
		        		latestConnection.setID(i);
		        		userMap.put(i, " ");
		        		EXECUTOR.execute(latestConnection);
		        		break;
		        	}
		        }
		        if (firstNull == -1)
		        {
		        	System.out.println("Server Full");
		        	//TODO server full
		        	latestConnection.close();
		        }
			}
		}
		catch (IOException ioe) { }
		finally 
		{
			if (sock != null)
			{	sock.close();	}
		}
	}
}
