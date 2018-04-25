/**
 *	@author Ariana Fairbanks
 */

package client;

import java.awt.Color;
import javax.swing.LookAndFeel;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class NewScrollBarUI extends BasicScrollBarUI
{

	@Override
	public void configureScrollBarColors()
	{
		LookAndFeel.installColors(scrollbar, "ScrollBar.background", "ScrollBar.foreground");
		thumbHighlightColor = Color.GREEN;
		thumbLightShadowColor = Color.GREEN;
		thumbDarkShadowColor = Color.BLACK;
		thumbColor = Color.BLACK;
		trackColor = Color.BLACK;
		trackHighlightColor = Color.BLACK;
	}

	@Override
	public void installComponents()
	{
		switch (scrollbar.getOrientation())
		{
		case 1: 
			incrButton = createIncreaseButton(5);
			decrButton = createDecreaseButton(1);
			break;
		case 0: 
			if (scrollbar.getComponentOrientation().isLeftToRight())
			{
				incrButton = createIncreaseButton(3);
				decrButton = createDecreaseButton(7);
			}
			else
			{
				incrButton = createIncreaseButton(7);
				decrButton = createDecreaseButton(3);
			}
			break;
		}
		incrButton.setBackground(Color.BLACK);
		incrButton.setForeground(Color.BLACK);
		incrButton.setOpaque(true);
		decrButton.setBackground(Color.BLACK);
		decrButton.setForeground(Color.BLACK);
		decrButton.setOpaque(true);
		scrollbar.add(incrButton);
		scrollbar.add(decrButton);
		scrollbar.setEnabled(scrollbar.isEnabled());
	}

}
