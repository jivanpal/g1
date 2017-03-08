package Views;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ManualInstructionsView extends JPanel {
	private final int INSTRUCTIONS_PER_PAGE = 5;

	public static int MIN_PAGE = 1;
	public static int MAX_PAGE = 11;

	public static final String[] columnNames = { "Number", "Instruction", "KeySeq" };
	private Object[][] data;

	private int pageNumber;
	private JTable leftPage;
	private JTable rightPage;

	public ManualInstructionsView(ArrayList<char[][]> data, int size, int height) {
		System.out.println("height is: " + height);
		pageNumber = 1;
		this.data = new Object[size][columnNames.length];
		setData(data);

		leftPage = new MyJTable();
		leftPage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		leftPage.setModel(new DefaultTableModel(getDataForPage(pageNumber), columnNames));
		leftPage.setRowHeight(height / 5);

		rightPage = new MyJTable();
		rightPage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		rightPage.setModel(new DefaultTableModel(getDataForPage(pageNumber + 1), columnNames));
		rightPage.setRowHeight(height / 5);

		setLayout(new GridLayout(1, 2));
		add(leftPage);
		add(rightPage);

		// printData();
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

	private void setData(ArrayList<char[][]> data) {
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(0).length; j++) {
				addToData(i * 10 + j, new String(data.get(i)[j]));
			}
		}
	}

	private void addToData(int pos, String keySeq) {
		data[pos][0] = pos + 1;
		data[pos][1] = getInstructionStringByPos(pos);
		data[pos][2] = keySeq;
	}

	private String getInstructionStringByPos(int pos) {
		String replenish = "";
		switch (pos % 10) {
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

	private void printData() {
		for (int i = 0; i < data.length; i++) {
			System.out.println("Num:" + data[i][0] + " Str:" + data[i][1] + " Inst:" + data[i][2]);
		}
	}

	public int getPage() {
		return this.pageNumber;
	}
}
