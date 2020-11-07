package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Instance implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector<TaskType> tasktypes;
	Vector<TeamMember> members;
	Vector<TeamMember> workingMembers; 
	ArrayList<Task> tasks;
	ArrayList<Task> unassigned;
	String date;
	String time;
	
	public Instance(Vector<TaskType> tasktypes, Vector<TeamMember> members,
			Vector<TeamMember> workingMembers, ArrayList<Task> tasks, ArrayList<Task> unassigned, String date, String time)
	{
		this.tasktypes = tasktypes;
		this.members = members;
		this.workingMembers = members;
		this.tasks = tasks;
		this.unassigned = unassigned;
		this.date = date;
		this.time = time;
	}

	public ArrayList<Task> getUnassigned() {
		return unassigned;
	}

	public void setUnassigned(ArrayList<Task> unassigned) {
		this.unassigned = unassigned;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Vector<TaskType> getTasktypes() {
		return tasktypes;
	}

	public void setTasktypes(Vector<TaskType> tasktypes) {
		this.tasktypes = tasktypes;
	}

	public Vector<TeamMember> getMembers() {
		return members;
	}

	public void setMembers(Vector<TeamMember> members) {
		this.members = members;
	}

	public Vector<TeamMember> getWorkingMembers() {
		return workingMembers;
	}

	public void setWorkingMembers(Vector<TeamMember> workingMembers) {
		this.workingMembers = workingMembers;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
	
}
