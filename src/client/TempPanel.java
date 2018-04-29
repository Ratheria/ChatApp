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

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

class TempPanel extends JPanel 
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
	
	public TempPanel(ChatClient frame)
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
		usersLabel = new JLabel("users");
		topLabel = new JLabel("WTDP Chat Client");
		sendButton = new JButton(" Send ");
		
		setUpPanel();
		setUpLayout();
	}

	private void setUpPanel() 
	{
		setLayout(springLayout);
		add(inputField);
		add(scroll);
		add(usersLabel);
		add(topLabel);
		add(sendButton);
		
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
		topLabel.setForeground(Color.GREEN);
		usersLabel.setForeground(Color.GREEN);
		displayLog.setForeground(Color.GREEN);
		displayLog.setBackground(Color.BLACK);
		sendButton.setBackground(Color.BLACK);
		sendButton.setForeground(Color.GREEN);
		inputField.setForeground(Color.GREEN);
		inputField.setBackground(Color.BLACK);
		topLabel.setFont(new Font("Monospaced", Font.PLAIN, 22));
		usersLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));
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
	}
	
	

}
