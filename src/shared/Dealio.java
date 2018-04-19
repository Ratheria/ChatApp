/**
 *	@author Ariana Fairbanks
 */

package shared;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Dealio
{

	private static ObjectMapper objectMap;
	
	public Dealio()
	{
		objectMap = new ObjectMapper();
	}
	
	public static Dealio determineDealioFromClient(String dealioContent)
	{
		Dealio result = null;
		try
		{
			if(dealioContent.contains("chatroom-begin"))
			{	result = objectMap.readValue(dealioContent, ChatroomBegin.class);	}
			else if(dealioContent.contains("chatroom-end"))
			{	result = objectMap.readValue(dealioContent, ChatroomEnd.class);	}
			else if(dealioContent.contains("chatroom-send"))
			{	result = objectMap.readValue(dealioContent, ChatroomSend.class);	}
		}
		catch (JsonParseException e)
		{	e.printStackTrace();	}
		catch (JsonMappingException e)
		{	e.printStackTrace();	}
		catch (IOException e)
		{	e.printStackTrace();	}
		return result;
	}
	
	public static Dealio determineDealioFromServer(String dealioContent)
	{
		Dealio result = null;
		

		return result;
	}
	
	public static String getDealioString()
	{
		return "";
	}
	
}
