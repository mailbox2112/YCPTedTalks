package edu.ycp.cs320.aroby.model;

import java.net.URL;
import java.util.ArrayList;

public class TedTalk {
	private String link;
	private int tedTalk_id, speaker_id, topic_id;
	private String title, description;
	private ArrayList<Review> review = new ArrayList<Review>();

	public String getLink() {
		return link;
	}
	public void setLink(String url) {
		this.link = url;
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
		return tedTalk_id;
	}

	public void setTedTalkId(int tedTalk_id) {
		this.tedTalk_id = tedTalk_id;
	}
	
	public int getTopicId() {
		return topic_id;
	}
	
	public void setTopicId(int topic_id) {
		this.topic_id = topic_id;
	}
	
	public int getSpeakerId() {
		return speaker_id;
	}
	
	public void setSpeakerId(int speaker_id) {
		this.speaker_id = speaker_id;
	}
}
