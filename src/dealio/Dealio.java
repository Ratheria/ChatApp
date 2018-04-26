/**
 *	@author Ariana Fairbanks
 */

package dealio;

public enum Dealio
{
	chatroom_begin		(false,	"chatroom-begin"),		chatroom_send	(false,	"chatroom-send"), 
	chatroom_special	(false,	"chatroom-special"),	chatroom_end	(false,	"chatroom-end"),
	chatroom_response	(true, 	"chat-response"),		chatroom_update	(true,	"chatroom-update"), 
	chatroom_broadcast	(true,	"chatroom-broadcast"),	chatroom_error	(true,	"chatroom-error");
	
	public boolean forServerUse;
	public String text;
	
	private Dealio(boolean forServerUse, String text)
	{
		this.forServerUse = forServerUse;
		this.text = text;
	}
	
	public static Dealio getType(String type)
	{
		Dealio result = null;
		switch(type)
		{
			case "chatroom-begin"		:	result = chatroom_begin;		break;
			case "chatroom-send"		:	result = chatroom_send;			break;
			case "chatroom-special"		:	result = chatroom_special;		break;
			case "chatroom-end"			:	result = chatroom_end;			break;
			case "chat-response"		:	result = chatroom_response;		break;
			case "chatroom-update"		:	result = chatroom_update;		break;
			case "chatroom-broadcast"	:	result = chatroom_broadcast;	break;
			case "chatroom-error"		:	result = chatroom_error;		break;
		}
		return result;
	}
}
