/**
 *	@author Ariana Fairbanks
 */

package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import dealio.Dealio;
import server.ChatServer;

public class ClientConnection implements Runnable
{
	private Socket server;
	private int id;
	private String username;
	private ArrayList<String> userList;
	private ChatClientFrame frame;

	public boolean connected = false;
	public JsonReader parseDealio;
	public JsonWriter writeDealio;

	//{\"type\":\"chatroom-begin\",\"username\":\"temp\",\"len\":4}
	//{"type":"chatroom-begin","username":"temp","len":4}
	
	//TODO notABigDealio dealioOrNoDealio
	
	public ClientConnection(Socket server)
	{
		this.server = server;
		try
		{
			writeDealio = Json.createWriter(server.getOutputStream());
			parseDealio = Json.createReader(server.getInputStream());
		}
		catch (IOException e)
		{	System.err.println(e);	}
	}
	
	@Override
	public void run()
	{
		frame = new ChatClientFrame(this);
		
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
	
	public void exit()
	{
		//TODO
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
	
	private void windowCloseListener()
	{
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        //TODO
		    }
		});
	}
	
}
