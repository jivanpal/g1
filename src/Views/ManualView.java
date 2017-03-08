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
		previousPage.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		previousPage.setOpaque(false);
		previousPage.setContentAreaFilled(false);
		previousPage.setBorderPainted(false);
		previousPage.setFocusable(false);
		
		
		nextPage = new JButton(">");
		nextPage.setFont(GameOptions.LARGE_BOLD_TEXT_FONT);
		nextPage.setOpaque(false);
		nextPage.setContentAreaFilled(false);
		nextPage.setBorderPainted(false);
		nextPage.setFocusable(false);
		
		previousPage.addActionListener(e -> goToPreviousPage());
		nextPage.addActionListener(e -> goToNextPage());
		
		add(previousPage, BorderLayout.WEST);
		add(nextPage, BorderLayout.EAST);
		
		instuctions = new ManualInstructionsView(data, size, heigth);

		add(instuctions,BorderLayout.CENTER);
		
	}

	private void goToNextPage() {
		instuctions.pageUp();
		update();
		System.out.println(instuctions.getPage());
	}

	private void goToPreviousPage() {
		instuctions.pageDown();
		update();
		System.out.println(instuctions.getPage());
	}

	private void update(){
		this.validate();
		this.repaint();
	}
}
