/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable
{
	public static final int BUFFER_SIZE = 1024;
	public boolean closed;
	private byte[] buffer;
	private Socket client;
	private InputStream fromClient;
	private OutputStream toClient;
	private int id;

	public Connection(Socket client)
	{	
		this.client = client;
		closed = false;
		buffer = new byte[BUFFER_SIZE];
		fromClient = null;
		toClient = null;
	}

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
		{	System.err.println(ioe);	}
	}
	
	public void close()
	{
		try
		{	client.close();	}
		catch (IOException e)
		{	e.printStackTrace();	}
	}
	
	public void setID(int id)
	{	this.id = id;	}
}
