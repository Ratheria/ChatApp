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
import javax.json.JsonObject;


public class ChatServer
{
	public static final int PORT = 8029;
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	public static final int MAX_CONNECTIONS = 2;
	public static boolean serverRunning = true;
	public static Connection[] clientConnections;
	public static Map<Integer, String> userMap = Collections.synchronizedMap(new HashMap<Integer, String>());
	private static ServerSocket sock = null;
	
	//TODO server GUI?
	//TODO synchronized client write
	
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
	
	public synchronized void sendDealio(JsonObject dealio, Object[] receiving)
	{
		//Only messages that go to other clients pass through here.
		if(receiving.length < 1)
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
			ArrayList<Object> receivingList = new ArrayList<Object>(Arrays.asList(receiving));
			for(Map.Entry<Integer, String> entry : userMap.entrySet())
			{
				if(receivingList.contains(entry.getValue()))
				{
					clientConnections[entry.getKey().intValue()].sendToClient(dealio);
				}
			}
		}
	}
	
}
