package components;
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.util.ArrayList;
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
import javax.swing.table.TableCellRenderer;

/** 
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class MembersTable extends JPanel implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	String[] columnNames;
    Object[][] data;
 
    public static JTable table;
    static Font font = new Font("Verdana", Font.PLAIN, 10);
    public static FontMetrics metrics = new FontMetrics(font) {
		private static final long serialVersionUID = 7044476510869677223L;};
    
    public MembersTable(String[] columnNames, Object[][] data)
    {
        super(new GridLayout(1,0));
 
        this.columnNames = columnNames;
        this.data = data;
        
        table = new JTable(new MyTableModel())
        {
					private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
                Component returnComp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = new Color(252,242,206);
                Color whiteColor = Color.WHITE;
                if (!returnComp.getBackground().equals(getSelectionBackground()))
                {
                	Color bg = (TaskAssigner.current.workingMembers.contains(TaskAssigner.current.members.get(row)) ? alternateColor : whiteColor);
                    returnComp .setBackground(bg);
                    bg = null;
                }
                return returnComp;
        		}
		};
        table.setFillsViewportHeight(true);
        
        table.setLocation(TaskAssigner.current.frame.getX()+416, TaskAssigner.current.frame.getY());
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        
        int n = 0;
        
        for(int i=0; i<columnNames.length; i++)
        {
            int width = table.getColumnModel().getColumn(i).getWidth();
            for(int j=0; j<data.length; j++)
            {
	            if(i<3 && metrics.getStringBounds((String) data[j][i], null).getWidth() > width)
	        		width = (int) metrics.getStringBounds((String) data[j][i], null).getWidth();
	        	else if(i==3 && metrics.getStringBounds(data[j][i].toString(), null).getWidth() > width)
	        		width = (int) metrics.getStringBounds(data[j][i].toString(), null).getWidth();
	            else if(i==4 && metrics.getStringBounds(data[j][i].toString(), null).getWidth() > width)
	            	width = (int) metrics.getStringBounds(data[j][i].toString(), null).getWidth();
	          
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
        	
    	   int width = table.getColumnModel().getColumn(col).getPreferredWidth();
           Object obj = data[row][col];
           data[row][col] = value;
           fireTableCellUpdated(row, col);
           a: try
           {
	            if(col == 0)
	            {
	            	for(int i=0; i<TaskAssigner.current.members.size(); i++)
    				{
    					if(TaskAssigner.current.members.get(i).getName().equals(value))
    					{
    						JOptionPane.showMessageDialog(TaskAssigner.membersPane, "A member cannot have the same"
	            					+ " name as another member.", "Error",
	            					JOptionPane.ERROR_MESSAGE);

	            				data[row][col] = obj;
	                        	break a;
    					}
    				}
	            	
	            	if(((CharSequence) value).chars().allMatch(Character::isWhitespace))
    				{
	            		JOptionPane.showMessageDialog(TaskAssigner.membersPane, "A member name cannot"
            					+ " be whitespace.", "Error",
            					JOptionPane.ERROR_MESSAGE);
            			break a;
    				}
	            	
		            TaskAssigner.current.removeMemberCombo.removeItem(TaskAssigner.current.members.get(row).getName());
		            TaskAssigner.current.removeMemberCombo.insertItemAt((String) value, row);
		            
		            TaskAssigner.current.memberCombo.removeItem(TaskAssigner.current.members.get(row).getName());
		            TaskAssigner.current.memberCombo.insertItemAt((String) value, row);
		            
		            TaskAssigner.current.members.get(row).setName((String) value);
		            
		            for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
		            {
		            	if(TaskAssigner.current.tasks.get(i).getAssignedMember().getName().equals(obj))
		            	{
		            		TaskAssigner.current.tasks.get(i).setAssignedMember(TaskAssigner.current.members.get(row));
		            	}
		            }
	            }
	            if(col == 1)
	            {
	            	value = ((String) value).replaceAll("\\s+","");
	            	
	            	List<String> list = Arrays.asList(((String) value).split(","));
	            	      	
	            	for(int i=0; i<list.size(); i++)
	            	{
	            		if(!TaskAssigner.current.taskTypeNames.contains(list.get(i)))
	            			throw new Exception();
	            	}
	            	         	
	            	ArrayList<Task> assignments = TaskAssigner.current.members.get(row).getAssignments();
	            	ArrayList<String> authorizations = new ArrayList<String>(list);
	            	for(int i=0; i<assignments.size(); i++)
	            	{
	            		if(!authorizations.contains(assignments.get(i).getType()))
	            		{
	            			Object[] options = new Object[]{"Ok", "Undo"};
	            			int x = JOptionPane.showOptionDialog(TaskAssigner.membersPane, "This member's new authorizations "
	            					+ "will make him unauthorized to work on one or more of his or her current assignments", "Warning", JOptionPane.DEFAULT_OPTION,
	            					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	            			if(x==1)
	            			{
	                        	data[row][col] = obj;
	                        	break a;
	            			}
	            			break;
	            		}
	            	}
	            	TaskAssigner.current.members.get(row).clearAuthorizations();
	            	TaskAssigner.current.members.get(row).setAuthorizations(authorizations);
	            }
	            if(col == 2)
	            {
	            	value = ((String) value).replaceAll("\\s+","");
	            	
	            	if(((CharSequence) value).chars().allMatch(Character::isWhitespace))
	            	{
	            		for(int i=0; i<TaskAssigner.current.members.size(); i++)
	            		{
	            			if(TaskAssigner.current.members.get(i).getName().equals(data[row][0]))
	            			{
	            				TaskAssigner.current.members.get(i).setWork(0);
	            				data[row][3]=0;
	            				for(int j=0; j<TaskAssigner.current.members.get(i).getAssignments().size(); j++)
	            				{
	            					TaskAssigner.current.members.get(i).getAssignments().get(j).setAssignedMember(null);
	            				}
	            				TaskAssigner.current.members.get(i).clearAssignments();
	            			}
	            		}
	            		break a;
	            	}
	            	
	            	List<String> assignments = Arrays.asList(((String) value).split(","));
	            	List<String> assignmentsObj = Arrays.asList(((String) obj).split(","));
	            	
	            	System.out.println(value);
	            	if(!value.equals("") && value!=null)
	            	{
		            	loop: for(int i=0; i<assignments.size(); i++)
		            	{
		            		for(int j=0; j<TaskAssigner.current.tasks.size(); j++)
		            		{
		            			if(TaskAssigner.current.tasks.get(j).getId().equals(assignments.get(i)))
			            			continue loop;
		            		}
	            			throw new Exception();
		            	}
	            	}
	            		            	
	            	boolean b = true;

	            	ArrayList<String> authorizations = TaskAssigner.current.members.get(row).getAuthorizations();
	            	loop: for(int i=0; i<assignments.size(); i++)
	            	{
	            		for(int j=0; j<TaskAssigner.current.tasks.size(); j++)
	            		{
	            			if(TaskAssigner.current.tasks.get(j).getId().equals(assignments.get(i)))
	            			{
	            				if(TaskAssigner.current.tasks.get(j).getAssignedMember() != null && TaskAssigner.current.tasks.get(j).getAssignedMember().getName() != data[row][0])
	            				{
			            			JOptionPane.showMessageDialog(TaskAssigner.membersPane, "A member is already working on"
			            					+ " one or more of the newly assigned assignments.", "Error", JOptionPane.ERROR_MESSAGE);
			            			data[row][col] = obj;
			            			break a;
	            				}
			            		if(b && !authorizations.contains(TaskAssigner.current.tasks.get(j).getType()))
			            		{
			            			Object[] options = new Object[]{"Ok", "Undo"};
			            			int x = JOptionPane.showOptionDialog(TaskAssigner.membersPane, "This member is not authorized"
			            					+ " to work on one or more of his or her new assignments.", "Warning", JOptionPane.DEFAULT_OPTION,
			            					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			            			if(x==1)
			            			{
			                        	data[row][col] = obj;
			                        	break a;
			            			}
			            			b = false;
			            			if(!b) break loop;
			            		}
	            			}
	            		}
	            	}
	            	
	            	for(int i=0; i<TaskAssigner.current.tasks.size(); i++)
	            	{
	            		Task task = TaskAssigner.current.tasks.get(i);
	            		if(assignmentsObj.contains(task.getId()) && !assignments.contains(task.getId()))
	            			task.setAssignedMember(null);
	            	}
	            	
	            	TaskAssigner.current.members.get(row).clearAssignments();
	            	TaskAssigner.current.members.get(row).setWork(0);
	            		           
	            	for(int i=0; i<assignmentsObj.size(); i++)
	            	{
	            		for(int j=0; j<TaskAssigner.current.tasks.size(); j++)
	            		{
	            			if(TaskAssigner.current.tasks.get(j).getId().equals(assignmentsObj.get(i)))
	            				TaskAssigner.current.tasks.get(j).setAssignedMember(null);
	            		}
	            	}
	            	
	            	for(int i=0; i<assignments.size(); i++)
	            	{
	            		for(int j=0; j<TaskAssigner.current.tasks.size(); j++)
	            		{
	            			System.out.println(TaskAssigner.current.tasks.get(j).getId()+ " "+ assignments.get(i));
	            			if(TaskAssigner.current.tasks.get(j).getId().equals(assignments.get(i)))
	            			{
	            				TaskAssigner.current.members.get(row).addAssignments(TaskAssigner.current.tasks.get(j));
	            				TaskAssigner.current.tasks.get(j).setAssignedMember(TaskAssigner.current.members.get(row));
	            				break;
	            			}
	            			if(j==TaskAssigner.current.tasks.size()-1)
	            				throw new Exception();
	            		}
	            	}
	            	
	            	data[row][3] = TaskAssigner.current.members.get(row).getWork();
	            }
	            if(col == 3)
	            {
	            	if(value.equals(""))
	            	{
	            		data[row][3] = 0;
	            		value="0";
	            	}
		            TaskAssigner.current.members.get(row).setWork(Integer.parseInt((String) value));
	            }
	            if(col==4)
	            {
	            	if(value.equals(""))
	            	{
	            		data[row][4] = 0;
	            		value="0";
	            	}
	            	if(0<Double.parseDouble((String) value) && Double.parseDouble((String) value)<=1)
			            TaskAssigner.current.members.get(row).setProportionOfWork(Double.parseDouble((String) value));
    				else
    				{
    					JOptionPane.showMessageDialog(TaskAssigner.membersPane, "Proportion of work"
            					+ " must be a proportion.", "Error", JOptionPane.ERROR_MESSAGE);
            			data[row][col] = obj;
    				}
	            }
	            if(metrics.getStringBounds((String) value, null).getWidth() > width)
	            {
	        		int width1 = (int) metrics.getStringBounds((String) value, null).getWidth();
	        		table.getColumnModel().getColumn(col).setMinWidth(width1);
	                table.getColumnModel().getColumn(col).setWidth(width1);
	                Dimension d = table.getPreferredScrollableViewportSize();
	                d.setSize(width1,d.getHeight());
	                table.setPreferredScrollableViewportSize(d);
	            }
           }    
           catch(Exception e)
           {
            	JOptionPane.showMessageDialog(null, "Changed value does not match data type", "Error", JOptionPane.ERROR_MESSAGE);
            	data[row][col] = obj;
            	e.printStackTrace();
           }
           TaskAssigner.current.setTaskValues();
           TaskAssigner.membersPane.repaint();
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
				
			}
        });
    }
}
