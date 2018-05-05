/**
 *	@author Ariana Fairbanks
 */

package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

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

