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
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/Index.jsp").forward(req, resp);	
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		if(req.getParameter("login") != null) {
			resp.sendRedirect("/aroby/login");
		} else if(req.getParameter("reviewPage") != null) {
			resp.sendRedirect("/aroby/reviewPage");
		} else if(req.getParameter("logout") != null) {
			HttpSession session1 = req.getSession(true);
			session1.invalidate();
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
		List<TedTalk> upTedTalks = new ArrayList<TedTalk>();
		List<Integer> tedTalkIds = new ArrayList<Integer>();
		List<Integer> speakerIds = new ArrayList<Integer>();
		List<Review> dates = new ArrayList<Review>();
		
		Search model = new Search();
		SearchController controller = new SearchController();
		topics = controller.getTopics();
		for(int i= 0; i<topics.size(); i++){	
			reviews.addAll(controller.findReviewsByTopic(topics.get(i).getTopic()));	
		}
		accounts = controller.getAccountFromReview(reviews);
		for(Review review : reviews) {
			TedTalk talk = controller.getTedTalkFromReview(review);
			// Because the Java .contains method sucks for anything that isn't a primitive, we work around that by
			// keeping track of the tedTalkIds instead of the actual TedTalks. Thanks, Java!
			boolean contain = tedTalkIds.contains(talk.getTedTalkId());
			if(!contain) {
				tedTalkIds.add(talk.getTedTalkId());
				tedTalks.add(talk);
			}
		}
		for(int i=0;i<tedTalks.size(); i++){
			ArrayList<Review> r = tedTalks.get(i).getReview();
			for(int j=0;j<r.size();j++){
				String day1 = r.get(j).getDate().substring(9, 10);
				String day2  = ZonedDateTime.now().toString().substring(9, 10);
				String month1 = r.get(j).getDate().substring(6, 7);
				String month2 = ZonedDateTime.now().toString().substring(6, 7);
				if(month1.equals(month2) && day1.contains(day2.subSequence(0, 1))){
					dates.add(r.get(j));
				}
			}
		}
		for(int i=0;i<dates.size();i++){
			for(TedTalk t : tedTalks){
				if(dates.get(i).getTedTalkId() == t.getTedTalkId()){
					upTedTalks.add(t);
				}
				if(upTedTalks.size() == 4){
					break;
				}
			}
		}
		HttpSession session = req.getSession();
		
		session.setAttribute("reviews", reviews);
		session.setAttribute("accounts", accounts);
		session.setAttribute("tedTalks", upTedTalks);
		session.setAttribute("results", true);
		
							
		req.getRequestDispatcher("/_view/index.jsp").forward(req, resp);
		
	}
}
