/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;


public class ChatServer
{
	public static final int PORT = 8029;
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	public static final int MAX_CONNECTIONS = 5;
	public static boolean serverRunning = true;
	public static Connection[] clientConnections;
	public static Map<Integer, String> userMap = Collections.synchronizedMap(new HashMap<Integer, String>());
	private static ServerSocket sock = null;
	
	//TODO server GUI?
	
	public ChatServer() throws IOException
	{
		clientConnections = new Connection[MAX_CONNECTIONS];
		userMap = new HashMap<Integer, String>();
		sock = new ServerSocket(PORT);
		EXECUTOR.execute(new UpdateConnections());
		while(serverRunning)
		{
			Connection latestConnection = new Connection(sock.accept(), this);
			int firstNull = -1;
	        for (int i = 0; i < MAX_CONNECTIONS; i++) 
	        {
	        	if (clientConnections[i] == null) 
	        	{
	        		//System.out.println("Connected");
	        		firstNull = i;
	        		clientConnections[i] = latestConnection;
	        		userMap.put(i, " ");
	        		break;
	        	}
	        }
        	latestConnection.setID(firstNull);
        	EXECUTOR.execute(latestConnection);
		}
		if (sock != null)
		{	sock.close();	}
	}
	
	public static void main(String[] args) throws IOException 
	{
		new ChatServer();
	}
	
	public synchronized static void updateConnections()
	{
		Connection connection;
		for (int i = 0; i < ChatServer.MAX_CONNECTIONS; i++) 
	    {
			connection = clientConnections[i];
			if(connection != null)
			{
	        	if(connection.closed)
	        	{
	        		connection.close();
	        		Integer currentID = new Integer(i);
	        		userMap.remove(currentID);
	        		clientConnections[i] = null;
	        	}
			}
	    }
	}
	
	public synchronized void sendDealio(JsonObject dealio, JsonArray receiving)
	{
		//Only messages that go to other clients pass through here.
		if(receiving.isEmpty())
		{
			for(Connection currentConnection : clientConnections)
			{
				if(currentConnection != null)
				{
					currentConnection.sendToClient(dealio);
				}
			}
		}
		else
		{
			for (JsonString value : receiving.getValuesAs(JsonString.class))
			{
	            String userStringValue = value.getString();
	            int userIntValue = Integer.parseInt(userStringValue.substring(userStringValue.indexOf(":") + 1));
	            Connection current = clientConnections[userIntValue];
	            if(current != null)
	            {
	            	current.sendToClient(dealio);
	            }
			}
		}
	}
	
}
