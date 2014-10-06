package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet implements Serializable {  
	public static final String TWEET_KEY = "TWEET";
	private static final long serialVersionUID = -7358883615455615013L;
	private long tweetId; 
	private String body;
	private String createdAt;
	private int retweetCount;
	private int favoriteCount;
	private String mediaURL;
	private boolean mentionsMe;
	private long userId;
	private User user;

	public Tweet() {
		super();
		mentionsMe = false; // default
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public boolean getMentionsMe() {
		return mentionsMe;
	}

	public void setMentionsMe(boolean mentionsMe) {
		this.mentionsMe = mentionsMe;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Extract values from jsonObject to populate the member variables
		try {
			tweet.tweetId = jsonObject.getLong("id");
			tweet.body = jsonObject.getString("text");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.retweetCount = jsonObject.getInt("retweet_count");
			tweet.favoriteCount = jsonObject.getInt("favorite_count");
			
			// Get media url
			if (jsonObject.optJSONObject("extended_entities") != null) {
				JSONObject extendedEntities = jsonObject.getJSONObject("extended_entities");
				if (extendedEntities.optJSONArray("media") != null) {
					JSONArray mediaArray = extendedEntities.getJSONArray("media");
					if (mediaArray.length() > 0) {
						JSONObject mediaObject = mediaArray.getJSONObject(0);
						if (mediaObject.getString("type").equals("photo")) {
							tweet.mediaURL = mediaObject.getString("media_url");
						}
					}
				}
			}
			
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
			tweet.userId = tweet.user.getUserId();
					
		} catch (JSONException ex) {
			return null;			
		}
		
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray array) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = array.getJSONObject(i);			
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		// Log.i("INFO", "Number of new tweets=" + array.length());
		// JXUtilities.printQueryResults(tweets);
		return tweets;
	}
	
	@Override
	public String toString() {
		return (body + " - " + user.getScreenName());
	}

}
