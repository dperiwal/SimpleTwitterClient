package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.fragments.TweetReplyFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.codepath.apps.basictwitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * This activity shows a chosen tweet in detail. It also helps in composing a
 * reply and sending it.
 * <p>
 * 
 * @author Damodar Periwal
 * 
 */
public class TweetDetailActivity extends FragmentActivity {
	private Tweet currTweet;
	private ImageView ivTweeterImage;
	private TextView tvTweeterName;
	private TextView tvTweeterHandle;
	private TextView tvTweetBody;
	private ImageView ivMedia;
	private TextView tvTimeStamp;
	private Button btnReplyTweet;
	private Button btnRetweet;
	private Button btnFavorite;
	private TextView tvRetweetCount;
	private TextView tvFavoriteCount;
	private static ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_detail);		
		currTweet = (Tweet) getIntent().getSerializableExtra(Tweet.TWEET_KEY);
		if (currTweet == null) {
			Log.i("INFO", "Tweet information is not found...Aborting");
			Toast.makeText(this, "Tweet information is not found...Aborting", Toast.LENGTH_SHORT).show();
			finish();  // Can't do much
			return;
		}
		setupResources();
		displayTweet(currTweet);
	}

	private void setupResources() {
		ivTweeterImage = (ImageView) findViewById(R.id.ivTweeterImage);
		tvTweeterName = (TextView) findViewById(R.id.tvTweeterName);
		tvTweeterHandle = (TextView) findViewById(R.id.tvTweeterHandle);
		tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
		ivMedia = (ImageView) findViewById(R.id.ivMedia);
		tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
		
		btnReplyTweet = (Button) findViewById(R.id.btnReplyTweet);
		btnReplyTweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				TweetReplyFragment tweetReplyFragment = new TweetReplyFragment(
						currTweet);
				ft.replace(R.id.flTweetReply, tweetReplyFragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		
		btnRetweet = (Button) findViewById(R.id.btnRetweet);
		btnRetweet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.retweet(TweetDetailActivity.this, currTweet);
			};
		});
        
		tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
		tvFavoriteCount = (TextView) findViewById(R.id.tvFavoriteCount);
	}

	private void displayTweet(Tweet tweet) {
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(),
				ivTweeterImage);
		tvTweeterName.setText(tweet.getUser().getName());
		tvTweeterHandle.setText("@" + tweet.getUser().getScreenName());
		tvTweetBody.setText(tweet.getBody());

		String mediaUrl = tweet.getMediaURL();
		// Log.i("INFO", "mediaURL is " + mediaUrl);
		if (mediaUrl != null) {
			ivMedia.setVisibility(View.VISIBLE);
			imageLoader.displayImage(mediaUrl, ivMedia);
		} else {
			ivMedia.setVisibility(View.GONE);
		}
		tvTimeStamp.setText(tweet.getCreatedAt());
		tvRetweetCount.setText(Integer.valueOf(tweet.getRetweetCount())
				.toString());
		tvFavoriteCount.setText(Integer.valueOf(tweet.getFavoriteCount())
				.toString());
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

}
