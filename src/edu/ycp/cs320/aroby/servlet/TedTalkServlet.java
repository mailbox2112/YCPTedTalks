package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.model.Speaker;
import edu.ycp.cs320.aroby.controller.ReviewController;
import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.TedTalk;
import edu.ycp.cs320.aroby.model.Topic;

public class TedTalkServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/_view/tedTalkPage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Review model = new Review();
		ReviewController control = new ReviewController();
		TedTalk talk = new TedTalk();
		TedTalkController controller = new TedTalkController();
		Boolean talkCreated = false;
		int rating = 0;
		
		ArrayList<Review> review = new ArrayList<Review>();
		HttpSession session = req.getSession(true);
		
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String url_string = req.getParameter("url");
		String topic = req.getParameter("topic");
		String author = req.getParameter("author");
		String review_1 = req.getParameter("review");
		ZonedDateTime date = ZonedDateTime.now();
		String recommend = req.getParameter("recommendations");
		String rating_s =req.getParameter("rating");
		if(rating_s != "" & rating_s != null){
			rating = Integer.parseInt(rating_s);
		}
		if(title != "" || description != "" || url_string != null 
				|| topic != "" || author != null || review_1 != null){
			Topic tops = new Topic();
			tops.setTopic("topic");
			
			Speaker auth = new Speaker();
			int ind = author.indexOf(" ");
			auth.setFirstname(author.substring(0, ind+ 1));
			auth.setLastname(author.substring(ind+1));
			
			model.setReview(review_1);
			model.setDate(date);
			model.setTedTalkId(talk.getTedTalkId());
			model.setReviewId(1);
			model.setDate(date);
			model.setTedTalkId(talk.getTedTalkId());
			model.setRecommendation(recommend);
			
			review.add(model);
			
			talk.setDescription(description);
			talk.setLink(new URL(url_string));
			talk.setTitle(title);
			talk.setSpeakerId(auth.getSpeakerId());
			talk.setTopicId (tops.getTopicId());
			talk.setReview(null);
			controller.set_TedTalk(talk.getTitle(), talk.getDescription(), talk.getTedTalkId(), talk.getSpeakerId(), talk.getTopicId(), talk.getLink(), talk.getReview());
			talkCreated = true;
			}
		else{
			talkCreated = false;
		}
		req.setAttribute("model", talk);
		resp.sendRedirect("/aroby/index");
		}	
	}
