/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import dealio.Dealio;
import dealio.DealioError;
import dealio.DealioUpdate;

public class Connection implements Runnable
{
	public boolean closed;
	public JsonReader parseDealio;
	public JsonWriter writeDealio;
	private Socket client;
	private String userName;
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
			//client.setKeepAlive(true);
		}
		catch (IOException e){}
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
			chatroomResponseDealio = createDealio(Dealio.chatroom_response, "", null, null, null);
			writeDealio.writeObject(chatroomResponseDealio);
			closed = true;
			// TODO different closed handling?
		}
		else
		{
			System.out.println("connected");
			chatroomBeginDealio = parseDealio.readObject();
			System.out.println(chatroomBeginDealio.toString());
			//System.out.println(chatroomBeginDealio.toString().equals("{\"type\":\"chatroom-begin\",\"username\":\"temp\",\"len\":4}"));
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
					userName = username + ":" + id;
					System.out.println(userName);
					ChatServer.userMap.put(id, userName);
					chatroomResponseDealio = createDealio(Dealio.chatroom_response, "", null, null, null);
					writeDealio.writeObject(chatroomResponseDealio);
					//TODO send dealio to acknowledge new user
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

	private JsonObject createDealio(Dealio dealio, String message, JsonArray recipients, DealioError error, DealioUpdate update)
	{
		//Json.createArrayBuilder().build()
		
		JsonObject newDealio;
		JsonObjectBuilder currentBuild = Json.createObjectBuilder().add("type", dealio.text);
		switch(dealio)
		{
			case chatroom_response:
				Object[] currentUserArray = ChatServer.userMap.values().toArray();
				JsonArray userList = Json.createArrayBuilder(Arrays.asList(currentUserArray)).build();
				currentBuild.add("id", id)
							.add("clientNo", ChatServer.userMap.size())
							.add("users", userList);
				break;
				
			case chatroom_broadcast:
				currentBuild.add("from", userName)
							.add("to", recipients)
							.add("message", message)
							.add("message_length", message.length());
				break;
				
			case chatroom_error:
				currentBuild.add("type_of_error", error.text)
							.add("id", userName);
				break;
				
			case chatroom_update:
				currentBuild.add("type_of_update", update.text)
							.add("id", userName);
				break;
				
			default:
				System.out.println("Not a server dealio.");
				break;
		}
		newDealio = currentBuild.build();
		System.out.println("created " + newDealio.toString());
		return newDealio;
	}
	
	public synchronized void handleDealio(JsonObject currentDealio)
	{
		System.out.println(currentDealio.toString());
		String type = currentDealio.getString("type");
		Dealio dealio = Dealio.getType(type);
		if(dealio != null)
		{
			switch(dealio)
			{
				case chatroom_begin:
					
					break;
				case chatroom_update:
					break;
				case chatroom_broadcast:
					break;
				case chatroom_error:
					break;
				default:
					System.out.println("Unexpected dealio type.");
					break;
			}
		}
	}
	
	/*
	chatroom_begin		(false,	"chatroom-begin"),		chatroom_send	(false,	"chatroom-send"), 
	chatroom_special	(false,	"chatroom-special"),	chatroom_end	(false,	"chatroom-end"),
	*/
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
	
	class ClientUpdate extends Thread
	{
		public void run()
		{
			while(!closed)
			{
				try
				{
					parseDealio = Json.createReader(new InputStreamReader(client.getInputStream()));
					JsonObject currentDealio = parseDealio.readObject();
					handleDealio(currentDealio);
				}
				catch(IOException e)
				{
					closed = true;
					System.out.println("triggered close");
				}
			}
		}
	}
}
