/**
 *	@author Ariana Fairbanks
 */

package client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatClient
{
	private static final int PORT = 8029;
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private static Socket sock = null;
	
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
			try
			{
				sock = new Socket(args[0], PORT);
				ClientConnection chatClient = new ClientConnection(sock);
				EXECUTOR.execute(chatClient);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
