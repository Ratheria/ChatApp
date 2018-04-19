/**
 *	@author Ariana Fairbanks
 */

package dealio;

public enum Dealio
{
	chatroom_begin, chatroom_send, 	chatroom_end,
	chatroom_special, // also “chatroom-send” (mistake?)
	
	chatroom_broadcast, // “chatroom-broadcast”
	chatroom_error, // “chatroom-error”
	chatroom_update, // “chatroom-update” 
	chatroom_response // “chat-response” (mistake?)
	;
	
	public static Dealio getType(String type)
	{
		Dealio result = null;
		switch(type)
		{
			case "chatroom-begin": result = chatroom_begin; break;
			case "chatroom-send": result = chatroom_send; break;
			//case "chatroom-special": result = chatroom_special; break;
			case "chatroom-end": result = chatroom_end; break;
			case "chatroom-broadcast": result = chatroom_broadcast; break;
			case "chatroom-error": result = chatroom_error; break;
			case "chatroom-update": result = chatroom_update; break;
			
			case "chat-response": result = chatroom_response; break;
		}
		return result;
	}
	
	public static String getTypeValue(Dealio type)
	{
		String result = null;
		switch(type)
		{
			case chatroom_begin: result = "chatroom-begin"; break;
			case chatroom_send: result = "chatroom-send"; break;
			case chatroom_special: result = "chatroom-special"; break;
			case chatroom_end: result = "chatroom-end"; break;
			case chatroom_broadcast: result = "chatroom-broadcast"; break;
			case chatroom_error: result = "chatroom-error"; break;
			case chatroom_update: result = "chatroom-update"; break;
			
			case chatroom_response: result = "chat-response"; break;
		}
		return result;
	}
}
