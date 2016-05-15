package com.twitterRecommender.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppConfigInitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) sce.getServletContext().getAttribute("appContext");
		context.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ApplicationContext context = new AnnotationConfigApplicationContext("com.twitterRecommender");
		sce.getServletContext().setAttribute("appContext", context);
	}

}
