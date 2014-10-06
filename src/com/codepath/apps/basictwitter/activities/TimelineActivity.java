package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.fragments.HomeTimeLineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimeLineFragment;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * This activity shows a list of tweets in my timeline. 
 * The list of tweets is scrollable - both forward and backward.
 * 
 * This activity also allows for composing new tweets by pressing the compose
 * button on the action bar. 
 * 
 * Pressing on a particular tweet takes to a detail view where the tweet 
 * can be replied also. 
 *  
 * @author Damodar Periwal
 *
 */
public class TimelineActivity extends FragmentActivity {
	static final int REQUEST_CODE = 50;
	private User userMe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	    
		setContentView(R.layout.activity_timeline);		
		setupUserProfile();	
		setupTabs();
	}
	
	private void setupTabs() {
		Bundle args = new Bundle();
		args.putSerializable(User.USER_KEY, userMe);
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
				.newTab()
				.setText("Home")
				.setIcon(R.drawable.ic_tab_home)
				.setTag("HomeTimelineFragment")
				.setTabListener(
						new FragmentTabListener<HomeTimeLineFragment>(
								R.id.flContainer, this, "Home",
								HomeTimeLineFragment.class, args));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Mentions")
				.setIcon(R.drawable.ic_tab_mention)
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new FragmentTabListener<MentionsTimeLineFragment>(
								R.id.flContainer, this, "Mentions",
								MentionsTimeLineFragment.class, args));
		actionBar.addTab(tab2);
	}
    
    public void onProfileView(MenuItem mi) {
    	Intent i = new Intent(this, ProfileActivity.class);
    	startActivity(i);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline_tweets, menu);
		// MenuItem composeItem = menu.findItem(R.id.action_compose);
		// MenuItem refreshItem = menu.findItem(R.id.action_refresh);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_compose) {
			// Create an intent for settings activity
			Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
			// Pass user data in the intent
			i.putExtra(User.USER_KEY, userMe);
			// Launch the new activity
			startActivityForResult(i, REQUEST_CODE);					
			return true;
		} else if (id == R.id.action_profile) {
			// Create an intent for profile activity
			Intent i = new Intent(this, ProfileActivity.class);
			// Pass the user data
			i.putExtra(User.USER_KEY, userMe);
	    	startActivity(i);				
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {			
			Tweet newTweet = (Tweet) data.getSerializableExtra(Tweet.TWEET_KEY);
			HomeTimeLineFragment homeTimeLineFragment = 
					(HomeTimeLineFragment) getSupportFragmentManager().findFragmentByTag("Home");
			homeTimeLineFragment.addATweet(newTweet);
			Log.d("DEBUG", "In onActivityResult, tweetid=" + newTweet.getTweetId() + ", uid=" + newTweet.getUser().getUserId()); 
		}
	}
	
	private void setupUserProfile() {		
		TwitterApplication.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				userMe = User.fromJSON(response);
				getActionBar().setTitle("@" + userMe.getScreenName());
			}
			
			@Override
			public void onFailure(Throwable e, String error) {
				Log.d("Debug", "In setupUserProfile:onFailure");
				Log.d("Debug", error);
			}
		});
	}
}
