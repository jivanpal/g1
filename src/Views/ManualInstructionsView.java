package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import GameLogic.GameOptions;

public class ManualInstructionsView extends JPanel {
	private final int INSTRUCTIONS_PER_PAGE = 5;

	public static final int MIN_PAGE = 1;
	public static final int MAX_PAGE = 11;

	//default column names for the table header
	public static final String[] columnNames = { "Number", "Instruction", "Key Seq" };
	//the data that populates the table
	private Object[][] data;

	private int pageNumber;
	private JTable leftPage;
	private JTable rightPage;

	public ManualInstructionsView(ArrayList<String> data, int size, int height) {
		pageNumber = 1;
		this.data = new Object[size][columnNames.length];
		setData(data);
		
		leftPage = new MyJTable();
		leftPage.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
		styleTable(leftPage, height,pageNumber);
		
		rightPage = new MyJTable();
		rightPage.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		styleTable(rightPage, height, pageNumber +1);

		setLayout(new BorderLayout());
		
		JPanel headers = new JPanel();
		headers.setLayout(new GridLayout(1, 2));
		headers.add(leftPage.getTableHeader());
		headers.add(rightPage.getTableHeader());
		
		JPanel manual = new JPanel();
		manual.setLayout(new GridLayout(1, 2));
		manual.add(leftPage);
		manual.add(rightPage);
		
		add(headers, BorderLayout.NORTH);
		add(manual, BorderLayout.CENTER);
	}
	
	/**
	 * Styling the JTable consistently with the rest of the UI
	 * @param table The JTable to style
	 * @param height The Height of the component
	 * @param pageNumber Current page number of the table
	 */
	private void styleTable(JTable table, int height, int pageNumber) {
		table.setShowGrid(false);
		table.setShowHorizontalLines(true);
		table.setFont(GameOptions.REGULAR_TEXT_FONT);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(height / INSTRUCTIONS_PER_PAGE);
		table.setModel(new MyJTableModel(getDataForPage(pageNumber), columnNames));
		table.setFocusable(false);
		table.setRowSelectionAllowed(false);
		table.setOpaque(true);
		table.setFillsViewportHeight(true);
		
		table.setBackground(Color.WHITE);
		table.setForeground(Color.BLACK);
		table.setFont(GameOptions.REGULAR_TEXT_FONT);
		
		table.getTableHeader().setBackground(Color.WHITE);
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setFont(GameOptions.REGULAR_TEXT_FONT);
		table.setSelectionBackground(Color.decode("#999999"));
		
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(table.getColumnClass(0));
		renderer.setHorizontalAlignment(JLabel.CENTER);
	}

	/**
	 * Updates the views
	 */
	private void update() {
		leftPage.setModel(new MyJTableModel(getDataForPage(pageNumber), columnNames));
		rightPage.setModel(new MyJTableModel(getDataForPage(pageNumber + 1), columnNames));
		this.validate();
		this.repaint();
	}

	/**
	 * Turns the manual to the next page
	 */
	public void pageDown() {
		if (pageNumber > MIN_PAGE) {
			this.pageNumber -= 2;
			update();
		}
	}
	
	/**
	 * Turns the manual to the previous page
	 */
	public void pageUp() {
		if (pageNumber < MAX_PAGE) {
			this.pageNumber += 2;
			update();
		}
	}
	
	/**
	 * Gets the data for a specific page number from the whole data
	 * @param page Page number that you want the data for
	 * @return The data for the specific page in the required format
	 */
	private Object[][] getDataForPage(int page) {
		Object[][] o = new Object[INSTRUCTIONS_PER_PAGE + 1][columnNames.length];
		int num = (page - 1) * INSTRUCTIONS_PER_PAGE;
		System.arraycopy(data, 0 + num, o, 0, INSTRUCTIONS_PER_PAGE);
		return o;
	}
	
	/**
	 * Method which sets the data got from the server
	 * This method is needed because of inconsistencies in the formats
	 * @param data
	 */
	private void setData(ArrayList<String> data) {
		for (int j = 0; j < data.size(); j++) {
			addToData(j, data.get(j));
		}
	}
	
	/**
	 * Helper method for adding the data from the server to the one that can be used in JTable
	 * @param realPos
	 * @param keySeq
	 */
	private void addToData(int realPos, String keySeq) {
		String[] split = keySeq.split(":");
		int pos = Integer.valueOf(split[0]) - 1;
		data[pos][0] = pos + 1;
		data[pos][1] = getInstructionStringByPos(realPos);
		data[pos][2] = split[1];
	}

	/**
	 * Getting instruction string based on the position of instruction in the date provided from the server
	 * @param pos The position of the instruction
	 * @return The instruction string
	 */
	private String getInstructionStringByPos(int pos) {
		String replenish = "";
		if (pos < 7) {
			replenish = "replenish Shields";
		} else if (pos < 14) {
			replenish = "replenish Fuel";
		} else if (pos < 21) {
			replenish = "replenish Laser Blaster";
		} else if (pos < 28) {
			replenish = "replenish Plasma Blaster";
		} else if (pos < 35) {
			replenish = "replenish Torpedo Weapon";
		} else if (pos < 42) {
			replenish = "turn on the heating";
		} else if (pos < 49) {
			replenish = "empty your bins";
		} else if (pos < 56) {
			replenish = "clean the windscreen";
		} else if (pos < 63) {
			replenish = "refill the coffee machine";
		} else {
			replenish = "do the laundry";
		}
		return ("If you want to " + replenish + ", do this key sequence:");
	}
	
	public int getPage() {
		return this.pageNumber;
	}

}
