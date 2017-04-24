package edu.ycp.cs320.aroby.model;

public class Topic {
	private String topic;
	private int topicId;
	
	public Topic() {
		
	}
	
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topic_id) {
		this.topicId = topic_id;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
