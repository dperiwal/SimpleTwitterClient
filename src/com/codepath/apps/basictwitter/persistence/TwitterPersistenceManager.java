package com.codepath.apps.basictwitter.persistence;

import java.util.List;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;

public interface TwitterPersistenceManager {

	/**
	 * Inserts the passed new User object in the database.
	 * 
	 * @param user The new User object
	 * @throws Exception
	 */
	public void insertUser(User user) throws Exception;
	
	/**
	 * Inserts the passed new User objects in the database.
	 * 
	 * @param users A list of User objects
	 */
	public void insertUsers(List<User> users) throws Exception;
	
	/**
	 * Updates an existing User object in the database.
	 * @param user The newer User object
	 * 
	 * @throws Exception
	 */
	public void updateUser(User user) throws Exception;

	/**
	 * Updates a User object if it already exists in the database or inserts
	 * the new User object.
	 * 
	 * @param user The User object
	 * @throws Exception
	 */
	public void saveUser(User user) throws Exception;
	
	/**
	 * Queries User objects from the database.
	 * 
	 * @param predicate Search predicate (WHERE clause)
	 * @param maxCount The maximum number of objects to be retrieved
	 * @param deep True => ALso get the associated persistent objects (defined in the class)
	 * @return List of qualifying users
	 * @throws Exception
	 */
	public List<Tweet> queryUsers(String predicate, int maxCount, boolean deep) throws Exception;
	
	/** 
	 * Inserts a new Tweet object in the database.
	 * 
	 * @param tweet
	 * @throws Exception
	 */
	public void insertTweet(Tweet tweet) throws Exception;
	
	/**
	 * Inserts the passed new Tweet objects without the associated User object. 
	 * However, the method makes sure that the associated User objects exist in the database.
	 * 
	 * @param tweets The new Tweet objects
	 * @throws Exception
	 */
	public void insetTweets(List<Tweet> tweets) throws Exception;
	 
	/**
	 * Saves only those tweets from the passed list of tweets that  
	 * do not already exist in the database.
	 * 
	 * @param tweets
	 * @throws Exception
	 */
	public void saveTweets(List<Tweet> tweets) throws Exception;
	
	/**
	 * Queries Tweet objects from the database.
	 * 
	 * @param predicate Search predicate (WHERE clause)
	 * @param maxCount The maximum number of objects to be retrieved
	 * @param deep True => Also get the associated persistent objects (defined in the class)
	 * @return List of qualifying tweets
	 * @throws Exception
	 */
	public List<Tweet> queryTweets(String predicate, int maxCount, boolean deep) throws Exception;

}
