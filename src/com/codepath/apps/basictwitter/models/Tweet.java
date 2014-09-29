package com.codepath.apps.basictwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet {  
	private long id; // Tweet id
	private String body;
	private String createdAt;
	private User user;
	
	public long getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Extract values from jsonObject to populate the member variables
		try {
			tweet.id = jsonObject.getLong("id");
			tweet.body = jsonObject.getString("text");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
			
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
		Log.i("INFO", "Number of new tweets=" + array.length());
		return tweets;
	}
	
	@Override
	public String toString() {
		return (body + " - " + user.getScreenName());
	}


}
