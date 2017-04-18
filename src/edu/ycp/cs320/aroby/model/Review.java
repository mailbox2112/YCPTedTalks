package edu.ycp.cs320.aroby.model;

import java.time.ZonedDateTime;

public class Review {
	private String review, recommendation;
	private int rating, account_id, tedTalk_id, review_id;
	private ZonedDateTime date;
	
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public ZonedDateTime getDate() {
		return date;
	}
	public void setDate(ZonedDateTime date) {
		this.date = date;
	}
	public int getTedTalkId() {
		return tedTalk_id;
	}
	public void setTedTalkId(int tedTalk_id) {
		this.tedTalk_id = tedTalk_id;
	}
	public int getAccountId() {
		return account_id;
	}
	public void setAccountId(int account_id) {
		this.account_id = account_id;
	}
	public int getReviewId() {
		return review_id;
	}
	public void setReviewId(int review_id) {
		this.review_id = review_id;
	}
}