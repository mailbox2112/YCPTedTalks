package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.controller.LoginController;
import edu.ycp.cs320.aroby.controller.NumbersController;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Numbers;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Account model = new Account();
		LoginController controller= new LoginController();
		
		String inputEmail = req.getParameter("email");
		String inputPass = req.getParameter("password");
		
		if(inputEmail != null && inputPass != null) {
			model.setEmail(inputEmail);
			model.setPassword(inputPass);
		}
		controller.setModel(model);
		
		// TODO: Session information
		// TODO: Modify servlet/jsp to use firstname/lastname instead of just name
		if (controller.login() == true) {
			model = controller.getModel();
			HttpSession session =  req.getSession(true);
			session.setAttribute("accountId", model.getAccountId());
			session.setAttribute("login", true);
			session.setAttribute("admin", model.getAdmin());
			session.setAttribute("name", model.getFirstName());
			if(session.getAttribute("login_failure") != null) {
				session.removeAttribute("login_failure");
			}
			resp.sendRedirect("/aroby/index");
		}
		else {
			HttpSession session = req.getSession(true);
			session.setAttribute("login_failure", true);
			resp.sendRedirect("/aroby/login");
		}
	}
}
