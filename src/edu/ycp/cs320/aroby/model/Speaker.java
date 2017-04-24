package edu.ycp.cs320.aroby.model;

public class Speaker {
	private int speakerId;
	private String firstname, lastname;
	public int getSpeakerId() {
		return speakerId;
	}
	public void setSpeakerId(int speaker_id) {
		this.speakerId = speaker_id;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
}
