package edu.ycp.cs320.aroby.controller;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.aroby.booksdb.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.booksdb.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.booksdb.persist.IDatabase;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.TedTalk;

public class SearchController {
	private Search model;
	private IDatabase db;
	
	public SearchController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	public void setModel(Search model) {
		this.model = model;
	}

	public Search getSearch(){
		model.setSearch(model.getSearch());
		return model;
	}
	
	public List<Review> findReviewsByAuthor(String firstname, String lastname) {
		List<Review> reviews = db.findReviewsbyAuthor(firstname.toLowerCase(), lastname.toLowerCase());
		
		return reviews;
	}
	
	public List<Review> findReviewsByTopic(String topic) {
		List<Review> reviews = db.findReviewbyTopic(topic.toLowerCase());
		
		return reviews;
	}
	
	public List<Review> findReviewsByTitle(String title) {
		List<Review> reviews  = db.findReviewbyTitle(title.toLowerCase());
		
		return reviews;
	}
	
	public TedTalk getTedTalkFromReview(Review review) {
		TedTalk tedTalk = db.findTedTalkByReview(review);
		return tedTalk;
	}
	
	public List<Account> getAccountFromReview(List<Review> reviews) {
		List<Account> accounts = new ArrayList<Account>();
		for(Review review : reviews) {
			Account account = db.findAccount(review.getAccountId());
			if(!accounts.contains(account)) {
				accounts.add(account);
			}
		}
		
		return accounts;
	}
}
