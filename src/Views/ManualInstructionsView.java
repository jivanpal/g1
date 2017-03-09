package Views;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import GameLogic.GameOptions;

public class ManualInstructionsView extends JPanel {
	private final int INSTRUCTIONS_PER_PAGE = 5;

	public static int MIN_PAGE = 1;
	public static int MAX_PAGE = 11;

	public static final String[] columnNames = { "Number", "Instruction", "KeySeq" };
	private Object[][] data;

	private int pageNumber;
	private JTable leftPage;
	private JTable rightPage;

	public ManualInstructionsView(ArrayList<String> data, int size, int height) {
		pageNumber = 1;
		this.data = new Object[size][columnNames.length];
		setData(data);
		
		leftPage = new MyJTable();
		leftPage.setShowGrid(false);
		leftPage.setFont(GameOptions.REGULAR_TEXT_FONT);
		leftPage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		leftPage.setModel(new DefaultTableModel(getDataForPage(pageNumber), columnNames));
		leftPage.setRowHeight(height / 5);

		rightPage = new MyJTable();
		rightPage.setShowGrid(false);
		rightPage.setFont(GameOptions.REGULAR_TEXT_FONT);
		rightPage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		rightPage.setModel(new DefaultTableModel(getDataForPage(pageNumber + 1), columnNames));
		rightPage.setRowHeight(height / 5);

		setLayout(new GridLayout(1, 2));
		add(leftPage);
		add(rightPage);
	}

	private void update() {
		leftPage.setModel(new DefaultTableModel(getDataForPage(pageNumber), columnNames));
		rightPage.setModel(new DefaultTableModel(getDataForPage(pageNumber + 1), columnNames));
		this.validate();
		this.repaint();
	}

	public void pageDown() {
		this.pageNumber -= 2;
		if (pageNumber < MIN_PAGE) {
			this.pageNumber = MIN_PAGE;
		}
		update();
	}

	public void pageUp() {
		this.pageNumber += 2;
		if (pageNumber > MAX_PAGE) {
			this.pageNumber = MAX_PAGE;
		}
		update();
	}

	private Object[][] getDataForPage(int page) {
		Object[][] o = new Object[INSTRUCTIONS_PER_PAGE][columnNames.length];
		int num = (page - 1) * INSTRUCTIONS_PER_PAGE;
		for (int i = 0; i < INSTRUCTIONS_PER_PAGE; i++) {
			// System.out.println("print data" + data[i]);
			o[i] = data[i + num];
		}
		return o;
	}

	private void setData(ArrayList<String> data) {
			for (int j = 0; j < data.size(); j++) {
				addToData(j,data.get(j));
			}
	}

	private void addToData(int pos, String keySeq) {
		String[] split = keySeq.split(":");
		data[pos][0] = Integer.valueOf(split[0]) + 1;
		data[pos][1] = getInstructionStringByPos(pos);
		data[pos][2] = split[1];
	}

	private String getInstructionStringByPos(int pos) {
		String replenish = "";
		if(pos < 7){
			replenish = "replenish Shields";
		} else if(pos < 14){
			replenish = "replenish Fuel";
		}else if(pos < 21){
			replenish = "replenish Laser Blaster";
		}else if(pos < 28){
			replenish = "replenish Plasma Blaster";
		}else if(pos < 35){
			replenish = "replenish Torpedo Weapon";
		}else if(pos < 42){
			replenish = "turn on the heating";
		}else if(pos < 49){
			replenish = "empty your bins";
		}else if(pos < 56){
			replenish = "clean the windscreen";
		}else if(pos < 63){
			replenish = "refill the coffee machine";
		}else {
			replenish = "do the laundry";
		}
		return ("If you want to " + replenish + ", do this key sequence:");
	}

	public int getPage() {
		return this.pageNumber;
	}
	
}
