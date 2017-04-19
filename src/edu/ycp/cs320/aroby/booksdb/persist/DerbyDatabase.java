package edu.ycp.cs320.aroby.booksdb.persist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.aroby.booksdb.model.Author;
import edu.ycp.cs320.aroby.booksdb.model.Book;
import edu.ycp.cs320.aroby.booksdb.model.BookAuthor;
import edu.ycp.cs320.aroby.booksdb.model.Pair;
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

	// transaction that retrieves a Book, and its Author by Title
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(final String title) {
		return executeTransaction(new Transaction<List<Pair<Author, Book>>>() {
			public List<Pair<Author, Book>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select authors.*, books.* " + "  from  authors, books, bookAuthors "
							+ "  where books.title = ? " + "    and authors.author_id = bookAuthors.author_id "
							+ "    and books.book_id     = bookAuthors.book_id");
					stmt.setString(1, title);

					List<Pair<Author, Book>> result = new ArrayList<Pair<Author, Book>>();

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						Book book = new Book();
						loadBook(book, resultSet, 4);

						result.add(new Pair<Author, Book>(author, book));
					}

					// check if the title was found
					if (!found) {
						System.out.println("<" + title + "> was not found in the books table");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// transaction that retrieves a list of Books with their Authors, given
	// Author's last name
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(final String lastName) {
		return executeTransaction(new Transaction<List<Pair<Author, Book>>>() {
			public List<Pair<Author, Book>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				// try to retrieve Authors and Books based on Author's last
				// name, passed into query
				try {
					stmt = conn.prepareStatement("select authors.*, books.* " + "  from  authors, books, bookAuthors "
							+ "  where authors.lastname = ? " + "    and authors.author_id = bookAuthors.author_id "
							+ "    and books.book_id     = bookAuthors.book_id "
							+ "  order by books.title asc, books.published asc");
					stmt.setString(1, lastName);

					// establish the list of (Author, Book) Pairs to receive the
					// result
					List<Pair<Author, Book>> result = new ArrayList<Pair<Author, Book>>();

					// execute the query, get the results, and assemble them in
					// an ArrayLsit
					resultSet = stmt.executeQuery();
					while (resultSet.next()) {
						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						Book book = new Book();
						loadBook(book, resultSet, 4);

						result.add(new Pair<Author, Book>(author, book));
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// transaction that retrieves all Books in Library, with their respective
	// Authors
	public List<Pair<Author, Book>> findAllBooksWithAuthors() {
		return executeTransaction(new Transaction<List<Pair<Author, Book>>>() {
			public List<Pair<Author, Book>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select authors.*, books.* " + "  from authors, books, bookAuthors "
							+ "  where authors.author_id = bookAuthors.author_id "
							+ "    and books.book_id     = bookAuthors.book_id " + "  order by books.title asc");

					List<Pair<Author, Book>> result = new ArrayList<Pair<Author, Book>>();

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						Book book = new Book();
						loadBook(book, resultSet, 4);

						result.add(new Pair<Author, Book>(author, book));
					}

					// check if any books were found
					if (!found) {
						System.out.println("No books were found in the database");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// transaction that retrieves all Authors in Library
	public List<Author> findAllAuthors() {
		return executeTransaction(new Transaction<List<Author>>() {
			public List<Author> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement("select * from authors " + " order by lastname asc, firstname asc");

					List<Author> result = new ArrayList<Author>();

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;

						Author author = new Author();
						loadAuthor(author, resultSet, 1);

						result.add(author);
					}

					// check if any authors were found
					if (!found) {
						System.out.println("No authors were found in the database");
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// transaction that inserts new Book into the Books table
	// also first inserts new Author into Authors table, if necessary
	// and then inserts entry into BookAuthors junction table
	public Integer insertBookIntoBooksTable(final String title, final String isbn, final int published,
			final String lastName, final String firstName) {
		return executeTransaction(new Transaction<Integer>() {
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;

				ResultSet resultSet1 = null;
				// (unused) ResultSet resultSet2 = null;
				ResultSet resultSet3 = null;
				// (unused) ResultSet resultSet4 = null;
				ResultSet resultSet5 = null;
				// (unused) ResultSet resultSet6 = null;

				// for saving author ID and book ID
				Integer author_id = -1;
				Integer book_id = -1;

				// try to retrieve author_id (if it exists) from DB, for
				// Author's full name, passed into query
				try {
					stmt1 = conn.prepareStatement(
							"select author_id from authors " + "  where lastname = ? and firstname = ? ");
					stmt1.setString(1, lastName);
					stmt1.setString(2, firstName);

					// execute the query, get the result
					resultSet1 = stmt1.executeQuery();

					// if Author was found then save author_id
					if (resultSet1.next()) {
						author_id = resultSet1.getInt(1);
						System.out.println("Author <" + lastName + ", " + firstName + "> found with ID: " + author_id);
					} else {
						System.out.println("Author <" + lastName + ", " + firstName + "> not found");

						// if the Author is new, insert new Author into Authors
						// table
						if (author_id <= 0) {
							// prepare SQL insert statement to add Author to
							// Authors table
							stmt2 = conn
									.prepareStatement("insert into authors (lastname, firstname) " + "  values(?, ?) ");
							stmt2.setString(1, lastName);
							stmt2.setString(2, firstName);

							// execute the update
							stmt2.executeUpdate();

							System.out.println(
									"New author <" + lastName + ", " + firstName + "> inserted in Authors table");

							// try to retrieve author_id for new Author - DB
							// auto-generates author_id
							stmt3 = conn.prepareStatement(
									"select author_id from authors " + "  where lastname = ? and firstname = ? ");
							stmt3.setString(1, lastName);
							stmt3.setString(2, firstName);

							// execute the query
							resultSet3 = stmt3.executeQuery();

							// get the result - there had better be one
							if (resultSet3.next()) {
								author_id = resultSet3.getInt(1);
								System.out.println("New author <" + lastName + ", " + firstName + "> ID: " + author_id);
							} else // really should throw an exception here -
									// the new author should have been inserted,
									// but we didn't find them
							{
								System.out.println("New author <" + lastName + ", " + firstName
										+ "> not found in Authors table (ID: " + author_id);
							}
						}
					}

					// now insert new Book into Books table
					// prepare SQL insert statement to add new Book to Books
					// table
					stmt4 = conn.prepareStatement("insert into books (title, isbn, published) " + "  values(?, ?, ?) ");
					stmt4.setString(1, title);
					stmt4.setString(2, isbn);
					stmt4.setInt(3, published);

					// execute the update
					stmt4.executeUpdate();

					System.out.println("New book <" + title + "> inserted into Books table");

					// now retrieve book_id for new Book, so that we can set up
					// BookAuthor entry
					// and return the book_id, which the DB auto-generates
					// prepare SQL statement to retrieve book_id for new Book
					stmt5 = conn.prepareStatement(
							"select book_id from books " + "  where title = ? and isbn = ? and published = ? "

					);
					stmt5.setString(1, title);
					stmt5.setString(2, isbn);
					stmt5.setInt(3, published);

					// execute the query
					resultSet5 = stmt5.executeQuery();

					// get the result - there had better be one
					if (resultSet5.next()) {
						book_id = resultSet5.getInt(1);
						System.out.println("New book <" + title + "> ID: " + book_id);
					} else // really should throw an exception here - the new
							// book should have been inserted, but we didn't
							// find it
					{
						System.out.println("New book <" + title + "> not found in Books table (ID: " + book_id);
					}

					// now that we have all the information, insert entry into
					// BookAuthors table
					// which is the junction table for Books and Authors
					// prepare SQL insert statement to add new Book to Books
					// table
					stmt6 = conn.prepareStatement("insert into bookAuthors (book_id, author_id) " + "  values(?, ?) ");
					stmt6.setInt(1, book_id);
					stmt6.setInt(2, author_id);

					// execute the update
					stmt6.executeUpdate();

					System.out.println("New entry for book ID <" + book_id + "> and author ID <" + author_id
							+ "> inserted into BookAuthors junction table");

					System.out.println("New book <" + title + "> inserted into Books table");

					return book_id;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
					// (unused) DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(resultSet3);
					DBUtil.closeQuietly(stmt3);
					// (unused) DBUtil.closeQuietly(resultSet4);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(resultSet5);
					DBUtil.closeQuietly(stmt5);
					// (unused) DBUtil.closeQuietly(resultSet6);
					DBUtil.closeQuietly(stmt6);
				}
			}
		});
	}

	// transaction that deletes Book (and possibly its Author) from Library
	public List<Author> removeBookByTitle(final String title) {
		return executeTransaction(new Transaction<List<Author>>() {
			public List<Author> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;

				ResultSet resultSet1 = null;
				ResultSet resultSet2 = null;
				// ResultSet resultSet3 = null;
				// ResultSet resultSet4 = null;
				ResultSet resultSet5 = null;
				// ResultSet resultSet6 = null;

				try {
					// first get the Author(s) of the Book to be deleted
					// just in case it's the only Book by this Author
					// in which case, we should also remove the Author(s)
					stmt1 = conn.prepareStatement("select authors.* " + "  from  authors, books, bookAuthors "
							+ "  where books.title = ? " + "    and authors.author_id = bookAuthors.author_id "
							+ "    and books.book_id     = bookAuthors.book_id");

					// get the Book's Author(s)
					stmt1.setString(1, title);
					resultSet1 = stmt1.executeQuery();

					// assemble list of Authors from query
					List<Author> authors = new ArrayList<Author>();

					while (resultSet1.next()) {
						Author author = new Author();
						loadAuthor(author, resultSet1, 1);
						authors.add(author);
					}

					// check if any Authors were found
					// this shouldn't be necessary, there should not be a Book
					// in the DB without an Author
					if (authors.size() == 0) {
						System.out.println("No author was found for title <" + title + "> in the database");
					}

					// now get the Book(s) to be deleted
					// we will need the book_id to remove associated entries in
					// BookAuthors (junction table)

					stmt2 = conn.prepareStatement("select books.* " + "  from  books " + "  where books.title = ? ");

					// get the Book(s)
					stmt2.setString(1, title);
					resultSet2 = stmt2.executeQuery();

					// assemble list of Books from query
					List<Book> books = new ArrayList<Book>();

					while (resultSet2.next()) {
						Book book = new Book();
						loadBook(book, resultSet2, 1);
						books.add(book);
					}

					// first delete entries in BookAuthors junction table
					// can't delete entries in Books or Authors tables while
					// they have foreign keys in junction table
					// prepare to delete the junction table entries
					// (bookAuthors)
					stmt3 = conn.prepareStatement("delete from bookAuthors " + "  where book_id = ? ");

					// delete the junction table entries from the DB for this
					// title
					// this works if there are not multiple books with the same
					// name
					stmt3.setInt(1, books.get(0).getBookId());
					stmt3.executeUpdate();

					System.out.println("Deleted junction table entries for book(s) <" + title + "> from DB");

					// now delete entries in Books table for this title
					stmt4 = conn.prepareStatement("delete from books " + "  where title = ? ");

					// delete the Book entries from the DB for this title
					stmt4.setString(1, title);
					stmt4.executeUpdate();

					System.out.println("Deleted book(s) with title <" + title + "> from DB");

					// now check if the Author(s) have any Books remaining in
					// the DB
					// only need to check if there are any entries in junction
					// table that have this author ID
					for (int i = 0; i < authors.size(); i++) {
						// prepare to find Books for this Author
						stmt5 = conn.prepareStatement(
								"select books.book_id from books, bookAuthors " + "  where bookAuthors.author_id = ? ");

						// retrieve any remaining books for this Author
						stmt5.setInt(1, books.get(i).getAuthorId());
						resultSet5 = stmt5.executeQuery();

						// if nothing returned, then delete Author
						if (!resultSet5.next()) {
							stmt6 = conn.prepareStatement("delete from authors " + "  where author_id = ?");

							// delete the Author from DB
							stmt6.setInt(1, authors.get(i).getAuthorId());
							stmt6.executeUpdate();

							System.out.println("Deleted author <" + authors.get(i).getLastname() + ", "
									+ authors.get(i).getFirstname() + "> from DB");

							// we're done with this, so close it, since we might
							// have more to do
							DBUtil.closeQuietly(stmt6);
						}

						// we're done with this, so close it since we might have
						// more to do
						DBUtil.closeQuietly(resultSet5);
						DBUtil.closeQuietly(stmt5);
					}
					return authors;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
					// DBUtil.closeQuietly(resultSet3);
					// DBUtil.closeQuietly(resultSet4);

					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
	}

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
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/CS320/library.db;create=true");

		// Set autocommit() to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}

	// retrieves Author information from query result set
	private void loadAuthor(Author author, ResultSet resultSet, int index) throws SQLException {
		author.setAuthorId(resultSet.getInt(index++));
		author.setLastname(resultSet.getString(index++));
		author.setFirstname(resultSet.getString(index++));
	}

	// retrieves Book information from query result set
	private void loadBook(Book book, ResultSet resultSet, int index) throws SQLException {
		book.setBookId(resultSet.getInt(index++));
		// book.setAuthorId(resultSet.getInt(index++)); // no longer used
		book.setTitle(resultSet.getString(index++));
		book.setIsbn(resultSet.getString(index++));
		book.setPublished(resultSet.getInt(index++));
	}

	// retrieves WrittenBy information from query result set
	private void loadBookAuthors(BookAuthor bookAuthor, ResultSet resultSet, int index) throws SQLException {
		bookAuthor.setBookId(resultSet.getInt(index++));
		bookAuthor.setAuthorId(resultSet.getInt(index++));
	}

	private void loadAccount(Account account, ResultSet resultSet, int index) throws SQLException {
		account.setAccountId(resultSet.getInt(index++));
		account.setEmail(resultSet.getString(index++));
		account.setPassword(resultSet.getString(index++));
		account.setFirstName(resultSet.getString(index++));
		account.setLastName(resultSet.getString(index++));
		account.setAdmin(resultSet.getBoolean(index++));
	}

	private void loadStudent(Student student, ResultSet resultSet, int index, Account account) throws SQLException {
		student.setStudentId(resultSet.getInt(index++));
		student.setAccountId(resultSet.getInt(index++));
		student.setYCPId(resultSet.getInt(index++));
		student.setMajor(resultSet.getString(index++));
		student.setEmail(account.getEmail());
		student.setAdmin(account.getAdmin());
		student.setFirstName(account.getFirstName());
		student.setLastName(account.getLastName());
		student.setPassword(account.getPassword());
	}
	
	private void loadTopic(Topic topic, ResultSet resultSet, int index) throws SQLException {
		topic.setTopicId(resultSet.getInt(index++));
		topic.setTopic(resultSet.getString(index++));
	}
	
	private void loadReview(Review review, ResultSet resultSet, int index) throws SQLException {
		review.setReviewId(resultSet.getInt(index++));
		review.setAccountId(resultSet.getInt(index++));
		review.setTedTalkId(resultSet.getInt(index++));
		review.setRating(resultSet.getInt(index++));
		review.setDate(ZonedDateTime.parse(resultSet.getString(index++)));
		review.setReview(resultSet.getString(index++));
		review.setRecommendation(resultSet.getString(index++));
	}
	
	private void loadTedTalk(TedTalk talk, ResultSet resultSet, int index) throws SQLException, MalformedURLException {
		talk.setTedTalkId(resultSet.getInt(index++));
		talk.setSpeakerId(resultSet.getInt(index++));
		talk.setTopicId(resultSet.getInt(index++));
		talk.setTitle(resultSet.getString(index++));
		talk.setDescription(resultSet.getString(index++));
		talk.setLink(new URL(resultSet.getString(index++)));
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
					// Create authors table
					stmt1 = conn.prepareStatement("create table authors (" + "	author_id integer primary key "
							+ "		generated always as identity (start with 1, increment by 1), "
							+ "	lastname varchar(40)," + "	firstname varchar(40)" + ")");
					stmt1.executeUpdate();

					System.out.println("Authors table created");

					// Create books table
					stmt2 = conn.prepareStatement("create table books (" + "	book_id integer primary key "
							+ "		generated always as identity (start with 1, increment by 1), " +
					// " author_id integer constraint author_id references
					// authors, " +
					"	title varchar(70)," + "	isbn varchar(15)," + "   published integer" + ")");
					stmt2.executeUpdate();

					System.out.println("Books table created");

					// Create bookauthors table
					stmt3 = conn.prepareStatement(
							"create table bookAuthors (" + "	book_id   integer constraint book_id references books, "
									+ "	author_id integer constraint author_id references authors " + ")");
					stmt3.executeUpdate();

					System.out.println("BookAuthors table created");

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
							+ "description varchar(140)," + "url varchar(140)" + ")");

					stmt8.executeUpdate();
					System.out.println("TedTalks table created");

					// Create reviews table
					stmt9 = conn.prepareStatement("create table reviews (" + "		review_id integer primary key"
							+ "			generated always as identity (start with 1, increment by 1),"
							+ "account_id integer references accounts,"
							+ "tedtalk_id integer constraint tedtalk_id references tedtalks," + "rating integer,"
							+ "date varchar(70)," + "review varchar(500)," + "recommendations varchar(70)" + ")");

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
				List<Author> authorList;
				List<Book> bookList;
				List<BookAuthor> bookAuthorList;
				List<Account> accountList;
				List<Student> studentList;
				List<Topic> topicList;
				List<Speaker> speakerList;
				List<TedTalk> tedtalkList;
				List<Review> reviewList;

				try {
					authorList = InitialData.getAuthors();
					bookList = InitialData.getBooks();
					bookAuthorList = InitialData.getBookAuthors();
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
					// must completely populate Authors table before populating
					// BookAuthors table because of primary keys
					insertAuthor = conn.prepareStatement("insert into authors (lastname, firstname) values (?, ?)");
					for (Author author : authorList) {
						// insertAuthor.setInt(1, author.getAuthorId()); //
						// auto-generated primary key, don't insert this
						insertAuthor.setString(1, author.getLastname());
						insertAuthor.setString(2, author.getFirstname());
						insertAuthor.addBatch();
					}
					insertAuthor.executeBatch();

					System.out.println("Authors table populated");

					// must completely populate Books table before populating
					// BookAuthors table because of primary keys
					insertBook = conn.prepareStatement("insert into books (title, isbn, published) values (?, ?, ?)");
					for (Book book : bookList) {
						// insertBook.setInt(1, book.getBookId()); //
						// auto-generated primary key, don't insert this
						// insertBook.setInt(1, book.getAuthorId()); // this is
						// now in the BookAuthors table
						insertBook.setString(1, book.getTitle());
						insertBook.setString(2, book.getIsbn());
						insertBook.setInt(3, book.getPublished());
						insertBook.addBatch();
					}
					insertBook.executeBatch();

					System.out.println("Books table populated");

					// must wait until all Books and all Authors are inserted
					// into tables before creating BookAuthor table
					// since this table consists entirely of foreign keys, with
					// constraints applied
					insertBookAuthor = conn
							.prepareStatement("insert into bookAuthors (book_id, author_id) values (?, ?)");
					for (BookAuthor bookAuthor : bookAuthorList) {
						insertBookAuthor.setInt(1, bookAuthor.getBookId());
						insertBookAuthor.setInt(2, bookAuthor.getAuthorId());
						insertBookAuthor.addBatch();
					}
					insertBookAuthor.executeBatch();
					System.out.println("BookAuthors table populated");

					// Insert the accounts into the accounts table
					insertAccounts = conn.prepareStatement(
							"insert into accounts (email, password, firstname, lastname, admin) values (?,?,?,?,?)");
					for (Account account : accountList) {
						insertAccounts.setString(1, account.getEmail());
						insertAccounts.setString(2, account.getPassword());
						insertAccounts.setString(3, account.getFirstName());
						insertAccounts.setString(4, account.getLastName());
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
						insertStudents.setString(3, student.getMajor());
						insertStudents.addBatch();
					}
					insertStudents.executeBatch();
					System.out.println("Students table populated");

					// Inserts the possible ted talk topics into the topics
					// table
					insertTopics = conn.prepareStatement("insert into topics (topic) values (?)");
					for (Topic topic : topicList) {
						insertTopics.setString(1, topic.getTopic());
						insertTopics.addBatch();
					}
					insertTopics.executeBatch();
					System.out.println("Topics table populated");

					// Inserts the speakers for a particular ted talk into the
					// speakers table
					insertSpeakers = conn.prepareStatement("insert into speakers (firstname, lastname) values (?,?)");
					for (Speaker speaker : speakerList) {
						insertSpeakers.setString(1, speaker.getFirstname());
						insertSpeakers.setString(2, speaker.getLastname());
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
						insertTedTalks.setString(3, talk.getTitle());
						insertTedTalks.setString(4, talk.getDescription());
						insertTedTalks.setString(5, talk.getLink().toString());
						insertTedTalks.addBatch();
					}
					insertTedTalks.executeBatch();
					System.out.println("TedTalks table populated");

					// Inserts reviews into the review table
					insertReviews = conn.prepareStatement(
							"insert into reviews " + "(account_id, tedtalk_id, rating, date, review, recommendations)"
									+ "values (?,?,?,?,?,?)");
					for (Review review : reviewList) {
						insertReviews.setInt(1, review.getAccountId());
						insertReviews.setInt(2, review.getTedTalkId());
						insertReviews.setInt(3, review.getRating());
						insertReviews.setString(4, review.getDate().toString());
						insertReviews.setString(5, review.getReview());
						insertReviews.setString(6, review.getRecommendation());
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

	public Student findStudent(final String email) {
		// Look up the student by their email
		return executeTransaction(new Transaction<Student>() {
			public Student execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create an empty account to load their account info into
					Account studentAccount = new Account();
					studentAccount = findAccount(email);
					
					// Then, select from students where the account id matches the FK
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

	public Boolean createNewAccount(final String email, final String password, 
			final String firstname, final String lastname, final boolean admin) {
		// First, check to see if the account exists. If so, we'll have to notify the user
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

					// If a result wasn't found, we can go ahead and create the new account
					if (!found) {
						stmt = conn.prepareStatement(
								"insert into accounts "
								+ "(email, password, firstname, lastname, admin) "
								+ "values (?,?,?,?,?) "
						);
						stmt.setString(1, email);
						stmt.setString(2, password);
						stmt.setString(3, firstname);
						stmt.setString(4, lastname);
						stmt.setBoolean(5, admin);
						stmt.executeUpdate();
						
						return true;
					} else {
						// Otherwise, a result WAS found, which means than the account already exists
						// So now we're mad and tell the user their account already exists
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
		// Since this gets called AFTER createAccount, we won't need to check for existing student
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

					// Now that we have the account, we can use the ID as a FK to the student
					if (found) {
						stmt = conn.prepareStatement(
								"insert into students "
								+ "(account_id, ycp_id, major) "
								+ "values (?,?,?) "
						);
						stmt.setInt(1, account.getAccountId());
						stmt.setInt(2, ycp_id);
						stmt.setString(3, major);
						stmt.executeUpdate();
						
						return true;
					} else {
						// If we didn't find an account for the student (which really shouldn't happen),
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

	public void insertNewTedTalk() {
		// TODO Auto-generated method stub
		
	}



	public void insertNewTopic() {
		// TODO Auto-generated method stub
		
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
					
					// Next, select the account IDs of the accounts matching that name
					stmt = conn.prepareStatement(
							"select * from accounts where firstname = ? and lastname = ?"
					);
					stmt.setString(1, firstname);
					stmt.setString(2, lastname);
					resultSet = stmt.executeQuery();
					
					Boolean found = false;
					while (resultSet.next()) {
						Account account = new Account();
						found = true;
						loadAccount(account, resultSet, 1);
					}
					
					for(Account acc : accounts) {
						// Retrieve all the review objects
						stmt = conn.prepareStatement("select * from reviews " + "where account_id = ?");
						stmt.setInt(1, acc.getAccountId());
						resultSet = stmt.executeQuery();
						
						// Load the retrieved review into an object, add to the list
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
					stmt = conn.prepareStatement(
							"select * from topics where topic = ?"
					);
					stmt.setString(1, topic);
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
						} catch(Exception ex) {
							System.out.println("Uh oh, bad link!");
						}
						talks.add(talk);
					}
					
					for (TedTalk ted : talks) {
						// Get any reviews for the TED talk
						stmt = conn.prepareStatement(
								"select * from reviews where tedtalk_id = ?"
						);
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
					stmt = conn.prepareStatement(
							"select * from tedtalks where title = ?"
					);
					stmt.setString(1, title);
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
		// TODO Auto-generated method stub
		return executeTransaction(new Transaction<Topic>() {
			public Topic execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// First, create an empty topic
					Topic topicObj = new Topic();
					
					// Then, select the topic that matches
					stmt = conn.prepareStatement("select * from topics " + "where topic = ?");
					stmt.setString(1, topic);
					resultSet = stmt.executeQuery();


	public List<Review> findReviewbyTopic(String topic) {

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

	public List<TedTalk> findTedTalkbyAuthor(String search) {

		// TODO Auto-generated method stub
		return null;
	}


}
