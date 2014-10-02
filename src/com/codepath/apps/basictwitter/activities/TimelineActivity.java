package com.codepath.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine.FetchDirection;
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
public class TimelineActivity extends Activity {
	static final int REQUEST_CODE = 50;
	
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private PopulateTimeLine populateTimeLine;
	private String userHandle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// MUST request the feature before setting content view
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
        
		setContentView(R.layout.activity_timeline);
		
		setupUserProfile();
		
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		
		setupListViewListener();
		
		SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		
		populateTimeLine = new PopulateTimeLine(this, tweets, aTweets, lvTweets);	
		populateTimeLine.setPullToRefresh(swipeContainer);
		populateTimeLine.startPopulatingTimeLine();
	}
	
	/**
	 * Sets up click listeners to delete or edit a TODO item in a list.
	 */
	private void setupListViewListener() {		
		// Set up a click listener to view the details of a tweet in a separate activity. 
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				launchTweetDetailActivity(position);			
			}
			
			private void launchTweetDetailActivity(int position) {
				if (position < 0) { // possible?
					return;
				}
				// Set up an intent for EditItemActivity with the parameter values
				// of the position and the value of the item at the selected position
				Tweet tweet = aTweets.getItem(position);
				Intent i = new Intent(TimelineActivity.this, TweetDetailActivity.class);
				i.putExtra("tweet", tweet);
				startActivity(i);			
			}		
		});
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
			// Pass image data in the intent
			i.putExtra("user_handle", "@user");
			// Launch the new activity
			startActivityForResult(i, REQUEST_CODE);					
			return true;
		}
		
		if (id == R.id.action_refresh) {
			populateTimeLine.reset();
			populateTimeLine.fetchMore(FetchDirection.FORWARD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// Fetch the recently posted tweet along with other new ones
			populateTimeLine.fetchMore(FetchDirection.FORWARD);
		}
	}
	
	private void setupUserProfile() {
		
		TwitterApplication.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					userHandle = "@" + response.getString("screen_name");
					getActionBar().setTitle(userHandle);
					String profileImageUrl = response.getString("profile_image_url");
					storeUserProfile(userHandle, profileImageUrl);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable e, String error) {
				Log.d("Debug", "In setupUserProfile:onFailure");
				Log.d("Debug", error);
			}
		});
	}
	
	private void storeUserProfile(String userHandle, String profileImageUrl) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = pref.edit();
		edit.putString("userHandle", userHandle);
		edit.putString("profileImageUrl", profileImageUrl);
		edit.commit();
		// Log.d("Debug", "userHandle=" + userHandle + ", profileImageUrl=" + profileImageUrl);
	}
	
}
