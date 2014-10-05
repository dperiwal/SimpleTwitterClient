package com.codepath.apps.basictwitter.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PopulateMentionsTimeLine extends PopulateTimeLine {

	public PopulateMentionsTimeLine(Activity containingActivity,
			ArrayList<Tweet> tweets, ArrayAdapter<Tweet> aTweets) {
		super(containingActivity, tweets, aTweets);
	}

	@Override
	protected void makeRESTcall(FetchDirection direction, int count, 
			long idHigherThan, long idLowerThan, AsyncHttpResponseHandler handler) {
		client.getMentionsTimeline(direction, TWEET_COUNT, idHigherThan, idLowerThan, handler);
	}

}
