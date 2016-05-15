package com.twitterRecommender.runner;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.twitterRecommender.api.TimeFinder;
import com.twitterRecommender.datamodel.BestTime;
import com.twitterRecommender.datamodel.TimeFinderResponse;
import com.twitterRecommender.exception.RecommenderNonRetryableException;
import com.twitterRecommender.exception.RecommenderRetryableException;
import com.twitterRecommender.sao.TwitterSao;

/**
 * Runner class from where the execution starts to fine best time.
 * 
 * @author juhi
 *
 */
public class TwitterRecommenderRunner {
	final static Logger LOG = Logger.getLogger(TwitterRecommenderRunner.class);

	/**
	 * @param args
	 * @throws Exception 
	 * @throws RecommenderNonRetryableException
	 * @throws RecommenderRetryableException
	 */
	public static void main(String[] args) throws Exception {
		long userId = Long.parseLong(args[0]);

		LOG.info("Processing informaton for " + userId);

        AnnotationConfigApplicationContext context;

        try{
            // Initialize the context.
            context = new AnnotationConfigApplicationContext("com.twitterRecommender");

            installShutdownHook(context);

        }catch(Throwable e){
        	throw new Exception("Uncaught exception", e);
        }

        LOG.info("user is " + userId);

        try {
			TimeFinder finder = context.getBean(TimeFinder.class);
			BestTime bestTime = finder.findBestTime(userId);
			TimeFinderResponse response = new TimeFinderResponse(context.getBean(TwitterSao.class).findUser(userId), bestTime);
			System.out.println(response);
		} catch (RecommenderRetryableException | RecommenderNonRetryableException e) {
			e.printStackTrace();
		}

	}

    private static void installShutdownHook(final AbstractApplicationContext context) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                context.close();
            }
        });
    }
}
