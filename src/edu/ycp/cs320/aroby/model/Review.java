package edu.ycp.cs320.aroby.model;

import java.time.ZonedDateTime;

public class Review {
	private String name, author, topic, review, descript, link, recommendation;
	private int rating;
	private ZonedDateTime date;
	
	public void setReview(String name, String author, String topic, String descript, String review, String link, String recommendation, int rating) {
		this.topic = topic;
		this.review = review;
		this.descript = descript;
		this.name = name;
		this.author = author;
		this.link = link;
		this.rating = rating;
	}
	
	public void setTime (ZonedDateTime date){
		date = ZonedDateTime.now();
	}
	
	public String getAuthor(){
		return author;
	}
	public String getName(){
		return name;
	}
	
	public String getReview(){
		return review;
	}
	
	public String getTopic(){
		return topic;
	}
	
	public String getDescription(){
		return descript;
	}
	
	public String getLink(){
		return link;
	}
	
	public String getRecommendation(){
		return recommendation;
	}
	
	public int getRating(){
		return rating;
	}
	
	public ZonedDateTime getDate(){
		return date;
	}
}