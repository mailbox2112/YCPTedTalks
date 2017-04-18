//come back to this later 

package edu.ycp.cs320.aroby.controller;

import java.net.URL;
import java.util.ArrayList;

import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.TedTalk;

public class TedTalkController {

	private TedTalk ted_talk;
	
	public void set_TedTalk(String title, String description, int tedTalk_id, int speaker_id, int topic_id, URL link, ArrayList<Review> review){
		ted_talk.setDescription(description);
		ted_talk.setLink(link);
		ted_talk.setReview(review);
		ted_talk.setSpeakerId(speaker_id);
		ted_talk.setTedTalkId(tedTalk_id);
		ted_talk.setTitle(title);
		ted_talk.setTopicId(topic_id);	
	}
	
	public boolean exists(){
		if (ted_talk.getTedTalkId() == 0){
			return false;
		}
		else{
			return true;
		}
	}
}
