package components;

import java.io.Serializable;

public class TaskType implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	String name;
	int workValue;
	int count;
	
	public TaskType(String name, int workValue, int count)
	{
		this.name = name;
		this.workValue = workValue;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String data) {
		this.name = data;
	}

	public int getWorkValue() {
		return workValue;
	}

	public void setWorkValue(int workValue) {
		this.workValue = workValue;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public void increaseCount()
	{
		this.count++;
	}
}
