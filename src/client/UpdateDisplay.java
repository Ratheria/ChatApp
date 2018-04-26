/**
 *	@author Ariana Fairbanks
 */

package client;

import javax.json.JsonObject;

import dealio.Dealio;

public class UpdateDisplay implements Runnable
{
	private ClientConnection base;
	
	public UpdateDisplay(ClientConnection base)
	{
		this.base = base;
	}
	
	@Override
	public void run()
	{
		while(base.connected)
		{
			try
			{	

				Thread.sleep(100);	
			}
			catch (InterruptedException e)
			{	e.printStackTrace();	}
			//System.out.println("ran");
		}
	}
	
}
