package edu.ycp.cs320.aroby.model;

import java.net.URL;
import java.util.ArrayList;

public class TedTalk {
	private URL link;
	private int tedTalkId, speakerId, topicId;
	private String title, description;
	private ArrayList<Review> review = new ArrayList<Review>();

	public URL getLink() {
		return link;
	}
	public void setLink(URL link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<Review> getReview() {
		return review;
	}
	
	public void setReview(ArrayList<Review> review) {
		this.review = review;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getTedTalkId() {
		return tedTalkId;
	}

	public void setTedTalkId(int tedTalk_id) {
		this.tedTalkId = tedTalk_id;
	}
	
	public int getTopicId() {
		return topicId;
	}
	
	public void setTopicId(int topic_id) {
		this.topicId = topic_id;
	}
	
	public int getSpeakerId() {
		return speakerId;
	}
	
	public void setSpeakerId(int speaker_id) {
		this.speakerId = speaker_id;
	}
}
