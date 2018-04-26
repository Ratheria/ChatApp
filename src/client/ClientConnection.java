/**
 *	@author Ariana Fairbanks
 */

package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import dealio.Dealio;

public class ClientConnection
{
	private Socket server;
	private int id;
	private String username;
	private ArrayList<String> userList;
	private ChatClient gui;
	private JsonReader parseDealio;
	private JsonWriter writeDealio;
	private boolean connected = false;

	//{\"type\":\"chatroom-begin\",\"username\":\"temp\",\"len\":4}
	//{"type":"chatroom-begin","username":"temp","len":4}
	
	//TODO notABigDealio dealioOrNoDealio
	
	public ClientConnection(Socket server, ChatClient gui)
	{
		this.server = server;
		this.gui = gui;
		try
		{
			writeDealio = Json.createWriter(server.getOutputStream());
			parseDealio = Json.createReader(new InputStreamReader(server.getInputStream()));
		}
		catch (IOException e)
		{	System.err.println(e);	}
	}
	
	public void sendMessage(String content)
	{
		JsonObject beginDealio;
		JsonObject currentDealio;
		if(connected)
		{
			//TODO
		}
		else
		{
			beginDealio = createDealio(Dealio.chatroom_begin, content);
			System.out.println(beginDealio);
			writeDealio.writeObject(beginDealio);
			
			connected = true;
			
			new ServerUpdate();
			//TODO
			
			currentDealio = parseDealio.readObject();
			System.out.println(currentDealio.toString());
			String type = currentDealio.getString("type");
			Dealio dealio = Dealio.getType(type);
			if(dealio != null)
			{
				if((dealio == Dealio.chatroom_response))
				{
					//TODO
					id = currentDealio.getInt("id");
					System.out.println(currentDealio.getJsonArray("users").toString());
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
	}
	
	private JsonObject createDealio(Dealio dealioType, String content)
	{
		JsonObjectBuilder currentBuild = Json.createObjectBuilder();
		switch(dealioType)
		{
			case chatroom_begin:
				currentBuild.add("type", "chatroom-begin")
				.add("username", content)
				.add("len", content.length());
				break;
			case chatroom_end:
				break;
			case chatroom_send:
				break;
			case chatroom_special:
				break;
			default:
				break;
		}
		return currentBuild.build();
	}
	
	//TODO ???
	public synchronized void handleDealio(JsonObject currentDealio)
	{
		System.out.println(currentDealio.toString());
		String type = currentDealio.getString("type");
		Dealio dealio = Dealio.getType(type);
		if(dealio != null)
		{
			switch(dealio)
			{
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

	class ServerUpdate extends Thread
	{
		public void run()
		{
			while(connected)
			{
				
			}
		}
	}
	
}
