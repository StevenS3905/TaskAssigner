package components;

import java.io.Serializable;

public class Task implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	String type;
	String id;
	int workValue;
	TeamMember assignedMember;
	boolean overridden;
	
	public Task(String type, String id, int workValue, TeamMember assignedMember, Boolean overridden)
	{
		this.type = type;
		this.id = id;
		this.workValue = workValue;
		this.assignedMember = assignedMember;
		this.overridden = overridden;
	}
	
	public TeamMember getAssignedMember() {
		return assignedMember;
	}

	public void setAssignedMember(TeamMember assignedMember) {
		this.assignedMember = assignedMember;
	}

	public void setWorkValue(int workValue) {
		this.workValue = workValue;
	}

	public String getType() {
		return type;
	}
	
	public boolean isOverridden()
	{
		return overridden;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWorkValue() {
		return workValue;
	}

	public void setOverridden(boolean b) {
		this.overridden = b;
	}

	
}
