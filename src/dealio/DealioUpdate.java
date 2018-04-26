/**
 *	@author Ariana Fairbanks
 */

package dealio;

public enum DealioUpdate
{
	enter("enter"), leave("leave");
	
	public String text;
	
	private DealioUpdate(String text)
	{
		this.text = text;
	}
}
