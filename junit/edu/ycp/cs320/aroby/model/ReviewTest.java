package edu.ycp.cs320.aroby.model;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.model.Review;
//set up tests
public class ReviewTest {
	private Review model1;
	
	// TODO: Fix these!
	@Before
	public void setUp() throws Exception {
		model1 = new Review();
		
		model1.setAccountId(1);
		model1.setDate(ZonedDateTime.now().toString());
		model1.setRating(4);
		model1.setReview("I love this!!");
		model1.setReviewId(1);
		model1.setTedTalkId(3);
		
	}
	
	@Test
	public void test_getAccountId(){
		assertEquals(1, model1.getAccountId());
	}
	
	@Test
	public void test_getRating(){
		assertEquals(4, model1.getRating());
	}
	
	@Test
	public void test_getReview(){
		assertTrue(model1.getReview() != null);
	}
	
	@Test
	public void test_getReviewId(){
		assertEquals(1, model1.getReviewId());
	}
	
	@Test
	public void test_getTedTalkId(){
		assertEquals(3, model1.getTedTalkId());
	}
}
