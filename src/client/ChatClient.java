/**
 *	@author Ariana Fairbanks
 */

package client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JFrame;

public class ChatClient extends JFrame 
{
	private static final long serialVersionUID = -5136294238715941655L;
	private static final int PORT = 8029;
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private static Socket sock = null;
	private ChatClientPanel panel;
	public ClientConnection clientConnection;
	public boolean connected;
	
	public ChatClient()
	{
		super();
		panel = new ChatClientPanel(this);
		setName("WTDP Chat Client");
		this.setTitle("WTDP Chat Client");
		setContentPane(panel);
		setSize(850, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connected = false;
	}
	
	public static void main(String[] args)
	{
		new ChatClient();
	}
	
	public void sendMessage(String message, String recipients)
	{
		clientConnection.sendMessage(message, recipients);
	}
	
	public void updateDisplay(String toAppend)
	{
		panel.updateDisplayLog(toAppend);
	}
	
	public void updateUsersLabel(ArrayList<String> currentUsers)
	{
		panel.updateUsersLabel(currentUsers);
	}
	
	public void connect(String serverString)
	{
		try
		{
			sock = new Socket(serverString, PORT);
			clientConnection = new ClientConnection(sock, this);
		}
		catch (IOException e){e.printStackTrace();}
	}
	
}

