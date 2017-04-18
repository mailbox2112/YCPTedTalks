package edu.ycp.cs320.aroby.controller;

import edu.ycp.cs320.aroby.model.Review;

// TODO: Fix this!
public class ReviewController {
	private Review model;

	/**
	 * Set the model.
	 * 
	 * @param model the model to set
	 */
	public void setModel(Review model) {
		this.model = model;
	}
	
	public Review getReview(){
		//model.setReview(model.getName(),model.getAuthor(), model.getTopic(), model.getDescription(), model.getReview(), model.getLink(), model.getRecommendation(), model.getRating());
		//model.getDate();
		return model;
	}
	
	public boolean isDone(){
		//if all the forms are filled out
		/*if(model.getReview() != null || model.getDescription() != null|| model.getTopic() != null ||  model.getName() != null ||  model.getAuthor() != null ||  model.getLink() != null || model.getRecommendation() != null || model.getRating() > 0){
			return true;
		}
		else{
			return false;
		}*/
		return true;
	}
}
