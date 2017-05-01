package edu.ycp.cs320.aroby.model;

import java.time.ZonedDateTime;
import java.util.Comparator;

public class Review {
	private String review, recommendation;
	private int rating, accountId, tedTalkId, reviewId;
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
	public String getDate() {
		date = ZonedDateTime.now();
		return date.toString();
	}
	public void setDate(ZonedDateTime date) {
		this.date = date;
	}
	public int getTedTalkId() {
		return tedTalkId;
	}
	public void setTedTalkId(int tedTalk_id) {
		this.tedTalkId = tedTalk_id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int account_id) {
		this.accountId = account_id;
	}
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int review_id) {
		this.reviewId = review_id;
	}
}