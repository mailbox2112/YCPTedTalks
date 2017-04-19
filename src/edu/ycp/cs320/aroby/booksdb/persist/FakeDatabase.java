package edu.ycp.cs320.aroby.booksdb.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.aroby.booksdb.model.Author;
import edu.ycp.cs320.aroby.booksdb.model.Book;
import edu.ycp.cs320.aroby.booksdb.model.Pair;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.Student;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class FakeDatabase implements IDatabase {
	
	private List<Author> authorList;
	private List<Book> bookList;
	private List<Account> accountList;
	private List<Review> reviewList;
	private List<Speaker> speakerList;
	private List<Student> studentList;
	private List<TedTalk> tedtalkList;
	private List<Topic> topicList;
	
	
	// Fake database constructor - initializes the DB
	// the DB only consists for a List of Authors and a List of Books
	public FakeDatabase() {
		authorList = new ArrayList<Author>();
		bookList = new ArrayList<Book>();
		accountList = new ArrayList<Account>();
		reviewList = new ArrayList<Review>();
		speakerList = new ArrayList<Speaker>();
		studentList = new ArrayList<Student>();
		tedtalkList = new ArrayList<TedTalk>();
		topicList = new ArrayList<Topic>();
		
		// Add initial data
		readInitialData();
		
//		System.out.println(authorList.size() + " authors");
//		System.out.println(bookList.size() + " books");
	}

	// loads the initial data retrieved from the CSV files into the DB
	public void readInitialData() {
		try {
			authorList.addAll(InitialData.getAuthors());
			bookList.addAll(InitialData.getBooks());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	
	// query that retrieves Book and its Author by Title
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(String title) {
		List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
		for (Book book : bookList) {
//			System.out.println("Book: <" + book.getTitle() + ">" + "  Title: <" + title + ">");
			
			if (book.getTitle().equals(title)) {
				Author author = findAuthorByAuthorId(book.getAuthorId());
				result.add(new Pair<Author, Book>(author, book));
			}
		}
		return result;
	}
	
	// query that retrieves all Books, for the Author's last name
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(String lastName)
	{
		// create list of <Author, Book> for returning result of query
		List<Pair<Author, Book>> result = new ArrayList<Pair<Author, Book>>();
		
		// search through table of Books
		for (Book book : bookList) {
			for (Author author : authorList) {
				if (book.getAuthorId() == author.getAuthorId()) {
					if (author.getLastname().equals(lastName)) {
						// if this book is by the specified author, add it to the result list
						result.add(new Pair<Author, Book>(author, book));						
					}
				}
			}
		}
		return result;
	}

	
	// query that retrieves all Books, with their Authors, from DB
	public List<Pair<Author, Book>> findAllBooksWithAuthors() {
		List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
		for (Book book : bookList) {
			Author author = findAuthorByAuthorId(book.getAuthorId());
			result.add(new Pair<Author, Book>(author, book));
		}
		return result;
	}
		

	// query that retrieves all Authors from DB
	public List<Author> findAllAuthors() {
		List<Author> result = new ArrayList<Author>();
		for (Author author : authorList) {
			result.add(author);
		}
		return result;
	}
	
	
	// query that inserts a new Book, and possibly new Author, into Books and Authors lists
	// insertion requires that we maintain Book and Author id's
	// this can be a real PITA, if we intend to use the IDs to directly access the ArrayLists, since
	// deleting a Book/Author in the list would mean updating the ID's, since other list entries are likely to move to fill the space.
	// or we could mark Book/Author entries as deleted, and leave them open for reuse, but we could not delete an Author
	//    unless they have no Books in the Books table
	public Integer insertBookIntoBooksTable(String title, String isbn, int published, String lastName, String firstName)
	{
		int authorId = -1;
		int bookId   = -1;
		
		// search Authors list for the Author, by first and last name, get author_id
		for (Author author : authorList) {
			if (author.getLastname().equals(lastName) && author.getFirstname().equals(firstName)) {
				authorId = author.getAuthorId();
			}
		}
		
		// if the Author wasn't found in Authors list, we have to add new Author to Authors list
		if (authorId < 0) {
			// set author_id to size of Authors list + 1 (before adding Author)
			authorId = authorList.size() + 1;
			
			// add new Author to Authors list
			Author newAuthor = new Author();			
			newAuthor.setAuthorId(authorId);
			newAuthor.setLastname(lastName);
			newAuthor.setFirstname(firstName);
			authorList.add(newAuthor);
			
			System.out.println("New author (ID: " + authorId + ") " + "added to Authors table: <" + lastName + ", " + firstName + ">");
		}

		// set book_id to size of Books list + 1 (before adding Book)
		bookId = bookList.size() + 1;

		// add new Book to Books list
		Book newBook = new Book();
		newBook.setBookId(bookId);
		newBook.setAuthorId(authorId);
		newBook.setTitle(title);
		newBook.setIsbn(isbn);
		newBook.setPublished(published);
		bookList.add(newBook);
		
		// return new Book Id
		return bookId;
	}
	
	//not implemented in FakeDB
	public List<Author> removeBookByTitle(final String title) {
		List<Author> authors = new ArrayList<Author>();
		
		return authors;
	}
	

	// query that retrieves an Author based on author_id
	private Author findAuthorByAuthorId(int authorId) {
		for (Author author : authorList) {
			if (author.getAuthorId() == authorId) {
				return author;
			}
		}
		return null;
	}


	public List<TedTalk> findTedTalkbyAuthor(String search) {
		Speaker speaker = null;
		List<TedTalk> result = new ArrayList<TedTalk>();
		for (Speaker s: speakerList){
			if(s.getFirstname().contains(search) || s.getLastname().contains(search)){
				speaker = s;
			}
		}
		for (TedTalk t : tedtalkList) {
			
		//	System.out.println(": "+ search + " - " + t.getSpeakerId() + "/" + t.getTedTalkId() + "/" + t.getTitle() + "/" + t.getTopicId());
			if (t.getSpeakerId() == speaker.getSpeakerId()){
				result.add(t);
			}
		}
		return result;
	}

	public List<TedTalk> findTedTalkbyTopic(String search) {
		List<TedTalk> result = new ArrayList<TedTalk>();
		Topic topic = null;
		for (Topic top : topicList){
			if(top.getTopic() == search){
				topic = top;
			}
		}
		for (TedTalk t : tedtalkList) {
			
		//	System.out.println(": "+ search + " - " + t.getSpeakerId() + "/" + t.getTedTalkId() + "/" + t.getTitle() + "/" + t.getTopicId());
			if (t.getTopicId() == topic.getTopicId()){
				result.add(t);
			}
		}
		return result;
  }
    public List<TedTalk> findTedTalkbyTitle(String search) {
		// TODO Auto-generated method stub
		List<TedTalk> result = new ArrayList<TedTalk>();
		for (TedTalk t : tedtalkList) {
			System.out.println(": "+ search + " - " + t.getSpeakerId() + "/" + t.getTedTalkId() + "/" + t.getTitle() + "/" + t.getTopicId());
			
			if (t.getTitle().contains(search)) {
				result.add(t);
			}
		}
		return result;
	}

	public Boolean createNewAccount(String email, String password, String firstname, String lastname, boolean admin) {
		// TODO Auto-generated method stub
  		// TODO Auto-generated method stub
		Account account = new Account();
		account.setAccountId(accountList.size()+1);
		account.setAdmin(admin);
		account.setEmail(email);
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setPassword(password);
    int i =accountList.size();
		accountList.add(account);
		if (accountList.size() >i){
      return true;
    }
  else{
    return false;
    }
	}

	public Boolean createNewStudent(int ycp_id, String major, String email) {
			Student student = new Student();
		student.setEmail(email);
		student.setMajor(major);
		student.setYCPId(ycp_id);
    int i = studentList.size();
		studentList.add(student);
		if studentList.size() >i){
      return true;
    }
  else{
    return false;
    }
	}

	public Account findAccount(String email) {
		// TODO Auto-generated method stub
		List<TedTalk> result = new ArrayList<TedTalk>();
		for (TedTalk t : tedtalkList){
			
			if (t.getTitle().contains(search)) {
				result.add(t);
			}
		}
		return result;
	}



	public Student findStudent(String email) {
		Student student = null;
		for(Student s : studentList){
			if(s.getEmail == email){
				student = s;
			}
		}
		return student;

	}
		
	public Account findAccount(String email) {
		// TODO Auto-generated method stub
		Account account = null;
		for (Account a : accountList) {			
			if (a.getEmail() == email) {
				account = a;
			}
		}
		return account;
	}

	public Student findStudentbyId(int id) {
		// TODO Auto-generated method stub
		Student student = null;
		for(Student s : studentList){
			if (s.getYCPId() == id){
				student = s;
			}
		}
		return student;
	}

	public Student findStudent(String email) {
		// TODO Auto-generated method stub
		Student student = null;
		for(Student s : studentList){
			if(s.getEmail() == email){
				student = s;
			}
		}
		return student;
	}
    	public List<Review> findReviewbyTitle(String title) {
		// TODO Auto-generated method stub
		List<Review> review = new ArrayList<Review>();
		List<Review> result = new ArrayList<Review>();
		for (TedTalk t : tedtalkList) {
		//	System.out.println(": "+ search + " - " + t.getSpeakerId() + "/" + t.getTedTalkId() + "/" + t.getTitle() + "/" + t.getTopicId());
			
			if (t.getTitle().contains(title)) {
				review = t.getReview();
				for(int i = 0; i<review.size(); i++){
					result.add(review.get(i));
				}
			}
		}
		return result;

	public List<Review> findReviewbyAuthor(String firstname, String lastname) {
		List<Review> review = new ArrayList<Review>();
		List<Review> result = new ArrayList<Review>();
		int acc_id = 0;
		for(Account acc : accountList){
			if(acc.getLastName() == lastname && acc.getFirstName() == firstname){
				acc_id = acc.getAccountId();
			}
		}
		for (TedTalk t : tedtalkList) {
		//	System.out.println(": "+ search + " - " + t.getSpeakerId() + "/" + t.getTedTalkId() + "/" + t.getTitle() + "/" + t.getTopicId());
			review = t.getReview();
			for(int i=0; i<review.size(); i++){
				int id = review.get(i).getAccountId();
				if (id == acc_id){
					result.add(review.get(i));
				}
			}
		}
		
		return result;
	}

	public List<Review> findReviewbyTopic(String topic) {
		// TODO Auto-generated method stub
		List<Review> review = new ArrayList<Review>();
		List<Review> result = new ArrayList<Review>();
		int top_id = 0;
		for(Topic t : topicList){
			if(t.getTopic().equals(topic)){
				top_id = t.getTopicId();
			}
		}
		for(TedTalk t :tedtalkList){
			review = t.getReview();
			if (top_id == t.getTopicId()){
				
				for(int i=0;i<review.size();i++){
					result.add(review.get(i));
				
				}
			}
		}
		return result;
	}


	public Topic findTopic(String topic) {
		// TODO Auto-generated method stub
		Topic t = null;
		
		for(Topic d : topicList){
			if(d.getTopic() == topic){
				t = d;
			}
		}
		return topic;
	}



	public void insertNewTedTalk(int speaker_id, int topic_id, String title, String description, String url) {

		// TODO Auto-generated method stub
		TedTalk t = new TedTalk();
		t.setDescription(description);
		t.setSpeakerId(speaker_id);
		t.setTopicId(topic_id);
		t.setTitle(title);
		t.setLink(url);
		tedtalkList.add(t);
	}

	public void insertNewSpeaker(String firstname, String lastname) {

		// TODO Auto-generated method stub
		Speaker s = new Speaker();
		s.setFirstname(firstname);
		s.setLastname(lastname);
		speakerList.add(s);
	}


	public void insertNewTopic(String topic) {

		// TODO Auto-generated method stub
		Topic t = new Topic();
		t.setTopic(topic);
		topicList.add(t);
	}

	public void insertReview(int acc_id, int ted_id, int rating,String date, String review, String recommendation) {

		// TODO Auto-generated method stub
		Review r = new Review();
		r.setRating(rating);
		r.setRecommendation(recommendation);
		r.setReview(review);
		r.setTedTalkId(ted_id);
		r.setAccountId(acc_id);
		r.setDate(date);
	}

	
}
