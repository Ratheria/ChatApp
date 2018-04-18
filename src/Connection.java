/**
 *	@author Ariana Fairbanks
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable
{
	public static final int BUFFER_SIZE = 1024;
	public boolean closed = false;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private Socket client;
	private InputStream fromClient = null;
	private OutputStream toClient = null;

	public Connection(Socket client)
	{	this.client = client;	}

	public void run()
	{
		//TODO actual connection and rejected connection
	
		try
		{
			fromClient = new BufferedInputStream(client.getInputStream());
			toClient = new BufferedOutputStream(client.getOutputStream());
			int numBytes;
			System.out.println("connected");
			while ((numBytes = fromClient.read(buffer)) != -1)
			{
				System.out.println("connected");
				toClient.write(buffer, 0, numBytes);
				toClient.flush();
			}
			
			
			if (fromClient != null)
			{	fromClient.close();	}
			if (toClient != null) 
			{	toClient.close();	}
		}
		catch (IOException ioe)
		{
			System.err.println(ioe);
		}
	}
	
	public void close()
	{
		try
		{	client.close();	}
		catch (IOException e)
		{	e.printStackTrace();	}
	}
}
