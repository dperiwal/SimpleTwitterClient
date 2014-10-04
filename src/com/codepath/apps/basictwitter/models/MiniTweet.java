package com.codepath.apps.basictwitter.models;

/**
 * A lightweight class for quick access to relevant Tweet in the database.
 * 
 * @author Damodar Periwal
 *
 */
public class MiniTweet {
	private long tweetId;

	public MiniTweet() {
		super();
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}
	
}
