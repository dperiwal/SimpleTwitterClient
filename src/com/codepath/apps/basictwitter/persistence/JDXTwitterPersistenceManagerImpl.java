package com.codepath.apps.basictwitter.persistence;

import java.util.ArrayList;
import java.util.List;

import com.codepath.apps.basictwitter.models.MiniTweet;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.softwaretree.jdxandroid.JDXHelper;
import com.softwaretree.jdxandroid.JDXSetup;

/**
 * A utility class providing application specific methods to save and retrieve
 * model objects using JDX ORM.
 * <p>
 * @author Damodar Periwal
 *
 */
public class JDXTwitterPersistenceManagerImpl implements TwitterPersistenceManager {

	public static int THRESHOLD_TWEET_COUNT = 100; // To trigger cleaning up old tweets stored locally
	public static int THRESHOLD_RETAIN_COUNT = 40; // To keep at least these many tweeets locally
	private JDXSetup jdxSetup;
	private int saveCount = 0;
	
	// JDXHelper provides a simple wrapper over JDXSetup
	private JDXHelper jdxHelper;
	
	private static String UserClassName = User.class.getName();
	private static String TweetClassName = Tweet.class.getName();
	private static String MiniTweetClassName = MiniTweet.class.getName();

	public JDXTwitterPersistenceManagerImpl(JDXSetup jdxSetup) {
		this.jdxSetup = jdxSetup;
		jdxHelper = new JDXHelper(jdxSetup);
	}

	public void insertUser(User user) throws Exception {
		jdxHelper.insert(user, false);		
	}
	
	public void insertUsers(List<User> users) throws Exception {
		jdxHelper.insert(users, false);		
	}
	
	public void updateUser(User user) throws Exception {
		jdxHelper.update(user, false);		
	}
	
	private boolean userExists(User user) throws Exception {
		int count = jdxHelper.getObjectCount(UserClassName, "userId",
				"userId=" + user.getUserId());
		return (count > 0);
	}

	public void saveUser(User user) throws Exception {
		// First check if the user already exists in the database.
		if (userExists(user)) {
			updateUser(user);
		} else {
			insertUser(user);
		}
	}
	
	public List<Tweet> queryUsers(String predicate, int maxCount, boolean deep) throws Exception {
		return (List<Tweet>) jdxHelper.getObjects(UserClassName, predicate, maxCount, deep, null);
	}

	public void insertTweet(Tweet tweet) throws Exception {
		jdxHelper.insert(tweet, false);
	}
	
	private boolean tweetExists(Tweet tweet) throws Exception {
		// First check if the user already exists in the database.
		int count = jdxHelper.getObjectCount(TweetClassName, "tweetId",
				"tweetId=" + tweet.getTweetId());
		return (count > 0);
	}

	public void insetTweets(List<Tweet> tweets) throws Exception {
		// First make sure that the associated User is saved in the database.
		for (Tweet tweet : tweets) {		
			saveUser(tweet.getUser());
		}
		// Now save all the tweets	
		jdxHelper.insert(tweets, false);	
	}
	
	public void saveTweets(List<Tweet> tweets) throws Exception {
		ArrayList<Tweet> tweetsToBeInserted = new ArrayList<Tweet>();
		for (Tweet tweet : tweets) {
			if (!tweetExists(tweet)) {
				tweetsToBeInserted.add(tweet);
			}
		}
		insetTweets(tweetsToBeInserted);
		
		// Try to clean up old objects every third time new tweets are saved	
		saveCount++;
		if (saveCount % 3 == 0) { 
			cleanup();
	    }
	}
	
	/*
	 * Gets rid of old Tweet objects, if necessary.
	 */
	private void cleanup() {
		try {
			// Using a lightweight class (MiniTweet) for information gathering.
			int count = jdxHelper.getObjectCount(MiniTweetClassName, "tweetId", null);
			// Log.d("DEBUG", "In cleanup is count = " + count);
			if (count > THRESHOLD_TWEET_COUNT) {
				// Delete (count - THRESHOLD_RETAIN_COUNT) Tweet objects
				List<MiniTweet> miniTweets = queryMiniTweets(
						"ORDER BY tweetId DESC", THRESHOLD_RETAIN_COUNT, false);
				if (miniTweets.size() >= THRESHOLD_RETAIN_COUNT) {
					MiniTweet lastTweetToBeRetained = miniTweets.get(THRESHOLD_RETAIN_COUNT - 1);
					long minTweetId = lastTweetToBeRetained.getTweetId();

					// Log.d("DEBUG", "minTweetId for cleanup is " + minTweetId);
					// Now delete all the Tweet objects with tweetId < minTweetId
					jdxHelper.delete2(TweetClassName, "tweetId < " + minTweetId);
				}
			} 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Tweet> queryTweets(String predicate, int maxCount, boolean deep) throws Exception {
		return (List<Tweet>) jdxHelper.getObjects(TweetClassName, predicate, maxCount, deep, null);
	}
	
	public List<MiniTweet> queryMiniTweets(String predicate, int maxCount, boolean deep) throws Exception {
		return (List<MiniTweet>) jdxHelper.getObjects(MiniTweetClassName, predicate, maxCount, deep, null);
	}
	
}
