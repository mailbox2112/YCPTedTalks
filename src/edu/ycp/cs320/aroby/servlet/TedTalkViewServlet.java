package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class TedTalkViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TedTalkController controller = new TedTalkController();
		// Get the url with the query parameter
		String uri = req.getRequestURI()+"?"+req.getQueryString();
		
		// Parse the url
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String param : uri.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], Integer.parseInt(pair[1]));
			} else {
				result.put(pair[0], 0);
			}
		}
		
		for(String key : result.keySet()) {
			if(key.contains("delid")){
				if(result.get(key) != 0) {
					controller.deleteReview(result.get(key));
					resp.sendRedirect("/aroby/index");
				}
			} else if(key.contains("tid")) {
				// Create controllers required to get the tedtalk information from the tedTalk ID above
				// Saved info into session information
				
				TedTalk talk = controller.findTedTalkByID(result.get(key));
				List<Review> reviews = controller.findReviewbyTitle(talk.getTitle());
				Topic topic = controller.findTopicByID(talk.getTopicId());
				Speaker spec = controller.findSpeakerByID(talk.getSpeakerId());
				List<Account> accList = controller.getAccountbyReviews(reviews);
				
				HttpSession session = req.getSession();
				session.setAttribute("talk", talk);
				session.setAttribute("reviews", reviews);
				session.setAttribute("topic", topic);
				session.setAttribute("speaker", spec);
				session.setAttribute("accounts", accList);
				
				req.getRequestDispatcher("/_view/tedTalkView.jsp").forward(req, resp);
			}	
		}		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String uri = req.getRequestURI()+"?"+req.getQueryString();
		
		// Parse the url
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String param : uri.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], Integer.parseInt(pair[1]));
			} else {
				result.put(pair[0], 0);
			}
		}
		
		if(req.getParameter("reviewPage") != null){
			resp.sendRedirect("/aroby/reviewPage");
	}
}
}