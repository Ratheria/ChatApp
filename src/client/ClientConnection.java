/**
 *	@author Ariana Fairbanks
 */

package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import dealio.Dealio;

public class ClientConnection
{
	private Socket server;
	private int id;
	private String userName;
	private ArrayList<String> userList;
	private ChatClient gui;
	private JsonReader parseDealio;
	private JsonWriter writeDealio;
	private boolean connected = false;
	private boolean finished = false;

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
		if(connected)
		{
			//TODO
		}
		else
		{
			beginDealio = createDealio(Dealio.chatroom_begin, content);
			System.out.println(beginDealio);
			writeDealio.writeObject(beginDealio);
			userName = "" + content;
			new ServerUpdate().start();
		}
	}
	
	private JsonObject createDealio(Dealio dealioType, String content)
	{
		JsonObject newDealio;
		JsonObjectBuilder currentBuild = Json.createObjectBuilder();
		currentBuild.add("type", dealioType.text);
		switch(dealioType)
		{
			case chatroom_begin:
				currentBuild.add("username", content)
				.add("len", content.length());
				break;
				
			case chatroom_end:
				break;
				
			case chatroom_send:
				break;
				
			case chatroom_special:
				break;

			default:
				System.out.println("Not a client dealio.");
				break;
		}
		newDealio = currentBuild.build();
		System.out.println("created " + newDealio.toString());
		return newDealio;
	}
	
	public synchronized void handleDealio(JsonObject currentDealio)
	{
		System.out.println("f " + currentDealio.toString());
		String type = currentDealio.getString("type");
		Dealio dealio = Dealio.getType(type);
		if(dealio != null)
		{
			switch(dealio)
			{
				case chatroom_response:
					if(connected)
					{
						System.out.println("Unexpected dealio type.");
					}
					else
					{
						id = currentDealio.getInt("id");
						userName = userName + ":" + id;
						JsonArray userJsonArray = currentDealio.getJsonArray("users");
						userList = new ArrayList<String>();
						for(JsonValue currentUserValue : userJsonArray) 
						{
						    userList.add( currentUserValue.toString() );
						}
						connected = true;
					}
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

	
	
	
	
	
	
	
	
	class ServerUpdate extends Thread
	{
		public void run()
		{
			while(!finished)
			{
				JsonObject currentDealio = parseDealio.readObject();
				System.out.println("c");
				handleDealio(currentDealio);
			}
		}
	}
	
}
