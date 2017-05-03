package edu.ycp.cs320.aroby.controller;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class TedTalkControllerTest {
	private Review review;
	private ArrayList<Review> rList;
	private ReviewController reviewControl;
	private TedTalk talk;
	private TedTalkController control1;
	private Speaker speaker;
	private Topic topic;
	private Account acc;
	private AccountController acc_control;
	
	@Before
	public void setUp() throws Exception{
		speaker = new Speaker();
		topic = new Topic();
		review = new Review();
		rList = new ArrayList<Review>();
		reviewControl = new ReviewController();
		talk =  new TedTalk();
		control1 = new TedTalkController();
		acc = new Account();
		acc_control = new AccountController();
		
		acc.setAccountId(6);
		acc.setAdmin(true);
		acc.setEmail("chiisuhi@yahoo.com");
		acc.setFirstName("Chii");
		acc.setLastName("Suhi");
		acc.setPassword("ojamajo");
		
		acc_control.setModel(acc);
		acc_control.createAccount(acc);
	
		topic.setTopic("Media");
		topic.setTopicId(9);
		
		speaker.setFirstname("Patty");
		speaker.setLastname("Jenkins");
		speaker.setSpeakerId(15);
		
		talk.setDescription("Lady Diana's death sparked a media frenzy and brought up discussion"
				+ "pertaining to paparazzi and private lives.");
		talk.setLink("http://www.gonegreek.com/comfort-colors-crewneck-with-greek-letters/");
		
		talk.setReview(rList);
		talk.setSpeakerId(speaker.getSpeakerId());
		talk.setTedTalkId(8);
		talk.setTitle("Give Us Some Privacy!");
		talk.setTopicId(topic.getTopicId());
		
		String review_string = "I really wish this conversation happened years ago";
		review.setReview(review_string);	
		review.setTedTalkId(talk.getTedTalkId());
		review.setDate(ZonedDateTime.now().toString());
		review.setAccountId(acc.getAccountId());
		review.setRating(4);
		review.setReviewId(2);
		reviewControl.setModel(review);
		rList.add(review);
		
		control1.setModel(talk);
		control1.insertNewSpeaker(speaker.getFirstname(), speaker.getLastname());
		control1.insertNewTopic(topic.getTopic());
		control1.insertNewTedTalk(talk.getTitle(), talk.getDescription(), talk.getLink(), speaker.getFirstname(), speaker.getLastname(), topic.getTopic());
		control1.insertReview(review.getRating(), review.getDate(), review.getReview(), acc.getFirstName(), acc.getLastName(), talk.getTitle());
		
	}
	
	@Test
	public void test_getTedTalk(){ //good
		assertTrue(control1.getTedTalk() != null);
	}
	
	@Test
	public void test_insertNewSpeaker(){ //good
		assertTrue(control1.findSpeaker("Patty", "Jenkins") != null);
	}

	@Test
	public void test_insertNewTopic(){ //good
		assertTrue(control1.findTopic("Media") != null);
	}

	@Test
	public void test_findTopic(){ //good
		assertTrue(control1.findTopic("Media") != null);
	}
	
	@Test
	public void test_findReview(){ //good
		assertTrue(control1.findReview(talk.getTitle()) != null);
	}
	
	@Test
	public void test_insertReview(){ // good
		assertTrue(control1.findReviewbyTitle("Give Us Some Privacy!") != null);
	}
	
	@Test
	public void test_findReviewByTitle(){ //good
		assertTrue(control1.findReviewbyTitle("Give Us Some Privacy!") != null);
	}
	
	@Test 
	public void test_findTedTalkByAuthor(){ //good
		assertTrue(control1.findTedTalkbyAuthor(speaker.getFirstname() + speaker.getLastname()) != null);
	}
	
	@Test
	public void test_findTedTalkbyID(){ //good
		assertTrue(control1.findTedTalkByID(74) != null);
	}
	
	@Test
	public void test_findTedTalkByTitle(){ //good
		assertTrue(control1.findTedTalkbyTitle("Give Us Some Privacy!") != null);
	}
	
	//@Test come back to this test later 
	//public void test_findTedTalkbyTopic(){ 
	//	assertTrue(control1.findTedTalkbyTopic(topic.getTopic()) == null);
	//}
	
	@Test
	public void test_findAccount(){ //good
		assertTrue(control1.findAccount(16) != null);
	}
	
	@Test
	public void test_findTopicByID(){ //good
		assertTrue(control1.findTopicByID(9) != null);
	
	}
	
	@Test
	public void test_findSpeakerByID(){ //good
		assertTrue(control1.findSpeakerByID(15) != null);
	
	}

	@Test
	public void test_getAccountfromReviews(){ //good
		assertTrue(control1.getTedTalk() != null);
	}
	
	@Test
	public void test_exists(){
		//assertTrue(!control1.exists());
	}
	
}

