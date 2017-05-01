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
	public void FindStudentTest() { //good
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
		Speaker speaker = new Speaker();
		
		speaker = db.findSpeaker("aaron", "roby");
		
		if (speaker.getFirstname().equals("aaron") && speaker.getLastname().equals("roby")) {
			System.out.println("Speaker found successfully!");
		} else {
			fail("No speaker found.");
		}
	}
	
	@Test
	public void DeleteTedTalkTest() {
		TedTalk ted = db.findTedTalkByID(1);
		boolean result = db.deleteTedTalk(1);
		
		TedTalk talk = db.findTedTalkByID(1);
		
		if (result == true) {
			if (ted.getTitle().equals(talk.getTitle())) {
				fail("TedTalk not deleted successfully");
			}
		}
	}
}