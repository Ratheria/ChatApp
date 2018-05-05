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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.PlainDocument;

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
	//private JPanel userPanel;
	private JTextField textField;
	private JLabel lblToUser;
	
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
		//userPanel = new JPanel();
		
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
		//add(userPanel);
		
		displayLog.setText("\tWelcome. Enter a username with fewer than 20 characters.");
		inputField.requestFocusInWindow();
		inputField.setDocument(new JTextFieldLimit(20));
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
		topLabel.setForeground(Color.GREEN);
		usersLabel.setForeground(Color.GREEN);
		displayLog.setForeground(Color.GREEN);
		displayLog.setBackground(Color.BLACK);
		sendButton.setBackground(Color.BLACK);
		sendButton.setForeground(Color.GREEN);
		inputField.setForeground(Color.GREEN);
		inputField.setBackground(Color.BLACK);
		topLabel.setFont(new Font("Monospaced", Font.PLAIN, 22));
		usersLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
		sendButton.setFont(new Font("Monospaced", Font.PLAIN, 14));
		displayLog.setFont(new Font("DialogInput", Font.PLAIN, 14));
		inputField.setFont(new Font("DialogInput", Font.PLAIN, 16));
		springLayout.putConstraint(SpringLayout.NORTH, topLabel, 15, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, topLabel, -10, SpringLayout.NORTH, scroll);
		springLayout.putConstraint(SpringLayout.EAST, inputField, -25, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.WEST, inputField, 10, SpringLayout.EAST, sendButton);
		springLayout.putConstraint(SpringLayout.NORTH, scroll, 45, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scroll, -80, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, scroll, -25, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.NORTH, usersLabel, 0, SpringLayout.SOUTH, scroll);
		springLayout.putConstraint(SpringLayout.WEST, usersLabel, 0, SpringLayout.WEST, scroll);
		springLayout.putConstraint(SpringLayout.EAST, usersLabel, 0, SpringLayout.EAST, inputField);
		springLayout.putConstraint(SpringLayout.NORTH, sendButton, 25, SpringLayout.NORTH, usersLabel);
		springLayout.putConstraint(SpringLayout.NORTH, inputField, 0, SpringLayout.NORTH, sendButton);
		springLayout.putConstraint(SpringLayout.WEST, scroll, 25, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, topLabel, 25, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, topLabel, -152, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.WEST, sendButton, 25, SpringLayout.WEST, this);
		displayLog.setBorder(new EmptyBorder(5, 15, 5, 15));
		scroll.setBorder(new LineBorder(new Color(0, 255, 0)));
		sendButton.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		inputField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(outline),
                BorderFactory.createEmptyBorder(0, 55, 0, 0)));
		inputField.requestFocusInWindow();
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 2, SpringLayout.SOUTH, inputField);
		springLayout.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, inputField);
		textField.setText("127.0.0.1");
		textField.setForeground(Color.GREEN);
		textField.setFont(new Font("DialogInput", Font.PLAIN, 16));
		textField.setColumns(20);
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(outline),

		                BorderFactory.createEmptyBorder(0, 55, 0, 0)));
		textField.setBackground(Color.BLACK);
		add(textField);
		
		lblToUser = new JLabel("Server IP ");
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.EAST, lblToUser);
		springLayout.putConstraint(SpringLayout.WEST, lblToUser, 0, SpringLayout.WEST, inputField);
		springLayout.putConstraint(SpringLayout.NORTH, lblToUser, 6, SpringLayout.SOUTH, inputField);
		springLayout.putConstraint(SpringLayout.SOUTH, lblToUser, -10, SpringLayout.SOUTH, this);
		lblToUser.setVerticalAlignment(SwingConstants.TOP);
		lblToUser.setVerticalTextPosition(SwingConstants.TOP);
		lblToUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblToUser.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblToUser.setForeground(Color.GREEN);
		lblToUser.setFont(new Font("Monospaced", Font.PLAIN, 12));
		add(lblToUser);
	}
	
	private void setUpListeners() 
	{
		sendButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent click) 
			{
				String input = inputField.getText();
				String server = textField.getText().trim();
				if(input.length() > 0 && server.length() > 0)
				{
					//TODO send
					if(!frame.connected)
					{
						frame.connect(server);
					}
					frame.sendMessage(inputField.getText(), textField.getText());
					inputField.setText("");
					textField.setText("");
				}
				inputField.requestFocusInWindow();
			}
		});
		
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	if(frame.connected)
		    	{
			    	frame.clientConnection.close();
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
		displayCaret = (DefaultCaret)displayLog.getCaret();
		displayLog.setCaretPosition(displayLog.getDocument().getLength());
		scroll.setViewportView(displayLog);
		//System.out.println(displayLog.getText());
	}
	
	public void updateUsersLabel(ArrayList<String> currentUsers)
	{
		String newText = "Current Users -";
		for(String user : currentUsers)
		{
			newText = newText + " " + user + ",";
		}
		newText = newText.substring(0, newText.length() - 1);
		usersLabel.setText(newText);
		if(frame.connected)
		{
			lblToUser.setText("To User ");
			inputField.setDocument(new JTextFieldLimit(280));
		}
		inputField.setText("");
		inputField.requestFocusInWindow();
	}

	
	class JTextFieldLimit extends PlainDocument
	{
		private static final long serialVersionUID = 1L;
		private int limit;

		JTextFieldLimit(int limit)
		{
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
		{
			if (str != null && (getLength() + str.length()) <= limit)
			{
				super.insertString(offset, str, attr);
			}
		}
	}
}