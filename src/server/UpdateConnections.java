/**
 *	@author Ariana Fairbanks
 */

package server;

public class UpdateConnections implements Runnable
{
	
	@Override
	public void run()
	{
		while(ChatServer.serverRunning)
		{
			ChatServer.updateConnections();
			try
			{	Thread.sleep(100);	}
			catch (InterruptedException e)
			{	e.printStackTrace();	}
			//System.out.println("ran");
		}
	}
	
}
