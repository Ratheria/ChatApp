/** *	@author Ariana Fairbanks */package client;import java.awt.Font;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.KeyEvent;import java.awt.event.KeyListener;import java.awt.event.WindowAdapter;import java.awt.event.WindowEvent;import javax.swing.BorderFactory;import javax.swing.JButton;import javax.swing.JFrame;import javax.swing.JPanel;import javax.swing.JScrollPane;import javax.swing.JTextArea;import javax.swing.JTextField;import javax.swing.border.Border;public class ChatScreen extends JFrame implements ActionListener, KeyListener{	private static final long serialVersionUID = -1571695469152471743L;	private JButton sendButton;	private JButton exitButton;	private JTextField sendText;	private JTextArea displayArea;	public ChatScreen()	{		setUndecorated(true);		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		JPanel p = new JPanel();		Border etched = BorderFactory.createEtchedBorder();		Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");		p.setBorder(titled);		/**		 * set up all the components		 */		sendText = new JTextField(30);		sendButton = new JButton("Send");		exitButton = new JButton("Exit");		/**		 * register the listeners for the different button clicks		 */		sendText.addKeyListener(this);		sendButton.addActionListener(this);		exitButton.addActionListener(this);		/**		 * add the components to the panel		 */		p.add(sendText);		p.add(sendButton);		p.add(exitButton);		/**		 * add the panel to the "south" end of the container		 */		getContentPane().add(p, "South");		/**		 * add the text area for displaying output. Associate a scrollbar with		 * this text area. Note we add the scrollpane to the container, not the		 * text area		 */		displayArea = new JTextArea(15, 40);		displayArea.setEditable(false);		displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));		JScrollPane scrollPane = new JScrollPane(displayArea);		getContentPane().add(scrollPane, "Center");		/**		 * set the title and size of the frame		 */		setTitle("GUI Demo");		pack();		setVisible(true);		sendText.requestFocus();		/** anonymous inner class to handle window closing events */		addWindowListener(new WindowAdapter()		{			@Override			public void windowClosing(WindowEvent evt)			{				System.exit(0);			}		});	}	/**	 * This gets the text the user entered and outputs it in the display area.	 */	public void displayText()	{		String message = sendText.getText().trim();		StringBuffer buffer = new StringBuffer(message.length());		// now reverse it		for (int i = message.length() - 1; i >= 0; i--)			buffer.append(message.charAt(i));		displayArea.append(buffer.toString() + "\n");		sendText.setText("");		sendText.requestFocus();	}	/**	 * This method responds to action events .... i.e. button clicks and	 * fulfills the contract of the ActionListener interface.	 */	@Override	public void actionPerformed(ActionEvent evt)	{		Object source = evt.getSource();		if (source == sendButton) displayText();		else if (source == exitButton) System.exit(0);	}	/**	 * These methods responds to keystroke events and fulfills the contract of	 * the KeyListener interface.	 */	/**	 * This is invoked when the user presses the ENTER key.	 */	@Override	public void keyPressed(KeyEvent e)	{		if (e.getKeyCode() == KeyEvent.VK_ENTER) displayText();	}	/** Not implemented */	@Override	public void keyReleased(KeyEvent e)	{}	/** Not implemented */	@Override	public void keyTyped(KeyEvent e)	{}	public static void main(String[] args)	{		@SuppressWarnings("unused")		JFrame win = new ChatScreen();	}}