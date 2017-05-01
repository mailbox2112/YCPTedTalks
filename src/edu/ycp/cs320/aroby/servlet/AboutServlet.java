package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.aroby.controller.AccountController;
import edu.ycp.cs320.aroby.controller.LoginController;
import edu.ycp.cs320.aroby.model.Account;
import edu.ycp.cs320.aroby.model.Professor;
import edu.ycp.cs320.aroby.model.Student;

public class AboutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/_view/about.jsp").forward(req, resp);
	}

	//@Override
	///protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	//}
}