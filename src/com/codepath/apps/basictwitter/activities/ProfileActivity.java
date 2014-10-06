package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.UserTimeLineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private User thisUser;
	
	private ImageView ivUserImage;
	private TextView tvUserName;
	private TextView tvTagline;
	private TextView tvNumFollowers;
	private TextView tvNumFollowing;
	private static ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);	
		setupResources();
	}
	
	private void setupResources() {	
		// Get the current user information passed in the intent.
		thisUser = (User) getIntent().getSerializableExtra(User.USER_KEY);
		getActionBar().setTitle("@" + (thisUser.getScreenName()));
		
		ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
		String profileImageUrl = thisUser.getProfileImageUrl();
		if (profileImageUrl != null) {
			imageLoader.displayImage(profileImageUrl, ivUserImage);
		}
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserName.setText(thisUser.getName());
		
		tvTagline = (TextView) findViewById(R.id.tvTagline);
		tvTagline.setText(thisUser.getDescription());
		
		tvNumFollowers = (TextView) findViewById(R.id.tvNumFollowers);
		tvNumFollowers.setText(Integer.valueOf(thisUser.getNumFollowers()).toString());
		
		tvNumFollowing = (TextView) findViewById(R.id.tvNumFollowing);	
		tvNumFollowing.setText(Integer.valueOf(thisUser.getNumFollowing()).toString());
		
		Fragment userTimeLineFragment = new UserTimeLineFragment();
		Bundle args = new Bundle();
		args.putSerializable(User.USER_KEY, thisUser);
		userTimeLineFragment.setArguments(args);
		
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flUserTimeLine, userTimeLineFragment);
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
