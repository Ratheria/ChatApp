/**
 *	@author Ariana Fairbanks
 */

package shared;

public class ChatroomResponse extends Dealio
{
	public final String type = "chat-response";
	private int id;
	private String[] users;
	
	public int getId()
	{	return id;	}
	public void setId(int id)
	{	this.id = id;	}
	public String[] getUsers()
	{	return users;	}
	public void setUsers(String[] users)
	{	this.users = users;	}
}
