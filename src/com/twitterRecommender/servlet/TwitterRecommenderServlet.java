package com.twitterRecommender.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.twitterRecommender.api.TimeFinder;
import com.twitterRecommender.datamodel.BestTime;
import com.twitterRecommender.datamodel.TimeFinderResponse;
import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;
import com.twitterRecommender.sao.TwitterSao;
import com.twitterRecommender.utils.Constants;

/**
 * Servlet implementation class TwitterRecommenderServlet
 */
@WebServlet("/TwitterRecommenderServlet")
public class TwitterRecommenderServlet extends HttpServlet {
	final static Logger LOG = Logger.getLogger(TwitterRecommenderServlet.class);
	private static final long serialVersionUID = 1L;

	private ApplicationContext context;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public TwitterRecommenderServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
     	this.context = (AnnotationConfigApplicationContext) config.getServletContext().getAttribute("appContext");

    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			long userId = Long.parseLong(request.getParameter("userId"));

			LOG.info("Starting request for user id: " + userId);
			TwitterSao twitterSao = context.getBean(TwitterSao.class);
			if (!twitterSao.isValidUser(userId)) {
				LOG.info("Invalid user id: " + userId);
				request.setAttribute("status", Constants.INVALID_USER);
			} else {
				request.setAttribute("status", Constants.VALID_USER);
				TimeFinder finder = context.getBean(TimeFinder.class);
				BestTime bestTime = finder.findBestTime(userId);
				TimeFinderResponse timeFinderResponse = new TimeFinderResponse(twitterSao.findUser(userId), bestTime);

				LOG.info("Best time for tweet: " + timeFinderResponse.getBestTime());

				request.setAttribute("bestTime", timeFinderResponse);
			}
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (RecommenderRetryableException | RecommenderNonRetryableException e) {
			LOG.error("Exception while fetching recommended time", e);
			throw new ServletException("Exception while fetching recommended time", e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
