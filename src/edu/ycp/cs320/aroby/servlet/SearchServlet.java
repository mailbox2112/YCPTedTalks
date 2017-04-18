package edu.ycp.cs320.aroby.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.aroby.model.Search;
import edu.ycp.cs320.aroby.controller.SearchController;
import edu.ycp.cs320.aroby.model.TedTalk;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/searchPage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
			Search model = new Search();
			SearchController controller = new SearchController();
			
			String search = req.getParameter("search");
			
			model.setSearch(search);
			String errorMessage = null;
			
			if(model.getSearch() != ""){
				controller.setModel(model);
				req.setAttribute("model", model);
				req.getRequestDispatcher("/_view/searchView.jsp").forward(req, resp);
			}
			else{
				errorMessage = "Type in something!";
			}
	}
	
	//private int getInteger(HttpServletRequest req, String name) {
		//return Integer.parseInt(req.getParameter(name));
	//}
}
