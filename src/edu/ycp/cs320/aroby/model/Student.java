package edu.ycp.cs320.aroby.model;

public class Student extends Account {
	private int ycpId, studentId;
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
		return ycpId;
	}

	public void setYCPId(int ycp_id) {
		this.ycpId = ycp_id;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int student_id) {
		this.studentId = student_id;
	}
}
