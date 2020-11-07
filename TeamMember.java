package components;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamMember implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	String name;
	ArrayList<String> authorizations;
	ArrayList<Task> assignments;
	int work;
	double proportionOfWork;
	ArrayList<String> nonpermissions;
	
	public TeamMember(String name, ArrayList<String> authorizations,
			ArrayList<Task> assignments, int work, double proportionOfWork, ArrayList<String> nonpermissions)
	{
		this.name = name;
		this.authorizations = authorizations;
		this.assignments = assignments;
		this.work = work;
		this.proportionOfWork = proportionOfWork;
		this.nonpermissions = nonpermissions;
	}
	
	public ArrayList<String> getNonpermissions() {
		return nonpermissions;
	}

	public void setNonpermissions(ArrayList<String> nonpermissions) {
		this.nonpermissions = nonpermissions;
	}
	
	public void addNonpermission(String str)
	{
		this.nonpermissions.add(str);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public ArrayList<String> getAuthorizations()
	{
		return authorizations;
	}
	
	public void addAssignments(Task assignment)
	{
		this.assignments.add(assignment);
		this.work += assignment.getWorkValue();
	}
	
	public void setAssignments(ArrayList<Task> assignments)
	{
		this.assignments = assignments;
	}
	
	public void removeAssignments(Task assignment)
	{
		this.assignments.remove(assignment);
	}
	
	public ArrayList<Task> getAssignments()
	{
		return assignments;
	}

	public void setAuthorizations(ArrayList<String> authorizations)
	{
		this.authorizations = authorizations;
	}
	
	public void addWork(int n)
	{
		this.work += n;
	}
	
	public void setWork(int n)
	{
		this.work = n;
	}
	
	public int getWork()
	{
		return work;
	}
	
	public void setProportionOfWork(double n)
	{
		this.proportionOfWork = n;
	}
	
	public double getProportionOfWork()
	{
		return proportionOfWork;
	}

	public void clearAssignments() {
		this.assignments.clear();
	}

	public void clearAuthorizations() {
		this.authorizations.clear();
	}
}