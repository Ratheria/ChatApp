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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import dealio.Dealio;

public class Connection implements Runnable
{
	public boolean closed;
	private JsonReader parseDealio;
	private JsonWriter writeDealio;
	private Socket client;
	private int id;

	//TODO error handling
	
	public Connection(Socket client)
	{
		this.client = client;
		closed = false;
		try
		{
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
			chatroomResponseDealio = createDealio(Dealio.chatroom_response);
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
			System.out.println(chatroomBeginDealio.toString().equals("{\"type\":\"chatroom-begin\",\"username\":\"temp\",\"len\":4}"));
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
					username = username + ":" + id;
					System.out.println(username);
					ChatServer.userMap.put(id, username);
					chatroomResponseDealio = createDealio(Dealio.chatroom_response);
					System.out.println(chatroomResponseDealio.toString());
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

	private JsonObject createDealio(Dealio dealioType)
	{
		JsonObjectBuilder currentBuild = Json.createObjectBuilder();
		switch(dealioType)
		{
			case chatroom_response:
				Object[] currentUserArray = ChatServer.userMap.values().toArray();
				JsonArray userList = Json.createArrayBuilder(Arrays.asList(currentUserArray)).build();
				currentBuild.add("type", "chatroom-response")
							.add("id", id)
							.add("clientNo", ChatServer.userMap.size())
							.add("users", userList);
				break;
			case chatroom_begin:
				break;
			case chatroom_broadcast:
				break;
			case chatroom_end:
				break;
			case chatroom_error:
				break;
			case chatroom_send:
				break;
			case chatroom_special:
				break;
			case chatroom_update:
				break;
			default:
				break;
		}

		
		return currentBuild.build();
	}
	
	public void close()
	{
		try
		{
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
