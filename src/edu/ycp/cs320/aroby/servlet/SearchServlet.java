package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;
import edu.ycp.cs320.aroby.controller.SearchController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
		for(String val : result.keySet()) {
			if(result.get(val) != 0) {
				SearchController controller = new SearchController();
				controller.deleteTedTalk(result.get(val));
			}
		}
		
		req.getRequestDispatcher("/_view/searchPage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		boolean searchError = false;
		
		Search model = new Search();
		SearchController controller = new SearchController();
		
		// Search is the LEFTMOST box
		String search = req.getParameter("search");
		// ExtraSearch is RIGHTMOST box
		String extraSearch = req.getParameter("extraSearch");
		String dropDownSelection = req.getParameter("options");
		
		model.setSearch(search);
		model.setExtraSearch(extraSearch);
		
		
		if(model.getSearch() != "" && model.getSearch() != null){
			controller.setModel(model);
			List<Review> reviews = new ArrayList<Review>();
			List<Account> accounts = new ArrayList<Account>();
			List<TedTalk> tedTalks = new ArrayList<TedTalk>();
			List<Topic> topics = new ArrayList<Topic>();
			List<Speaker> speakers = new ArrayList<Speaker>();
			List<Integer> tedTalkIds = new ArrayList<Integer>();
			List<Integer> speakerIds = new ArrayList<Integer>();
			
			// Check what they selected from the drop down
			// If we selected authors, we'll find reviews by author
			if(dropDownSelection.equals("author")) {
				reviews = controller.findReviewsByAuthor(model.getSearch(), model.getExtraSearch());
				accounts = controller.getAccountFromReview(reviews);
				topics = controller.getTopics();
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
				
				// Find all the speakers for each TEDTalk, but don't add repeats or the JSP will display redundant info
				for(TedTalk talk : tedTalks) {
					Speaker speaker = controller.getSpeakerFromTedTalk(talk);
					boolean contain = speakerIds.contains(speaker.getSpeakerId());
					if(!contain) {
						speakerIds.add(speaker.getSpeakerId());
						speakers.add(speaker);
					}
				}
			// If not, we'll find them by topic
			} else if(dropDownSelection.equals("topic")) {
				reviews = controller.findReviewsByTopic(search);
				accounts = controller.getAccountFromReview(reviews);
				topics = controller.getTopics();
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
				
				// Find all the speakers for each TEDTalk, but don't add repeats or the JSP will display redundant info
				for(TedTalk talk : tedTalks) {
					Speaker speaker = controller.getSpeakerFromTedTalk(talk);
					boolean contain = speakerIds.contains(speaker.getSpeakerId());
					if(!contain) {
						speakerIds.add(speaker.getSpeakerId());
						speakers.add(speaker);
					}
				}
			// Or by title
			} else if(dropDownSelection.equals("title")) {
				reviews = controller.findReviewsByTitle(search);
				accounts = controller.getAccountFromReview(reviews);
				topics = controller.getTopics();
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
				
				// Find all the speakers for each TEDTalk, but don't add repeats or the JSP will display redundant info
				for(TedTalk talk : tedTalks) {
					Speaker speaker = controller.getSpeakerFromTedTalk(talk);
					boolean contain = speakerIds.contains(speaker.getSpeakerId());
					if(!contain) {
						speakerIds.add(speaker.getSpeakerId());
						speakers.add(speaker);
					}
				}
			}
			
			// Put the lists into the session info, then go back to the search page
			HttpSession session = req.getSession();
			if (reviews.size() != 0 && accounts.size() != 0 && tedTalks.size() != 0 && topics.size() != 0 && speakers.size() != 0) {
				session.setAttribute("reviews", reviews);
				session.setAttribute("accounts", accounts);
				session.setAttribute("tedTalks", tedTalks);
				session.setAttribute("topics", topics);
				session.setAttribute("speakers", speakers);
				session.setAttribute("results", true);
			} else {
				session.setAttribute("results", false);
			}
			session.removeAttribute("error");
			req.getRequestDispatcher("/_view/searchView.jsp").forward(req, resp);
		} else {
			searchError = true;
			HttpSession session = req.getSession();
			session.setAttribute("error", searchError);
			req.getRequestDispatcher("/_view/searchPage.jsp").forward(req, resp);
		}
	}
}
