package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
	public static final String USER_KEY = "USER";
	private static final long serialVersionUID = 782520282314155005L;
	private String name;
	private long userId;
	private String screenName;
	private String profileImageUrl;
	private String profileBackgroundImageUrl;
	private String description;
	private int numTweets;
	private int numFollowers;
	private int numFollowing;

	public User() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumTweets() {
		return numTweets;
	}

	public void setNumTweets(int numTweets) {
		this.numTweets = numTweets;
	}

	public int getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(int numFollowers) {
		this.numFollowers = numFollowers;
	}
	
	public int getNumFollowing() {
		return numFollowing;
	}

	public void setNumFollowing(int numFollowing) {
		this.numFollowing = numFollowing;
	}

	public static User fromJSON(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.userId = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
			user.description = jsonObject.getString("description");
			user.numTweets = jsonObject.getInt("statuses_count");
			user.numFollowers = jsonObject.getInt("followers_count");
			user.numFollowing = jsonObject.getInt("friends_count");
		} catch (JSONException ex) {
			return null;
		}
		return user;
	}
}
