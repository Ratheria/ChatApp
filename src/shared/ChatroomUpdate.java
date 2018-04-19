/**
 *	@author Ariana Fairbanks
 */

package shared;

public class ChatroomUpdate
{
	public final String type = "chatroom_update";
	private String type_of_update;
	private String id;
	
	public String getType_of_update()
	{	return type_of_update;	}
	public void setType_of_update(String type_of_update)
	{	this.type_of_update = type_of_update;	}
	public String getId()
	{	return id;	}
	public void setId(String id)
	{	this.id = id;	}
}
