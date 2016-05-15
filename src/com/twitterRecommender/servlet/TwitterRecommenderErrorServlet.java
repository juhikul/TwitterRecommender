package com.twitterRecommender.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class TwitterRecommenderErrorServlet
 */
@WebServlet("/TwitterRecommenderErrorServlet")
public class TwitterRecommenderErrorServlet extends HttpServlet {
	final static Logger LOG = Logger.getLogger(TwitterRecommenderErrorServlet.class);

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TwitterRecommenderErrorServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: process error
		Exception e = (Exception)request.getAttribute("javax.servlet.error.exception");
		request.setAttribute("errorMessage", e.getMessage() + " " + e.getClass().getName());
		request.getRequestDispatcher("error.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
