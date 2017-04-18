package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.controller.ReviewController;
import edu.ycp.cs320.aroby.controller.TedTalkController;
import edu.ycp.cs320.aroby.model.TedTalk;

public class TedTalkServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/_view/tedTalkPage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TedTalk talk = new TedTalk();
		TedTalkController controller = new TedTalkController();
		controller.set_TedTalk(talk.getDescription(), talk.getTitle(), talk.getTedTalkId(), talk.getTopicId(), talk.getSpeakerId(), talk.getLink(),talk.getReview());
		String errorMessage = null;
		
		if(talk.equals(null)){
			errorMessage = "No such Ted Talk exists";
		}
		else{
			req.setAttribute("talk", talk);
			req.getRequestDispatcher("/_view/tedTalkPage.jsp").forward(req, resp);
		}
	}
}
