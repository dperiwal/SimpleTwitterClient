package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.codepath.apps.basictwitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {

	private static String userHandle;
	private static String profileImageUrl;
	private static boolean userProfileInitialized = false;
	
	private Button btnTweet;
	private Button btnCancel;
	private EditText etTweetBody;
	private TwitterClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		setupResources();
	}	
	
	private void setupResources() {
		if (!userProfileInitialized) {
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			userHandle = pref.getString("userHandle", "@user");
			profileImageUrl = pref.getString("profileImageUrl", null);
			userProfileInitialized = true;
		}
		
		TextView tvTweetHandle = (TextView) findViewById(R.id.tvTweeterHandle);
		tvTweetHandle.setText(userHandle);
		
		ImageView ivTweeter = (ImageView) findViewById(R.id.ivTweeter);
		ImageLoader imageLoader = ImageLoader.getInstance();	
		imageLoader.displayImage(profileImageUrl, ivTweeter);		
		
		btnTweet = (Button) findViewById(R.id.btnTweet);
		btnTweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				postTweet();

			}
		});
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();

			}
		});
		etTweetBody = (EditText) findViewById(R.id.etTweetBody);
		
		client = TwitterApplication.getRestClient();
	}

	private void postTweet() {
		String tweet = etTweetBody.getText().toString();
		if (Utils.isNullOrEmpty(tweet)) {
			Toast.makeText(this, "Can't post an empty tweet", Toast.LENGTH_SHORT).show();
			return;
		}
		
		client.postTweet(tweet, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				Intent i = new Intent();
				setResult(RESULT_OK, i);
				finish();
			}

			@Override
			public void onFailure(Throwable e, String error) {
				Log.d("Debug", "in postTweet:onFailure");
				Log.d("Debug", error);
			}
		});
	}

	private void cancel() {
		// Return to the caller
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish();
	}

}
