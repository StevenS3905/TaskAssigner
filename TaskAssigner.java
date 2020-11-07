package components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.opencsv.CSVWriter;

public class TaskAssigner implements Serializable {

	private static final long serialVersionUID = 1L;

	static File file = new File("C:\\Users\\steve\\Desktop\\Robotics");
	static String sep = File.separator;

	static TaskAssigner original;
	public static TaskAssigner current;
	public static ArrayList<TaskAssigner> frames = new ArrayList<TaskAssigner>();

	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
	static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	static DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("HH:mm");

	public String date;
	public String time;
	
	static Font font = new Font("Verdana", Font.PLAIN, 10);
    public static FontMetrics metrics = new FontMetrics(font) {
		private static final long serialVersionUID = 7044476510869677223L;};
	
	JRadioButton autoButton = new JRadioButton("Auto");
    JRadioButton manualButton = new JRadioButton("Manual");
    //Group the radio buttons.
    static ButtonGroup group = new ButtonGroup();

	public ArrayList<Task> unassigned;
	public ArrayList<Task> tasks;
	public Vector<TaskType> tasktypes;
	public Vector<TeamMember> members;
	public Vector<TeamMember> workingMembers;
	public Vector<JCheckBox> membersCheckboxes;
	public Vector<JCheckBox> authorizationCheckboxes;
	public Vector<String> memberNames;
	public Vector<String> taskTypeNames;
	public Vector<String> taskIds;

	public Integer count;
	public JFrame datefrm;
	public JFrame memberfrm;
	public JFrame frame;
	public JFrame data;
	public static TaskTypesTable taskTypesPane;
	public static TasksTable2 tasksPane;
	public static MembersTable membersPane;

	public JComboBox<String> memberCombo;
	public JComboBox<String> addTaskCombo;
	public JComboBox<String> removeTaskTypeCombo;
	public JComboBox<String> removeTaskCombo;
	public JComboBox<String> removeMemberCombo;
	public JComboCheckBox addAuthorizationsCombo;
	public JTextField taskWorkValueField;
	public JTextField addTaskIDField;

	static String folder = sep + "TaskAssigner" + sep + "logs";
	static String javaFolder = sep + "TaskAssigner" + sep + "javaLogs";

	public TaskAssigner(Vector<TaskType> tasktypes1, Vector<TeamMember> members1,
			Vector<TeamMember> workingMembers1, ArrayList<Task> tasks1,
			ArrayList<Task> unassigned1, String date1, String time1, int operation, Rectangle rect, boolean b)
			throws IOException, ClassNotFoundException {
		current = this;
		
		frames.add(this);
		
		this.date = date1;
		this.time = time1;
		this.count = 1;

		this.datefrm = new JFrame();
		this.memberfrm = new JFrame();
		this.frame = new JFrame("TaskAssigner " + current.date + " " + current.time);
		this.data = new JFrame("Data " + current.date + current.time);

		frame.setDefaultCloseOperation(operation);

		this.tasktypes = tasktypes1;
		this.members = members1;
		this.workingMembers = workingMembers1;
		this.tasks = tasks1;
		this.unassigned = unassigned1;

		this.authorizationCheckboxes = new Vector<JCheckBox>();
		this.memberCombo = new JComboBox<String>();
		this.addTaskCombo = new JComboBox<String>();
		this.removeTaskTypeCombo = new JComboBox<String>();
		this.removeTaskCombo = new JComboBox<String>();
		this.removeMemberCombo = new JComboBox<String>();
		this.addAuthorizationsCombo = new JComboCheckBox(authorizationCheckboxes);
		this.taskWorkValueField = new JTextField();
		this.addTaskIDField = new JTextField();

		this.membersCheckboxes = new Vector<JCheckBox>();
		this.memberNames = new Vector<String>();
		this.taskTypeNames = new Vector<String>();
		this.taskIds = new Vector<String>();

		
		for(int i=0; i<members.size(); i++)
		{
			members.get(i).setNonpermissions(new ArrayList<String>());
		}
		
		JLabel addTaskTypeBlockLabel = new JLabel("Add Types of Tasks");
		addTaskTypeBlockLabel.setBounds(5, 0, 125, 30);

		JLabel addTaskTypeFieldLabel = new JLabel("Name of Type:", SwingConstants.RIGHT);
		addTaskTypeFieldLabel.setBounds(5, 25, 125, 30);

		JTextField addTaskTypeField = new JTextField();
		addTaskTypeField.setBounds(140, 30, 100, 20);

		JLabel workValueLabel = new JLabel("Work Value:", SwingConstants.RIGHT);
		workValueLabel.setBounds(5, 50, 125, 30);

		JTextField workValueField = new JTextField();
		workValueField.setBounds(140, 55, 100, 20);

		JButton addTaskTypeButton = new JButton("Enter");
		addTaskTypeButton.setBounds(280, 40, 80, 25);

		JLabel addMemberBlockLabel = new JLabel("Add Member");
		addMemberBlockLabel.setBounds(5, 100, 125, 30);

		JLabel addNameLabel = new JLabel("Name:", SwingConstants.RIGHT);
		addNameLabel.setBounds(5, 125, 125, 30);

		JTextField nameField = new JTextField();
		nameField.setBounds(140, 130, 100, 20);

		JLabel proportionOfWorkLabel = new JLabel("Proportion of work:", SwingConstants.RIGHT);
		proportionOfWorkLabel.setBounds(5, 150, 125, 30);

		JTextField proportionOfWorkField = new JTextField("1");
		proportionOfWorkField.setBounds(140, 155, 100, 20);

		JLabel addAuthorizationsLabel = new JLabel("Add Authorizations:", SwingConstants.RIGHT);
		addAuthorizationsLabel.setBounds(5, 175, 125, 30);

		addAuthorizationsCombo.setBounds(140, 180, 100, 20);

		JButton addMemberButton = new JButton("Enter");
		addMemberButton.setBounds(280, 150, 80, 25);

		JLabel removeTaskTypeLabel = new JLabel("Remove Type of Task:", SwingConstants.RIGHT);
		removeTaskTypeLabel.setBounds(5, 225, 125, 30);

		removeTaskTypeCombo.setBounds(140, 230, 100, 20);

		JButton removeTaskTypeButton = new JButton("Remove");
		removeTaskTypeButton.setBounds(280, 225, 80, 26);

		JLabel removeMemberLabel = new JLabel("Remove Member:", SwingConstants.RIGHT);
		removeMemberLabel.setBounds(5, 267, 125, 30);

		removeMemberCombo.setBounds(140, 272, 100, 20);

		JButton removeMemberButton = new JButton("Remove");
		removeMemberButton.setBounds(280, 267, 80, 26);

		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(16, 312, 112, 45);

		JButton save1Button = new JButton("Save");
		save1Button.setBounds(144, 312, 112, 45);

		JButton fileConfigButton = new JButton(
				"<html>" + "<center>" + "File" + "<br>" + "Config" + "</center>" + "</html");
		fileConfigButton.setBounds(272, 312, 112, 45);
		
		JLabel assignmentMethodLabel = new JLabel("<html> Assignment Method </html", SwingConstants.CENTER);
		assignmentMethodLabel.setBounds(250, 5, 145, 30);

		autoButton.setBounds(280, 30, 80, 15);
	    manualButton.setBounds(280, 50, 80, 15);

		JLabel addTaskBlockLabel = new JLabel("<html> Add Tasks </html", SwingConstants.CENTER);
		addTaskBlockLabel.setBounds(140, 10, 100, 30);

		JLabel addTaskIDLabel = new JLabel("<html> Task ID: </html", SwingConstants.RIGHT);
		addTaskIDLabel.setBounds(5, 35, 125, 30);

		addTaskIDField.setBounds(140, 40, 100, 20);

		JLabel memberLabel = new JLabel("<html> Assigned Member: </html", SwingConstants.RIGHT);
		memberLabel.setBounds(5, 60, 125, 30);

		memberCombo.setBounds(140, 65, 100, 20);

		JLabel taskWorkValueLabel = new JLabel("<html> Work Value: </html", SwingConstants.RIGHT);
		taskWorkValueLabel.setBounds(5, 85, 125, 30);

		taskWorkValueField.setBounds(140, 90, 100, 20);

		JLabel addTaskTypeComboLabel = new JLabel("<html> Task Type: </html", SwingConstants.RIGHT);
		addTaskTypeComboLabel.setBounds(5, 110, 125, 30);

		addTaskCombo.setBounds(140, 115, 100, 20);

		JButton addTaskButton = new JButton("<html> Enter </html");
		addTaskButton.setBounds(280, 85, 80, 25);

		JLabel removeTaskLabel = new JLabel("<html> Remove Task: </html", SwingConstants.RIGHT);
		removeTaskLabel.setBounds(5, 145, 125, 30);
		
		removeTaskCombo.setBounds(140, 150, 100, 20);
		
		JButton removeTaskButton = new JButton("<html> Remove </html");
		removeTaskButton.setBounds(280, 145, 80, 26);
		
		JLabel setWorkingMembersLabel = new JLabel(
				"<html>" + "<center>" + "Set Working" + "<br>" + "Members:" + "</center>" + "</html",
				SwingConstants.RIGHT);
		setWorkingMembersLabel.setBounds(5, 180, 125, 40);
		
		JComboCheckBox setWorkingMembersCombo = new JComboCheckBox(membersCheckboxes);
		setWorkingMembersCombo.setBounds(140, 190, 100, 20);
		
		JButton setWorkingMembersButton = new JButton("<html> Set </html");
		setWorkingMembersButton.setBounds(280, 185, 80, 26);

		JButton showAssignmentsButton = new JButton(
				"<html>" + "<center>" + "Show" + "<br>" + "Assignments" + "</center>" + "</html");
		showAssignmentsButton.setBounds(16, 235, 112, 45);

		JButton clearTasksButton = new JButton("<html> Clear Tasks </html");
		clearTasksButton.setBounds(144, 235, 112, 45);

		JButton clearWorkloadsButton = new JButton(
				"<html>" + "<center>" + "Clear" + "<br>" + "Workloads" + "</center>" + "</html");
		clearWorkloadsButton.setBounds(272, 235, 112, 45);

		JButton showTaskTypesButton = new JButton(
				"<html>" + "<center>" + "Show" + "<br>" + "Task Types" + "</center>" + "</html");
		showTaskTypesButton.setBounds(16, 292, 112, 45);

		JButton showTasksButton = new JButton("<html> Show Tasks </html");
		showTasksButton.setBounds(144, 292, 112, 45);

		JButton showMembersButton = new JButton(
				"<html>" + "<center>" + "Show" + "<br>" + "Members" + "</center>" + "</html");
		showMembersButton.setBounds(272, 292, 112, 45);

		JButton saveButton = new JButton("<html> Save </html");
		saveButton.setBounds(144, 349, 112, 45);

		JButton instancesButton = new JButton("<html> Instances </html");
		instancesButton.setBounds(16, 349, 112, 45);

		JButton saveInstanceButton = new JButton(
				"<html>" + "<center>" + "Save" + "<br>" + "Instance" + "</center>" + "</html");
		saveInstanceButton.setBounds(272, 349, 112, 45);

		JButton editValuesButton = new JButton("<html> Edit Values </html");
		editValuesButton.setBounds(144, 406, 112, 45);

		JButton timeConfigButton = new JButton(
				"<html>" + "<center>" + "Date & Time" + "<br>" + "Config" + "</center>" + "</html");
		timeConfigButton.setBounds(272, 406, 112, 45);

		JButton saveAssignmentsButton = new JButton(
				"<html>" + "<center>" + "Save Assignments" + "</center>" + "</html");
		saveAssignmentsButton.setBounds(16, 406, 112, 45);
		
		JButton setTemporaryPermissionsButton = new JButton("<html>" + "<center>" + "Temporary Permissions" + "</center>" + "</html");
		setTemporaryPermissionsButton.setBounds(144, 463, 112, 45);

		frame.add(addTaskBlockLabel);
		frame.add(addTaskIDLabel);
		frame.add(addTaskIDField);
		frame.add(memberLabel);
		frame.add(memberCombo);
		frame.add(taskWorkValueLabel);
		frame.add(taskWorkValueField);
		frame.add(addTaskTypeComboLabel);
		frame.add(addTaskCombo);
		frame.add(addTaskButton);
		frame.add(removeTaskLabel);
		frame.add(removeTaskCombo);
		frame.add(removeTaskButton);
		frame.add(setWorkingMembersButton);
		frame.add(setWorkingMembersLabel);
		frame.add(setWorkingMembersCombo);
		frame.add(showAssignmentsButton);
		frame.add(clearTasksButton);
		frame.add(clearWorkloadsButton);
		frame.add(showTaskTypesButton);
		frame.add(showTasksButton);
		frame.add(showMembersButton);
		frame.add(saveButton);
		frame.add(editValuesButton);
		frame.add(saveInstanceButton);
		frame.add(instancesButton);
		frame.add(timeConfigButton);
		frame.add(saveAssignmentsButton);
		frame.add(assignmentMethodLabel);
		frame.add(autoButton);
		frame.add(manualButton);
		frame.add(setTemporaryPermissionsButton);

		data.add(addTaskTypeBlockLabel);
		data.add(addTaskTypeFieldLabel);
		data.add(addTaskTypeField);
		data.add(workValueLabel);
		data.add(workValueField);
		data.add(addTaskTypeButton);

		data.add(addMemberBlockLabel);
		data.add(addNameLabel);
		data.add(nameField);
		data.add(proportionOfWorkLabel);
		data.add(proportionOfWorkField);
		data.add(addAuthorizationsLabel);
		data.add(addMemberButton);
		data.add(addAuthorizationsCombo);

		data.add(removeTaskTypeLabel);
		data.add(removeTaskTypeCombo);

		data.add(removeMemberLabel);
		data.add(removeMemberCombo);

		data.add(removeTaskTypeButton);
		data.add(removeMemberButton);
		data.add(resetButton);
		data.add(fileConfigButton);
		data.add(save1Button);

		frame.setBounds(rect);
		data.setBounds((int) frame.getX() + 426, (int) frame.getY(), 416, 404);

		frame.setLayout(null);
		frame.setVisible(true);
		instancesButton.setEnabled(b);
		
	    group.add(autoButton);
	    group.add(manualButton);
	    
	    autoButton.setText("Auto");
	    manualButton.setText("Manual");
	    autoButton.setSelected(true);

		addTaskTypeField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskTypeButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskTypeButton.doClick();
			}

		});

		workValueField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskTypeButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskTypeButton.doClick();
			}

		});

		addTaskIDField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

		});

		memberCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

		});

		taskWorkValueField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

		});

		addTaskCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addTaskButton.doClick();
			}

		});

		nameField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addMemberButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addMemberButton.doClick();
			}

		});

		proportionOfWorkField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addMemberButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addMemberButton.doClick();
			}

		});

		addAuthorizationsCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addMemberButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addMemberButton.doClick();
			}

		});

		removeTaskTypeCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeTaskTypeButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeTaskTypeButton.doClick();
			}

		});

		removeMemberCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeMemberButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeMemberButton.doClick();
			}

		});

		removeTaskCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeTaskButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeTaskButton.doClick();
			}

		});

		setWorkingMembersCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeTaskButton.doClick();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					removeTaskButton.doClick();
			}

		});

		addTaskTypeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t: try {
					Integer workValue = Integer.parseInt(workValueField.getText());

					String temptxt = addTaskTypeField.getText();
					temptxt = temptxt.replaceAll("\\s+", "");

					addTaskTypeField.setText("");
					workValueField.setText("");

					for (int i = 0; i < tasktypes.size(); i++) {
						if (tasktypes.get(i).getName().equals(temptxt)) {
							JOptionPane.showMessageDialog(frame,
									"A task type cannot be" + " named the same as another task type.", "Error",
									JOptionPane.ERROR_MESSAGE);

							break t;
						}
					}

					if (temptxt.chars().allMatch(Character::isWhitespace)) {
						JOptionPane.showMessageDialog(frame, "A task type name cannot" + " be whitespace.", "Error",
								JOptionPane.ERROR_MESSAGE);
						break t;
					} else {
						JCheckBox checkbox = new JCheckBox(temptxt);
						TaskType temp = new TaskType(temptxt, workValue, 1);

						removeTaskTypeCombo.addItem(temptxt);
						addTaskCombo.addItem(temptxt);

						tasktypes.add(temp);
						authorizationCheckboxes.add(checkbox);

						if (tasktypes.size() == 1)
							taskWorkValueField.setText(workValue.toString());
					}
					setTaskValues();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(frame, "Work value must be an integer.", "Error",
							JOptionPane.ERROR_MESSAGE);
					workValueField.setText("");
				}
			}
		});

		addTaskCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				setTaskValues();
			}
		});

		addTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t: try {
					Object taskWorkValue = null;
					boolean overridden = false;

					taskWorkValue = Integer.parseInt(taskWorkValueField.getText());
					for (int i = 0; i < tasktypes.size(); i++) {
						if (tasktypes.get(i).getName().equals((String) addTaskCombo.getSelectedItem())) {
							if (Integer.parseInt(taskWorkValueField.getText()) != tasktypes.get(i).getWorkValue())
								overridden = true;
						}
					}

					String type = (String) addTaskCombo.getSelectedItem();
					String id = (String) addTaskIDField.getText();
					id = id.replaceAll("\\s+", "");

					addTaskIDField.setText("");

					for (int i = 0; i < tasks.size(); i++) {
						if (tasks.get(i).getId().equals(id)) {
							JOptionPane.showMessageDialog(frame, "A task cannot have the same" + " ID as another task.",
									"Error", JOptionPane.ERROR_MESSAGE);

							break t;
						}
					}

					if (id.chars().allMatch(Character::isWhitespace)) {
						JOptionPane.showMessageDialog(frame, "A task ID cannot" + " be whitespace.", "Error",
								JOptionPane.ERROR_MESSAGE);
						break t;
					}

					TeamMember assignedMember = null;

					for (int i = 0; i < members.size(); i++) {
						if (members.get(i).getName().equals(memberCombo.getSelectedItem())) {
							if (!members.get(i).getAuthorizations().contains(type)) {
								Object[] options = new Object[] { "Ok", "Undo" };
								int x = JOptionPane.showOptionDialog(frame,
										"This member is not authorized" + " to work on this assignment.", "Warning",
										JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
										options[0]);
								if (x == 1) {
									break t;
								}
							}
							assignedMember = members.get(i);
						}
					}

					removeTaskCombo.addItem(id);

					Task temp;
					temp = new Task(type, id, (int) taskWorkValue, assignedMember, overridden);

					tasks.add(temp);

					if (assignedMember != null) {
						assignedMember.addAssignments(temp);
						temp.setAssignedMember(assignedMember);
						JOptionPane.showMessageDialog(frame,
								temp.getId() + " is assigned to " + assignedMember.getName(), "Assignment",
								JOptionPane.PLAIN_MESSAGE);
					} else
						JOptionPane.showMessageDialog(frame, temp.getId() + " could not be assigned.", "Assignment",
								JOptionPane.PLAIN_MESSAGE);

					for (int i = 0; i < tasktypes.size(); i++) {
						if (tasktypes.get(i).getName().equals(type))
							tasktypes.get(i).increaseCount();
					}

					setTaskValues();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(frame, "Work value must either be a null or an integer.", "Error",
							JOptionPane.ERROR_MESSAGE);
					taskWorkValueField.setText("");
				}
			}
		});

		addMemberButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t: try {
					if (nameField.getText().toLowerCase().equals("unassigned")) {
						JOptionPane.showMessageDialog(frame, "A member cannot be named ''Unassigned''", "Error",
								JOptionPane.ERROR_MESSAGE);
						nameField.setText("");
						proportionOfWorkField.setText("1");
						break t;
					}

					double proportionOfWork = Double.parseDouble(proportionOfWorkField.getText());

					if (proportionOfWork == 0) {
						JOptionPane.showMessageDialog(frame, "Proportion of work cannot be zero.", "Error",
								JOptionPane.ERROR_MESSAGE);
						nameField.setText("");
						proportionOfWorkField.setText("1");
						break t;
					}

					String temptxt = nameField.getText();

					nameField.setText("");

					proportionOfWorkField.setText("1");

					for (int i = 0; i < members.size(); i++) {
						if (members.get(i).getName().equals(temptxt)) {
							JOptionPane.showMessageDialog(frame,
									"A member cannot have the same" + " name as another member.", "Error",
									JOptionPane.ERROR_MESSAGE);

							break t;
						}
					}

					if (temptxt.chars().allMatch(Character::isWhitespace)) {
						JOptionPane.showMessageDialog(frame, "A member name cannot" + " be whitespace.", "Error",
								JOptionPane.ERROR_MESSAGE);
						break t;
					}

					if (0 < proportionOfWork && proportionOfWork <= 1) {
						removeMemberCombo.addItem(temptxt);
						memberCombo.addItem(temptxt);

						ArrayList<String> authorizations = new ArrayList<String>();

						for (int i = 0; i < addAuthorizationsCombo.getItemCount(); i++) {
							if (((JCheckBox) addAuthorizationsCombo.getItemAt(i)).isSelected()) {
								((JCheckBox) addAuthorizationsCombo.getItemAt(i)).setSelected(false);
								authorizations.add(((JCheckBox) addAuthorizationsCombo.getItemAt(i)).getText());
							}
						}

						TeamMember temp = new TeamMember(temptxt, authorizations, new ArrayList<Task>(), 0,
								proportionOfWork, new ArrayList<String>());
						workingMembers.add(temp);
						members.add(temp);
					} else
						throw new Exception();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(frame, "Proportion of Work Must Be a Decimal", "Error",
							JOptionPane.ERROR_MESSAGE);
					proportionOfWorkField.setText("");
				}
				setTaskValues();
			}
		});

		removeTaskTypeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tasktype = (String) removeTaskTypeCombo.getSelectedItem();

				for (int i = 0; i < authorizationCheckboxes.size(); i++) {
					if (authorizationCheckboxes.get(i).getText().equals(tasktype))
						authorizationCheckboxes.remove(i);
				}

				for (int i = 0; i < tasktypes.size(); i++) {
					if (tasktypes.get(i).getName().equals(tasktype))
						tasktypes.remove(i);
				}

				for (int i = 0; i < tasks.size(); i++) {
					if (tasks.get(i).getType().equals(tasktype))
						tasks.get(i).setType(null);
				}

				for (int i = 0; i < members.size(); i++) {
					if (members.get(i).getAuthorizations().contains(tasktype)) {
						ArrayList<String> arr = members.get(i).getAuthorizations();
						arr.remove(tasktype);
						members.get(i).setAuthorizations(arr);
					}
				}

				removeTaskTypeCombo.removeItem(tasktype);
				addTaskCombo.removeItem(tasktype);
				setTaskValues();
			}
		});

		removeTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = (String) removeTaskCombo.getSelectedItem();
				removeTaskCombo.removeItem(id);
				Task task = null;
				for (int i = 0; i < tasks.size(); i++) {
					if (tasks.get(i).getId().equals(id)) {
						task = tasks.get(i);
						tasks.remove(i);
					}
				}

				for (int j = 0; j < members.size(); j++) {
					if (members.get(j).getAssignments().contains(task)) {
						members.get(j).getAssignments().remove(task);
						members.get(j).setWork(members.get(j).getWork() - task.getWorkValue());
					}
				}
				setTaskValues();
			}
		});

		setWorkingMembersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				workingMembers.clear();
				for (int i = 0; i < setWorkingMembersCombo.getItemCount(); i++) {
					if (setWorkingMembersCombo.getItemAt(i).isSelected()) {
						for (int j = 0; j < members.size(); j++) {
							if (members.get(j).getName().equals(setWorkingMembersCombo.getItemAt(i).getText())) {
								workingMembers.add(members.get(j));
							}
						}
					}
				}
				setTaskValues();
			}
		});

		removeMemberButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String) removeMemberCombo.getSelectedItem();
				removeMemberCombo.removeItem(name);
				memberCombo.removeItem(name);
				TeamMember member = null;
				for (int i = 0; i < members.size(); i++) {
					if (members.get(i).getName().equals(name)) {
						member = members.get(i);
						members.remove(i);
						workingMembers.remove(i);
					}
				}
				for (int i = 0; i < tasks.size(); i++) {
					if (tasks.get(i).getAssignedMember().equals(member)) {
						tasks.get(i).setAssignedMember(null);
					}
				}
				setTaskValues();
			}
		});

		showAssignmentsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unassigned.clear();
				for (int i = 0; i < tasks.size(); i++) {
					if (tasks.get(i).getAssignedMember() == null) {
						unassigned.add(tasks.get(i));
					}
				}

				String[][] arr = getAssignments();

				String[] columnNames = new String[members.size() + 2];
				columnNames[0] = "Row Number";
				for (int i = 1; i < members.size() + 1; i++) {
					columnNames[i] = members.get(i - 1).getName() + " " + members.get(i - 1).getWork();
				}
				columnNames[columnNames.length - 1] = "Unassigned";

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JFrame frame = new JFrame("Table");
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						// Create and set up the content pane.
						TableDemo newContentPane = new TableDemo(columnNames, arr);
						newContentPane.setOpaque(true);
						// content panes must be opaque
						frame.setContentPane(newContentPane);

						// Display the window.
						frame.pack();
						frame.setVisible(true);
					}
				});
			}
		});

		clearTasksButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want clear all tasks?\n" + " hit save to finalize this action",
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (x == 0) 
				{
					tasks.clear();
					removeTaskCombo.removeAllItems();
					for (int i = 0; i < workingMembers.size(); i++) {
						workingMembers.get(i).clearAssignments();
						workingMembers.get(i).setWork(0);
					}
					for (int i = 0; i < tasktypes.size(); i++) {
						tasktypes.get(i).setCount(0);
					}
					setTaskValues();
				}
			}
		});

		clearWorkloadsButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int x = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to set everyone's workloads to zero?\n" + " hit save to finalize this action",
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (x == 0)
				{
					for (int i = 0; i < workingMembers.size(); i++) {
						workingMembers.get(i).setWork(0);
					}
					setTaskValues();
				}
			}
		});

		showTaskTypesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] columnNames = { "Task Type", "Tasks", "Work Value" };
				Object[][] data = new Object[tasktypes.size()][3];
				for (int i = 0; i < tasktypes.size(); i++) {
					data[i][0] = tasktypes.get(i).getName();
					data[i][2] = tasktypes.get(i).getWorkValue();
				}

				for (int i = 0; i < tasktypes.size(); i++) {
					StringBuilder tasksOfType = new StringBuilder();
					for (int j = 0; j < tasks.size(); j++) {
						if (tasks.get(j).getType().equals(tasktypes.get(i).getName()))
							tasksOfType.append(tasks.get(j).getId() + ", ");
					}
					if (tasksOfType.length() != 0)
						tasksOfType.delete(tasksOfType.length() - 2, tasksOfType.length() - 1);
					data[i][1] = tasksOfType.toString();
				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JFrame TaskTypesFrame = new JFrame("Editable Table");
						TaskTypesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						// Create and set up the content pane.
						taskTypesPane = new TaskTypesTable(columnNames, data);
						taskTypesPane.setOpaque(true);
						// content panes must be opaque
						TaskTypesFrame.setContentPane(taskTypesPane);

						// Display the window.
						TaskTypesFrame.pack();
						TaskTypesFrame.setVisible(true);
					}
				});
			}
		});

		showTasksButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < workingMembers.size(); i++) {
					memberNames.add(workingMembers.get(i).getName());
				}

				for (int i = 0; i < tasktypes.size(); i++) {
					taskTypeNames.add(tasktypes.get(i).getName());
				}

				String[] columnNames = { "Task ID", "Assigned Member", "Task Type", "Work Value" };
				Object[][] data = new Object[tasks.size()][4];

				for (int i = 0; i < tasks.size(); i++) {
					data[i][0] = tasks.get(i).getId();
					if (tasks.get(i).getAssignedMember() != null)
						data[i][1] = tasks.get(i).getAssignedMember().getName();
					else
						data[i][1] = "null";

					data[i][2] = tasks.get(i).getType();
					data[i][3] = tasks.get(i).getWorkValue();
				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						JFrame TasksFrame = new JFrame("Editable Table");
						tasksPane = new TasksTable2(columnNames, data);
						TasksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

						// Create and set up the content pane.
						tasksPane.setOpaque(true);
						// content panes must be opaque
						TasksFrame.setContentPane(tasksPane);

						// Display the window.
						TasksFrame.pack();
						TasksFrame.setVisible(true);
					}
				});
			}
		});

		showMembersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] columnNames = { "Name", "Authorizations", "Assignments", "Workload", "Proportion of Work" };
				Object[][] data = new Object[members.size()][5];
				for (int i = 0; i < members.size(); i++) {
					ArrayList<String> authorizationsList = members.get(i).getAuthorizations();
					StringBuilder authorizations = new StringBuilder();
					for (int j = 0; j < authorizationsList.size(); j++) {
						if (j != authorizationsList.size() - 1)
							authorizations.append(authorizationsList.get(j) + ", ");
						else
							authorizations.append(authorizationsList.get(j));
					}

					ArrayList<Task> assignmentsList = members.get(i).getAssignments();
					StringBuilder assignments = new StringBuilder();
					for (int j = 0; j < assignmentsList.size(); j++) {
						if (j != assignmentsList.size() - 1)
							assignments.append(assignmentsList.get(j).getId() + ", ");
						else
							assignments.append(assignmentsList.get(j).getId());
					}

					data[i][0] = members.get(i).getName();
					data[i][1] = authorizations.toString();
					data[i][2] = assignments.toString();
					data[i][3] = members.get(i).getWork();
					data[i][4] = members.get(i).getProportionOfWork();
				}

				for (int i = 0; i < tasktypes.size(); i++) {
					taskTypeNames.add(tasktypes.get(i).getName());
				}

				for (int i = 0; i < tasks.size(); i++) {
					taskIds.add(tasks.get(i).getId());
				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JFrame membersFrame = new JFrame("Editable Table " + current.date + " " + current.time);
						membersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						// Create and set up the content pane.
						membersPane = new MembersTable(columnNames, data);
						membersPane.setOpaque(true);

						// content panes must be opaque
						membersFrame.setContentPane(membersPane);

						// Display the window.
						membersFrame.pack();
						membersFrame.setVisible(true);
					}
				});
			}
		});

		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to reset the all values?\n" + " hit save to finalize this action",
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (x == 0) {
					members.clear();
					tasktypes.clear();
					tasks.clear();
					workingMembers.clear();
					unassigned.clear();
					file = null;
					date = "";
					time = "";

					removeMemberCombo.removeAllItems();
					removeTaskCombo.removeAllItems();
					removeTaskTypeCombo.removeAllItems();
					addTaskCombo.removeAllItems();
					addAuthorizationsCombo.removeAllItems();
					memberCombo.removeAllItems();
					setWorkingMembersCombo.removeAllItems();
					
					setTaskValues();
				}
			}
		});

		editValuesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.setBounds((int) frame.getX() + 426, (int) frame.getY(), 416, 404);
				data.setLayout(null);
				data.setVisible(true);
			}
		});

		save1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		saveInstanceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String datetime = null;
					if (date != null && time != null)
						datetime = date + "_" + time;
					else if (time != null)
						datetime = time;
					else if (date != null)
						datetime = date;
					else
						datetime = "";

					datetime = datetime.replaceAll("/", "-");
					datetime = datetime.replaceAll(" ", "_");
					datetime = datetime.replaceAll(":", "-");
					
					Vector<TeamMember> temp = members;
					for(int i=0; i<temp.size(); i++)
						temp.get(i).setNonpermissions(new ArrayList<String>());

					Instance instance = new Instance(tasktypes, temp, workingMembers, tasks, unassigned, date, time);

					FileOutputStream instanceFile = new FileOutputStream(
							file + sep + "TaskAssigner" + sep + "javaLogs" + sep + "instance_" + datetime + ".java");
					ObjectOutputStream instanceOut = new ObjectOutputStream(instanceFile);

					instanceOut.writeObject(instance);

					instanceFile.close();
					instanceOut.close();
				}

				catch (Exception ex) {
					if (file == null)
						JOptionPane.showMessageDialog(frame, "No file path has been specified", "Error",
								JOptionPane.ERROR_MESSAGE);
					else
						JOptionPane.showMessageDialog(frame, "Exception is caught", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		instancesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<String> v = new Vector<String>();
				Vector<Instance> inst = new Vector<Instance>();
				File javaLogs = new File(file.getPath() + sep + "TaskAssigner" + sep + "javaLogs");
				JFrame f = new JFrame();
				JList<String> list = new JList<String>(v);

				for (int i = 0; i < javaLogs.listFiles().length; i++)
				{
					try
					{
						v.add(javaLogs.listFiles()[i].getName());
						FileInputStream instanceFile = new FileInputStream(javaLogs.listFiles()[i]);
						@SuppressWarnings("resource")
						ObjectInputStream instanceIn = new ObjectInputStream(instanceFile);

						inst.add((Instance) instanceIn.readObject());
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(frame, "Exception is caught", "Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}

				list.setSize(250, 350);

				f.add(list);
				JButton btn = new JButton("Create New Instance");
				btn.setBounds(25, 360, 180, 40);
				f.add(btn);
				f.setSize(250, 450);
				f.setLayout(null);
				f.setVisible(true);

				list.addListSelectionListener(new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent arg0)
					{
						javax.swing.SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								Instance instance = inst.get(arg0.getLastIndex());
								try
								{
									Component[] arr = ((Container) ((Container)((Container) frame.getComponents()[0]).getComponents()[1]).
											getComponents()[0]).getComponents();
									
									for(int i=0; i<arr.length; i++)
										arr[i].setEnabled(false);
									data.dispose();
									datefrm.dispose();
									memberfrm.dispose();
									
									current = new TaskAssigner(instance.getTasktypes(), instance.getMembers(),
											instance.getWorkingMembers(), instance.getTasks(), instance.getUnassigned(),
											instance.getDate(), instance.getTime(), JFrame.DISPOSE_ON_CLOSE,
											new Rectangle(0, 510, 416, 557), false);
																	
									current.frame.addWindowListener(new WindowListener()
									{
						                public void windowClosing(WindowEvent e)
						                {
						                	for(int i=0; i<arr.length; i++)
												arr[i].setEnabled(true);
						                	current.data.dispose();
						                	current.datefrm.dispose();
											current.memberfrm.dispose();
						                	current = original;
						                }
										public void windowActivated(WindowEvent arg0){}
										public void windowClosed(WindowEvent arg0){}
										public void windowDeactivated(WindowEvent arg0){}
										public void windowDeiconified(WindowEvent arg0){}
										public void windowIconified(WindowEvent arg0){}
										public void windowOpened(WindowEvent arg0){}
						            });
									
									for(int i = 0; i < current.tasktypes.size(); i++)
									{
										JCheckBox checkbox = new JCheckBox(current.tasktypes.get(i).getName());
										current.removeTaskTypeCombo.addItem(current.tasktypes.get(i).getName());
										current.addTaskCombo.addItem(current.tasktypes.get(i).getName());
										current.authorizationCheckboxes.add(checkbox);
									}

									for(int i = 0; i < current.members.size(); i++)
									{
										current.memberCombo.addItem(current.members.get(i).getName());
										current.removeMemberCombo.addItem(current.members.get(i).getName());
									}
									current.memberCombo.addItem("Unassigned");

									for(int i = 0; i < current.tasktypes.size(); i++)
									{
										if (current.addTaskCombo.getSelectedItem()
												.equals(current.tasktypes.get(i).getName()))
											current.taskWorkValueField
													.setText(Double.toString(current.tasktypes.get(i).getWorkValue()));
									}

									for(int i = 0; i < current.tasks.size(); i++)
									{
										current.removeTaskCombo.addItem(current.tasks.get(i).getId());
									}

									current.setTaskValues();
									f.dispose();
								}
								catch (Exception e)
								{
									JOptionPane.showMessageDialog(frame, "Exception is caught", "Error",
											JOptionPane.ERROR_MESSAGE);
									e.printStackTrace();
								}
							}
						});
					}
				});

				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						javax.swing.SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								try
								{
									Component[] arr = ((Container)((Container)((Container) frame.getComponents()[0]).getComponents()[1]).
											getComponents()[0]).getComponents();
									
									for(int i=0; i<arr.length; i++)
										arr[i].setEnabled(false);
									data.dispose();
									datefrm.dispose();
									memberfrm.dispose();
									
									current = new TaskAssigner(current.tasktypes, current.members,
											current.workingMembers, current.tasks, current.unassigned,
											current.date, current.time, JFrame.DISPOSE_ON_CLOSE,
											new Rectangle(0, 510, 416, 557), false);
																	
									current.frame.addWindowListener(new WindowListener()
									{
						                public void windowClosing(WindowEvent e)
						                {
						                	for(int i=0; i<arr.length; i++)
												arr[i].setEnabled(true);
						                	current.data.dispose();
						                	current.datefrm.dispose();
											current.memberfrm.dispose();
						                	current = original;
						                }
										public void windowActivated(WindowEvent arg0){}
										public void windowClosed(WindowEvent arg0){}
										public void windowDeactivated(WindowEvent arg0){}
										public void windowDeiconified(WindowEvent arg0){}
										public void windowIconified(WindowEvent arg0){}
										public void windowOpened(WindowEvent arg0){}
						            });
									
									for(int i = 0; i < current.tasktypes.size(); i++)
									{
										JCheckBox checkbox = new JCheckBox(current.tasktypes.get(i).getName());
										current.removeTaskTypeCombo.addItem(current.tasktypes.get(i).getName());
										current.addTaskCombo.addItem(current.tasktypes.get(i).getName());
										current.authorizationCheckboxes.add(checkbox);
									}

									for(int i = 0; i < current.members.size(); i++)
									{
										current.memberCombo.addItem(current.members.get(i).getName());
										current.removeMemberCombo.addItem(current.members.get(i).getName());
									}
									current.memberCombo.addItem("Unassigned");

									for(int i = 0; i < current.tasktypes.size(); i++)
									{
										if (current.addTaskCombo.getSelectedItem()
												.equals(current.tasktypes.get(i).getName()))
											current.taskWorkValueField
													.setText(Double.toString(current.tasktypes.get(i).getWorkValue()));
									}

									for(int i = 0; i < current.tasks.size(); i++)
									{
										current.removeTaskCombo.addItem(current.tasks.get(i).getId());
									}

									current.setTaskValues();
									f.dispose();
								}
								catch (Exception e)
								{
									JOptionPane.showMessageDialog(frame, "Exception is caught", "Error",
											JOptionPane.ERROR_MESSAGE);
									e.printStackTrace();
								}
							}
						});
					}
				});
			}
		});

		timeConfigButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dateAndTimeConfig();
			}
		});

		saveAssignmentsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String datetime = null;
					if (date != null && time != null)
						datetime = date + "_" + time;
					else if (time != null)
						datetime = time;
					else if (date != null)
						datetime = date;
					else
						datetime = "";

					datetime = datetime.replaceAll("/", "-");
					datetime = datetime.replaceAll(" ", "_");
					datetime = datetime.replaceAll(":", "-");
					
					String[][] rows = getAssignments();
	
					ArrayList<String[]> list = new ArrayList<String[]>();
					FileWriter csvWriter = new FileWriter(file + sep + "TaskAssigner" + sep + "logs" + sep + "data_" + datetime + ".csv");
					CSVWriter writer = new CSVWriter(csvWriter);
					
					String[] temp = new String[rows[0].length];
					temp[0] = "Row Number";
					for(int i=0; i<members.size(); i++)
					{
						temp[i+1] = members.get(i).getName();
					}
					temp[temp.length-1] = "Unassigned";
					
					list.add(temp);
					for(int i=0; i<rows.length; i++)
						list.add(rows[i]);
										
					writer.writeAll(list);
					
					writer.close();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});

		fileConfigButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String str = "";
					if (new File(file.getPath() + sep + "TaskAssigner").exists())
						str = (String) JOptionPane.showInputDialog(frame, "", "Current File Directory", 1, null, null,
								file);
					else
						str = (String) JOptionPane.showInputDialog(frame, "", "Current File Directory", 1, null, null,
								null);
					if(!str.equals(file.toString()))
					{
					File logs = new File(file.getPath() + sep + "TaskAssigner" + sep + "logs");
					File javaLogs = new File(file.getPath() + sep + "TaskAssigner" + sep + "javaLogs");
					new File(str + folder).mkdirs();

					ArrayList<Object> arr = new ArrayList<Object>();
					for (int i = 0; i < logs.listFiles().length; i++) {
						FileReader in = null;
						FileWriter out = null;
						try {

							in = new FileReader(
									file + sep + "TaskAssigner" + sep + "logs" + sep + logs.listFiles()[i].getName());
							out = new FileWriter(
									str + sep + "TaskAssigner" + sep + "logs" + sep + logs.listFiles()[i].getName());

							int c;
							while ((c = in.read()) != -1) {
								out.write(c);
							}
						} finally {
							if (in != null) {
								in.close();
							}
							if (out != null) {
								out.close();
							}
						}
					}
					if(javaLogs.listFiles()!=null)
					{
						for (int i = 0; i < javaLogs.listFiles().length; i++) {
							FileReader in = null;
							FileWriter out = null;
							try {
	
								in = new FileReader(file + sep + "TaskAssigner" + sep + "javaLogs" + sep
										+ javaLogs.listFiles()[i].getName());
								out = new FileWriter(file + sep + "TaskAssigner" + sep + "javaLogs" + sep
										+ javaLogs.listFiles()[i].getName());
	
								int c;
								while ((c = in.read()) != -1) {
									out.write(c);
								}
							} finally {
								if (in != null) {
									in.close();
								}
								if (out != null) {
									out.close();
								}
							}
						}
					}

					FileOutputStream membersFile = new FileOutputStream(
							str + sep + "TaskAssigner" + sep + "members.java");
					ObjectOutputStream membersOut = new ObjectOutputStream(membersFile);

					FileOutputStream taskTypesFile = new FileOutputStream(
							str + sep + "TaskAssigner" + sep + "tasktypes.java");
					ObjectOutputStream taskTypesOut = new ObjectOutputStream(taskTypesFile);

					membersOut.writeObject(members);
					taskTypesOut.writeObject(tasktypes);

					membersOut.close();
					membersFile.close();

					taskTypesOut.close();
					taskTypesFile.close();

					for (int i = 0; i < arr.size(); i++) {
						FileOutputStream fileFile = new FileOutputStream(
								str + sep + "TaskAssigner" + sep + "logs" + sep + ((File) arr.get(i)).getName());
						ObjectOutputStream fileOut = new ObjectOutputStream(fileFile);
						fileOut.writeObject(arr.get(i));
						fileOut.close();
						fileFile.close();
					}

					deleteDirectory(new File(file.getPath() + sep + "TaskAssigner"));
					file = new File(str);
				}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Exception is caught", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		setTemporaryPermissionsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFrame frm = new JFrame();
				JLabel lbl = new JLabel("Temporary Permissions", SwingConstants.CENTER);
				lbl.setBounds(45, 15, 150, 20);
				frm.add(lbl);
				int i;
				Vector<JComboCheckBox> checkboxes = new Vector<JComboCheckBox>();
				for (i = 0; i < members.size(); i++)
				{
					ArrayList<String> authorizations = members.get(i).getAuthorizations();
					Vector<JCheckBox> temp = new Vector<JCheckBox>();
					for(int j=0; j<authorizations.size(); j++)
					{
						temp.add(new JCheckBox(authorizations.get(j)));
						if(!members.get(i).getNonpermissions().contains(authorizations.get(j)))
						temp.get(j).setSelected(true);
					}
					JComboCheckBox checkbox = new JComboCheckBox(temp);
					checkboxes.add(checkbox);
					String name = members.get(i).getName();
					JLabel label = new JLabel(name, SwingConstants.RIGHT);
					label.setBounds(10, 50+30*i, 50, 20);
					checkbox.setBounds(70, 50+30*i, 100, 20);
					frm.add(label);
					frm.add(checkbox);
				}
				JButton go = new JButton("Go");
				go.setBounds(70, 65 + 30 * i, 100, 30);

				frm.add(go);

				frm.setBounds(frame.getX()+416, frame.getY(), 230, 205 + 30 * i);
				frm.setLayout(null);
				frm.setVisible(true);
				
				go.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						for(int i=0; i<members.size(); i++)
						{
							members.get(i).setNonpermissions(new ArrayList<String>());
							for(int j=0; j<members.get(i).getAuthorizations().size(); j++)
							{
								if(!(checkboxes.get(i).getItemAt(j)).isSelected())
									members.get(i).addNonpermission(checkboxes.get(i).getItemAt(j).getText());
							}
						}
						setTaskValues();
						frm.dispose();
					}
				});
			}
		});
	}

	public static void save() {
		Vector<TaskType> temp = current.tasktypes;
		try {
			for (int i = 0; i < current.tasktypes.size(); i++)
				current.tasktypes.get(i).setCount(1);
			
			Vector<TeamMember> membersTemp = current.members;
			for(int i=0; i<membersTemp.size(); i++)
				membersTemp.get(i).setNonpermissions(new ArrayList<String>());

			FileOutputStream fileFile = new FileOutputStream(
					System.getProperty("user.dir") + sep + "TaskAssignerFilePath.java");
			ObjectOutputStream fileOut = new ObjectOutputStream(fileFile);
			// Saving of object in a file
			FileOutputStream membersFile = new FileOutputStream(file + sep + "TaskAssigner" + sep + "members.java");
			ObjectOutputStream membersOut = new ObjectOutputStream(membersFile);

			FileOutputStream taskTypesFile = new FileOutputStream(file + sep + "TaskAssigner" + sep + "tasktypes.java");
			ObjectOutputStream taskTypesOut = new ObjectOutputStream(taskTypesFile);

			// * Method for serialization of object

			membersOut.writeObject(membersTemp);
			taskTypesOut.writeObject(current.tasktypes);
			fileOut.writeObject(file.getPath());

			membersOut.close();
			membersFile.close();

			taskTypesOut.close();
			taskTypesFile.close();

			fileOut.close();
			fileFile.close();
		}

		catch (Exception ex) {
			if (file == null)
				JOptionPane.showMessageDialog(current.frame, "No file path has been specified", "Error",
						JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(current.frame, "Exception is caught", "Error", JOptionPane.ERROR_MESSAGE);
		}
		current.tasktypes = temp;
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static String[][] getAssignments() {
		int alpha = 0;
		for (int k = 0; k < current.members.size(); k++) {
			if (current.members.get(k).getWork() > alpha)
				alpha = current.members.get(k).getWork();
		}
		int n = 0;
		for (int i = 0; i < current.unassigned.size(); i++) {
			n += current.unassigned.get(i).getWorkValue();
		}
		if (n > alpha)
			alpha = n;

		String[][] arr = new String[alpha][current.members.size() + 2];

		for (int k = 0; k < alpha; k++) {
			String[] array = new String[current.members.size() + 2];
			array[0] = Integer.toString(k + 1);
			for (int l = 0; l < current.members.size(); l++) {
				if (k >= current.members.get(l).getWork())
					array[l + 1] = "";
				else {
					int temp = 0;
					for (int m = 0; m < current.members.get(l).getAssignments().size(); m++) {
						if (k == temp)
							array[l + 1] = current.members.get(l).getAssignments().get(m).getId() + " - "
									+ current.members.get(l).getAssignments().get(m).getWorkValue();
						else if (k > temp)
							array[l + 1] = "|";
						temp += current.members.get(l).getAssignments().get(m).getWorkValue();
					}
				}
			}

			if (k >= n)
				array[array.length - 1] = "";
			else {
				int unassignedTemp = 0;
				for (int m = 0; m < current.unassigned.size(); m++) {
					if (k == unassignedTemp)
						array[array.length - 1] = current.unassigned.get(m).getId() + " - "
								+ current.unassigned.get(m).getWorkValue();
					else if (k > unassignedTemp)
						array[array.length - 1] = "|";
					unassignedTemp += current.unassigned.get(m).getWorkValue();
				}
			}

			arr[k] = array;
		}
		return arr;
	}

	public void setTaskValues()
	{
		current.taskIds.clear();
		for (int i = 0; i < current.tasks.size(); i++) {
			current.taskIds.add(current.tasks.get(i).getId());
		}

		for (int i = 0; i < current.tasktypes.size(); i++) {
			if (current.addTaskCombo.getSelectedItem().equals(current.tasktypes.get(i).getName()))
			{
				current.taskWorkValueField.setText(Integer.toString(current.tasktypes.get(i).getWorkValue()));
				if(current.tasktypes.get(i).getName().equals("FS") || current.tasktypes.get(i).getName().equals("IE"))
				{
					current.addTaskIDField.setText(
							current.tasktypes.get(i).getCount() + current.tasktypes.get(i).getName());
					while (current.taskIds.contains(current.addTaskIDField.getText())) {
						current.tasktypes.get(i).increaseCount();
						current.addTaskIDField
								.setText(current.tasktypes.get(i).getCount() + current.tasktypes.get(i).getName());
					}
				}
				else
				{
					current.addTaskIDField.setText(count.toString());
					while (current.taskIds.contains(current.addTaskIDField.getText()))
					{
						count++;
						current.addTaskIDField.setText(count.toString());
					}
				}
			}
		}
			
		if(autoButton.isSelected())
		{
			String str = (String) current.addTaskCombo.getSelectedItem();
			
			TeamMember beta = null;
			int i;
			for (i = 0; i < current.workingMembers.size(); i++) {
				TeamMember member = current.workingMembers.get(i);
				if (current.workingMembers.get(i).getAuthorizations().contains(str) && 
						   (member.getNonpermissions()==null || !member.getNonpermissions().contains(str))) {
					beta = current.workingMembers.get(i);
					break;
				}
			}
	
			if (beta == null) {
				current.memberCombo.setSelectedItem("Unassigned");
			}

			else {
				for (int j = i + 1; j < current.workingMembers.size(); j++) {
					TeamMember member = current.workingMembers.get(j);
					if(member.getAuthorizations().contains(str) && 
					   (member.getNonpermissions()==null || !member.getNonpermissions().contains(str)))
					{
						if (member.getWork()
								/ member.getProportionOfWork() < beta.getWork() / beta.getProportionOfWork())
							beta = member;
		
						else if (member.getWork() / member.getProportionOfWork() == beta.getWork()
										/ beta.getProportionOfWork()
								&& member.getProportionOfWork() > beta.getProportionOfWork())
							beta = member;
		
						else if (member.getWork() / member.getProportionOfWork() == beta.getWork()
										/ beta.getProportionOfWork()
								&& member.getAuthorizations().size() < beta.getAuthorizations().size())
							beta = member;
		
						else if (member.getWork() / member.getProportionOfWork() == beta.getWork()
										/ beta.getProportionOfWork()
								&& member.getAuthorizations().size() == beta.getAuthorizations().size() && Math.random() < .5)
							beta = member;
					}
				}
				current.memberCombo.setSelectedItem(beta.getName());
			}
		}
	}

	public static void dateAndTimeConfig() {
		JLabel datelbl = new JLabel("Date:");
		JTextField dateField = new JTextField();
		datelbl.setBounds(10, 10, 35, 25);
		if (current.date.chars().allMatch(Character::isWhitespace))
			dateField.setText(dtf2.format(LocalDateTime.now()));
		else
			dateField.setText(current.date);
		dateField.setBounds(50, 10, 75, 25);

		JLabel timelbl = new JLabel("Time:");
		timelbl.setBounds(10, 40, 35, 25);

		JTextField timeField = new JTextField();
		if (current.time.chars().allMatch(Character::isWhitespace))
			timeField.setText(dtf4.format(LocalDateTime.now()));
		else
			timeField.setText(current.time);

		timeField.setBounds(50, 40, 75, 25);

		JButton btn = new JButton("Enter");
		btn.setBounds(50, 80, 75, 30);

		current.datefrm.add(dateField);
		current.datefrm.add(timeField);
		current.datefrm.add(datelbl);
		current.datefrm.add(timelbl);
		current.datefrm.add(btn);
		current.datefrm.setBounds(current.frame.getX()+416, current.frame.getY(), 200, 165);
		current.datefrm.setLayout(null);
		current.datefrm.setVisible(true);

		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				current.date = dateField.getText();
				current.time = timeField.getText();
				current.frame.setTitle("Task Assigner " + current.date + ' ' + current.time);

				current.datefrm.dispose();
				current.datefrm.setVisible(false);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			original = new TaskAssigner(new Vector<TaskType>(), new Vector<TeamMember>(), new Vector<TeamMember>(),
					new ArrayList<Task>(), new ArrayList<Task>(), "", "", JFrame.EXIT_ON_CLOSE,
					new Rectangle(0, 0, 416, 557), true);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(current.frame, "ClassNotFoundException is Caught", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(current.frame, "IOException is Caught", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(current.frame, "Exception is Caught", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		String str = "";

		try {
			FileInputStream fileFile = new FileInputStream(
					System.getProperty("user.dir") + sep + "TaskAssignerFilePath.java");
			ObjectInputStream fileIn = new ObjectInputStream(fileFile);

			str = (String) fileIn.readObject();
			file = new File(str);

			// Reading the object from a file
			FileInputStream membersFile = new FileInputStream(file + sep + "TaskAssigner" + sep + "members.java");
			ObjectInputStream membersIn = new ObjectInputStream(membersFile);

			FileInputStream taskTypesFile = new FileInputStream(file + sep + "TaskAssigner" + sep + "tasktypes.java");
			ObjectInputStream taskTypesIn = new ObjectInputStream(taskTypesFile);

			current.members = (Vector<TeamMember>) membersIn.readObject();
			current.tasktypes = (Vector<TaskType>) taskTypesIn.readObject();

			membersIn.close();
			membersFile.close();

			taskTypesIn.close();
			taskTypesFile.close();

			fileIn.close();
			fileFile.close();
		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("Class Not Found Exception is caught");
		}

		catch (Exception ex) {
			System.out.println("Exception is caught");
		}

		if (file == null || (!new File(str + folder).canRead() && !new File(str + folder).canWrite())) {
			if ((!new File(str + folder).canRead() && !new File(str + folder).canWrite()))
				JOptionPane.showMessageDialog(current.frame, "The previously saved file path could not be accessed", "",
						JOptionPane.WARNING_MESSAGE);
			boolean first = true;
			String path = null;
			while (true) {
				if (first) {
					path = JOptionPane.showInputDialog(current.frame, "Please enter desired file path.\n ex: C:" + sep
							+ "Users" + sep + "JohnDoe" + sep + "Desktop");
					if (path == null)
						break;
					file = new File(path);
					first = false;
				}

				else if (!file.canRead() || !file.canWrite()) {
					path = JOptionPane.showInputDialog(current.frame,
							"I'm sorry, a folder could not be created via the specified file path. Please enter anonther. \n ex: C:"
									+ sep + "Users" + sep + "JohnDoe" + sep + "Desktop",
							"Error", JOptionPane.ERROR_MESSAGE);
					if (path == null)
						break;
					file = new File(path);
				}

				else if (new File(file + folder).exists() && new File(file + javaFolder).exists()) {
					JOptionPane.showMessageDialog(current.frame,
							"File path has been successfully channeled.\n"
									+ " Any adding or removing of files and/or tampering with\n "
									+ "the newly created folder may result in data loss or\n inacessibility",
							"", JOptionPane.PLAIN_MESSAGE);
					break;
				}

				else {
					new File(path + folder).mkdirs();
					new File(path + javaFolder).mkdir();
					JOptionPane.showMessageDialog(current.frame,
							"File path has been successfully channeled.\n"
									+ " Any adding or removing of files and/or tampering with\n "
									+ "the newly created folder may result in data loss or inacessibility",
							"", JOptionPane.PLAIN_MESSAGE);
					break;
				}

			}
		}

		dateAndTimeConfig();

		if (current.members.size() != 0) {
			int i;
			JCheckBox[] checkboxes = new JCheckBox[current.members.size()];
			for (i = 0; i < current.members.size(); i++) {
				JCheckBox checkbox = new JCheckBox(current.members.get(i).getName());
				checkbox.setBounds(50, 50 + 30 * i, 60, 30);
				checkboxes[i] = checkbox;
				current.memberfrm.add(checkbox);
				current.membersCheckboxes.add(checkbox);
			}
			JLabel label = new JLabel("Who is working today?", SwingConstants.CENTER);
			label.setBounds(0, 10, 200, 40);
			JButton go = new JButton("Go");
			go.setBounds(50, 65 + 30 * i, 100, 30);

			JButton selectAll = new JButton("Select All");
			selectAll.setBounds(50, 110 + 30 * i, 100, 30);

			current.memberfrm.add(label);
			current.memberfrm.add(go);
			current.memberfrm.add(selectAll);

			current.memberfrm.add(checkboxes[0]);
			current.memberfrm.setBounds(current.frame.getX(), current.frame.getY(), 200, 205 + 30 * i);
			current.memberfrm.setLayout(null);
			current.memberfrm.setVisible(true);

			selectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < checkboxes.length; i++) {
						checkboxes[i].setSelected(true);
					}
				}
			});
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < checkboxes.length; i++) {
						if (checkboxes[i].isSelected()) {
							current.workingMembers.add(current.members.get(i));
						}
					}
					current.memberfrm.setVisible(false);
					current.memberfrm.dispose();

					current.setTaskValues();
				}
			});
		}

		for (int i = 0; i < current.tasktypes.size(); i++) {
			JCheckBox checkbox = new JCheckBox(current.tasktypes.get(i).getName());
			current.removeTaskTypeCombo.addItem(current.tasktypes.get(i).getName());
			current.addTaskCombo.addItem(current.tasktypes.get(i).getName());
			current.authorizationCheckboxes.add(checkbox);
		}

		for (int i = 0; i < current.members.size(); i++) {
			current.memberCombo.addItem(current.members.get(i).getName());
			current.removeMemberCombo.addItem(current.members.get(i).getName());
			current.members.get(i).clearAssignments();
			current.members.get(i).setWork(0);
		}
		current.memberCombo.addItem("Unassigned");

		for (int i = 0; i < current.tasktypes.size(); i++) {
			if (current.addTaskCombo.getSelectedItem().equals(current.tasktypes.get(i).getName()))
				current.taskWorkValueField.setText(Double.toString(current.tasktypes.get(i).getWorkValue()));
		}
	}
}