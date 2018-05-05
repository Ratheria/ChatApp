/**
 *	@author Ariana Fairbanks
 */

package server;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChatServerPanel extends JPanel
{
	private static final long serialVersionUID = 4646763206566977634L;
	private ChatServer frame;
	private JButton endButton;
	
	public ChatServerPanel(ChatServer frame)
	{
		this.frame = frame;
		endButton = new JButton(" Close Server ");
		setUpPanel();
		setUpLayout();
		setUpListeners();
	}

	private void setUpPanel() 
	{
		add(endButton);
	}

	private void setUpLayout() 
	{
		setBackground(new Color(0, 0, 0));
		endButton.setFocusPainted(false);
		endButton.setContentAreaFilled(false);
		endButton.setBackground(Color.BLACK);
		endButton.setForeground(Color.GREEN);
		endButton.setFont(new Font("MingLiU_HKSCS-ExtB", Font.PLAIN, 30));
		endButton.setBorder(BorderFactory.createLineBorder(Color.GREEN));
	}
	
	private void setUpListeners() 
	{
		endButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent click) 
			{
				frame.serverRunning = false;
				System.exit(0);
			}
		});
	}
}
