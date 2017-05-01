package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.booksdb.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.controller.ReviewController;
import edu.ycp.cs320.aroby.controller.SearchController;
import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.ReviewComparator;
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/index.jsp").forward(req, resp);	
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		if(req.getParameter("login") != null) {
			resp.sendRedirect("/aroby/login");
		} else if(req.getParameter("reviewPage") != null) {
			resp.sendRedirect("/aroby/reviewPage");
		} else if(req.getParameter("logout") != null) {
			HttpSession session = req.getSession(true);
			session.invalidate();
			resp.sendRedirect("/aroby/index");
		} else if(req.getParameter("createAccount") != null) {
			resp.sendRedirect("/aroby/createAccount");
		} else if(req.getParameter("searchPage") != null) {
			resp.sendRedirect("/aroby/searchPage");
		}else if(req.getParameter("tedTalkPage") != null) {
			resp.sendRedirect("/aroby/tedTalkPage");
		}
		List<Review> reviews = new ArrayList<Review>();
		List<Account> accounts = new ArrayList<Account>();
		List<TedTalk> tedTalks = new ArrayList<TedTalk>();
		List<Topic> topics = new ArrayList<Topic>();
		List<Integer> tedTalkIds = new ArrayList<Integer>();
		
		SearchController controller = new SearchController();
		topics = controller.getTopics();
		//find all reviews
		for(int i= 0; i<topics.size(); i++){	
			reviews.addAll(controller.findReviewsByTopic(topics.get(i).getTopic()));	
		}
		//sort the reviews using review comparator 
		if(reviews.size()>1){
			Collections.sort(reviews, new ReviewComparator());
		}
		// remove the reviews at the top of the list
		if(reviews.size()>4){
			while(reviews.size()>4){
				reviews.remove(5);
			}
		}
		accounts = controller.getAccountFromReview(reviews);
		
		//find the ted talks based on the review
		for(Review review : reviews) {
			TedTalk talk = controller.getTedTalkFromReview(review);
			boolean contain = tedTalkIds.contains(talk.getTedTalkId());
			if(!contain) {
				tedTalkIds.add(talk.getTedTalkId());
				tedTalks.add(talk);
			}
			HttpSession session = req.getSession();
			session.setAttribute("reviews", reviews);
			session.setAttribute("accounts", accounts);
			session.setAttribute("tedTalks", tedTalks);
			session.setAttribute("results", true);						
			req.getRequestDispatcher("/_view/index.jsp").forward(req, resp);
		}

	}
}
