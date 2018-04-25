/**
 *	@author Ariana Fairbanks
 */

package client;

public class ChatClient
{
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.err.println("Usage: java ChatClient <server>");
			System.exit(0);
			//TODO allow user to change?
		}
		else
		{
			ClientConnection chatClient = new ClientConnection(args[0]);
		}
	}
}
