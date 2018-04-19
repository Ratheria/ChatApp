/**
 *	@author Ariana Fairbanks
 */

package shared;

public class ChatroomSend extends Dealio
{
	public final String type = "chatroom-send";
	private String from;
	private String[] to;
	private String message;
	private int message_length;
	
	public String getFrom()				
	{	return from;	}
	public void setFrom(String from)		
	{	this.from = from;	}
	public String[] getTo()				
	{	return to;	}
	public void setTo(String[] to)			
	{	this.to = to;	}
	public String getMessage()			
	{	return message;	}
	public void setMessage(String message)	
	{	this.message = message;	}
	public int getMessage_length()		
	{	return message_length;	}
	public void setMessage_length(int message_length)
	{	this.message_length = message_length;	}

}
