package com.codepath.apps.basictwitter.utils;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * This utility class manages fetching and populating of the tweets
 * in a timeline. Once it is initialized with the REST client, 
 * the list view and the associated array adapter, it can fetch 
 * more tweets in forward direction as well as in backward direction.
 * 
 * @author Damodar Periwal
 *
 */
public class PopulateTimeLine {	
	public enum FetchDirection {
		FORWARD, BACKWARD		
	}	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private long idHigherThan;
	private long idLowerThan; 
	private boolean freshStart;
	private Context context;
	SwipeRefreshLayout swipeContainer;
	
	public PopulateTimeLine(Context context, ArrayList<Tweet> tweets,
			ArrayAdapter<Tweet> aTweets, ListView lvTweets) {
		super();
		this.context = context;
		this.tweets = tweets;
		this.aTweets = aTweets;
		this.lvTweets = lvTweets;
		this.client = TwitterApplication.getRestClient();
		idHigherThan = 1;
		idLowerThan = Long.MAX_VALUE; // Will be reset after the first fetch
		this.freshStart = true;
	}
	
	public void reset() {
		aTweets.clear();
		freshStart = true;
		idHigherThan = 1;
		idLowerThan = Long.MAX_VALUE; // Will be reset after the first fetch	
	}
	
	public void fetchMore(final FetchDirection direction) {
    	Log.d("Debug", "In fetchMore, higherThan(" + idHigherThan + 
				"), lowerThan(" + idLowerThan + ")");
    	if (!Utils.isNetworkAvailable(context)) {
			Log.i("INFO", Utils.NETWORK_UNAVAILABLE_MSG);
			Toast.makeText(context, Utils.NETWORK_UNAVAILABLE_MSG, Toast.LENGTH_SHORT).show();
			return;
		}
		client.getHomeTimeline(direction, idHigherThan, idLowerThan, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				// Log.d("Debug", "In populateTimeline:onSuccess, new tweets=" + json.length());
				if (direction == FetchDirection.FORWARD) {
				    swipeContainer.setRefreshing(false); 
				}
				if (json.length() == 0) {
					return; // Nothing to do
				}
				// Log.d("Debug",json.toString());
				ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);

				if (direction == FetchDirection.FORWARD) { // we have got newer tweets
					updateIdHigherThan(newTweets);			
				    tweets.addAll(0, newTweets); // prepend the new tweets
				    aTweets.notifyDataSetChanged();
				}
				else { // We have got older tweets
					updateIdLowerThan(newTweets);
					aTweets.addAll(newTweets); // append
				}
				if (freshStart) { // Very first time, assumes forward fetch has happened
					updateIdLowerThan(newTweets);
					freshStart = false;
				}
			}
			
			@Override
			public void onFailure(Throwable e, String error) {
				Log.d("Debug", "in populateTimeline:onFailure");
				Log.d("Debug", error);
			}
		});
	}
	
	public void setPullToRefresh(SwipeRefreshLayout swipeRefreshLayout) {
		swipeContainer = swipeRefreshLayout;
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				fetchMore(FetchDirection.FORWARD);
			}
		});
	}
		
	public void startPopulatingTimeLine() {
		// Log.d("Debug", "In startPopulatingTimeLine");
		reset();	
		// Attach a ScrollListner to query for tweets.	
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				fetchMore(FetchDirection.BACKWARD); // Get older tweets
			}
		});
		fetchMore(FetchDirection.FORWARD);; // First time
	}
	
	private void updateIdHigherThan(ArrayList<Tweet> newTweets) {
		for (int i = 0; i < newTweets.size(); i++) {
			long newId = newTweets.get(i).getId();
			if (newId > idHigherThan) {
				idHigherThan = newId;
			}
		}
		Log.d("Debug", "New idHigherThan=" + idHigherThan);
	}
	
	private void updateIdLowerThan(ArrayList<Tweet> newTweets) {
		for (int i = 0; i < newTweets.size(); i++) {
			long newId = newTweets.get(i).getId();
			if (newId < idLowerThan) {
				idLowerThan = newId;
			}
		}		
		Log.d("Debug", "New idLowerThan=" + idLowerThan);
	}
}
