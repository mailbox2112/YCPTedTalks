package edu.ycp.cs320.aroby.model;

public class Speaker {
	private int speaker_id;
	private String firstname, lastname;
	public int getSpeakerId() {
		return speaker_id;
	}
	public void setSpeakerId(int speaker_id) {
		this.speaker_id = speaker_id;
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
