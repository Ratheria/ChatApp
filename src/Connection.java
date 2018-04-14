/**
 *	@author Ariana Fairbanks
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable
{
	private Socket client;
	public static final int BUFFER_SIZE = 256;

	public Connection(Socket client)
	{	this.client = client;	}

	public void run()
	{
		try
		{
			byte[] buffer = new byte[BUFFER_SIZE];
			InputStream fromClient = null;
			OutputStream toClient = null;

			try
			{
				fromClient = new BufferedInputStream(client.getInputStream());
				toClient = new BufferedOutputStream(client.getOutputStream());
				int numBytes;
				System.out.println("connected");
				while ((numBytes = fromClient.read(buffer)) != -1)
				{
					System.out.println("connected");
					toClient.write(buffer, 0, numBytes);
					toClient.flush();
				}
			}
			catch (IOException ioe)
			{
				System.err.println(ioe);
			}
			finally
			{
				// close streams and socket
				if (fromClient != null) fromClient.close();
				if (toClient != null) toClient.close();
			}
		}
		catch (java.io.IOException ioe)
		{
			System.err.println(ioe);
		}
	}
}
