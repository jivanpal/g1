package Views;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

public class ManualInstructionsView extends JPanel {
	private final int INSTRUCTIONS_PER_PAGE = 5;
	
	public static int MIN_PAGE = 1;
	public static int MAX_PAGE = 10;
	
	private final String[] columnNames = {"Number", "Instruction", "KeySeq"};
	private Object[][] data = new Object[MAX_PAGE][columnNames.length];
	
	private int pageNumber;
	private JTable leftPage;
	private JTable rightPage;
	
	public ManualInstructionsView(Object[][] data){
		pageNumber = 1;
		this.data = data;
		
		leftPage = new JTable(getDataForPage(pageNumber),columnNames);
		rightPage = new JTable(getDataForPage(pageNumber + 1), columnNames);
		
		setLayout(new BorderLayout());
		add(leftPage,BorderLayout.WEST);
		add(rightPage,BorderLayout.EAST);
		
		
	}
	
	public void update(){
		leftPage = new JTable(getDataForPage(pageNumber),columnNames);
		rightPage = new JTable(getDataForPage(pageNumber + 1), columnNames);
		this.validate();
		this.repaint();
	}
	
	public void pageDown(){
		this.pageNumber -= 2;
		if(pageNumber < MIN_PAGE){
			this.pageNumber = MIN_PAGE;
		}
	}
	
	public void pageUp(){
		this.pageNumber += 2;
		if(pageNumber > MAX_PAGE){
			this.pageNumber = MAX_PAGE;
		}
	}
	
	private Object[][] getDataForPage(int page){
		Object[][] o = new Object[INSTRUCTIONS_PER_PAGE][columnNames.length];
		for(int i = page*INSTRUCTIONS_PER_PAGE;i<(page+1)*INSTRUCTIONS_PER_PAGE;i++){
			o[i] = data[i];
		}
		return o;
	}
	
	
}
