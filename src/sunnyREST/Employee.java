package sunnyREST;

import java.io.Serializable;

public class Employee implements Serializable{
	private String name;
	private String emailAddress;

	public Employee() {
	}
	
	public Employee(String name, String emailAddress) {
		setName(name);
		setEmailAddress(emailAddress);
	}

	public void setName(String name) { 
		this.name = name; 
	}

	public String getName() { 
		return name;
	}


	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}