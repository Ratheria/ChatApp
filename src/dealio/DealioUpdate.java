/**
 *	@author Ariana Fairbanks
 */

package dealio;

public enum DealioUpdate
{
	enter("enter", " joined "), leave("leave", " left ");
	
	public String text;
	public String updateMessage;
	
	private DealioUpdate(String text, String updateMessage)
	{
		this.text = text;
		this.updateMessage = updateMessage;
	}
}
