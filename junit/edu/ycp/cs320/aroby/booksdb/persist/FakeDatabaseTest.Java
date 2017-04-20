package edu.ycp.cs320.aroby.booksdb.persist;

import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Student;

public class FakeDatabaseTest{
	
	private IDatabase db = null;
	
	@Before
	public void setup(){
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	@Test
	public void FindAccountTest(){
		Account a = db.findAccount("tclark22@ycp.edu");
		
		assertTrue(a.getEmail().equals("tclark22@ycp.edu"));
		assertTrue(a.getFirstName().equals("Terrell"));
		assertTrue(a.getLastName().equals("Clark"));
		assertTrue(a.getPassword().equals("password"));
		assertTrue(a.getAdmin().equals(true));
	}
	@Test
	public void FindStudentTest(){
		Student s = db.findStudent("student@ycp.edu");
		
		assertEquals(s.getAccountId(), 4);
		assertTrue(s.getMajor().equals("EE"));
		assertEquals(s.getYCPId(), 909009999);
		assertTrue(s.getFirstName().equals("John"));
	}
}