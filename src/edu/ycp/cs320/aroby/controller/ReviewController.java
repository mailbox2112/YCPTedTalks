package edu.ycp.cs320.aroby.controller;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.persist.IDatabase;

// TODO: Fix this!
public class ReviewController {
	private Review model;
	private IDatabase db;

	/**
	 * Set the model.
	 * 
	 * @param model the model to set
	 */
	
	public ReviewController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	public void setModel(Review model) {
		this.model = model;
	}
	
	public Review getModel(){
		return model;
	}
	public Account findAccount(int accountId){
		return db.findAccount(accountId);
	}
	
	public TedTalk findTedTalk(String title){
		return db.findTedTalkbyTitle(title);
	}
	public Boolean insertReview(int rating, String date, String review, String firstname, String lastname, String title){
		return db.insertReview(rating, date, review, firstname, lastname, title);
	}
	
	public Speaker findSpeaker(String firstname, String lastname){
		return db.findSpeaker(firstname, lastname);
	}
	
	public Boolean isCorrect(){
		if(model.getAccountId() != 0 || model.getRating() > 0 || model.getRating() < 5 || model.getReview() != "" || model.getReviewId() != 0 
				|| model.getTedTalkId() != 0){
			return true;
		}
		else{
			return false;
		}
	}
}
