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
				JsonObject currentDealio = base.parseDealio.readObject();
				System.out.println("  " + currentDealio.toString());
				String type = currentDealio.getString("type");
				Dealio dealio = Dealio.getType(type);
				base.handleDealio(dealio);
				Thread.sleep(100);	
			}
			catch (InterruptedException e)
			{	e.printStackTrace();	}
			//System.out.println("ran");
		}
	}
	
}
