/**
 *	@author Ariana Fairbanks
 */

package client;

import javax.swing.JFrame;

public class ChatClientFrame extends JFrame 
{
	private static final long serialVersionUID = -5136294238715941655L;
	@SuppressWarnings("unused")
	private ClientConnection base;
	private ChatClientPanel panel;

	public ChatClientFrame(ClientConnection base)
	{
		this.base = base;
		panel = new ChatClientPanel(base);
		setName("WTDP Chat Client");
		this.setTitle("WTDP Chat Client");
		setContentPane(panel);
		setSize(750, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
