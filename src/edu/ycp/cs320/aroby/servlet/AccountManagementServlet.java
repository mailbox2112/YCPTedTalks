package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.booksdb.persist.DerbyDatabase;
import edu.ycp.cs320.aroby.controller.AccountController;
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

public class AccountManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI() + "?" + req.getQueryString();

		// Parse the url
		Map<String, String> result = new HashMap<String, String>();
		for (String param : uri.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}

		for (String key : result.keySet()) {
			if (result.get(key).equals("email")) {
				req.getRequestDispatcher("/_view/changeEmail.jsp").forward(req, resp);
			} else if (result.get(key).equals("password")) {
				req.getRequestDispatcher("/_view/changePassword.jsp").forward(req, resp);
			} else if (result.get(key).equals("major")) {
				
			} else {
				req.getRequestDispatcher("/_view/accountManagement.jsp").forward(req, resp);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String errorMessage = null;
		if (req.getParameter("oldPassword") != null) {
			// Create a new controller, get the session info, and get the account based on login information
			AccountController controller = new AccountController();
			HttpSession session = req.getSession();
			int accountId = (Integer) session.getAttribute("accountId");
			Account model = controller.getAccountFromDb(accountId);
			controller.setModel(model);

			// Next, make sure the new passwords match
			if (req.getParameter("password") != null && req.getParameter("reenteredPassword") != null) {
				String pass = (String)req.getParameter("password");
				String reenteredPass = (String)req.getParameter("reenteredPassword");
				
				if(pass.equals(reenteredPass)) {
					// Call controller method to set the password in DB to the new password just entered
					boolean success = controller.changePassword(pass);
					if(success) {
						System.out.println("Password change successful.");
					}
					req.getRequestDispatcher("/_view/accountManagement.jsp").forward(req, resp);
				} else {
					// Put an error message into the request if the passwords don't match
					errorMessage = "Passwords do not match!";
					req.setAttribute("errorMessage", errorMessage);
					req.getRequestDispatcher("/_view/changePassword.jsp").forward(req, resp);
				}
			}
		}
		else if (req.getParameter("oldEmail") != null) {
			// Create a new controller, get the session info, and get the account based on login information
			AccountController controller = new AccountController();
			HttpSession session = req.getSession();
			int accountId = (Integer) session.getAttribute("accountId");
			Account model = controller.getAccountFromDb(accountId);
			controller.setModel(model);
			
			// Next, make sure the new emails match
			if (req.getParameter("email") != null && req.getParameter("reenteredEmail") != null) {
				String email = (String) req.getParameter("email");
				String reenteredEmail = (String) req.getParameter("reenteredEmail");
				
				if (email.equals(reenteredEmail)) {
					// Call controller method to set the new email in DB to the new email just entered
					boolean success = controller.changeEmail(reenteredEmail);
					
					if(success) {
						System.out.println("Email change successful.");
					}
					req.getRequestDispatcher("/_view/accountManagement.jsp").forward(req, resp);
				} else {
					// Put an error message into the request if the emails don't match
					errorMessage = "Email do not match!";
					req.setAttribute("errorMessage", errorMessage);
					req.getRequestDispatcher("/_view/changeEmail.jsp").forward(req, resp);
				}
			}
		}
	}
}
