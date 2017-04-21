package edu.ycp.cs320.aroby.booksdb.persist;

import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Student;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.model.TedTalk;

public class FakeDatabaseTest{
	
	private IDatabase db = null;
	
	@Before
	public void setup(){
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	@Test
	public void FindnInsertAccountTest(){
		db.createNewAccount("student2@ycp.edu", "Charles", "Xavier", "password", false);
		Account a = db.findAccount("student2@ycp.edu");
		
		assertTrue(a.getEmail().equals("student2@ycp.edu"));
		assertTrue(a.getFirstName().equals("Charles"));
		assertTrue(a.getLastName().equals("Xavier"));
		assertTrue(a.getPassword().equals("password"));
		assertTrue(a.getAdmin().equals(false));
		
	}
	@Test
	public void FindAccountTest2(){
		Account a2 = db.findAccount("tclark22@ycp.edu");
		
		assertEquals(a2.getFirstName(), ("Terrell"));
		assertTrue(a2.getLastName().equals("Clark"));
		assertTrue(a2.getEmail().equals("tclark22@ycp.edu"));
		assertTrue(a2.getPassword().equals("password"));
		assertTrue(a2.getAdmin().equals(true));
	}
	@Test
	public void FindnInsertStudentTest(){
		String email = "student2@ycp.edu";
		db.createNewAccount("student2@ycp.edu", "Charles", "Xavier", "password", false);
		Account a2 = db.findAccount(email);
		
		db.createNewStudent(a2.getAccountId(), "student2@ycp.edu", "ME", 901001199);
		
		Student s = db.findStudent(email);
		
		assertEquals(s.getEmail(), "student2@ycp.edu");
		assertTrue(s.getMajor().equals("ME"));
		assertEquals(s.getYCPId(), 901001199);
		
		
	}
	
	@Test
	public void FindnInsertReviewsTest(){
		db.insertReview(1, 2, 4, "2017-07-04T12:00:00-05:00", "abc", "abcde");
		db.insertNewTedTalk(1, 1, "title", "description", "url");
		List<Review> r_a = new ArrayList<Review>();
		r_a = db.findReviewbyAuthor("Aaron", "Roby");
		
		
		//System.out.println(""+ r_a.size());
		assertTrue(r_a.get(1).getReview().equals("abc"));
	}
//	@Test
//	public void FindReviewTest1(){
//		List<Review> r_o = db.findReviewbyTopic("BS");
		
//		
//		assertTrue(r_o.get(0).getReview().equals("This is seriously the best TED Talk ever"));
		
//	}
	//@Test
//	public void FindReviewTest2(){
//		List<Review> r_t = db.findReviewbyTitle("A Guide To Masterful BS");
		
//		assertTrue(r_t.get(0).getReview().equals("This is seriously the best TED Talk ever"));
		
//	}
	@Test
	public void FindTedTalk(){
		List<TedTalk> t = new ArrayList<TedTalk>();
		t = db.findTedTalkbyTitle("A Guide To Masterful BS");
		assertTrue(!t.isEmpty());
		assertTrue(t.get(0).getTitle().equals("A Guide To Masterful BS"));
		
	}
	@Test
	public void FindnInsertTedTalk(){
		List<TedTalk> t = new ArrayList<TedTalk>();
		db.insertNewTedTalk(1, 4, "xba", "bab", "www.ted.com");
		t = db.findTedTalkbyTitle("xba");
		assertTrue(t.get(0).getDescription().equals("bab"));
	}
	@Test
	public void FindTopic(){
		Topic t = db.findTopic("Engineering");
		assertEquals(t.getTopic(),("Engineering"));
		
	}
}