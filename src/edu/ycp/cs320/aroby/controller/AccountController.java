package edu.ycp.cs320.aroby.controller;

import edu.ycp.cs320.aroby.model.Account;

public class AccountController {
	private Account model;
	
	public void setModel(Account model) {
		this.model = model;
	}
	
	public Account getModel() {
		return model;
	}

	public void createAccount(Account model) {
		// TODO: add account to the database!
	}
}
