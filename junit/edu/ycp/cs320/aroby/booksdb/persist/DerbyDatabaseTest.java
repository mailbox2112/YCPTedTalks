package edu.ycp.cs320.aroby.booksdb.persist;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Student;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.model.Review;

public class DerbyDatabaseTest {
	private IDatabase db = null;
	
	// WARNING: Make sure you drop the database before doing these tests!
	@Before
	public void setup() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	
	@Test
	public void FindAccountTest() {
		Account account = new Account();
		
		account = db.findAccount("aroby@ycp.edu");
		
		if(account == null) {
			fail("No account retrieved.");
		} else if(account.getEmail().equals("aroby@ycp.edu")) {
			System.out.println("Account found.");
		}
		
		account = db.findAccount("clocke3@ycp.edu");
		
		if(account.getEmail().equals("clocke3@ycp.edu")) {
			System.out.println("Account found.");
		} else {
			fail("Account not retrieved successfully.");
		}
	}
	
	@Test
	public void FindStudentTest() {
		Student student = new Student();
		
		student = db.findStudent("student@ycp.edu");
		
		if(student.getEmail().equals("student@ycp.edu")) {
			System.out.println("Student account retrieved.");
		} else {
			fail("Account not retrieved successfully.");
		}
	}
	
	@Test
	public void CreateAccountTest() {
		Account account = new Account();
		account.setAdmin(true);
		account.setEmail("test@ycp.edu");
		account.setFirstName("Tester");
		account.setLastName("Testinghouse");
		account.setPassword("test");
		
		Boolean result = db.createNewAccount(account.getEmail(), account.getPassword(), 
				account.getFirstName(), account.getLastName(), account.getAdmin());
		
		// Make sure the account was created, as intended
		if (result == true) {
			System.out.println("Successfully created new account.");
		} else {
			fail("Account not created successfully.");
		}
		
		// Set the email to an existing account
		account.setEmail("aroby@ycp.edu");
		
		result = db.createNewAccount(account.getEmail(), account.getPassword(), 
				account.getFirstName(), account.getLastName(), account.getAdmin());
		
		// Make sure the account WASN'T created, as intended
		if (result == false) {
			System.out.println("Account already exists. Success!");
		} else {
			fail("Oops, we created an account that already exists.");
		}
	}

	@Test
	public void CreateStudentTest() {
		Student account = new Student();
		account.setAdmin(false);
		account.setEmail("testStudent@ycp.edu");
		account.setFirstName("Tester");
		account.setLastName("Studentson");
		account.setPassword("test");
		
		account.setMajor("EE");
		account.setYCPId(001100);
		
		db.createNewAccount(account.getEmail(), account.getPassword(), 
				account.getFirstName(), account.getLastName(), account.getAdmin());
		
		Boolean result = db.createNewStudent(account.getYCPId(), account.getMajor(), account.getEmail());
		
		if (result == true) {
			System.out.println("Account created successfully.");
		} else {
			fail("Uh oh, the account wasn't created successfully.");
		}
	}
	
	@Test
	public void FindTopicTest() {
		Topic topic = new Topic();
		
		topic = db.findTopic("Engineering");
		
		if (topic == null) {
			fail("Topic not found!");
		} else {
			System.out.println("Topic found successfully.");
		}
	}
	
	@Test
	public void FindReviewsByAuthorTest() {
		List<Review> reviews = new ArrayList<Review>();
		
		reviews = db.findReviewsbyAuthor("Aaron", "Roby");
		
		if (reviews == null) {
			fail("No reviews found!");
		} else {
			System.out.println("Reviews found successfully.");
		}
	}
	
	@Test
	public void FindReviewsByTopicTest() {
		List<Review> reviews = new ArrayList<Review>();
		
		reviews = db.findReviewbyTopic("BS");
		
		if (reviews == null) {
			fail("No reviews found.");
		} else {
			System.out.println("Reviews found successfully.");
		}
	}
	
	@Test
	public void FindReviewsByTitleTest() {
		List<Review> reviews = new ArrayList<Review>();
		
		reviews = db.findReviewbyTitle("A Guide To Masterful BS");
		
		if (reviews == null) {
			fail("No reviews found.");
		} else {
			System.out.print("Reviews found successfully.");
		}
	}
}
