package edu.ycp.cs320.aroby.controller;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Student;
import edu.ycp.cs320.aroby.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.persist.IDatabase;

public class AccountController {
	private Account model;
	private IDatabase db;
	
	public AccountController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	public Account getModel() {
		return model;
	}
	
	public void setModel(Account model) {
		this.model = model;
	}

	public boolean createAccount(Account model) {
		boolean success = false;
		success = db.createNewAccount(model.getEmail(), model.getPassword(),
					model.getFirstName().toLowerCase(), model.getLastName().toLowerCase(), model.getAdmin());
		
		return success;
	}
	
	public boolean createStudent(Student model) {
		boolean success = false;
		success = db.createNewAccount(model.getEmail(), model.getPassword(),
				model.getFirstName().toLowerCase(), model.getLastName().toLowerCase(), model.getAdmin());
		
		if (success) {
			success = db.createNewStudent(model.getYCPId(), model.getMajor().toLowerCase(), model.getEmail());
		}
		
		return success;
	}
	
	public Account getAccountFromDb(int accountId) {
		Account acc = db.findAccount(accountId);
		return acc;
	}
	
	public boolean changePassword(String newPassword) {
		return db.changePassword(model.getAccountId(), newPassword);
	}
	
	public boolean changeEmail(String newEmail) {
		return db.changeEmail(model.getAccountId(), newEmail);
	}
	
	public Student getStudentFromAccountInfo(Account account) {
		return db.findStudent(account.getEmail());
	}
	
	public boolean changeMajor(String major) {
		Student student = getStudentFromAccountInfo(model);
		return db.changeMajor(student.getStudentId(), major);
	}
}
