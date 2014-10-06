package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.codepath.apps.basictwitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {

	public static int MAX_TWEET_SIZE = 140;
	
	private Button btnTweet;
	private Button btnCancel;
	private EditText etTweetBody;
	private TextView tvRemainingCharsCount;
	private TwitterClient client;
	
	private User thisUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		setupResources();
	}	
	
	private void setupResources() {	
		// Get the current user information passed in the intent.
		thisUser = (User) getIntent().getSerializableExtra(User.USER_KEY);
		
		TextView tvTweetHandle = (TextView) findViewById(R.id.tvTweeterHandle);
		tvTweetHandle.setText(thisUser.getScreenName());
		
		ImageView ivTweeter = (ImageView) findViewById(R.id.ivTweeter);
		ImageLoader imageLoader = ImageLoader.getInstance();	
		imageLoader.displayImage(thisUser.getProfileImageUrl(), ivTweeter);		
		
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
			
		tvRemainingCharsCount = (TextView) findViewById(R.id.tvRemainingCharsCount); 
		
		etTweetBody = (EditText) findViewById(R.id.etTweetBody);
		etTweetBody.setMovementMethod(new ScrollingMovementMethod());
		etTweetBody.addTextChangedListener(new TextWatcher() {		
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int currCharCount = s.length();
				int remainingChars = MAX_TWEET_SIZE - currCharCount;
				tvRemainingCharsCount.setText(Integer.valueOf(remainingChars).toString());		
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub			
			}
		});
		
		client = TwitterApplication.getRestClient();
	}

	private void postTweet() {
		String tweet = etTweetBody.getText().toString();
		if (Utils.isNullOrEmpty(tweet)) {
			Toast.makeText(this, "Can't post an empty tweet", Toast.LENGTH_SHORT).show();
			return;
		}
		
		client.postTweet(tweet, 0, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {		
				Tweet newTweet = Tweet.fromJSON(response);	
				Intent i = new Intent();
				i.putExtra(Tweet.TWEET_KEY, newTweet);
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
