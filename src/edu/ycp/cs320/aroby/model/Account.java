package edu.ycp.cs320.aroby.model;

public class Account {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Boolean admin;
	private int account_id;
	
	public Account() {
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public int getAccountId() {
		return account_id;
	}

	public void setAccountId(int account_id) {
		this.account_id = account_id;
	}
}
