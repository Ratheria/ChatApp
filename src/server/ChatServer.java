/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.swing.JFrame;


public class ChatServer extends JFrame
{
	private static final long serialVersionUID = 3905863957240410182L;
	public final int PORT = 8029;
	public final Executor EXECUTOR = Executors.newCachedThreadPool();
	public final static int MAX_CONNECTIONS = 5;
	public boolean serverRunning = true;
	public Connection[] clientConnections;
	public static Map<Integer, String> userMap = Collections.synchronizedMap(new HashMap<Integer, String>());
	private ServerSocket sock = null;
	private ChatServerPanel panel;
	
	//TODO add more to server GUI
	
	public ChatServer() throws IOException
	{
		setSize(300, 80);
		setResizable(false);
		panel = new ChatServerPanel(this);
		setContentPane(panel);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientConnections = new Connection[MAX_CONNECTIONS];
		userMap = new HashMap<Integer, String>();
		sock = new ServerSocket(PORT);
		while(serverRunning)
		{
			Connection latestConnection = new Connection(sock.accept(), this);
			updateConnections();
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
	
	public synchronized void updateConnections()
	{
		Connection connection;
		for (int i = 0; i < ChatServer.MAX_CONNECTIONS; i++) 
	    {
			connection = clientConnections[i];
			if(connection != null)
			{
	        	if(connection.closed)
	        	{
	        		Integer currentID = new Integer(i);
	        		userMap.remove(currentID);
	        		clientConnections[i] = null;
	        		if(!connection.closedNaturally)
	        		{
	        			connection.leaveChatroom();
	        		}
	        		connection.close();
	        	}
			}
	    }
	}
	
	public synchronized void sendDealio(JsonObject dealio, JsonArray receiving)
	{
		//Only messages that go to other clients pass through here.
		updateConnections();
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
