package Menus;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * Hover effects for buttons. The button changes colour when the mouse hovers
 * over it.
 * 
 * @author Jaren Liu
 *
 */
public class HoverMouseListener implements MouseListener {
	public JButton button;

	/**
	 * Stores the button in the field so it can get changed
	 * 
	 * @param button
	 *            The JButton which is being changed
	 */
	public HoverMouseListener(JButton button) {
		this.button = button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * When mouse enters the component area, changes colour.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		button.setForeground(Color.GREEN);

	}

	/**
	 * When mouse exits the component area, changes back to the original colour.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		button.setForeground(UIManager.getColor("control"));
	}

}
