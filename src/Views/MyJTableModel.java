package Views;

import javax.swing.table.DefaultTableModel;
/**
 * Custom JTableModel which prevents cells from being edited
 * @author Ivan Panchev
 *
 */
public class MyJTableModel extends DefaultTableModel{
	public MyJTableModel(Object[][] data, String[] columns) {
		super(data,columns);
	}
	
	/**
	 * Implements the prevention of cells from being edited
	 */
	@Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
}
