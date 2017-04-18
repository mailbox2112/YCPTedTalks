package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.aroby.model.Review;
import edu.ycp.cs320.aroby.controller.ReviewController;

public class ReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/reviewPage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Review model = new Review();
		ReviewController controller = new ReviewController();
		int rating;

			String name = req.getParameter("name");
			String author = req.getParameter("author");
			String description = req.getParameter("descript");
			String topic = req.getParameter("topic");
			String review = req.getParameter("review");
			String link = req.getParameter("link");
			String recommendations = req.getParameter("recommendations");
			String rating_string = req.getParameter("rating");
			if(rating_string != "" & rating_string != null){
				rating = Integer.parseInt(rating_string);
				//model.setReview(name, author, topic, description, review, link, recommendations, rating);
			}
			controller.setModel(model);
			String errorMessage = null;
			
			if(name == "" || author == "" || description == "" || topic == "" || review == ""  || link == "" || rating_string == ""){
				req.setAttribute("model", model);
				req.getRequestDispatcher("/_view/reviewPage.jsp").forward(req, resp);
				errorMessage = "Complete all required fields";
			}
			else{
				controller.isDone();
				System.out.print("Your review was submitted");
				resp.sendRedirect("/aroby/readPage");
			}	
	}
}
