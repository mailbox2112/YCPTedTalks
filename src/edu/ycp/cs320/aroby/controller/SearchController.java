package edu.ycp.cs320.aroby.controller;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.persist.IDatabase;

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
		List<Integer> accountIds = new ArrayList<Integer>();
		for(Review review : reviews) {
			Account account = db.findAccount(review.getAccountId());
			if(!accountIds.contains(account.getAccountId())) {
				accountIds.add(account.getAccountId());
				accounts.add(account);
			}
		}
		
		return accounts;
	}
	
	public List<Topic> getTopics() {
		List<Topic> topics = new ArrayList<Topic>();
		topics = db.getAllTopics();
		return topics;
	}
	
	public Speaker getSpeakerFromTedTalk(TedTalk talk) {
		Speaker speaker = new Speaker();
		speaker = db.findSpeakerFromTedTalk(talk.getSpeakerId());
		
		return speaker;
	}
	
	public boolean deleteTedTalk(int tedTalkId) {
		boolean result = db.deleteTedTalk(tedTalkId);
		return result;
	}
}
