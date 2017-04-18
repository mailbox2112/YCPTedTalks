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
	
	public void createNewAccount(String email, String password, String firstName, String lastName, boolean admin);
	public void createNewStudent(String email, String password, String firstName, String lastName, boolean admin, int id, String major);
	public Account findAccountbyEmail(String email);
	public Student findStudentbyId(int id);
	public Student findStudentbyLastName(String lastname);
	public void insertNewTedTalk();
	public void insertNewSpeaker();
	public void insertNewTopic();
	public void insertReview();
	public List<Review> findReviewsbyAuthor(String author);
	public List<Review> findReviewsbyTopic(String topic);
	public List<Review> findReviewsbyTitle(String title);
	public Topic findTopic(String topic);
	public List<TedTalk> findTedTalkbyAuthor(String search);
	public List<TedTalk> findTedTalkbyTopic(String search);
	public List<TedTalk> findTedTalkbyTitle(String search);
}
