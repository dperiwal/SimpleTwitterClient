package com.codepath.apps.basictwitter.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.persistence.PersistenceManager;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * This utility class manages fetching and populating of the tweets
 * in a timeline. Once it is initialized with the REST client, 
 * the list view and the associated array adapter, it can fetch 
 * more tweets in forward direction as well as in backward direction.
 * 
 * It is designed to get tweets from a local data source in case of 
 * any network error.
 * 
 * @author Damodar Periwal
 *
 */
public class PopulateTimeLine {	
	public enum FetchDirection {
		FORWARD, BACKWARD		
	}	
	public int TWEET_COUNT = 25; // For every fetch
	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private ProgressBar pb;
	private long idHigherThan;
	private long idLowerThan; 
	private boolean freshStart;
	private Activity containingActivity;
	private SwipeRefreshLayout swipeContainer;

	private PersistenceManager persistenceManager;
	
	public PopulateTimeLine(Activity containingActivity, ArrayList<Tweet> tweets,
			ArrayAdapter<Tweet> aTweets, ListView lvTweets, ProgressBar pb) {
		super();
		this.containingActivity = containingActivity;
		this.tweets = tweets;
		this.aTweets = aTweets;
		this.lvTweets = lvTweets;
		this.pb = pb;
		this.client = TwitterApplication.getRestClient();
		idHigherThan = 1;
		idLowerThan = Long.MAX_VALUE; // Will be reset after the first fetch
		this.freshStart = true;
	}
	
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;		
	}
	
	public void reset() {
		aTweets.clear();
		freshStart = true;
		idHigherThan = 1;
		idLowerThan = Long.MAX_VALUE; // Will be reset after the first fetch	
	}
	
	/**
	 * Fetches next set of tweets from the server in either forward or 
	 * backward direction based on the parameter.
	 * 
	 * @param direction Forward or Backward
	 */
	public void fetchMore(final FetchDirection direction) {
     /*    	Log.d("Debug", "In fetchMore, higherThan(" + idHigherThan + 
				"), lowerThan(" + idLowerThan + ")");*/
		// containingActivity.setProgressBarIndeterminateVisibility(true);
		
		pb.setVisibility(ProgressBar.VISIBLE);
    	if (!Utils.isNetworkAvailable(containingActivity)) {
			Log.i("INFO", Utils.NETWORK_UNAVAILABLE_MSG);
			Toast.makeText(containingActivity, Utils.NETWORK_UNAVAILABLE_MSG, Toast.LENGTH_SHORT).show();
			if (direction == FetchDirection.FORWARD) {
			    swipeContainer.setRefreshing(false); 
			}			

			// Get tweets from the local database
			List<Tweet> newTweets = getTweetsFromLocalDB(direction,idHigherThan, idLowerThan);
			updateAdapter(direction, newTweets);

			pb.setVisibility(ProgressBar.INVISIBLE);
			//containingActivity.setProgressBarIndeterminateVisibility(false);			
			return;
		}
    	
		client.getHomeTimeline(direction, TWEET_COUNT, idHigherThan, idLowerThan, 
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				// Log.d("Debug", "In populateTimeline:onSuccess, new tweets=" + json.length());
				// containingActivity.setProgressBarIndeterminateVisibility(false);
				pb.setVisibility(ProgressBar.INVISIBLE);
				if (direction == FetchDirection.FORWARD) {
				    swipeContainer.setRefreshing(false); 
				}
				if (json.length() == 0) {
					return; // Nothing to do
				}
				// Log.d("Debug",json.toString());
				ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
				saveTweetsInLocalDB(newTweets);
				
				updateAdapter(direction, newTweets);		
			}
			
			@Override
			public void onFailure(Throwable e, String error) {
				// containingActivity.setProgressBarIndeterminateVisibility(false);
				pb.setVisibility(ProgressBar.INVISIBLE);
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
	
	private void updateAdapter(FetchDirection direction, List<Tweet> newTweets) {	
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
	
	private void updateIdHigherThan(List<Tweet> newTweets) {
		for (int i = 0; i < newTweets.size(); i++) {
			long newId = newTweets.get(i).getTweetId();
			if (newId > idHigherThan) {
				idHigherThan = newId;
			}
		}
		// Log.d("Debug", "New idHigherThan=" + idHigherThan);
	}
	
	private void updateIdLowerThan(List<Tweet> newTweets) {
		for (int i = 0; i < newTweets.size(); i++) {
			long newId = newTweets.get(i).getTweetId();
			if (newId < idLowerThan) {
				idLowerThan = newId;
			}
		}		
		// Log.d("Debug", "New idLowerThan=" + idLowerThan);
	}
	
	private void saveTweetsInLocalDB(ArrayList<Tweet> newTweets) {
		if (persistenceManager == null) {
			return;
		}
		// Toast.makeText(containingActivity, "Entering saveTweetsInLocalDB", Toast.LENGTH_SHORT).show();
		try {
			persistenceManager.saveTweets(newTweets);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Toast.makeText(containingActivity, "Exiting saveTweetsInLocalDB", Toast.LENGTH_SHORT).show();
	}
	
	private List<Tweet> getTweetsFromLocalDB(FetchDirection direction, long idHigherThan, long idLowerThan) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		if (persistenceManager == null) {
			return tweets;
		}
		
		String predicate;
		if (direction == FetchDirection.FORWARD) {
			predicate = "tweetId > " + Long.valueOf(idHigherThan).toString();
		} else {
			predicate = "tweetId < " + Long.valueOf(idLowerThan).toString();
		}	
		predicate = predicate + " ORDER BY tweetId DESC";
		// System.out.println("Getting tweets from the local database, predicate=" + predicate);
		// Log.i("INFO", "Getting tweets from the local database, predicate=" + predicate);
		try {
			tweets = persistenceManager.queryTweets(predicate, TWEET_COUNT, true);
			// Log.i("INFO", "Tweet count from the local database=" + tweets.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return tweets;
	}

}
