/**
 *	@author Ariana Fairbanks
 */

package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import dealio.Dealio;
import dealio.DealioError;
import dealio.DealioUpdate;

public class Connection implements Runnable
{
	public boolean closed;
	public boolean closedNaturally;
	public InputStream parseDealio;
	public OutputStream writeDealio;
	private Socket client;
	private String userName;
	private ChatServer theServer;
	private int id;
	private boolean connected;

	public Connection(Socket client, ChatServer theServer)
	{
		this.theServer = theServer;
		this.client = client;
		closed = false;
		closedNaturally = false;
		connected = false;
		try
		{
			parseDealio = new BufferedInputStream(client.getInputStream());
			writeDealio = new BufferedOutputStream(client.getOutputStream());
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
		JsonObject chatroomResponseDealio;
		if (id == -1)
		{
			System.out.println("Server Full");
			chatroomResponseDealio = createDealio(Dealio.chatroom_response, "", null, null, null);
			sendToClient(chatroomResponseDealio);
			closed = true;
			// TODO different closed handling?
		}
		else
		{
			System.out.println("connected");
			new ClientUpdate().start();
		}
		System.out.println("finished");
	}

	private JsonObject createDealio(Dealio dealio, String message, JsonArray recipients, DealioError error, DealioUpdate update)
	{
		if(recipients == null)
		{
			recipients = Json.createArrayBuilder().build();
		}
		JsonObject newDealio;
		JsonObjectBuilder currentBuild = Json.createObjectBuilder().add("type", dealio.text);
		switch(dealio)
		{
			case chatroom_response:
				JsonArray userList = Json.createArrayBuilder(ChatServer.currentUsers).build();
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
				if(DealioUpdate.enter.text.equals(update.text))
				{
					theServer.userJoin(userName);
				}
				else
				{
					theServer.userLeave(userName);
				}
				break;
				
			default:
				System.out.println("Not a server dealio.");
				break;
		}
		newDealio = currentBuild.build();
		System.out.println("created " + newDealio.toString());
		return newDealio;
	}
	
	private void wait(int time)
	{
		int waitTime = time;
		while(waitTime > 0)
		{
			waitTime--;
		}
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
					if(!connected)
					{
						String username = currentDealio.getString("username").toLowerCase();
						if(username.length() > 20)
						{
							System.out.println("username too long");
						}
						userName = username + ":" + id;
						System.out.println(userName);
						ChatServer.userMap.put(id, userName);
						sendToClient(createDealio(Dealio.chatroom_response, "", null, null, null));
						wait(150);
						theServer.sendDealio(createDealio(Dealio.chatroom_update, "", null, null, DealioUpdate.enter), Json.createArrayBuilder().build());
						connected = true;
					}
					break;
					
				case chatroom_send:
					JsonArray receiving = currentDealio.getJsonArray("to");
					theServer.sendDealio(createDealio(Dealio.chatroom_broadcast, currentDealio.getString("message"), currentDealio.getJsonArray("to"), null, null), receiving);
					break;
					
				case chatroom_special:
					sendToClient(createDealio(Dealio.chatroom_error, "", null, DealioError.special_unsupported, null));
					break;
					
				case chatroom_end:
					leaveChatroom();
					closed = true;
					closedNaturally = true;
					break;
					
				default:
					System.out.println("Unexpected dealio type.");
					break;
					//(Dealio dealio, String message, JsonArray recipients, DealioError error, DealioUpdate update)
			}
		}
	}
	
	public void leaveChatroom()
	{
		theServer.sendDealio(createDealio(Dealio.chatroom_update, "", null, null, DealioUpdate.leave), Json.createArrayBuilder().build());
	}
	
	public synchronized void sendToClient(JsonObject currentDealio)
	{
		try
		{
			byte[] toWrite = currentDealio.toString().getBytes();
			writeDealio.write(toWrite, 0, toWrite.length);
			writeDealio.flush();
			System.out.println("sent " + currentDealio.toString());
		}
		catch(IOException e)
		{
			System.out.println("write error");
			closed = true;
		}
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
	
	class ClientUpdate extends Thread
	{
		public void run()
		{
			while(!closed)
			{
				try
				{
					@SuppressWarnings("unused")
					int numBytes;
					byte[] buffer = new byte[1024];
					String dealioString = "";
					while ((numBytes = parseDealio.read(buffer)) != -1) 
					{
						dealioString += new String(buffer).trim();
						//System.out.println(dealioString);
						break;
					}	
					if(dealioString.length() > 1)
					{
						JsonReader dealioParser = Json.createReader(new StringReader(dealioString));
						JsonObject currentDealio = dealioParser.readObject();
						dealioParser.close();
						if(currentDealio != null)
						{
							handleDealio(currentDealio);
						}
					}
				}
				catch(IOException e)
				{
					theServer.sendDealio(createDealio(Dealio.chatroom_update, "", null, null, DealioUpdate.leave),Json.createArrayBuilder().build());
					int waitTime = 150;
					while(waitTime > 0)
					{
						waitTime--;
					}
					closed = true;
					System.out.println("triggered close");
				}
			}
		}
	}
}
