/**
 *	@author Ariana Fairbanks
 */

package server;

public class UpdateConnections implements Runnable
{
	
	@Override
	public void run()
	{
		Connection connection;
		while(ChatServer.serverRunning)
		{
			for (int i = 0; i < ChatServer.MAX_CONNECTIONS; i++) 
		    {
				connection = ChatServer.clientConnections[i];
				if(connection != null)
				{
		        	if(connection.closed)
		        	{
		        		connection.close();
		        		Integer currentID = new Integer(i);
		        		ChatServer.userMap.remove(currentID);
		        		ChatServer.clientConnections[i] = null;
		        	}
				}
		    }
			try
			{	Thread.sleep(2000);	}
			catch (InterruptedException e)
			{	e.printStackTrace();	}
			//System.out.println("ran");
		}
	}
	
}
