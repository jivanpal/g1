package Views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import GameLogic.GameOptions;

public class ManualView extends JPanel{
	private JButton previousPage;
	private JButton nextPage;
	private ManualInstructionsView instuctions;
	
	public ManualView(ArrayList<char[][]> data, int size, int heigth){
		setLayout(new BorderLayout());
		
		previousPage = new JButton("<");
		styleButton(previousPage);
		
		nextPage = new JButton(">");
		styleButton(nextPage);
		
		previousPage.addActionListener(e -> goToPreviousPage());
		nextPage.addActionListener(e -> goToNextPage());
		
		add(previousPage, BorderLayout.WEST);
		add(nextPage, BorderLayout.EAST);
		
		instuctions = new ManualInstructionsView(data, size, heigth);

		add(instuctions,BorderLayout.CENTER);
		
	}
	
	private void styleButton(JButton button){
		button.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
	}

	private void goToNextPage() {
		instuctions.pageUp();
		update();
	}

	private void goToPreviousPage() {
		instuctions.pageDown();
		update();
	}

	private void update(){
		this.validate();
		this.repaint();
	}
}
