package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.controller.AccountController;
import edu.ycp.cs320.aroby.model.Account;


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
				req.getRequestDispatcher("/_view/changeMajor.jsp").forward(req, resp);
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
		else if (req.getParameter("oldMajor") != null) {
			// Get the account info and set the controller's model accordingly
			AccountController controller = new AccountController();
			HttpSession session = req.getSession();
			int accountId = (Integer) session.getAttribute("accountId");
			Account model = controller.getAccountFromDb(accountId);
			controller.setModel(model);
			
			// If we have things in the major fields
			if (req.getParameter("major") != null && req.getParameter("reenteredMajor") != null) {
				String major = (String) req.getParameter("major");
				String reenteredMajor = (String) req.getParameter("reenteredMajor");
				
				// Check that they reentered the correct major
				if (major.equals(reenteredMajor)) {
					// Change the major in the DB
					boolean success = controller.changeMajor(major);
					
					if (success) {
						System.out.println("Major changed successfully.");
					}
					req.getRequestDispatcher("/_view/accountManagement.jsp").forward(req, resp);
				} else {
					// Put an error message into the request if they didn't match
					errorMessage = "Majors do not match";
					req.setAttribute("errorMessage", errorMessage);
					req.getRequestDispatcher("/_view/changeMajor.jsp").forward(req, resp);
				}
			}
		}
	}
}
