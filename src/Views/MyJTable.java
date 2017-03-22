package Views;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
/**
 * Custom JTable which adjusts the column width according to width of content
 * @author Ivan Panchev
 *
 */
public class MyJTable extends JTable {
	public MyJTable() {
		super();
	}
	
	/**
	 * Implements the adjusting of the column width according to width of content 
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component component = super.prepareRenderer(renderer, row, column);
		int rendererWidth = component.getPreferredSize().width;
		TableColumn tableColumn = getColumnModel().getColumn(column);
		tableColumn.setPreferredWidth(
				Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
		return component;
	}

}
