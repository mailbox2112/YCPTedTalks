package edu.ycp.cs320.aroby.booksdb.persist;

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
import edu.ycp.cs320.aroby.model.Student;

public interface IDatabase {
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(String title);
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(String lastName);
	public Integer insertBookIntoBooksTable(String title, String isbn, int published, String lastName, String firstName);
	public List<Pair<Author, Book>> findAllBooksWithAuthors();
	public List<Author> findAllAuthors();
	public List<Author> removeBookByTitle(String title);
	//all above from lab 6
	
	public void createNewAccount(String email, String firstName, String lastName, String password, boolean admin);
	public void createNewStudent(int acc_id, String email, String major, int ycp_id);
	public Account findAccount(String email);
	public Student findStudentbyId(int id);
	public Student findStudent(String email);
	public void insertNewTedTalk(int speaker_id, int topic_id, String title, String description, String url);
	public void insertNewSpeaker(String firstname, String lastname);
	public void insertNewTopic(String topic);
	public void insertReview(int acc_id, int ted_id, int rating, String date, String review, String recommendation);
	public List<Review> findReviewbyAuthor( String firstname, String lastname);
	public List<Review> findReviewbyTopic(String topic);
	public List<Review> findReviewbyTitle(String title);
	public Topic findTopic(String topic);
	public List<TedTalk> findTedTalkbyAuthor(String search);
	public List<TedTalk> findTedTalkbyTopic(String search);
	public List<TedTalk> findTedTalkbyTitle(String search);
}
