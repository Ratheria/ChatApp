/**
 *	@author Ariana Fairbanks
 */

package shared;

public class ChatroomBegin extends Dealio
{
	public final String type = "chatroom-begin";
	private String username;
	private int len;
	
	public String getUsername()
	{	return username;	}
	public void setUsername(String username)
	{	this.username = username.toLowerCase();	}
	public int getLen()
	{	return len;	}
	public void setLen(int len)
	{	this.len = len;	}
}
