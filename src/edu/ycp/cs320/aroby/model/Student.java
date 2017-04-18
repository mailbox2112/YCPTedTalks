package edu.ycp.cs320.aroby.model;

public class Student extends Account {
	private int ycp_id, student_id;
	private String major;
	
	public Student() {
	}
	
	public String getMajor() {
		return major;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}

	public int getYCPId() {
		return ycp_id;
	}

	public void setYCPId(int ycp_id) {
		this.ycp_id = ycp_id;
	}

	public int getStudentId() {
		return student_id;
	}

	public void setStudentId(int student_id) {
		this.student_id = student_id;
	}
}
