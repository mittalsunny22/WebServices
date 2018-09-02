package sunnyREST;

import java.io.Serializable;
import java.util.List; 

public class Team implements Serializable {

	private List<Employee> Employees;

	private String name;

	public Team() { }

	public Team(String name, List<Employee> Employees) {
		setName(name);
		setEmployees(Employees);
	}

	public void setName(String name) { 
		this.name = name;
	}

	public String getName() { 
		return name;
	}

	public void setEmployees(List<Employee> Employees) { 
		this.Employees = Employees;
	}

	public List<Employee> getEmployees() {
		return Employees;
	}

	public void setRosterCount(int n) { } // no-op but needed for property

	public int getRosterCount() { 
		return (Employees == null) ? 0 : Employees.size();
	}
} 