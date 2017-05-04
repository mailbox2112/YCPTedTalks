package edu.ycp.cs320.aroby.booksdb.persist;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Student;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.persist.IDatabase;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;

public class DerbyDatabaseTest {
	private IDatabase db = null;
	
	// WARNING: Make sure you drop the database before doing these tests!
	@Before
	public void setup() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	
	@Test
	public void FindAccountTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Account account = new Account();
		
		account = db.findAccount("aroby@ycp.edu");
		
		if(account == null) {
			fail("No account retrieved.");
		} else if(account.getEmail().equals("aroby@ycp.edu")) {
			System.out.println("Account found.");
		}
		
		account = db.findAccount(2);
		
		if(account.getAccountId() == 2) {
			System.out.println("Account found.");
		} else {
			fail("Account not retrieved successfully.");
		}
	}
	
	@Test
	public void FindStudentTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Student student = new Student();
		
		student = db.findStudent("student@ycp.edu");
		
		if(student.getEmail().equals("student@ycp.edu")) {
			System.out.println("Student account retrieved.");
		} else {
			fail("Account not retrieved successfully.");
		}
	}
	
	@Test
	public void CreateAccountTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
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
	
	public void CreateStudentTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
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
	public void FindTopicTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Topic topic = new Topic();
		
		topic = db.findTopic("Engineering");
		
		if (topic == null) {
			fail("Topic not found!");
		} else {
			System.out.println("Topic found successfully.");
		}
	}
	
	@Test
	public void FindReviewsByAuthorTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		List<Review> reviews = new ArrayList<Review>();
		
		reviews = db.findReviewsbyAuthor("Aaron", "Roby");
		
		for(Review review : reviews) {
			if(review.getAccountId() != 1) {
				fail("Reviews not successfully retrieved");
			}
		}
	}
	
	@Test
	public void FindReviewsByTopicTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		List<Review> reviews = new ArrayList<Review>();
		
		reviews = db.findReviewbyTopic("BS");
		
		if (reviews == null) {
			fail("No reviews found.");
		} else {
			System.out.println("Reviews found successfully.");
		}
	}
	
	@Test
	public void FindTedTalksByTitleTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		TedTalk talk = new TedTalk();
		
		talk = db.findTedTalkbyTitle("BS");
		
		if(talk == null){
			fail("No ted talks found.");
		}
		else{
			System.out.println("TedTalks found succesfully!");
		}
	
	}
	@Test
	public void FindTedTalksBySpeakerTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		List<TedTalk> talks = new ArrayList<TedTalk>();
		
		talks = db.findTedTalkbySpeaker("Roby");
		
		if(talks == null){
			fail("No ted talks found.");
		}
		else{
			System.out.println("TedTalks found succesfully!");
		}
	
	}
	
	@Test
	public void FindTedTalksByTopicTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		List<TedTalk> talks = new ArrayList<TedTalk>();
		
		talks = db.findTedTalkbyTopic("Science");
		
		if(talks == null){
			fail("No ted talks found.");
		}
		else{
			System.out.println("TedTalks found succesfully!");
		}
	}
	
	@Test
	public void insertTedTalkTest() throws MalformedURLException {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Speaker speaker = new Speaker();
		speaker = db.findSpeaker("Aaron","Roby");	
		
		Topic topic = new Topic();
		topic.setTopic("BS");
		
		Boolean result = db.insertNewTedTalk("Bulldogs","They're not vicious", "https://github.com/mailbox2112/YCPTedTalks",speaker.getFirstname(), speaker.getLastname(), topic.getTopic());
		
		if (result == true) {
			System.out.println("TedTalk created successfully.");
		} else {
			fail("Uh oh, the TedTalk wasn't created successfully.");
		}
	}
	
	@Test
	public void insertReviewTest(){
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Account acc = new Account();
		acc = db.findAccount(3);
		
		TedTalk talk = new TedTalk();
		talk = db.findTedTalkbyTitle("a guide to masterful bs");
		
		String date = ZonedDateTime.now().toString();
		
		Boolean result = db.insertReview(2, date, "You are shit", acc.getFirstName(), acc.getLastName(), talk.getTitle());
	
		if (result == true) {
			System.out.println("Review added successfully.");
		} else {
			fail("Uh oh, the review wasn't added successfully.");
		}
	}
	
	@Test
	public void insertNewSpeakerTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Speaker speaker = new Speaker();
		speaker.setFirstname("Hank");
		speaker.setLastname("Hill");
		
		Boolean result = db.insertNewSpeaker(speaker.getFirstname(), speaker.getLastname());
		
		if (result == true) {
			System.out.println("Speaker added successfully.");
		} else {
			fail("Uh oh, the speaker wasn't added successfully.");
		}
		
	}
	
	@Test
	public void insertNewTopicTest() { //good
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Topic topic = new Topic();
		topic.setTopic("Propane");
		
		Boolean result = db.insertNewTopic("Propane");
		
		if (result == true) {
			System.out.println("Topic added successfully.");
		} else {
			fail("Uh oh, the topic wasn't added successfully.");
		}
	}
	
	@Test
	public void FindSpeakerTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		Speaker speaker = new Speaker();
		
		speaker = db.findSpeaker("aaron", "roby");
		
		if (speaker.getFirstname().toLowerCase().equals("aaron") && speaker.getLastname().toLowerCase().equals("roby")) {
			System.out.println("Speaker found successfully!");
		} else {
			fail("No speaker found.");
		}
	}
	
	@Test
	public void DeleteTedTalkTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		TedTalk ted = db.findTedTalkByID(1);
		boolean result = db.deleteTedTalk(1);
		
		TedTalk talk = db.findTedTalkByID(1);
		
		if (result == true) {
			if (ted.getTitle().equals(talk.getTitle())) {
				fail("TedTalk not deleted successfully");
			}
		}
	}
	
	@Test
	public void FindTedTalkByTitleTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		TedTalk ted = db.findTedTalkbyTitle("a guide to masterful bs");
		
		if (ted != null) {
			if (ted.getTitle().toLowerCase().equals("a guide to masterful bs")) {
				System.out.println("Success!");
			} else {
				fail("Correct tedtalk not retrieved");
			}
		} else {
			fail("Nothing retrieved");
		}
	}
	
	@Test
	public void FindTedTalkByIdTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		TedTalk ted = db.findTedTalkByID(1);
		
		if (ted != null) {
			if (ted.getTedTalkId() == 1) {
				System.out.println("Success!");
			} else {
				fail("Correct tedtalk not retrieved");
			}
		} else {
			fail("No tedtalk retrieved");
		}
	}
	
	@Test
	public void FindTopicByIdTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		Topic topic = db.findTopicbyID(1);
		
		if (topic != null) {
			if (topic.getTopicId() == 1) {
				System.out.println("Success!");
			} else {
				fail("Incorrect topic retrieved.");
			}
		} else {
			fail("No topic retrieved.");
		}
	}
	
	@Test
	public void FindSpeakerByIdTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		Speaker speaker = db.findSpeakerbyID(1);
		
		if (speaker != null) {
			if (speaker.getSpeakerId() == 1) {
				System.out.println("Success!");
			} else {
				fail("Incorrect speaker retrieved.");
			}
		} else {
			fail("No speaker retrieved.");
		}
	}
	
	@Test
	public void GetAllTopicsTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		List<Topic> topics = db.getAllTopics();
		
		if (topics != null) {
			if (topics.size() != 0) {
				System.out.println("Topics retrieved successfully");
			} else {
				fail("No topics within list");
			}
		} else {
			fail("No topics retrieved");
		}
	}
	
	@Test
	public void DeleteReviewTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		List<Review> reviews = db.findReviewbyTitle("a guide to masterful bs");
		boolean result = db.deleteReview(reviews.get(0).getReviewId());
		
		List<Review> updatedReviews = db.findReviewbyTitle("a guide to masterful bs");
		
		if (result == true) {
			if (updatedReviews.size() != reviews.size()-1) {
				fail("Review not deleted successfully");
			}
		}
	}
	
	@Test
	public void ChangePasswordTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		Account account = db.findAccount(1);
		String testPass = "test";
		
		db.changePassword(account.getAccountId(), testPass);
		
		Account result = db.findAccount(account.getAccountId());
		
		if (result != null && account != null) {
			if (result.getPassword().equals(account.getPassword())) {
				fail("Password not updated successfully.");
			} else if (!result.getPassword().equals(testPass)) {
				fail("Password not updated successfully.");
			}
		} else {
			fail("Account not retrieved successfully");
		}
	}
	
	@Test
	public void ChangeEmailTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		Account account = db.findAccount(1);
		String testEmail = "test@ycp.edu";
		
		db.changeEmail(account.getAccountId(), testEmail);
		
		Account result = db.findAccount(account.getAccountId());
		
		if (account != null & result != null) {
			if (result.getEmail().equals(account.getEmail())) {
				fail("Email not updated successfully");
			} else if (!result.getEmail().equals(testEmail)) {
				fail("Emails do not match");
			}
		} else {
			fail("Account not retrieved successfully");
		}
	}
	
	@Test
	public void ChangeMajorTest() {
		db.deleteTables();
		db.createTables();
		db.loadInitialData();
		
		String studentEmail = "student@ycp.edu";
		String testMajor = "CE";
		Student student = db.findStudent(studentEmail);
		
		db.changeMajor(student.getStudentId(), testMajor);
		
		Student result = db.findStudent(studentEmail);
		
		if (student != null & result != null) {
			if (result.getMajor().equals(student.getMajor())) {
				fail("Major not changed successfully.");
			} else if (!result.getMajor().equals(testMajor)) {
				fail("Majors do not match");
			}
		} else {
			fail("Students not retrieved successfully");
		}
	}
}