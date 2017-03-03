package Views;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;

public class ManualInstructionsView extends JPanel {
	private final int INSTRUCTIONS_PER_PAGE = 5;
	
	public static int MIN_PAGE = 1;
	public static int MAX_PAGE = 10;
	
	public static final String[] columnNames = {"Number", "Instruction", "KeySeq"};
	private Object[][] data = new Object[MAX_PAGE][columnNames.length];
	
	private int pageNumber;
	private JTable leftPage;
	private JTable rightPage;
	
	public ManualInstructionsView(ArrayList<char[][]> data){
		pageNumber = 1;
		setData(data);
		
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
	
	private void setData(ArrayList<char[][]> data){
		for(int i=0;i<data.size();i++){
			for(int j = 0; j<data.get(0).length;j++){
				addToData(i*10 + j, new String(data.get(i)[j]));
			}
		}
	}
	
	private void addToData(int pos, String keySeq){
		data[pos][0] = pos +1;
		data[pos][1] = getInstructionStringByPos(pos);
		data[pos][2] = keySeq;
	}

	private String getInstructionStringByPos(int pos) {
		String replenish = "";
		switch(pos % 10){
			case 0:
				replenish = "replenish Laser Blaster";
				break;
			case 1:
				replenish = "replenish Torpedo Weapon";
				break;
			case 2:
				replenish = "replenish Plasma Blaster";
				break;
			case 3:
				replenish = "replenish Shields";
				break;
			case 4:
				replenish = "replenish Fuel";
				break;
			case 5:
				replenish = "turn on the heating";
				break;
			case 6:
				replenish = "empty your bins";
				break;
			case 7:
				replenish = "clean the windscreen";
				break;
			case 8:
				replenish = "refill the coffee machine";
				break;
			case 9:
				replenish = "do the laundry";
				break;				
		}
		return ("If you want to " + replenish + ", do this key sequence:");
	}
}
