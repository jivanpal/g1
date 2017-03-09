package Menus;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.UIManager;

public class HoverMouseListener implements MouseListener {
	public JButton button;
	
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

	@Override
	public void mouseEntered(MouseEvent e) {
		button.setForeground(Color.GREEN);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		button.setForeground(UIManager.getColor("control"));
	}
	

}
