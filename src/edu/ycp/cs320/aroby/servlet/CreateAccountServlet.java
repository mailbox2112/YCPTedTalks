package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.controller.LoginController;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Professor;
import edu.ycp.cs320.aroby.model.Student;

public class CreateAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/_view/accountCreation.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginController controller= new LoginController();
		Account model = new Account();
		Boolean accountCreated = false;
		HttpSession session =  req.getSession(true);
		
		String inputEmail = req.getParameter("email");
		String inputPass = req.getParameter("password");
		String inputName = req.getParameter("name");
		String inputProfID = req.getParameter("professor_id");
		String inputMajor = req.getParameter("major");
		String inputYCPID = req.getParameter("ycp_id");
		String inputYear = req.getParameter("year");
		
		if(inputEmail != "" && inputPass != "" && inputName != "" &&
				inputEmail != null && inputPass != null && inputName != null) {
			if(inputProfID != "" && inputProfID != null) {
				// TODO: check for existing account and/or create new professor account!
				model = new Professor(); 
				model.setEmail(inputEmail);
				model.setFirstName(inputName);
				model.setPassword(inputPass);
				controller.setModel(model);
				accountCreated = true;
			}
			else if(inputMajor != "" && inputYCPID != "" && inputYear != "" &&
						inputMajor != null && inputYCPID != null && inputYear != null) {
				int student_id = Integer.parseInt(inputYCPID);
				int year = Integer.parseInt(inputYear);
				
				// TODO: create new student account after checking for existing ones
				model = new Student();
				model.setEmail(inputEmail);
				model.setFirstName(inputName);
				model.setPassword(inputPass);
				controller.setModel(model);
				accountCreated = true;
			}
		} else {
			accountCreated = false;
		}
		
		// TODO: Session information
		// TODO: modify jsp/servlet to use firstname/lastname
		// Session information signals if an account has been created successfully.
		// If so, we'll set the session attributes and redirect them to the index page.
		// If not, the page will tell them to enter all required fields
		if (accountCreated == true) {
			session.setAttribute("login", true);
			session.setAttribute("name", model.getFirstName());
			if (session.getAttribute("bad_info") != null) {
				session.removeAttribute("bad_info");
			}
			resp.sendRedirect("/aroby/index");
		}
		else {
			session.setAttribute("bad_info", true);
			resp.sendRedirect("/aroby/createAccount");
		}
	}
}
