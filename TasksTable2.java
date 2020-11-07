package components;
 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.util.ArrayList;

/*
 * TasksTable2.java requires no other files.
 */
 
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
 
/** 
 * TasksTable2 is just like TableDemo, except that it
 * explicitly initializes column sizes and it uses a combo box
 * as an editor for the Sport column.
 */
public class TasksTable2 extends JPanel {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private boolean DEBUG = false;
	
	String[] columnNames;
	Object[][] data;
	
    public static JTable table;
    static Font font = new Font("Verdana", Font.PLAIN, 10);
    public static FontMetrics metrics = new FontMetrics(font) {
		private static final long serialVersionUID = 7044476510869677223L;};
 
    public TasksTable2(String[] columnNames, Object[][] data){
        super(new GridLayout(1,0));
        
        this.columnNames = columnNames;
        this.data = data;
 
        table = new JTable(new MyTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
 
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        
        //Set up column sizes.
        //initColumnSizes(table);
        //Fiddle with the Sport column's cell editors/renderers.
        setUpMemberColumn(table, table.getColumnModel().getColumn(1));

        setUpTaskTypeColumn(table, table.getColumnModel().getColumn(2));
        table.setLocation(TaskAssigner.current.frame.getX()+416, TaskAssigner.current.frame.getY());
        //Add the scroll pane to this panel.
        
        Font font = new Font("Verdana", Font.PLAIN, 10);
        FontMetrics metrics = new FontMetrics(font) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * 
			 */
			};
	        
		int n = 0;
        
        for(int i=0; i<columnNames.length; i++)
        {
            int width = table.getColumnModel().getColumn(i).getWidth();
            for(int j=0; j<data.length; j++)
            {
            	
	            if(i!=3 && metrics.getStringBounds((String) data[j][i], null).getWidth() > width)
	        		width = (int) metrics.getStringBounds((String) data[j][i], null).getWidth();
	            else if(i==3 && metrics.getStringBounds(Integer.toString((int) data[j][i]), null).getWidth() > width)
	        		width = (int) metrics.getStringBounds(Integer.toString((int) data[j][i]), null).getWidth();
            }
            table.getColumnModel().getColumn(i).setMinWidth(width);
            table.getColumnModel().getColumn(i).setWidth(width);
            n+=width;
        }
        
        table.setPreferredScrollableViewportSize(new Dimension(600+n-225
        		, table.getRowCount() * table.getRowHeight() + 50));
        
        add(scrollPane);
    }
 
    /*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     *
    private void initColumnSizes(JTable table) {
        MyTableModel model = (MyTableModel)table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();
 
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
 
            comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;
 
            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                             getTableCellRendererComponent(
                                 table, longValues[i],
                                 false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;
 
            if (DEBUG) {
                System.out.println("Initializing width of column "
                                   + i + ". "
                                   + "headerWidth = " + headerWidth
                                   + "; cellWidth = " + cellWidth);
            }
 
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }*/
 
    public void setUpMemberColumn(JTable table,
                                 TableColumn memberColumn) {
        //Set up the editor for the member cells.
        JComboBox<String> comboBox = new JComboBox<String>(TaskAssigner.current.memberNames);
        memberColumn.setCellEditor(new DefaultCellEditor(comboBox));
 
        //Set up tool tips for the member cells.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        memberColumn.setCellRenderer(renderer);
    }
    
    public void setUpTaskTypeColumn(JTable table,
            TableColumn taskTypeColumn) {
		//Set up the editor for the member cells.
		JComboBox<String> comboBox = new JComboBox<String>(TaskAssigner.current.taskTypeNames);
		taskTypeColumn.setCellEditor(new DefaultCellEditor(comboBox));
		
		//Set up tool tips for the member cells.
		DefaultTableCellRenderer renderer =
		new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for combo box");
		taskTypeColumn.setCellRenderer(renderer);
	}
 
    class MyTableModel extends AbstractTableModel {
       
		/**
		 * 
		 */
		private static final long serialVersionUID = -4911101086389672639L;
		
		
		public MyTableModel()
		{
    		
		}
		
        
 
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
         */
		public Class<? extends Object> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return true;
        }
 
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }
                        
        	int width = table.getColumnModel().getColumn(col).getWidth();

            Object obj = data[row][col];;
            
            data[row][col] = value;
            fireTableCellUpdated(row, col);
 
            a: try
            {
 	            if(col == 0)
 	            {
 	            	for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
    				{
    					if(TaskAssigner.current.tasks.get(i).getId().equals(value))
    					{
    						JOptionPane.showMessageDialog(TaskAssigner.tasksPane, "A task cannot have the same"
	            					+ " ID as another task.", "Error", JOptionPane.ERROR_MESSAGE);
            				data[row][col] = obj;
            				break a;
    					}
    				}
 	            	
 	            	if(((CharSequence) value).chars().allMatch(Character::isWhitespace))
    				{
 	            		JOptionPane.showMessageDialog(TaskAssigner.tasksPane, "A task ID cannot"
            					+ " be whitespace.", "Error",
            					JOptionPane.ERROR_MESSAGE);
            			break a;
    				}
 		            TaskAssigner.current.removeTaskCombo.removeItem(TaskAssigner.current.tasks.get(row).getId());
 		            TaskAssigner.current.removeTaskCombo.insertItemAt((String) value, row);

 	            	TaskAssigner.current.tasks.get(row).setId((String) value);
 	            	
 	            	for(int i=0; i<TaskAssigner.current.workingMembers.size(); i++)
 	            	{
 	            		ArrayList<Task> assignments = TaskAssigner.current.workingMembers.get(i).getAssignments();
 	            		for(int j=0; j<assignments.size(); j++)
 	            		{
 	            			if(assignments.get(j).getId().equals(obj))
 	            				assignments.get(j).setId((String) value);
 	            		}
 	            	}
 	            }
 	            if(col == 1)
 	            {
 	            	for(int i=0; i<TaskAssigner.current.members.size(); i++)
 	            	{
 	            		if(TaskAssigner.current.members.get(i).getName().equals(value))
 	            		{
            				if(!TaskAssigner.current.members.get(i).getAuthorizations().contains(TaskAssigner.current.tasks.get(row).getType()))
            				{
            					Object[] options = new Object[]{"Ok", "Undo"};
            					int x = JOptionPane.showOptionDialog(TaskAssigner.tasksPane, "The newly assigned member "
            							+ "is not authorized to work on this task.", "Warning", JOptionPane.DEFAULT_OPTION,
               					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            					if(x==1)
            					{
            						data[row][col] = obj;
            						break a;
            					}
            				}
            				TaskAssigner.current.tasks.get(row).setAssignedMember(TaskAssigner.current.members.get(i));
            				TaskAssigner.current.members.get(i).addAssignments(TaskAssigner.current.tasks.get(row));
            				break;
 	            		}
 	            	}
 	            	for(int i=0; i<TaskAssigner.current.members.size(); i++)
 	            	{
 	            		if(TaskAssigner.current.members.get(i).getName().equals(obj))
 	            		{
            				TaskAssigner.current.members.get(i).setWork(TaskAssigner.current.members.get(i).getWork()-TaskAssigner.current.tasks.get(row).getWorkValue());
 	            			TaskAssigner.current.members.get(i).removeAssignments(TaskAssigner.current.tasks.get(row));
 	            			
 	            		}
 	            	}
 	            }
 	            if(col == 2)
 	            {
 	            	for(int i=0; i<TaskAssigner.current.tasktypes.size(); i++)
 	            	{
 	            		if(TaskAssigner.current.tasktypes.get(i).getName().equals(value))
 	            		{
 	            			if(!TaskAssigner.current.tasks.get(row).getAssignedMember().getAuthorizations().contains(value))
 	            			{
	 	            			Object[] options = new Object[]{"Ok", "Undo"};
	         					int x = JOptionPane.showOptionDialog(TaskAssigner.tasksPane, "The member assigned to this task "
	         							+ "is not authorized to work on this type of case.", "Warning", JOptionPane.DEFAULT_OPTION,
	            					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	         					if(x==1)
	         					{
	         						data[row][col] = obj;
	         						break a;
	         					}
 	            			}
	            			TaskAssigner.current.tasks.get(row).setType(TaskAssigner.current.tasktypes.get(i).getName());
	            			if(TaskAssigner.current.tasks.get(row).getId().contains((CharSequence) obj))
	            			{
	            				TaskAssigner.current.tasks.get(row).setId(TaskAssigner.current.tasks.get(row).getId().replace(
	            			    (CharSequence) obj, TaskAssigner.current.tasks.get(row).getType()));
	            				
	            				data[row][0] = TaskAssigner.current.tasks.get(row).getId();
	            			}
	            			
         	            	int work = TaskAssigner.current.tasks.get(row).getAssignedMember().getWork();
         	            	if(!TaskAssigner.current.tasks.get(row).isOverridden())
         	            	{
         	            		work -= TaskAssigner.current.tasks.get(row).getWorkValue();
             	            	TaskAssigner.current.tasks.get(row).setWorkValue(TaskAssigner.current.tasktypes.get(i).getWorkValue());
         	            		data[row][3] = TaskAssigner.current.tasktypes.get(i).getWorkValue();
             	            	work += TaskAssigner.current.tasks.get(row).getWorkValue();
             	            	TaskAssigner.current.tasks.get(row).getAssignedMember().setWork(work);
         	            	}
	            			break;
 	            		}
 	            	}
 	            }
 	            if(col == 3)
 	            {
 	            	if(value.equals(""))
 	            	{
 	            		data[row][3] = 0;
 	            		value="0";
 	            	}
 	            	TaskAssigner.current.tasks.get(row).setWorkValue((int) value);
 	            	if((int) value != (int) obj)
 	            		TaskAssigner.current.tasks.get(row).setOverridden(true);
 	            	
 	            	int work = TaskAssigner.current.tasks.get(row).getAssignedMember().getWork();
 	            	work -= (int) obj;
 	            	work += (int) value;
 	            	
 	            	TaskAssigner.current.tasks.get(row).getAssignedMember().setWork(work);
 	            }
 	           if(metrics.getStringBounds(value.toString(), null).getWidth() > width)
	            {
	        		int width1 = (int) metrics.getStringBounds((String) value, null).getWidth();
	        		table.getColumnModel().getColumn(col).setMinWidth(width1);
	                table.getColumnModel().getColumn(col).setWidth(width1);
	                Dimension d = table.getPreferredScrollableViewportSize();
	                d.setSize(d.getWidth()-width+width1,d.getHeight());
	                table.setPreferredScrollableViewportSize(d);
	            }
            }
            catch(Exception e)
            {
             	JOptionPane.showMessageDialog(TaskAssigner.tasksPane, "Changed value does not match data type", "Error", JOptionPane.ERROR_MESSAGE);
             	data[row][col] = obj;
             	e.printStackTrace();
            }
            
            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
	        TaskAssigner.tasksPane.repaint();
            TaskAssigner.current.setTaskValues();
        }
 
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     *
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TasksTable2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        TasksTable2 newContentPane = new TasksTable2(columnNames, data);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }*/
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }
}