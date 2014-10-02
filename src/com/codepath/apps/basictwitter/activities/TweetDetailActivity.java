package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.codepath.apps.basictwitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * This activity shows a chosen tweet in detail.
 * It also helps in composing a reply and sending it.
 * <p>
 * @author Damodar Periwal
 *
 */
public class TweetDetailActivity extends Activity {
	private Tweet currTweet;
	private ImageView ivTweeterImage;
	private TextView tvTweeterName;
	private TextView tvTweeterHandle;
	private TextView tvTweetBody;
	private ImageView ivMedia ;
	private TextView tvTimeStamp;
	private TextView tvRetweetCount;
	private TextView tvFavoriteCount;
	private EditText etTweetReply;
	private Button btnPostReply;
	private Button btnCancelReply;
	private static ImageLoader imageLoader = ImageLoader.getInstance();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_detail);
		setupResources();		
		currTweet = (Tweet) getIntent().getSerializableExtra("tweet");
		displayTweet(currTweet);
	}

	private void setupResources() {
		ivTweeterImage = (ImageView) findViewById(R.id.ivTweeterImage);
		tvTweeterName = (TextView) findViewById(R.id.tvTweeterName);
		tvTweeterHandle = (TextView) findViewById(R.id.tvTweeterHandle);
		tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
		ivMedia = (ImageView) findViewById(R.id.ivMedia);
		tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
		tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
		tvFavoriteCount = (TextView) findViewById(R.id.tvFavoriteCount);
		etTweetReply = (EditText) findViewById(R.id.etTweetReply);
		btnPostReply = (Button) findViewById(R.id.btnPostReply);
		btnCancelReply = (Button) findViewById(R.id.btnCancelReply);
	}
	
	private void displayTweet(Tweet tweet) {
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivTweeterImage);
		tvTweeterName.setText(tweet.getUser().getName());
		tvTweeterHandle.setText("@" + tweet.getUser().getScreenName());
		tvTweetBody.setText(tweet.getBody());
		
		String mediaUrl = tweet.getMediaURL();
		Log.i("INFO", "mediaURL is " + mediaUrl);
		if (mediaUrl != null) {
			ivMedia.setVisibility(View.VISIBLE);
		    imageLoader.displayImage(mediaUrl, ivMedia);
		} else {
			ivMedia.setVisibility(View.GONE);
		}
		tvTimeStamp.setText(tweet.getCreatedAt());
		tvRetweetCount.setText(Integer.valueOf(tweet.getRetweetCount()).toString());
		tvFavoriteCount.setText(Integer.valueOf(tweet.getFavoriteCount()).toString());
		
		setReplyVisibility(View.GONE);
	}
	
	private void setReplyVisibility(int visibility) {
		etTweetReply.setText("");
		etTweetReply.setVisibility(visibility);
		btnPostReply.setVisibility(visibility);
		btnCancelReply.setVisibility(visibility);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_detail, menu);
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
	
	// On Reply button press
	public void replyTweet(View view) {
		setReplyVisibility(View.VISIBLE);
	}
	
	// On Send button press
	public void postReply(View view) {
		String reply = etTweetReply.getText().toString();
		if (Utils.isNullOrEmpty(reply)) {
			Toast.makeText(this, "Can't post an empty reply", Toast.LENGTH_SHORT).show();
			return;
		}
		
		TwitterClient client = TwitterApplication.getRestClient();
		
		client.postTweet(reply, currTweet.getTweetId(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				Toast.makeText(getBaseContext(), "Reply sent.", Toast.LENGTH_SHORT).show();
				setReplyVisibility(View.GONE);	
			}

			@Override
			public void onFailure(Throwable e, String error) {
				Log.d("Debug", "in postTweet:onFailure");
				Log.d("Debug", error);
				setReplyVisibility(View.GONE);	
			}
		});
	}
	
	// On Cancel button press
	public void cancelReply(View view) {
		setReplyVisibility(View.GONE);		
	}
	
}
