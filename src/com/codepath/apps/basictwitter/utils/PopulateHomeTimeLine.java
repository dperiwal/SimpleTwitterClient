package com.codepath.apps.basictwitter.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PopulateHomeTimeLine extends PopulateTimeLine {

	public PopulateHomeTimeLine(User user, Activity containingActivity,
			ArrayList<Tweet> tweets, ArrayAdapter<Tweet> aTweets) {
		super(user, containingActivity, tweets, aTweets);
	}
	
	@Override
	protected void makeRESTcall(User user, FetchDirection direction, int count, 
			long idHigherThan, long idLowerThan, AsyncHttpResponseHandler handler) {
		client.getHomeTimeline(direction, TWEET_COUNT, idHigherThan, idLowerThan, handler);
		
	}
	
	

}
