
package edu.ycp.cs320.aroby.persist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.Student;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}

	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;


	// wrapper SQL transaction function that calls actual transaction function
	// (which has retries)
	public <ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}

	// SQL transaction function which retries the transaction MAX_ATTEMPTS times
	// before failing
	public <ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();

		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;

			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been
						// reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}

			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}

			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	// TODO: Here is where you specify the location of your Derby SQL database
	// TODO: You will need to change this location to the same path as your
	// workspace for this example
	// TODO: Change it here and in SQLDemo under
	// CS320_Lab06->edu.ycp.cs320.sqldemo
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby: c:/CS 320/library.db;create=true");

		// Set autocommit() to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}

	private void loadAccount(Account account, ResultSet resultSet, int index) throws SQLException {
		account.setAccountId(resultSet.getInt(index++));
		account.setEmail(resultSet.getString(index++));
		account.setPassword(resultSet.getString(index++));
		account.setFirstName(WordUtils.capitalize(resultSet.getString(index++)));
		account.setLastName(WordUtils.capitalize(resultSet.getString(index++)));
		account.setAdmin(resultSet.getBoolean(index++));
	}

	private void loadStudent(Student student, ResultSet resultSet, int index, Account account) throws SQLException {
		student.setStudentId(resultSet.getInt(index++));
		student.setAccountId(resultSet.getInt(index++));
		student.setYCPId(resultSet.getInt(index++));
		student.setMajor(resultSet.getString(index++));
		student.setEmail(account.getEmail());
		student.setAdmin(account.getAdmin());
		student.setFirstName(WordUtils.capitalize(account.getFirstName()));
		student.setLastName(WordUtils.capitalize(account.getLastName()));
		student.setPassword(account.getPassword());
	}

	private void loadTopic(Topic topic, ResultSet resultSet, int index) throws SQLException {
		topic.setTopicId(resultSet.getInt(index++));
		topic.setTopic(WordUtils.capitalize(resultSet.getString(index++)));
	}

	private void loadSpeaker(Speaker speaker, ResultSet resultSet, int index) throws SQLException {
		speaker.setSpeakerId(resultSet.getInt(index++));
		speaker.setFirstname(WordUtils.capitalize(resultSet.getString(index++)));
		speaker.setLastname(WordUtils.capitalize(resultSet.getString(index++)));
	}

	private void loadReview(Review review, ResultSet resultSet, int index) throws SQLException {
		review.setReviewId(resultSet.getInt(index++));
		review.setAccountId(resultSet.getInt(index++));
		review.setTedTalkId(resultSet.getInt(index++));
		review.setRating(resultSet.getInt(index++));
		review.setDate((resultSet.getString(index++)));
		review.setReview(resultSet.getString(index++));
	}

	private void loadTedTalk(TedTalk talk, ResultSet resultSet, int index) throws SQLException, MalformedURLException {
		talk.setTedTalkId(resultSet.getInt(index++));
		talk.setSpeakerId(resultSet.getInt(index++));
		talk.setTopicId(resultSet.getInt(index++));
		talk.setTitle(WordUtils.capitalize(resultSet.getString(index++)));
		talk.setDescription(resultSet.getString(index++));
		talk.setLink(resultSet.getString(index++));
	}

	// creates the Authors and Books tables
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;
				PreparedStatement stmt7 = null;
				PreparedStatement stmt8 = null;
				PreparedStatement stmt9 = null;

				try {
					// Create accounts table
					stmt4 = conn.prepareStatement("create table accounts (" + "	account_id integer primary key"
							+ " 		generated always as identity (start with 1, increment by 1),"
							+ "	email varchar(70)," + "	password varchar(70)," + "	firstname varchar(70),"
							+ "	lastname varchar(70)," + "	admin boolean" + ")");

					stmt4.executeUpdate();
					System.out.println("Account table created");

					// Create students table
					stmt5 = conn.prepareStatement("create table students (" + "	student_id integer primary key"
							+ "		generated always as identity (start with 1, increment by 1),"
							+ " account_id integer constraint account_id references accounts," + " ycp_id integer,"
							+ " major varchar(70)" + ")");

					stmt5.executeUpdate();
					System.out.println("Students table created");

					// Create topics table
					stmt6 = conn.prepareStatement("create table topics (" + "topic_id integer primary key"
							+ "	generated always as identity (start with 1, increment by 1)," + "topic varchar(70)"
							+ ")");
					stmt6.executeUpdate();
					System.out.println("Topics table created");

					// Create speakers table
					stmt7 = conn.prepareStatement("create table speakers (" + " 	speaker_id integer primary key"
							+ "		generated always as identity (start with 1, increment by 1),"
							+ "firstname varchar(70)," + "lastname varchar(70)" + ")");
					stmt7.executeUpdate();
					System.out.println("Speakers table created");

					// Create ted talks table
					stmt8 = conn.prepareStatement("create table tedtalks (" + "	tedtalk_id integer primary key"
							+ "		generated always as identity (start with 1, increment by 1),"
							+ "speaker_id integer constraint speaker_id references speakers,"
							+ "topic_id integer constraint topic_id references topics," + "title varchar(70),"
							+ "description varchar(1000)," + "url varchar(140)" + ")");

					stmt8.executeUpdate();
					System.out.println("TedTalks table created");

					// Create reviews table
					stmt9 = conn.prepareStatement("create table reviews (" + "		review_id integer primary key"
							+ "			generated always as identity (start with 1, increment by 1),"
							+ "account_id integer references accounts,"
							+ "tedtalk_id integer constraint tedtalk_id references tedtalks on delete cascade," + "rating integer,"
							+ "date varchar(70)," + "review varchar(500)" + ")");

					stmt9.executeUpdate();
					System.out.println("Review table created");

					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
					DBUtil.closeQuietly(stmt7);
					DBUtil.closeQuietly(stmt8);
					DBUtil.closeQuietly(stmt9);
				}
			}
		});
	}

	// loads data retrieved from CSV files into DB tables in batch mode
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				List<Account> accountList;
				List<Student> studentList;
				List<Topic> topicList;
				List<Speaker> speakerList;
				List<TedTalk> tedtalkList;
				List<Review> reviewList;

				try {
					accountList = InitialData.getAccounts();
					studentList = InitialData.getStudents();
					topicList = InitialData.getTopics();
					speakerList = InitialData.getSpeakers();
					tedtalkList = InitialData.getTedTalks();
					reviewList = InitialData.getReviews();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertAuthor = null;
				PreparedStatement insertBook = null;
				PreparedStatement insertBookAuthor = null;
				PreparedStatement insertAccounts = null;
				PreparedStatement insertStudents = null;
				PreparedStatement insertTopics = null;
				PreparedStatement insertSpeakers = null;
				PreparedStatement insertTedTalks = null;
				PreparedStatement insertReviews = null;

				try {
					// Insert the accounts into the accounts table
					insertAccounts = conn.prepareStatement(
							"insert into accounts (email, password, firstname, lastname, admin) values (?,?,?,?,?)");
					for (Account account : accountList) {
						insertAccounts.setString(1, account.getEmail());
						insertAccounts.setString(2, account.getPassword());
						insertAccounts.setString(3, account.getFirstName().toLowerCase());
						insertAccounts.setString(4, account.getLastName().toLowerCase());
						insertAccounts.setBoolean(5, account.getAdmin());
						insertAccounts.addBatch();
					}
					insertAccounts.executeBatch();
					System.out.println("Accounts table populated");

					// Inserts the student info into the student table
					insertStudents = conn
							.prepareStatement("insert into students (account_id, ycp_id, major) values (?,?,?)");
					for (Student student : studentList) {
						insertStudents.setInt(1, student.getAccountId());
						insertStudents.setInt(2, student.getYCPId());
						insertStudents.setString(3, student.getMajor().toLowerCase());
						insertStudents.addBatch();
					}
					insertStudents.executeBatch();
					System.out.println("Students table populated");

					// Inserts the possible ted talk topics into the topics
					// table
					insertTopics = conn.prepareStatement("insert into topics (topic) values (?)");
					for (Topic topic : topicList) {
						insertTopics.setString(1, topic.getTopic().toLowerCase());
						insertTopics.addBatch();
					}
					insertTopics.executeBatch();
					System.out.println("Topics table populated");

					// Inserts the speakers for a particular ted talk into the
					// speakers table
					insertSpeakers = conn.prepareStatement("insert into speakers (firstname, lastname) values (?,?)");
					for (Speaker speaker : speakerList) {
						insertSpeakers.setString(1, speaker.getFirstname().toLowerCase());
						insertSpeakers.setString(2, speaker.getLastname().toLowerCase());
						insertSpeakers.addBatch();
					}
					insertSpeakers.executeBatch();
					System.out.println("Speakers table populated");

					// Inserts the ted talks into the ted talks table
					insertTedTalks = conn.prepareStatement(
							"insert into tedtalks (speaker_id, topic_id, title, description, url) values (?,?,?,?,?)");
					for (TedTalk talk : tedtalkList) {
						insertTedTalks.setInt(1, talk.getSpeakerId());
						insertTedTalks.setInt(2, talk.getTopicId());
						insertTedTalks.setString(3, talk.getTitle().toLowerCase());
						insertTedTalks.setString(4, talk.getDescription());
						insertTedTalks.setString(5, talk.getLink().toString());
						insertTedTalks.addBatch();
					}
					insertTedTalks.executeBatch();
					System.out.println("TedTalks table populated");

					// Inserts reviews into the review table
					insertReviews = conn.prepareStatement("insert into reviews "
							+ "(account_id, tedtalk_id, rating, date, review)" + "values (?,?,?,?,?)");
					for (Review review : reviewList) {
						insertReviews.setInt(1, review.getAccountId());
						insertReviews.setInt(2, review.getTedTalkId());
						insertReviews.setInt(3, review.getRating());
						insertReviews.setString(4, review.getDate());
						insertReviews.setString(5, review.getReview());
						insertReviews.addBatch();
					}
					insertReviews.executeBatch();
					System.out.println("Reviews table populated");

					return true;
				} finally {
					DBUtil.closeQuietly(insertBook);
					DBUtil.closeQuietly(insertAuthor);
					DBUtil.closeQuietly(insertBookAuthor);
					DBUtil.closeQuietly(insertAccounts);
					DBUtil.closeQuietly(insertTopics);
					DBUtil.closeQuietly(insertSpeakers);
					DBUtil.closeQuietly(insertTedTalks);
					DBUtil.closeQuietly(insertReviews);
				}
			}
		});
	}

	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();

		System.out.println("Loading initial data...");
		db.loadInitialData();

		System.out.println("Library DB successfully initialized!");
	}
	
	public Boolean deleteTables() {
		// Look up an account by their email
		return executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("drop table reviews");
					stmt.executeUpdate();
					stmt = conn.prepareStatement("drop table tedtalks");
					stmt.executeUpdate();
					stmt = conn.prepareStatement("drop table topics");
					stmt.executeUpdate();
					stmt = conn.prepareStatement("drop table speakers");
					stmt.executeUpdate();
					stmt = conn.prepareStatement("drop table students");
					stmt.executeUpdate();
					stmt = conn.prepareStatement("drop table accounts");
					stmt.executeUpdate();


					return true;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Account findAccount(final String email) {
		// Look up an account by their email
		return executeTransaction(new Transaction<Account>() {
			public Account execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from accounts " + "where email = ?");
					stmt.setString(1, email);
					resultSet = stmt.executeQuery();

					Account account = new Account();

					// for testing that a result was returned
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadAccount(account, resultSet, 1);
					}

					// check if any accounts were found
					if (!found) {
						System.out.println("No accounts were found in the database");
					}

					return account;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Account findAccount(final int accountId) {
		// Look up an account by their email
		return executeTransaction(new Transaction<Account>() {
			public Account execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from accounts " + "where account_id = ?");
					stmt.setInt(1, accountId);
					resultSet = stmt.executeQuery();

					Account account = new Account();

					// for testing that a result was returned
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadAccount(account, resultSet, 1);
					}

					// check if any accounts were found
					if (!found) {
						System.out.println("No accounts were found in the database");
					}

					return account;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Boolean changePassword(final int accountId, final String password) {
		// Look up an account by their email
		return executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;

				try {
					stmt = conn.prepareStatement("update accounts set password = ? where account_id = ?");
					stmt.setString(1, password);
					stmt.setInt(2, accountId);
					stmt.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Boolean changeEmail(final int accountId, final String email) {
		return executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;

				try {
					stmt = conn.prepareStatement("update accounts set email = ? where account_id = ?");
					stmt.setString(1, email);
					stmt.setInt(2, accountId);
					stmt.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Boolean changeMajor(final int studentId, final String major) {
		return executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;

				try {
					stmt = conn.prepareStatement("update students set major = ? where student_id = ?");
					stmt.setString(1, major);
					stmt.setInt(2, studentId);
					stmt.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Student findStudent(final String email) {
		// Look up the student by their email
		return executeTransaction(new Transaction<Student>() {
			public Student execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create an empty account to load their account info
					// into
					Account studentAccount = new Account();
					studentAccount = findAccount(email);

					// Then, select from students where the account id matches
					// the FK
					stmt = conn.prepareStatement("select * from students " + "where account_id = ?");
					stmt.setInt(1, studentAccount.getAccountId());
					resultSet = stmt.executeQuery();

					// Empty student object to load data into
					Student student = new Student();

					// Load the student info into the empty student object
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadStudent(student, resultSet, 1, studentAccount);
					}

					// Check if any students were found
					if (!found) {
						System.out.println("No students were found in the database");
					}

					return student;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public List<Topic> getAllTopics() {
		return executeTransaction(new Transaction<List<Topic>>() {
			public List<Topic> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create an empty list of topics
					List<Topic> topics = new ArrayList<Topic>();

					// Then, select from students where the account id matches
					// the FK
					stmt = conn.prepareStatement("select * from topics");
					resultSet = stmt.executeQuery();

					// Empty student object to load data into

					// Load the student info into the empty student object
					Boolean found = false;
					while (resultSet.next()) {
						Topic topic = new Topic();
						found = true;
						loadTopic(topic, resultSet, 1);
						topics.add(topic);
					}

					// Check if any students were found
					if (!found) {
						System.out.println("No students were found in the database");
					}

					return topics;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Boolean createNewAccount(final String email, final String password, final String firstname,
			final String lastname, final boolean admin) {
		// First, check to see if the account exists. If so, we'll have to
		// notify the user
		// Then, create the account
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from accounts " + "where email = ?");
					stmt.setString(1, email);
					resultSet = stmt.executeQuery();

					// Read through the ResultSet to see if the account exists
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
					}

					// If a result wasn't found, we can go ahead and create the
					// new account
					if (!found) {
						stmt = conn.prepareStatement("insert into accounts "
								+ "(email, password, firstname, lastname, admin) " + "values (?,?,?,?,?) ");
						stmt.setString(1, email);
						stmt.setString(2, password);
						stmt.setString(3, firstname.toLowerCase());
						stmt.setString(4, lastname.toLowerCase());
						stmt.setBoolean(5, admin);
						stmt.executeUpdate();

						return true;
					} else {
						// Otherwise, a result WAS found, which means than the
						// account already exists
						// So now we're mad and tell the user their account
						// already exists
						return false;
					}

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Boolean createNewStudent(final int ycp_id, final String major, final String email) {
		// Create a student and return a Boolean if successful
		// Since this gets called AFTER createAccount, we won't need to check
		// for existing student
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from accounts " + "where email = ?");
					stmt.setString(1, email);
					resultSet = stmt.executeQuery();

					Account account = new Account();

					// Read through the ResultSet to see if the account exists
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadAccount(account, resultSet, 1);
					}

					// Now that we have the account, we can use the ID as a FK
					// to the student
					if (found) {
						stmt = conn.prepareStatement(
								"insert into students " + "(account_id, ycp_id, major) " + "values (?,?,?) ");
						stmt.setInt(1, account.getAccountId());
						stmt.setInt(2, ycp_id);
						stmt.setString(3, major.toLowerCase());
						stmt.executeUpdate();

						return true;
					} else {
						// If we didn't find an account for the student (which
						// really shouldn't happen),
						// return false
						return false;
					}

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public List<Review> findReviewsbyAuthor(final String firstname, final String lastname) {
		return executeTransaction(new Transaction<List<Review>>() {
			public List<Review> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create a list of reviews and an author
					List<Review> result = new ArrayList<Review>();
					List<Account> accounts = new ArrayList<Account>();

					// Next, select the account IDs of the accounts matching
					// that name
					stmt = conn.prepareStatement("select * from accounts where firstname = ? and lastname = ?");
					stmt.setString(1, firstname.toLowerCase());
					stmt.setString(2, lastname.toLowerCase());
					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						Account account = new Account();
						found = true;
						loadAccount(account, resultSet, 1);
						accounts.add(account);
					}

					for (Account acc : accounts) {
						// Retrieve all the review objects
						stmt = conn.prepareStatement("select * from reviews " + "where account_id = ?");
						stmt.setInt(1, acc.getAccountId());
						resultSet = stmt.executeQuery();

						// Load the retrieved review into an object, add to the
						// list
						while (resultSet.next()) {
							Review review = new Review();
							found = true;
							loadReview(review, resultSet, 1);
							result.add(review);
						}
					}

					if (!found) {
						System.out.println("No reviews were found in the database");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public List<Review> findReviewbyTopic(final String topic) {
		// TODO Auto-generated method stub
		return executeTransaction(new Transaction<List<Review>>() {
			@SuppressWarnings("resource")
			public List<Review> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create a list of reviews and an author
					List<Review> result = new ArrayList<Review>();
					List<TedTalk> talks = new ArrayList<TedTalk>();
					Topic top = new Topic();

					// Next, get the topic
					stmt = conn.prepareStatement("select * from topics where topic = ?");
					stmt.setString(1, topic.toLowerCase());
					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTopic(top, resultSet, 1);
					}

					// Get all the TED Talks with that topic
					stmt = conn.prepareStatement("select * from tedtalks " + "where topic_id = ?");
					stmt.setInt(1, top.getTopicId());
					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						TedTalk talk = new TedTalk();
						found = true;
						try {
							loadTedTalk(talk, resultSet, 1);
						} catch (Exception ex) {
							System.out.println("Uh oh, bad link!");
						}
						talks.add(talk);
					}

					for (TedTalk ted : talks) {
						// Get any reviews for the TED talk
						stmt = conn.prepareStatement("select * from reviews where tedtalk_id = ?");
						stmt.setInt(1, ted.getTedTalkId());
						resultSet = stmt.executeQuery();

						while (resultSet.next()) {
							Review review = new Review();
							found = true;
							loadReview(review, resultSet, 1);
							result.add(review);
						}
					}

					if (!found) {
						System.out.println("No reviews were found in the database");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public List<Review> findReviewbyTitle(final String title) {
		// TODO Auto-generated method stub
		return executeTransaction(new Transaction<List<Review>>() {
			@SuppressWarnings("resource")
			public List<Review> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				List<Review> result = new ArrayList<Review>();

				try {
					// First, create a list of reviews and a TED Talk

					TedTalk talk = new TedTalk();

					// Next, get the topic
					stmt = conn.prepareStatement("select * from tedtalks where title = ?");
					stmt.setString(1, title.toLowerCase());
					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTedTalk(talk, resultSet, 1);
					}

					// Get all the reviews for that talk
					stmt = conn.prepareStatement("select * from reviews " + "where tedtalk_id = ?");
					stmt.setInt(1, talk.getTedTalkId());
					resultSet = stmt.executeQuery();

					found = false;
					while (resultSet.next()) {
						Review review = new Review();
						found = true;
						loadReview(review, resultSet, 1);
						result.add(review);
					}

					if (!found) {
						System.out.println("No reviews were found in the database");
					}

				} catch (MalformedURLException e) {
					System.out.println("Bad url!");
					e.printStackTrace();
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}

	public Topic findTopic(final String topic) {
		return executeTransaction(new Transaction<Topic>() {
			public Topic execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create an empty topic
					Topic topicObj = new Topic();

					// Then, select the topic that matches
					stmt = conn.prepareStatement("select * from topics " + "where topic = ?");
					stmt.setString(1, topic.toLowerCase());
					resultSet = stmt.executeQuery();

					// Load the topic info into the topic object
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTopic(topicObj, resultSet, 1);
					}

					// Check if any topics were found
					if (!found) {
						System.out.println("No students were found in the database");
					}

					return topicObj;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Speaker findSpeaker(final String firstname, final String lastname) {
		// TODO Auto-generated method stub
		return executeTransaction(new Transaction<Speaker>() {
			public Speaker execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create an empty topic
					Speaker speaker = new Speaker();

					// Then, select the topic that matches
					stmt = conn.prepareStatement("select * from speakers " + "where firstname = ? and lastname = ?");
					stmt.setString(1, firstname.toLowerCase());
					stmt.setString(2, lastname.toLowerCase());
					resultSet = stmt.executeQuery();

					// Load the topic info into the topic object
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadSpeaker(speaker, resultSet, 1);
					}

					// Check if any topics were found
					if (!found) {
						System.out.println("No students were found in the database");
					}

					return speaker;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Boolean insertNewTedTalk(final String title, final String description, final String url,
			final String firstname, final String lastname, final String topic) {
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from speakers " + "where firstname = ? and lastname = ?");
					stmt.setString(1, firstname.toLowerCase());
					stmt.setString(2, lastname.toLowerCase());
					resultSet = stmt.executeQuery();

					Speaker speaker = new Speaker();

					// Read through the ResultSet to see if the speaker exists
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadSpeaker(speaker, resultSet, 1);
					}

					stmt = conn.prepareStatement("select * from topics " + "where topic = ?");
					stmt.setString(1, topic.toLowerCase());
					resultSet = stmt.executeQuery();

					Topic topic = new Topic();

					// Read through the ResultSet to see if the topic exists
					while (resultSet.next()) {
						found = true;
						loadTopic(topic, resultSet, 1);
					}

					// Now that we have speaker and topic id, we can use the IDs
					// as foreign keys to the tedtalk
					if (found) {
						stmt = conn.prepareStatement("insert into tedtalks "
								+ "(speaker_id, topic_id, title, description, url) " + "values (?,?,?, ?, ?) ");
						stmt.setInt(1, speaker.getSpeakerId());
						stmt.setInt(2, topic.getTopicId());
						stmt.setString(3, title.toLowerCase());
						stmt.setString(4, description);
						stmt.setString(5, url.toString());
						stmt.executeUpdate();

						return true;
					} else {
						// If we didn't find an account for the student (which
						// really shouldn't happen),
						// return false
						return false;
					}

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Boolean insertNewSpeaker(final String firstname, final String lastname) {
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from speakers " + "where firstname = ? or lastname = ?");
					stmt.setString(1, firstname.toLowerCase());
					stmt.setString(2, lastname.toLowerCase());
					resultSet = stmt.executeQuery();

					Speaker speaker = new Speaker();

					// Read through the ResultSet to see if the speaker exists
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadSpeaker(speaker, resultSet, 1);
					}

					// Now that we have speaker and topic id, we can use the IDs
					// as foreign keys to the tedtalk
					if (found) {
						return false;
					} else {
						stmt = conn
								.prepareStatement("insert into speakers " + "(firstname, lastname) " + "values (?,?) ");
						stmt.setString(1, firstname.toLowerCase());
						stmt.setString(2, lastname.toLowerCase());
						stmt.executeUpdate();
						return true;
					}

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Speaker findSpeakerFromTedTalk(final int speakerId) {
		return executeTransaction(new Transaction<Speaker>() {
			@SuppressWarnings("resource")
			public Speaker execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from speakers " + "where speaker_id = ?");
					stmt.setInt(1, speakerId);
					resultSet = stmt.executeQuery();

					Speaker speaker = new Speaker();

					// Read through the ResultSet to see if the speaker exists
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadSpeaker(speaker, resultSet, 1);
					}

					return speaker;

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Boolean insertNewTopic(final String top) {
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from topics " + "where topic = ?");
					stmt.setString(1, top.toLowerCase());
					resultSet = stmt.executeQuery();

					Topic topic = new Topic();

					// Read through the ResultSet to see if the topic exists
					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTopic(topic, resultSet, 1);
					}

					// if topic was not found, we can insert a topic in the
					// database
					if (found) {
						return false;
					} else {
						stmt = conn.prepareStatement("insert into topics " + "(topic) " + "values (?) ");
						stmt.setString(1, top.toLowerCase());
						stmt.executeUpdate();
						return true;
					}

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	public Boolean insertReview(final int rating, final String date, final String review, final String firstname,
			final String lastname, final String title) {
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;

				try {
					stmt = conn.prepareStatement("select * from accounts " + " where firstname = ? and lastname = ?");

					stmt.setString(1, firstname.toLowerCase());
					stmt.setString(2, lastname.toLowerCase());

					resultSet = stmt.executeQuery();

					Account acc = new Account();

					while (resultSet.next()) {
						loadAccount(acc, resultSet, 1);
					}

					stmt2 = conn.prepareStatement("select * from tedtalks where title = ?");
					stmt2.setString(1, title.toLowerCase());

					resultSet2 = stmt2.executeQuery();

					TedTalk talk = new TedTalk();
					while (resultSet2.next()) {
						loadTedTalk(talk, resultSet2, 1);
					}

					stmt = conn.prepareStatement("insert into reviews "
							+ "(account_id, tedtalk_id, rating, date, review)" + "values (?,?,?,?,?)");

					stmt.setInt(1, acc.getAccountId());
					stmt.setInt(2, talk.getTedTalkId());
					stmt.setInt(3, rating);
					stmt.setString(4, date);
					stmt.setString(5, review);

					stmt.executeUpdate();
					
					return true;
				} catch (MalformedURLException e) {
					e.printStackTrace();
					System.out.print("Don't know what this means but something's going on");
					return false;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	public Boolean deleteTedTalk(final int tedTalkId) {
		return executeTransaction(new Transaction<Boolean>() {
			@SuppressWarnings("resource")
			public Boolean execute(Connection conn) throws SQLException {
				Boolean result = false;
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;

				try {

					stmt = conn.prepareStatement("delete from tedtalks where tedtalk_id = ?");

					stmt.setInt(1, tedTalkId);

					stmt.executeUpdate();
					result = true;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt2);
				}
				return result;
			}
		});
	}
	
	public Boolean deleteReview(final int reviewId) {
		return executeTransaction(new Transaction<Boolean>() {
			public Boolean execute(Connection conn) throws SQLException {
				Boolean result = false;
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;

				try {

					stmt = conn.prepareStatement("delete from reviews where review_id = ?");

					stmt.setInt(1, reviewId);

					stmt.executeUpdate();
					result = true;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt2);
				}
				return result;
			}
		});
	}

	public List<TedTalk> findTedTalkbySpeaker(final String search) {
		return executeTransaction(new Transaction<List<TedTalk>>() {
			public List<TedTalk> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				List<TedTalk> result = new ArrayList<TedTalk>();

				try {
					Speaker speaker = new Speaker();

					stmt = conn.prepareStatement("select * from speakers where lastname = ?");
					stmt.setString(1, search.toLowerCase());

					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadSpeaker(speaker, resultSet, 1);
					}

					// Get all the tedtalks
					stmt = conn.prepareStatement("select * from tedtalks where speaker_id = ?");
					stmt.setInt(1, speaker.getSpeakerId());
					resultSet = stmt.executeQuery();

					found = false;
					while (resultSet.next()) {
						TedTalk talk = new TedTalk();
						found = true;
						loadTedTalk(talk, resultSet, 1);
						result.add(talk);
					}

					if (!found) {
						System.out.println("No tedtalks were found in the database");
					}

				} catch (MalformedURLException e) {
					System.out.println("Bad url!");
					e.printStackTrace();
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}

	public List<TedTalk> findTedTalkbyTopic(final String search) {
		return executeTransaction(new Transaction<List<TedTalk>>() {
			public List<TedTalk> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				List<TedTalk> result = new ArrayList<TedTalk>();

				try {
					Topic topic = new Topic();

					// Next, get the topic
					stmt = conn.prepareStatement("select * from topics where topic = ?");
					stmt.setString(1, search.toLowerCase());
					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTopic(topic, resultSet, 1);
					}

					// Get all the tedtalks for that topic
					stmt2 = conn.prepareStatement("select * from tedtalks where topic_id = ?");
					stmt2.setInt(1, topic.getTopicId());
					resultSet2 = stmt2.executeQuery();

					while (resultSet2.next()) {
						TedTalk talk = new TedTalk();
						found = true;
						loadTedTalk(talk, resultSet, 1);
						result.add(talk);
					}

					if (!found) {
						System.out.println("No tedtalks were found in the database");
					}

				} catch (MalformedURLException e) {
					System.out.println("Bad url!");
					e.printStackTrace();
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt2);
				}
				return result;
			}
		});
	}

	public TedTalk findTedTalkbyTitle(final String search) {
		return executeTransaction(new Transaction<TedTalk>() {
			public TedTalk execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				TedTalk result = new TedTalk();

				try {
					// First, create a space for the TedTalk

					TedTalk talk = new TedTalk();

					// Query for title
					stmt = conn.prepareStatement("select * from tedtalks where title = ?");
					stmt.setString(1, search.toLowerCase());
					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTedTalk(talk, resultSet, 1);
					}

					result = talk;
				} catch (MalformedURLException e) {
					System.out.println("Bad url!");
					e.printStackTrace();
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}

	public TedTalk findTedTalkByReview(final Review review) {
		return executeTransaction(new Transaction<TedTalk>() {
			public TedTalk execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				TedTalk result = new TedTalk();

				try {
					// First, create a space for TedTalk
					TedTalk talk = new TedTalk();

					// Query for title
					stmt = conn.prepareStatement("select * from tedtalks where tedtalk_id = ?");
					stmt.setInt(1, review.getTedTalkId());
					resultSet = stmt.executeQuery();

					Boolean found = false;
					while (resultSet.next()) {
						found = true;
						loadTedTalk(talk, resultSet, 1);
					}

					result = talk;
				} catch (MalformedURLException e) {
					System.out.println("Bad url!");
					e.printStackTrace();
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}

	public TedTalk findTedTalkByID(final int tedTalkID) {
		return executeTransaction(new Transaction<TedTalk>() {
			public TedTalk execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				TedTalk result = new TedTalk();

				try {

					TedTalk talk = new TedTalk();
					stmt = conn.prepareStatement("select * from tedtalks where tedtalk_id = ?");

					stmt.setInt(1, tedTalkID);
					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						loadTedTalk(talk, resultSet, 1);
					}

					result = talk;
				} catch (MalformedURLException e) {
					System.out.print("Bad url...");
					e.printStackTrace();
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}

	public Topic findTopicbyID(final int topicID) {
		return executeTransaction(new Transaction<Topic>() {
			public Topic execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				Topic result = new Topic();

				try {

					Topic top = new Topic();
					stmt = conn.prepareStatement("select * from topics where topic_id = ?");

					stmt.setInt(1, topicID);
					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						loadTopic(top, resultSet, 1);
					}

					result = top;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}

	public Speaker findSpeakerbyID(final int speakerID) {
		return executeTransaction(new Transaction<Speaker>() {
			public Speaker execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				Speaker result = new Speaker();

				try {
					Speaker spec = new Speaker();

					stmt = conn.prepareStatement("select * from speakers where speaker_id = ?");

					stmt.setInt(1, speakerID);
					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						loadSpeaker(spec, resultSet, 1);
					}
					result = spec;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
				return result;
			}
		});
	}
}