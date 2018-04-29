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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
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
	private ClientConnection clientConnection;
	public boolean connected;
	private  String serverString;
	
	public ChatClient(String serverString)
	{
		super();
		panel = new ChatClientPanel(this);
		setName("WTDP Chat Client");
		this.setTitle("WTDP Chat Client");
		this.serverString = serverString;
		setContentPane(panel);
		setSize(850, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connected = false;
	}
	
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
			new ChatClient(args[0]);
		}
	}
	
	public void sendMessage(String message)
	{
		clientConnection.sendMessage(message);
	}
	
	public void updateDisplay(String toAppend)
	{
		panel.updateDisplayLog(toAppend);
	}
	
	public void connect()
	{
		try
		{
			sock = new Socket(serverString, PORT);
			clientConnection = new ClientConnection(sock, this);
		}
		catch (IOException e){e.printStackTrace();}
	}
	
	class ChatClientPanel extends JPanel 
	{
		private static final long serialVersionUID = -8341376012711000351L;
		private ChatClient frame;
		private JTextField inputField;
		private JTextArea displayLog;
		private DefaultCaret displayCaret;
		private SpringLayout springLayout;
		private JScrollPane scroll;
		private Color outline;
		private JScrollBar scrollBar;
		private JLabel usersLabel;
		private JLabel topLabel;
		private JButton sendButton;
		private JPanel userPanel;
		
		public ChatClientPanel(ChatClient frame)
		{
			this.frame = frame;
			setBorder(null);
			springLayout = new SpringLayout();
			displayLog = new JTextArea();
			displayCaret = (DefaultCaret)displayLog.getCaret();
			inputField = new JTextField();
			scroll = new JScrollPane(displayLog);
			outline = new Color(0, 255, 0);
			scrollBar = scroll.getVerticalScrollBar();
			usersLabel = new JLabel("");
			topLabel = new JLabel("WTDP Chat Client");
			sendButton = new JButton(" Send ");
			userPanel = new JPanel();
			
			setUpPanel();
			setUpLayout();
			setUpListeners();
		}

		private void setUpPanel() 
		{
			setLayout(springLayout);
			add(inputField);
			add(scroll);
			add(usersLabel);
			add(topLabel);
			add(sendButton);
			add(userPanel);
			
			displayLog.setText("\tWelcome. Enter a username with fewer than 20 characters.");
			inputField.requestFocusInWindow();
			displayCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			UIManager.put("ScrollBarUI", "view.ScrollBarUI");
			scrollBar.setUI(new NewScrollBarUI());
			inputField.requestFocusInWindow();
		}

		private void setUpLayout() 
		{
			setBackground(new Color(0, 0, 0));
			displayLog.setEditable(false);
			displayLog.setWrapStyleWord(true);
			displayLog.setTabSize(4);
			displayLog.setLineWrap(true);
			inputField.setColumns(20);
			inputField.setText("");
			sendButton.setFocusPainted(false);
			sendButton.setContentAreaFilled(false);
			userPanel.setOpaque(false);
			userPanel.setForeground(Color.GREEN);
			topLabel.setForeground(Color.GREEN);
			usersLabel.setForeground(Color.GREEN);
			displayLog.setForeground(Color.GREEN);
			displayLog.setBackground(Color.BLACK);
			sendButton.setBackground(Color.BLACK);
			sendButton.setForeground(Color.GREEN);
			inputField.setForeground(Color.GREEN);
			inputField.setBackground(Color.BLACK);
			topLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
			usersLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
			sendButton.setFont(new Font("Monospaced", Font.PLAIN, 12));
			displayLog.setFont(new Font("DialogInput", Font.PLAIN, 14));
			inputField.setFont(new Font("DialogInput", Font.PLAIN, 14));
			springLayout.putConstraint(SpringLayout.NORTH, userPanel, 10, SpringLayout.SOUTH, usersLabel);
			springLayout.putConstraint(SpringLayout.WEST, userPanel, 0, SpringLayout.EAST, scroll);
			springLayout.putConstraint(SpringLayout.SOUTH, userPanel, 195, SpringLayout.NORTH, scroll);
			springLayout.putConstraint(SpringLayout.EAST, userPanel, -25, SpringLayout.EAST, this);
			springLayout.putConstraint(SpringLayout.WEST, scroll, 25, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.EAST, scroll, -120, SpringLayout.EAST, this);
			springLayout.putConstraint(SpringLayout.NORTH, topLabel, 15, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.SOUTH, topLabel, -10, SpringLayout.NORTH, scroll);
			springLayout.putConstraint(SpringLayout.WEST, topLabel, 25, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.EAST, topLabel, -230, SpringLayout.EAST, this);
			springLayout.putConstraint(SpringLayout.SOUTH, inputField, -15, SpringLayout.SOUTH, this);
			springLayout.putConstraint(SpringLayout.EAST, inputField, -25, SpringLayout.EAST, this);
			springLayout.putConstraint(SpringLayout.WEST, inputField, 10, SpringLayout.EAST, sendButton);
			springLayout.putConstraint(SpringLayout.EAST, usersLabel, -25, SpringLayout.EAST, this);
			springLayout.putConstraint(SpringLayout.WEST, usersLabel, 135, SpringLayout.EAST, topLabel);
			springLayout.putConstraint(SpringLayout.SOUTH, usersLabel, -10, SpringLayout.NORTH, scroll);
			springLayout.putConstraint(SpringLayout.NORTH, scroll, 45, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.NORTH, inputField, 15, SpringLayout.SOUTH, scroll);
			springLayout.putConstraint(SpringLayout.SOUTH, scroll, -60, SpringLayout.SOUTH, this);
			springLayout.putConstraint(SpringLayout.NORTH, sendButton, 15, SpringLayout.SOUTH, scroll);
			springLayout.putConstraint(SpringLayout.SOUTH, sendButton, -15, SpringLayout.SOUTH, this);
			springLayout.putConstraint(SpringLayout.WEST, sendButton, 25, SpringLayout.WEST, this);
			displayLog.setBorder(new EmptyBorder(5, 15, 5, 15));
			scroll.setBorder(new LineBorder(new Color(0, 255, 0)));
			sendButton.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			inputField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(outline),
	                BorderFactory.createEmptyBorder(0, 55, 0, 0)));
			inputField.requestFocusInWindow();
		}
		
		private void setUpListeners() 
		{
			sendButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent click) 
				{
					//TODO send
					//TODO character limits
					if(!frame.connected)
					{
						frame.connect();
					}
					sendMessage(inputField.getText());
					inputField.setText("");
					inputField.requestFocusInWindow();
				}
			});
			
			inputField.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if(inputField.getText().length() > 0)
					{
						
					}
				}
			});
			
			frame.addWindowListener(new WindowAdapter()
			{
			    public void windowClosing(WindowEvent e)
			    {
			        //TODO
			    	if(frame.connected)
			    	{
				    	clientConnection.close();
			    	}
			    	else
			    	{
			    		System.exit(0);
			    	}
			    }
			});
		}
		
		public void updateDisplayLog(String toAppend)
		{
			String displayText = displayLog.getText();
			//System.out.println(displayText);
			displayLog.setText(displayText + "\n\n" + toAppend);
			//displayCaret = (DefaultCaret)displayLog.getCaret();
			//displayLog.setCaretPosition(displayLog.getDocument().getLength());
			//scroll.setViewportView(displayLog);
			//System.out.println(displayLog.getText());
		}
		
		public void updateUsersLabel()
		{
			//TODO
		}

	}
}

