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
	
	//{"type":"chatroom-begin","username":"temp","len":4}
	
	//TODO notABigDealio dealioOrNoDealio
	
	//TODO update gui
	//TODO character limits and fine tuning
	
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
			beginDealio = createDealio(Dealio.chatroom_begin, content, null);
			System.out.println(beginDealio);
			writeDealio.writeObject(beginDealio);
			userName = "" + content;
			new ServerUpdate().start();
		}
	}
	
	private JsonObject createDealio(Dealio dealioType, String message, JsonArray recipients)
	{
		JsonObject newDealio;
		JsonObjectBuilder currentBuild = Json.createObjectBuilder();
		currentBuild.add("type", dealioType.text);
		switch(dealioType)
		{
			case chatroom_begin:
				currentBuild.add("username", message)
							.add("len", message.length());
				break;
				
			case chatroom_end:
				currentBuild.add("id", userName);		
				break;
				
			case chatroom_send:
				currentBuild.add("from", userName)
							.add("to", recipients)
							.add("message", message)
							.add("message-length", message.length());
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
		System.out.println(currentDealio.toString());
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

	
	public void close()
	{
		//TODO
		//send chatroom-end dealio
		//close thread then stop
	}
	
	class ServerUpdate extends Thread
	{
		public void run()
		{
			while(!finished)
			{
				try
				{
					parseDealio = Json.createReader(new InputStreamReader(server.getInputStream()));
					JsonObject currentDealio = parseDealio.readObject();
					handleDealio(currentDealio);
				}
				catch(IOException e)
				{
					System.out.println("error");
				}
			}
		}
	}
	
}
