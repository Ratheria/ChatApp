/**
 *	@author Ariana Fairbanks
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatServer
{
	private static final int PORT = 8029;
	private static final int MAX_CONNECTIONS = 2;
	private static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private static Connection[] clientConnections;
	private static HashMap<Integer, String> userMap;
	private static ServerSocket sock = null;
	private static boolean serverRunning = true;
	private static int connections = 0;
	
	//TODO server GUI?
	
	public static void main(String[] args) throws IOException 
	{
		clientConnections = new Connection[MAX_CONNECTIONS];
		userMap = new HashMap<Integer, String>();
		try 
		{
			sock = new ServerSocket(PORT);
			while(serverRunning)
			{
				Connection latestConnection = new Connection(sock.accept());
				int firstNull = -1;
				
				for(Connection connection : clientConnections)
				{
					if(connection != null)
					{
			        	if(connection.closed)
			        	{
			        		connection.close();
			        		connection = null;
			        	}
					}
				}
				
		        for (int i = 0; i < MAX_CONNECTIONS; i++) 
		        {
		        	if (clientConnections[i] == null) 
		        	{
		        		//System.out.println("Connected");
		        		firstNull = i;
		        		clientConnections[i] = latestConnection;
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
