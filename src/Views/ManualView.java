package Views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ManualView extends JPanel{
	private JButton previousPage;
	private JButton nextPage;
	private ManualInstructionsView instuctions;
	
	public ManualView(ArrayList<char[][]> data, int size){
		setLayout(new BorderLayout());
		previousPage = new JButton("<");
		nextPage = new JButton(">");
		
		previousPage.addActionListener(e -> goToPreviousPage());
		nextPage.addActionListener(e -> goToNextPage());
		
		add(previousPage, BorderLayout.WEST);
		add(nextPage, BorderLayout.EAST);
		
		instuctions = new ManualInstructionsView(data, size);

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
