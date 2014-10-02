package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.basictwitter.TwitterApplication;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {  
	private static final long serialVersionUID = -7358883615455615013L;
	@Column(name = "tweet_id")
	private long tweetId; 
	@Column(name = "body")
	private String body;
	@Column(name = "created_at")
	private String createdAt;
	@Column(name = "retweet_count")
	private int retweetCount;
	@Column(name = "favorite_count")
	private int favoriteCount;
	@Column(name = "media_url")
	private String mediaURL;
	@Column(name = "user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	
	public Tweet() {
		super();
	}

	public long getTweetId() {
		return tweetId;
	}

	public String getBody() {
		return body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public String getMediaURL() {
		return mediaURL;
	}
	
	public User getUser() {
		return user;
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
			
		} catch (JSONException ex) {
			return null;			
		}
		
		if (TwitterApplication.USE_ACTIVE_ANDROID) {
			// First save the user and then the tweet
			tweet.user.save();
			tweet.save();
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
		return tweets;
	}
	
	@Override
	public String toString() {
		return (body + " - " + user.getScreenName());
	}
	
	public static List<Tweet> getAll(String predicate) {
        // This is how you execute a query
        return new Select()
          .all()
          .from(Tweet.class)
          .where(predicate)
          .limit(20)
          .orderBy("tweet_id DESC")
          .execute();
    }

}
