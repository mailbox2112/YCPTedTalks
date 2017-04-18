package edu.ycp.cs320.aroby.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.model.Review;

public class ReviewControllerTest {
	private Review model1, model2;
	private ReviewController controller1, controller2;
	
	@Before
	public void setUp() throws Exception {
		model1 = new Review();
		model2 = new Review();
		controller1 = new ReviewController();
		controller2 = new ReviewController();
		
		//model1.setReview("A", "Agatha Christie", "Murder", "She writes suspenseful books.", "I like her books.", "https://en.wikipedia.org/wiki/Agatha_Christie", "Absolute read", 4);
		//model2.setReview("A", "", "Murder", "", "I like her books.", "https://en.wikipedia.org/wiki/Agatha_Christie", "", 1);
		
		controller1.setModel(model1);
		controller2.setModel(model2);
		
		controller1.getReview();
		controller2.getReview();
	}
	
	@Test
	public void test_isDone(){
		assertTrue(controller1.isDone());
		assertFalse(!controller2.isDone());
	}
}