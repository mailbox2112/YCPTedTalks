package edu.ycp.cs320.aroby.model;

public class Topic {
	private String topic;
	private int topic_id;
	
	public Topic() {
		
	}
	
	public int getTopicId() {
		return topic_id;
	}
	public void setTopicId(int topic_id) {
		this.topic_id = topic_id;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
