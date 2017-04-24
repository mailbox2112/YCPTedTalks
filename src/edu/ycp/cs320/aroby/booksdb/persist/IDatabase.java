package edu.ycp.cs320.aroby.booksdb.persist;

import java.net.URL;
import java.sql.Date;
import java.util.List;

import edu.ycp.cs320.aroby.booksdb.model.Author;
import edu.ycp.cs320.aroby.booksdb.model.Book;
import edu.ycp.cs320.aroby.booksdb.model.Pair;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.Student;

public interface IDatabase {
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(String title);
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(String lastName);
	public Integer insertBookIntoBooksTable(String title, String isbn, int published, String lastName, String firstName);
	public List<Pair<Author, Book>> findAllBooksWithAuthors();
	public List<Author> findAllAuthors();
	public List<Author> removeBookByTitle(String title);
	//all above from lab 6
	
	
	//Chihea's 
	public Boolean insertNewTedTalk(String title, String description, URL url, String firstname, String lastname, String topic);
	public Boolean insertNewSpeaker(String firstname, String lastname);
	public Boolean insertNewTopic(String top);
	public Boolean insertReview(int rating, String date, String review, String recommendations, String firstname, String lastname, String title);
	
	// Me
	public Boolean createNewAccount(String email, String password, String firstname, String lastname, boolean admin);
	public Boolean createNewStudent(int ycp_id, String major, String email);
	public Account findAccount(String email);
	public Account findAccount(int accountId);
	public Student findStudent(String email);
	public TedTalk findTedTalkByReview(Review review);
	
	// Me
	public List<Review> findReviewsbyAuthor(String firstname, String lastname);
	public List<Review> findReviewbyTopic(String topic);
	public List<Review> findReviewbyTitle(String title);
	public Topic findTopic(String topic);
	public Speaker findSpeaker(String firstname, String lastname);
	
	//Chihea
	public List<TedTalk> findTedTalkbyAuthor(String search);
	public List<TedTalk> findTedTalkbyTopic(String search);
	public TedTalk findTedTalkbyTitle(String search);
}
