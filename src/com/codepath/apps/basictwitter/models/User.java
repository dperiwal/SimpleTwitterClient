package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "User")
public class User extends Model implements Serializable {
	private static final long serialVersionUID = 782520282314155005L;
	@Column(name = "name")
	private String name;
	@Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "screen_name")
	private String screenName;
	@Column(name = "profile_image_url")
	private String profileImageUrl;

	public User() {
		super();
	}

	public String getName() {
		return name;
	}
	
	public long getUid() {
		return uid;
	}
	
	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static User fromJSON(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			
		} catch (JSONException ex) {
			return null;
		}
		return user;
	}
}
