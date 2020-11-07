package components;
 
/*
 * TableDemo.java requires no other files.
 */
 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.util.ArrayList;

/** 
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class TableDemo extends JPanel implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	String[] columnNames;
    Object[][] data;
    public static JTable table;
    static Font font = new Font("Verdana", Font.PLAIN, 10);
    public static FontMetrics metrics = new FontMetrics(font) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	};
 
    public TableDemo(String[] columnNames, Object[][] data)
    {
        super(new GridLayout(1,0));
 
        this.columnNames = columnNames;
        this.data = data;
        
        ArrayList<String> FStasks = new ArrayList<String>();
		for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
		{
			if(TaskAssigner.current.tasks.get(i).getType().equals("FS"))
			{
				FStasks.add(TaskAssigner.current.tasks.get(i).getId());
			}
		}
		        
        table = new JTable(new MyTableModel())
		{
	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

	/**
			 * 
			 */
			
			
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component returnComp = super.prepareRenderer(renderer, row, column);
        Color alternateColor = new Color(255,99,71);
        Color whiteColor = Color.WHITE;
        String str = (String) data[row][column];
        if (!returnComp.getBackground().equals(getSelectionBackground())){
        	Color bg = (str != null && str.length() != 0 && !str.equals("|") && column !=0 && FStasks.contains(str.substring
        			(0, str.indexOf(" "))) ? alternateColor : whiteColor);
            returnComp .setBackground(bg);
            bg = null;
        }
        return returnComp;
		}};
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setFillsViewportHeight(true);
        table.setLocation(TaskAssigner.current.frame.getX()+416, TaskAssigner.current.frame.getY());
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
 
        int n = 0;
        
        table.getColumnModel().getColumn(0).setMinWidth(95);
        table.getColumnModel().getColumn(0).setMaxWidth(95);
        
        for(int i=0; i<columnNames.length; i++)
        {
            int width = table.getColumnModel().getColumn(i).getWidth();
            for(int j=0; j<data.length; j++)
            {
	            if(data[j][i] !=null && metrics.getStringBounds((String) data[j][i], null).getWidth() > width)
	        		width = (int) metrics.getStringBounds((String) data[j][i], null).getWidth();
            }
            table.getColumnModel().getColumn(i).setMinWidth(width);
            table.getColumnModel().getColumn(i).setWidth(width);
            n+=width;
        }
        
        table.setPreferredScrollableViewportSize(new Dimension(600+n-225
        		, table.getRowCount() * table.getRowHeight() + 50));
        //Add the scroll pane to this panel.
        add(scrollPane);
        
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for(int i=0; i<columnNames.length; i++)
	        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
 
    class MyTableModel extends AbstractTableModel
    {
    	
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return data.length;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        public Object getValueAt(int row, int col) {
        	return data[row][col];
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         *
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }
 
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         *
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
           }
		*/
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI(String[] columnNames, Object [][] data) {
        //Create and set up the window.
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        TableDemo newContentPane = new TableDemo(columnNames, data);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
        });
    }
}
