package Views;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ManualView extends JPanel {
	private JButton previousPage;
	private JButton nextPage;
	private ManualInstructionsView instuctions;
	
	public ManualView(){
		setLayout(new BorderLayout());
		previousPage = new JButton("<");
		nextPage = new JButton(">");
		
		previousPage.addActionListener(e -> goToPreviousPage());
		nextPage.addActionListener(e -> goToNextPage());
		
		add(previousPage, BorderLayout.WEST);
		add(nextPage, BorderLayout.EAST);
		
		instuctions = new ManualInstructionsView();
		add(instuctions,BorderLayout.CENTER);
		
	}

	private void goToNextPage() {
		instuctions.pageUp();
		instuctions.update();
	}

	private void goToPreviousPage() {
		instuctions.pageDown();
		instuctions.update();
	}
	
}
