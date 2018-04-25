/**
 *	@author Ariana Fairbanks
 */

package client;

import java.io.IOException;
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
import server.ChatServer;

public class ClientConnection
{
	private static final int PORT = 8029;
	private String server;
	private int id;
	private String username;
	private ArrayList<String> userList;
	private ChatClientFrame frame;

	public boolean connected = false;
	public Socket sock;
	public JsonReader parseDealio;
	public JsonWriter writeDealio;

	//{\"type\":\"chatroom-begin\",\"username\":\"temp\",\"len\":4}
	//{"type":"chatroom-begin","username":"temp","len":4}
	
	public ClientConnection(String server)
	{
		this.server = server;
		frame = new ChatClientFrame(this);
	}
	
	public boolean sendMessage(String content)
	{
		boolean result = true;
		if(connected)
		{
			//TODO
		}
		else
		{
			connected = connect();
			if(connected)
			{
				JsonObject beginDealio = createDealio(Dealio.chatroom_begin, content);
				System.out.println(beginDealio);
				writeDealio.writeObject(beginDealio);
				try
				{	parseDealio = Json.createReader(sock.getInputStream());	}
				catch (IOException e){}
				JsonObject currentDealio = parseDealio.readObject();
				System.out.println(currentDealio.toString());
				String type = currentDealio.getString("type");
				Dealio dealio = Dealio.getType(type);
			}
			else
			{
				result = false;
			}
		}
		return result;
	}
	
	private JsonObject createDealio(Dealio dealioType, String content)
	{
		JsonObjectBuilder currentBuild = Json.createObjectBuilder();
		switch(dealioType)
		{
			case chatroom_response:
				break;
			case chatroom_begin:
				currentBuild.add("type", "chatroom-begin")
				.add("username", content)
				.add("len", content.length());
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
	
	public void exit()
	{
		//TODO
	}
	
	public boolean connect()
	{
		try
		{
			sock = new Socket(server, PORT);
			connected = true;
			writeDealio = Json.createWriter(sock.getOutputStream());
		}
		catch (IOException ioe)
		{	System.err.println(ioe);	}
		return connected;
	}
	
	public void handleDealio(Dealio dealio)
	{
		
	}
	
}
