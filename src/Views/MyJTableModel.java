package Views;

import javax.swing.table.DefaultTableModel;

public class MyJTableModel extends DefaultTableModel{
	public MyJTableModel(Object[][] data, String[] columns) {
		super(data,columns);
	}
	
	@Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
}
