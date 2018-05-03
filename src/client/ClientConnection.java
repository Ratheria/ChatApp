/**
 *	@author Ariana Fairbanks
 */

package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import dealio.Dealio;
import dealio.DealioUpdate;

public class ClientConnection
{
	@SuppressWarnings("unused")
	private Socket server;
	private int id;
	private String userName;
	private static ArrayList<String> userList;
	private ChatClient gui;
	private ServerUpdate updateThread;
	private InputStream parseDealio;
	private OutputStream writeDealio;
	private boolean finished = false;
	private JsonObject last;

	//TODO fine tuning
	//TODO managing threads
	
	public ClientConnection(Socket server, ChatClient gui)
	{
		this.server = server;
		this.gui = gui;
		last = null;
		try
		{
			parseDealio = new BufferedInputStream(server.getInputStream());
			writeDealio = new BufferedOutputStream(server.getOutputStream());
		}
		catch (IOException e)
		{	System.err.println(e);	}
		//updateThread = new ServerUpdate();
		//updateThread.start();
		new ServerUpdate().start();
	}
	
	public synchronized void sendMessage(String content, String recipients)
	{
		if(gui.connected)
		{
			//System.out.println("not doing this yet");
			JsonArrayBuilder listBuilder = Json.createArrayBuilder();
			if(recipients.length() > 2)
			{
				if(recipients.contains(","))
				{
					String[] recipientArray = recipients.split(",");
					for(String recipientValue : recipientArray)
					{
						if(recipientValue.contains(":") && recipientValue.length() > 2)
						{
							listBuilder.add(recipientValue);
						}
					}
				}
				else
				{
					listBuilder.add(recipients);
				}
				listBuilder.add(userName);
			}
			sendToServer(createDealio(Dealio.chatroom_send, content, listBuilder.build()));
		}
		else
		{
			sendToServer(createDealio(Dealio.chatroom_begin, content, null));
			userName = "" + content;
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
				//TODO not supported yet
				break;

			default:
				System.out.println("Not a client dealio.");
				break;
		}
		newDealio = currentBuild.build();
		System.out.println("created " + newDealio.toString());
		return newDealio;
	}
	
	public synchronized void sendToServer(JsonObject currentDealio)
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
				case chatroom_response:
					if(gui.connected)
					{
						System.out.println("Unexpected dealio type.");
					}
					else
					{
						id = currentDealio.getInt("id");
						if(id == -1)
						{
							gui.updateDisplay("Server is full.");
						}
						else
						{
							userName = userName + ":" + id;
							JsonArray userJsonArray = currentDealio.getJsonArray("users");
							userList = new ArrayList<String>();
							for(JsonValue currentUserValue : userJsonArray) 
							{
								String CUV = currentUserValue.toString();
							    userList.add( CUV.substring(1, CUV.length() - 1) );
							}
							gui.connected = true;
						}
					}
					break;
					
				case chatroom_update:
					String updatedUser = currentDealio.getString("id");
					String updateType = currentDealio.getString("type_of_update");
					DealioUpdate update = DealioUpdate.enter;
					if(!update.text.equals(updateType))
					{
						update = DealioUpdate.leave;
					}
					String updatedText = " - - - User " + updatedUser + " just" + update.updateMessage + "the chatroom.";
					gui.updateDisplay(updatedText);
					//System.out.println(updatedUser);
					//System.out.println(userList.contains(updatedUser));
					if(!updatedUser.equals(userName))
					{
						if(update == DealioUpdate.enter)
						{
							userList.add(updatedUser);
						}
						else
						{
							userList.remove(updatedUser);
						}
					}
					//System.out.println(userList.size());
					if(userList.size() < 2)
					{
						gui.updateDisplay(" - - - You are alone on this server.");
					}
					gui.updateUsersLabel(userList);
					break;
					
				case chatroom_broadcast:
					String from = currentDealio.getString("from");
					JsonArray to = currentDealio.getJsonArray("to");
					String sentTo = "everyone";
					boolean fromYou = from.equals(userName);
					if(!to.isEmpty())
					{
						sentTo = fromYou ? "" : " you,";
						
						for(JsonValue user : to)
						{
							String userString = user.toString();
							userString = userString.substring(1, userString.length() - 1);
							if(userString.equals(userName) || userString.equals(from)){	}
							else
							{
								sentTo = sentTo + " " + userString + ",";
							}
						}
						sentTo = sentTo.substring(0, sentTo.length() - 1);
					}
					if(fromYou)
					{
						from = "You";
					}
					from += " said to";
					String message = currentDealio.getString("message");
					String updateDisplayString = from + sentTo + "\n\t" + message;
					gui.updateDisplay(updateDisplayString);
					break;
					
				case chatroom_error:
					System.out.println(currentDealio.getString("type_of_error"));
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
		sendToServer(createDealio(Dealio.chatroom_end, "", null));
		//updateThread.interrupt();
		finished = true;
		System.exit(0);
	}
	
	class ServerUpdate extends Thread
	{
		public void run()
		{
			while(!finished)
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
						if(!(currentDealio.equals(last)))
						{
							last = currentDealio;
							handleDealio(currentDealio);
						}

					}
				}
				catch(IOException e)
				{
					gui.updateDisplay(" - - - Read Error: The Server Probably Went Offline\n - - - Connection Ended");
					System.out.println("read error");
					finished = true;
				}
			}
		}
	}
	
}
