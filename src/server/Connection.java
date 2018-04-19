/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import shared.ChatroomBegin;
import shared.Dealio;

public class Connection implements Runnable
{
	public static final int BUFFER_SIZE = 2048;
	public InputStream fromClient;
	public OutputStream toClient;
	public boolean closed;
	private byte[] buffer;
	private Socket client;
	private int id;

	//TODO error handling
	
	public Connection(Socket client)
	{
		this.client = client;
		closed = false;
		buffer = new byte[BUFFER_SIZE];
		fromClient = null;
		toClient = null;
		try
		{
			fromClient = new DataInputStream(client.getInputStream());
			toClient = new DataOutputStream(client.getOutputStream());
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
		Dealio currentDealio;
		if (id == -1)
		{
			System.out.println("Server Full");
			// TODO server full
			closed = true;
			// TODO different closed handling?
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
			currentDealio = Dealio.determineDealioFromClient(dealioContent);
			if((currentDealio instanceof ChatroomBegin))
			{
				String username = ((ChatroomBegin) currentDealio).getUsername();
				//TODO too long
				username += ":" + id;
				ChatServer.userMap.put(id, username);
				System.out.println(username);
			}
			else
			{
				System.out.println("error");
			}
		}
		System.out.println("finished");
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
