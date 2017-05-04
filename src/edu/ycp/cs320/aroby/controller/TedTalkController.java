//come back to this later 

package edu.ycp.cs320.aroby.controller;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.persist.IDatabase;

public class TedTalkController {

	private Topic top;
	private Speaker speaker;
	private IDatabase db;
	private TedTalk model;
	
	public TedTalkController(){
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	
	}
	public void setModel(TedTalk model) {
		this.model = model;
	}
	
	public TedTalk getTedTalk(){
		return model;
	}

	
	public Boolean insertNewSpeaker(String firstname, String lastname){
		 db.insertNewSpeaker(firstname, lastname);
		 return true;
	}
	
	public Speaker findSpeaker(String firstname, String lastname){
		speaker = db.findSpeaker(firstname, lastname);
		return speaker;
	}
	
	public Boolean insertNewTopic(String top){
		db.insertNewTopic(top);
		return true;
	}
	
	public Topic findTopic(String topic){
		top = db.findTopic(topic);
		return top;
	}
	
	public ArrayList<Review> findReview(String title){
		ArrayList<Review> review = (ArrayList<Review>) db.findReviewbyTitle(title);
		return review;
	}
	
	public Boolean insertReview(int rating, String date, String review, String firstname, String lastname, String title){
		db.insertReview(rating, date, review, firstname, lastname, title);
		return true;
	}
	
	public Boolean insertNewTedTalk(String title, String description, String url, String firstname, String lastname, String topic){
		Boolean t = db.insertNewTedTalk(model.getTitle(), model.getDescription(), model.getLink(), firstname, lastname, topic);
		return t;
	}
	
	//may not need these methods because i dont need to search for anything
	public List<TedTalk> findTedTalkbyAuthor(String speaker){
		List<TedTalk> t1 = db.findTedTalkbySpeaker(speaker);
		return t1;
	}
	
	public TedTalk findTedTalkByID(int tedTalkID){
		TedTalk tid = db.findTedTalkByID(tedTalkID);
		return tid;
	}
	
	public TedTalk findTedTalkbyTitle(String title){
		TedTalk t2 = db.findTedTalkbyTitle(title);
		return t2;
	}

	public List<TedTalk> findTedTalkbyTopic(String topic){
		List<TedTalk> t3 = db.findTedTalkbyTopic(topic);
		return t3;
}

	public Account findAccount(int id) {
		Account a = db.findAccount(id);
		
		return a;
	}
	public List<Review> findReviewbyTitle(String title) {
		List<Review> t = db.findReviewbyTitle(title);
		
		return t;
	}
	
	public Topic findTopicByID(int topicID){
		return db.findTopicbyID(topicID);
	}
	
	public Speaker findSpeakerByID(int speakerID){
		return db.findSpeakerbyID(speakerID);
	}
	
	public List<Account> getAccountbyReviews(List<Review> reviews){
		List<Account> accounts = new ArrayList<Account>();
		List<Integer> acc_ids = new ArrayList<Integer>();
		for(Review review : reviews){
			Account acc = db.findAccount(review.getAccountId());
			if(!acc_ids.contains(acc.getAccountId())){
				acc_ids.add(acc.getAccountId());
				accounts.add(acc);
			}
		}
		return accounts;
	}
	
	public boolean deleteReview(int reviewId) {
		boolean result = db.deleteReview(reviewId);
		return result;
	}
}
