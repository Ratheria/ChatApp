/**
 *	@author Ariana Fairbanks
 */

package shared;

public class ChatroomError
{
	public final String type = "chatroom-error";
	private String id;
	private DealioError type_of_error;
	
	public String getId()
	{	return id;	}
	public void setId(String id)
	{	this.id = id;	}
	public DealioError getType_of_error()
	{	return type_of_error;	}
	public void setType_of_error(DealioError type_of_error)
	{	this.type_of_error = type_of_error;	}
}
