package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.activities.TweetDetailActivity;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine;

public abstract class TweetsListFragment extends Fragment {	
	protected ProgressBar pb;
	protected ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	protected ListView lvTweets;
	protected User user;
	protected boolean profileActivityListener = true; // by default

	private PopulateTimeLine populateTimeLine;
	
	protected abstract PopulateTimeLine getTimelinePopulator();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets, profileActivityListener);
		user = (User) getArguments().getSerializable(User.USER_KEY);
		populateTimeLine = getTimelinePopulator();	
		populateTimeLine.startPopulatingTimeLine();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		// Assign our view reference.
		pb = (ProgressBar) v.findViewById(R.id.pbLoading);
		populateTimeLine.setProgressBar(pb);
		
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		populateTimeLine.setEndlessScrollListener(lvTweets);

		SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);		
		populateTimeLine.setPullToRefresh(swipeContainer);
	
		setupListViewListener();
		
		// System.out.println("In onCreateView(): " + this.getClass().getName());
		
		// Return the view
		return v;
	}
	
	protected User getUser() {
		return user;
	}
	
	public void addATweet(Tweet newTweet) {
		// Put the new tweet in a list as that is the required type of argument
		// for the following methods.
		ArrayList<Tweet> newTweets = new ArrayList<Tweet>();
		newTweets.add(newTweet);
		populateTimeLine.saveTweetsInLocalDB(newTweets);
		populateTimeLine.updateAdapter(PopulateTimeLine.FetchDirection.FORWARD, newTweets);	
	}
	
	protected void setProfileActivityListener(boolean profileActivityListener) {
		this.profileActivityListener = profileActivityListener;
	}
	
	/**
	 * Sets up click listeners to see details of a Tweet in the list.
	 */
	private void setupListViewListener() {	
		
		// Set up a long click listener to view the details of a tweet in a separate activity. 
		lvTweets.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				launchTweetDetailActivity(position);
				return true;			
			}
			
			private void launchTweetDetailActivity(int position) {
				if (position < 0) { // possible?
					return;
				}
				// Set up an intent for EditItemActivity with the parameter values
				// of the position and the value of the item at the selected position
				Tweet tweet = aTweets.getItem(position);
				Intent i = new Intent(getActivity(), TweetDetailActivity.class);
				i.putExtra(Tweet.TWEET_KEY, tweet);
				startActivity(i);			
			}		
		});
			
	}
}
