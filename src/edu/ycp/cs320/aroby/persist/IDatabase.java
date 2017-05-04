package edu.ycp.cs320.aroby.persist;

import java.net.URL;
import java.sql.Date;
import java.util.List;

import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.Student;

public interface IDatabase {
	//Chihea's 
	public Boolean insertNewTedTalk(String title, String description, String url, String firstname, String lastname, String topic); // tested
	public Boolean insertNewSpeaker(String firstname, String lastname); // tested
	public Boolean insertNewTopic(String top); // tested
	public Boolean insertReview(int rating, String date, String review, String firstname, String lastname, String title); // tested
	public List<TedTalk> findTedTalkbySpeaker(String search); // tested
	public List<TedTalk> findTedTalkbyTopic(String search); // tested
	public TedTalk findTedTalkbyTitle(String search); // tested
	public TedTalk findTedTalkByID(int tedTalkID); // tested
	public Topic findTopicbyID(int topicID); // tested
	public Speaker findSpeakerbyID(int speakerID); // tested

	
	// Me
	public Boolean createNewAccount(String email, String password, String firstname, String lastname, boolean admin); // tested
	public Boolean createNewStudent(int ycp_id, String major, String email); // tested
	public Account findAccount(String email); // tested
	public Account findAccount(int accountId); // tested
	public Student findStudent(String email); // tested
	public TedTalk findTedTalkByReview(Review review); 
	public List<Topic> getAllTopics(); // tested
	public List<Review> findReviewsbyAuthor(String firstname, String lastname); // tested
	public List<Review> findReviewbyTopic(String topic); // tested
	public List<Review> findReviewbyTitle(String title); // tested
	public Topic findTopic(String topic); // tested
	public Speaker findSpeaker(String firstname, String lastname); // tested
	public Speaker findSpeakerFromTedTalk(int speakerId); 
	public Boolean deleteTedTalk(int tedTalkId); // tested
	public Boolean deleteReview(int reviewId); // tested
	public Boolean changePassword(int accountId, String password); // tested
	public Boolean changeEmail(int accountId, String email); // tested
	public Boolean changeMajor(int studentId, String major); // tested
	public Boolean deleteTables(); // tested indirectly
	public void loadInitialData(); // tested indirectly
	public void createTables(); // tested indirectly

}
