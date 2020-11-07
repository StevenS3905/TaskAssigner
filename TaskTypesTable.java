package components;
 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/*
 * TableDemo.java requires no other files.
 */
 
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/** 
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class TaskTypesTable extends JPanel implements Serializable{
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
		private static final long serialVersionUID = 7044476510869677223L;};
 
    public TaskTypesTable(String[] columnNames, Object[][] data)
    {
        super(new GridLayout(1,0));
 
        this.columnNames = columnNames;
        this.data = data;
        
        table = new JTable(new MyTableModel());
        table.setFillsViewportHeight(true);
        table.setLocation(TaskAssigner.current.frame.getX()+416, TaskAssigner.current.frame.getY());
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        
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
	            	
		            if(i!=2 && metrics.getStringBounds((String) data[j][i], null).getWidth() > width)
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
 
        //Add the scroll pane to this panel.
        add(scrollPane);
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
            return true;
        }
 
        /* 
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
        	int width = table.getColumnModel().getColumn(col).getWidth();
            Object obj = data[row][col];
            data[row][col] = value;
            fireTableCellUpdated(row, col);
           a: try
           {
	            if(col == 0)
	            {
	            	for(int i=0; i<TaskAssigner.current.tasktypes.size(); i++)
    				{
    					if(TaskAssigner.current.tasktypes.get(i).getName().equals((String) value))
    					{
    						JOptionPane.showMessageDialog(TaskAssigner.taskTypesPane, "A task type name cannot have the same name"
	            					+ " as another task type.", "Warning", 
	            					JOptionPane.ERROR_MESSAGE);
            				data[row][col] = obj;
                        	break a;
    					}
    				}
	            	
	            	if(((CharSequence) value).chars().allMatch(Character::isWhitespace))
    				{
	            		JOptionPane.showMessageDialog(TaskAssigner.taskTypesPane, "A task type cannot"
            					+ " be whitespace.", "Error",
            					JOptionPane.ERROR_MESSAGE);
            			break a;
    				}
	            	
		            TaskAssigner.current.addTaskCombo.removeItem(TaskAssigner.current.tasktypes.get(row).getName());
		            TaskAssigner.current.addTaskCombo.insertItemAt((String) value, row);
		            
		            TaskAssigner.current.removeTaskTypeCombo.removeItem(TaskAssigner.current.tasktypes.get(row).getName());
		            TaskAssigner.current.removeTaskTypeCombo.insertItemAt((String) value, row);
		            
		            TaskAssigner.current.authorizationCheckboxes.get(row).setText((String) value);
		            
	            	TaskAssigner.current.tasktypes.get(row).setName((String) value);
	            	
	            	for(int i=0; i<TaskAssigner.current.members.size(); i++)
	            	{
	            		if(TaskAssigner.current.members.get(i).getAuthorizations().contains(obj))
							TaskAssigner.current.members.get(i).getAuthorizations().set(TaskAssigner.current.members.get(i).getAuthorizations().indexOf(obj), (String) value);
	            	}
	            	
	            	for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
	            	{
	            		if(TaskAssigner.current.tasks.get(i).getType().equals(obj))
	            			TaskAssigner.current.tasks.get(i).setType((String) value);
	            	}
	            }
           		if(col == 1)
           		{
	            	value = ((String) value).replaceAll("\\s+","");
	            	obj = ((String) obj).replaceAll("\\s+","");
	            	
	            	List<String> list = Arrays.asList(((String) value).split(","));
	            	
	            	List<String> tasks = Arrays.asList(((String) value).split(","));
	            	List<String> tasksObj = Arrays.asList(((String) obj).split(","));
	            	
	            	boolean b = true;

	            	for(int i=0; i<list.size(); i++)
	            	{
	            		if(!TaskAssigner.current.taskIds.contains(list.get(i)) && !list.get(i).chars().allMatch(Character::isWhitespace))
	            		{
	            			JOptionPane.showMessageDialog(TaskAssigner.taskTypesPane,
			            	"That task has not been inputted", "Error", JOptionPane.ERROR_MESSAGE);
			            			
	            			data[row][col] = obj;
			            	break a;
	            		}
	            		
	            		for(int j=0; j<data.length; j++)
		            	{
		            		if(j==row) continue;
		            		data[j][1] = ((String) data[j][1]).replaceAll("\\s+","");
		            		List<String> temp = Arrays.asList(((String) data[j][1]).split(","));
		            		if(temp.contains(list.get(i)) && !list.get(i).chars().allMatch(Character::isWhitespace))
		            		{
		            			JOptionPane.showMessageDialog(TaskAssigner.taskTypesPane,
		            			"This task already has a type", "Error", JOptionPane.ERROR_MESSAGE);
		            			
		            			data[row][col] = obj;
		            			break a;
		            		}
		            	}
	            		
	            		for(int j=0; j<TaskAssigner.current.tasks.size(); j++)
	            		{
	            			if(TaskAssigner.current.tasks.get(j).getId().equals(list.get(i)))
	            			{
	            				if(TaskAssigner.current.tasks.get(j).getAssignedMember() != null &&
	            				!TaskAssigner.current.tasks.get(j).getAssignedMember().getAuthorizations().contains(data[row][0]) && b)
	            				{
	            					Object[] options = new Object[]{"Ok", "Undo"};
	    	            			int x = JOptionPane.showOptionDialog(TaskAssigner.taskTypesPane, "One or more of the workers assigned to the new tasks of this type "
	    	            					+ "is unauthorized to work on his new assignment", "Warning", JOptionPane.DEFAULT_OPTION,
	    	            					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	    	            			if(x==1)
	    	            			{
	    	                        	data[row][col] = obj;
	    	                        	break a;
	    	            			}
	    	            			b = false;
	            				}
	            				
	            				TaskAssigner.current.tasks.get(j).setType((String) data[row][0]);
	            				
            					System.out.println(TaskAssigner.current.tasks.get(j).isOverridden());
	            				if(!TaskAssigner.current.tasks.get(j).isOverridden())
	            				{
	            					if(TaskAssigner.current.tasks.get(j).getAssignedMember() != null)
	            					{
		            					int work = TaskAssigner.current.tasks.get(j).getAssignedMember().getWork();
		            					work -= TaskAssigner.current.tasks.get(j).getWorkValue();
		            					work += (int) data[row][2];
		            					TaskAssigner.current.tasks.get(j).getAssignedMember().setWork(work);
	            					}
	            					TaskAssigner.current.tasks.get(j).setWorkValue((int) data[row][2]);
	            				}
	            			}
	            		}
	            	}
	            	
	            	for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
	            	{
	            		Task task = TaskAssigner.current.tasks.get(i);
	            		if(tasksObj.contains(task.getId()) && !tasks.contains(task.getId()))
	            			task.setType(null);
	            	}
           		}
	            if(col == 2)
	            {
	            	
	            	if(value.equals(""))
 	            	{
 	            		data[row][1] = 0;
 	            		value="0";
 	            	}
	            	
	            	TaskAssigner.current.tasktypes.get(row).setWorkValue(Integer.parseInt((String) value));
	            	
	            	for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
	            	{
	            		if(TaskAssigner.current.tasks.get(i).getType().equals(data[row][0]) && !TaskAssigner.current.tasks.get(i).isOverridden())
	            		{
	            			TaskAssigner.current.tasks.get(i).setWorkValue(Integer.parseInt((String) value));
	            			int work = TaskAssigner.current.tasks.get(i).getAssignedMember().getWork();
	            			TaskAssigner.current.tasks.get(i).getAssignedMember().setWork(work - (int) obj + Integer.parseInt((String) value));
	            		}
	            	}
	            }
	            if(value != obj && metrics.getStringBounds((String) value, null).getWidth() > width)
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
            	JOptionPane.showMessageDialog(TaskAssigner.taskTypesPane, "Changed value does not match data type", "Error", JOptionPane.ERROR_MESSAGE);
            	data[row][col] = obj;
            	e.printStackTrace();
           }
           TaskAssigner.current.setTaskValues();
           TaskAssigner.taskTypesPane.repaint();
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     *
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
    }*/
 
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
