package edu.ycp.cs320.aroby.model;

public class Professor extends Account {
	private int professor_id;
	
	public Professor() {
	}
	
	public int getProfessorID() {
		return professor_id;
	}
	
	public void setProfessorID(int professor_id) {
		this.professor_id = professor_id;
	}
}
