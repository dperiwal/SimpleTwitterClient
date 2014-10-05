package com.codepath.apps.basictwitter.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PopulateHomeTimeLine extends PopulateTimeLine {

	public PopulateHomeTimeLine(Activity containingActivity,
			ArrayList<Tweet> tweets, ArrayAdapter<Tweet> aTweets) {
		super(containingActivity, tweets, aTweets);
	}
	
	@Override
	protected void makeRESTcall(FetchDirection direction, int count, 
			long idHigherThan, long idLowerThan, AsyncHttpResponseHandler handler) {
		client.getHomeTimeline(direction, TWEET_COUNT, idHigherThan, idLowerThan, handler);
		
	}
	
	

}
