package edu.ycp.cs320.aroby.model;

import java.sql.Time;

public class ReadAReview {
	private String topic, reviewText, name, title, author, descript, reflection;
	private double rating;
	private Time date;
	private String link;
	
	
	public ReadAReview(String name, String author, String topic, String descript, String reviewText, String link) {
		this.name = name;
		this.author = author;
		this.topic = topic;
		this.descript = descript;
		this.reviewText = reviewText;
		this.link = link;
		
	}
	
	public void setTopic(String topic){
		this.topic = topic;
	}
	public void setReviewText(String reviewText){
		this.reviewText = reviewText;
	}
	public void setRating(Double rating){
		this.rating = rating;
	}
	public void setDate(Time date){
		this.date = date;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setAuthor(String author){
		this.author = author;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setDescription(String descript){
		this.descript = descript;
	}
	public void setReflection(String reflection){
		this.reflection = reflection;
	}
	public void setLink(String link){
		this.link = link;
	}
	public String getTopic(){
		return topic;
	}
	public String getReviewText(){
		return reviewText;
	}
	public double getRating(){
		return rating;
	}
	public Time getDate(){
		return date;
	}
	public String getName(){
		return name;
	}
	public String getAuthor(){
		return author;
	}
	public String getTitle(){
		return title;
	}
	public String getDescript(){
		return descript;
	}
	public String getReflection(){
		return reflection;
	}
	public String getLink(){
		return link;
	}
}