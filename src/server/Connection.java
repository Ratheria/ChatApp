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
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import dealio.Dealio;

public class Connection implements Runnable
{
	public boolean closed;
	public OutputStream toClient;
	private InputStream fromClient;
	private JsonReader parseDealio;
	private JsonWriter writeDealio;
	private Socket client;
	private int id;

	//TODO error handling
	
	public Connection(Socket client)
	{
		this.client = client;
		closed = false;
		fromClient = null;
		toClient = null;
		try
		{
			//fromClient = new DataInputStream(client.getInputStream());
			//toClient = new DataOutputStream(client.getOutputStream());
			parseDealio = Json.createReader(client.getInputStream());
			writeDealio = Json.createWriter(client.getOutputStream());
			client.setKeepAlive(true);
		}
		catch (IOException e)
		{}
	}

	@Override
	public void run()
	{
		try
		{	startConnection();	}
		catch (IOException e)
		{	e.printStackTrace();	}
	}
	
	private void startConnection() throws IOException
	{
		JsonObject chatroomBeginDealio;
		JsonObject chatroomResponseDealio;
		if (id == -1)
		{
			System.out.println("Server Full");
			chatroomResponseDealio = createChatroomResponse();
			System.out.print(chatroomResponseDealio.toString());
			writeDealio.writeObject(chatroomResponseDealio);
			closed = true;
			// TODO different closed handling?
		}
		else
		{
			System.out.println("connected");
			chatroomBeginDealio = parseDealio.readObject();
			System.out.println(chatroomBeginDealio.toString());
			String type = chatroomBeginDealio.getString("type");
			Dealio dealio = Dealio.getType(type);
			if(dealio != null)
			{
				if((dealio == Dealio.chatroom_begin))
				{
					String username = chatroomBeginDealio.getString("username").toLowerCase();
					if(username.length() > 20)
					{
						System.out.println("username too long");
					}
					username += ":" + id;
					ChatServer.userMap.put(id, username);
					System.out.println(username);
					chatroomResponseDealio = createChatroomResponse();
					writeDealio.writeObject(chatroomResponseDealio);
				}
				else
				{
					System.out.println("unexpected dealio type");
				}
			}
			else
			{
				System.out.println("malformed dealio");
			}
		}
		System.out.println("finished");
	}

	private JsonObject createChatroomResponse()
	{
		 return Json.createObjectBuilder().add("id", id)
		.add("clientNo", ChatServer.userMap.size())
		.add("users", ChatServer.userMap.values().toArray().toString())
		.build();
	}
	
	public void close()
	{
		try
		{
			if (fromClient != null)
			{	fromClient.close();	}
			if (toClient != null)
			{	toClient.close();	}
			parseDealio.close();
			writeDealio.close();
			client.close();
		}
		catch (IOException e)
		{	e.printStackTrace();	}
	}

	public void setID(int id)
	{	this.id = id;	}
}
