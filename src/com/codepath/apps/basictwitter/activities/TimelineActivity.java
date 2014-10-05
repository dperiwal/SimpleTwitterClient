package com.codepath.apps.basictwitter.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.fragments.HomeTimeLineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimeLineFragment;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.persistence.AppSpecificJDXSetup;
import com.codepath.apps.basictwitter.persistence.JDXPersistenceManagerImpl;
import com.codepath.apps.basictwitter.persistence.PersistenceManager;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.softwaretree.jdxandroid.JDXSetup;

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
public class TimelineActivity extends FragmentActivity implements TimelineActivityCallbacks {
	static final int REQUEST_CODE = 50;
	
	private PersistenceManager persistenceManager = null;
	private PopulateTimeLine populateTimeLine;
	private String userHandle;
	
	JDXSetup jdxSetup = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	    
		setContentView(R.layout.activity_timeline);	
		setupPersistenceManager();	
		setupUserProfile();	
		setupTabs();
	}
	
	private void setupTabs() {
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
								HomeTimeLineFragment.class));
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
								MentionsTimeLineFragment.class));
		actionBar.addTab(tab2);
	}

	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	
	private void setupPersistenceManager() {	
		try {
			AppSpecificJDXSetup.initialize(); // must be done before calling getInstance()
			jdxSetup = AppSpecificJDXSetup.getInstance(this);
			persistenceManager = new JDXPersistenceManagerImpl(jdxSetup);
		} catch (Exception ex) {
			Toast.makeText(getBaseContext(), "Exception: " + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
			persistenceManager = null;
		}
	}
	
    /**
     * Do the necessary cleanup.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }
    
    private void cleanup() {
        AppSpecificJDXSetup.cleanup(); // Do this when the application is exiting.
        jdxSetup = null;
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
		
		return super.onOptionsItemSelected(item);
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
