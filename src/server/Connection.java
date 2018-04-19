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
	public static final int BUFFER_SIZE = 2048;
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
		try
		{
			fromClient = new BufferedInputStream(client.getInputStream());
			toClient = new BufferedOutputStream(client.getOutputStream());
			client.setKeepAlive(true);
		}
		catch (IOException e)
		{}
	}

	public void run()
	{
		try
		{	startConnection();	}
		catch (IOException e)
		{	e.printStackTrace();	}
	}
	
	private void startConnection() throws IOException
	{
		if (id == -1)
		{
			System.out.println("Server Full");
			closed = true;
			// TODO different closed handling?
			// TODO server full
		}
		else
		{
			int numBytes;
			System.out.println("connected");
			String dealioContent = "";
			boolean fullResponse = false;
			while (!fullResponse) 
			{
				numBytes = fromClient.read(buffer);
				String newContent = new String(buffer).trim();
				dealioContent += newContent;
				System.out.println(newContent);
				System.out.println(numBytes); 
				if(dealioContent.endsWith("}"))
				{	fullResponse = true;	}
				//toClient.write(buffer, 0, numBytes); 
				//toClient.flush(); 
				buffer = new byte[BUFFER_SIZE]; 
			}
			handleDealio(dealioContent);
		}
		System.out.println("finished");
	}

	private void handleDealio(String dealioContent)
	{

	}

	public void close()
	{
		try
		{
			if (fromClient != null)
			{	fromClient.close();	}
			if (toClient != null)
			{	toClient.close();	}
			client.close();
		}
		catch (IOException e)
		{	e.printStackTrace();	}
	}

	public void setID(int id)
	{	this.id = id;	}
}
