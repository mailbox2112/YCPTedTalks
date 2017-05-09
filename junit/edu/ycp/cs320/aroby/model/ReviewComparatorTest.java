package edu.ycp.cs320.aroby.model;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.ycp.cs320.aroby.model.ReviewComparator;
import java.util.Comparator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.aroby.model.Review;
//set up tests
public class ReviewComparatorTest {
	private Review review1;
	private Review review2;
	private Review review3;
	private List<Review> rList = new ArrayList<Review>();
	
	// TODO: Fix these!
	@Before
	public void setUp() throws Exception {
		review1 = new Review();
		review2 = new Review();
		review3 = new Review();
		review1.setDate(LocalDateTime.of(2017, 8, 21, 7, 30).toString());
		review2.setDate(LocalDateTime.of(2016, 9, 9, 11, 47).toString());
		review3.setDate(LocalDateTime.now().toString());
		
	}
	@Test 
	public void TestSort(){
		rList.add(review1);
		rList.add(review2);
		rList.add(review3);
		assertTrue(rList.get(0) == review1);
		assertTrue(rList.get(1) == review2);
		assertTrue(rList.get(2) == review3);
		
		Collections.sort(rList, new ReviewComparator());
		assertTrue(rList.get(0) == review3);
		assertTrue(rList.get(1) == review2);
		assertTrue(rList.get(2) == review1);
	}

}
