package edu.ycp.cs320.aroby.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.model.Review;
//set up tests
public class ReviewTest {
	private Review model1;
	private Review model2;
	private Review model3;
	
	// TODO: Fix these!
	@Before
	public void setUp() throws Exception {
		model1 = new Review();
		model2 = new Review();
		model3 = new Review();
		
		//model1.setReview("clocke3", "bab", "Bees", "They're dying.", "I really liked it.", "https://drive.google.com/drive/u/1/my-drive", "I hate it", 2);
		//model2.setReview("aroby", "cab", "Trims", "I need one.", "I can do it by myself.", "http://www.sqlcourse2.com/having.html", "10/10 would recommend",5);
		//model3.setReview("tclrk", "dab", "Sustainability", "We need it.", "I'm interested in it.", "http://www.w3schools.com/html/html_examples.asp","I think the info was lacking", 3);
	}
	
	@Test
	public void test_getAuthor(){
		//assertEquals("bab", model1.getAuthor());
		//assertEquals("cab", model2.getAuthor());
		//assertEquals("dab", model3.getAuthor());
	}
	
	@Test
	public void test_getName(){
		//assertEquals("clocke3", model1.getName());
		//assertEquals("aroby", model2.getName());
		//assertEquals("tclrk", model3.getName());
	}
	
	@Test
	public void test_getTopic(){
		//assertEquals("Bees", model1.getTopic());
		//assertEquals("Trims", model2.getTopic());
		//assertEquals("Sustainability", model3.getTopic());
	}
	
	@Test
	public void test_getDescription(){
		//assertEquals("They're dying.", model1.getDescription());
		//assertEquals("I need one.", model2.getDescription());
		//assertEquals("We need it.", model3.getDescription());
	}
	
	@Test
	public void test_getReview(){
		assertEquals("I really liked it.", model1.getReview());
		assertEquals("I can do it by myself.", model2.getReview());
		assertEquals("I'm interested in it.", model3.getReview());
	}
	
	@Test
	public void test_getLink(){
		//assertEquals("https://drive.google.com/drive/u/1/my-drive", model1.getLink());
		//assertEquals("http://www.sqlcourse2.com/having.html", model2.getLink());
		//assertEquals("http://www.w3schools.com/html/html_examples.asp", model3.getLink());
	}
	
	@Test
	public void test_getRecommendation(){
		assertEquals("I hate it", model1.getRecommendation());
		assertEquals("10/10 would recommend", model2.getRecommendation());
		assertEquals("http://www.w3schools.com/html/html_examples.asp", model3.getRecommendation());
	}

	@Test
	public void test_getRating(){
		assertTrue(model1.getRating() == 2);
		assertTrue(model2.getRating() == 5);
		assertTrue(model3.getRating() == 3);
	}
}