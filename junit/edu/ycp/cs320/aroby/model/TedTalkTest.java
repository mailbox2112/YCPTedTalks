package edu.ycp.cs320.aroby.model;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TedTalkTest {
		private TedTalk talk1;
	
		@Before
	public void setUp() throws Exception{
		talk1 = new TedTalk();
		Review converter1 = new Review();
		Review converter2 = new Review();
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		String title1 = "Angina: How to Stop It";
		String description1 = "Laurie shares her experiences with heart attacks.";
		
		String link_1 = "https://www.example.com/docs/resource1.html";
		
		String review1 = "She is very heartwarming and her talk was wonderuful.";
		String review2 = "I don't understand this discussion.";
		
		converter1.setReview(review1);
		converter2.setReview(review2);
		reviews.add(converter1);
		reviews.add(converter2);

		talk1.setReview(reviews);
		talk1.setDescription(description1);
		talk1.setLink(link_1);
		talk1.setTitle(title1);
		talk1.setTopicId(2);
		talk1.setTedTalkId(3);
		talk1.setSpeakerId(2);
	}
		
	@Test
	public void test_getLink() throws MalformedURLException{
		assertEquals("https://www.example.com/docs/resource1.html", talk1.getLink());
	}
	
	@Test
	public void test_getDescription(){
		assertEquals("Laurie shares her experiences with heart attacks.", talk1.getDescription());
	}

	
	@Test
	public void test_getReview(){
		assertTrue(talk1.getReview().size() == 2);
	}

	
	@Test
	public void test_getTitle(){
		assertEquals("Angina: How to Stop It", talk1.getTitle());
	}

	
	@Test
	public void test_getTedTalkId(){
		assertEquals(3, talk1.getTedTalkId());
	}

	@Test
	public void test_getSpeakerId(){
		assertEquals(2, talk1.getSpeakerId());
	}
	
	@Test
	public void test_getTopicId(){
		assertEquals(2, talk1.getTopicId());
	}


}
