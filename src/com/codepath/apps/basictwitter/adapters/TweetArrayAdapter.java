package com.codepath.apps.basictwitter.adapters;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.activities.ProfileActivity;
import com.codepath.apps.basictwitter.fragments.TweetReplyFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	private boolean profileActivityListener = true; // by default
	
	public TweetArrayAdapter(Context context, List<Tweet> objects) {
		super(context, R.layout.tweet_item, objects);
	}
	
	public TweetArrayAdapter(Context context,
			ArrayList<Tweet> tweets, boolean profileActivityListener) {
		super(context, R.layout.tweet_item, tweets);
		this.profileActivityListener = profileActivityListener;
	}

	// View lookup cache
	private static class ViewHolder {
		ImageView ivProfileImage;
		TextView tvUserName;
		TextView tvUserHandle;
		TextView tvCreationTime;
		TextView tvBody;
		Button btnReplyTweet;
		Button btnRetweet;
		Button btnFavorite;
		TextView tvRetweetCount;
		TextView tvFavoriteCount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		final Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_item, parent, false);
			viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
			viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
			viewHolder.tvUserHandle = (TextView) convertView.findViewById(R.id.tvUserHandle);
			viewHolder.tvCreationTime = (TextView) convertView.findViewById(R.id.tvCreationTime);
			viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
			viewHolder.btnReplyTweet = (Button) convertView.findViewById(R.id.btnReplyTweet);
			viewHolder.btnRetweet = (Button) convertView.findViewById(R.id.btnRetweet);
			viewHolder.tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
			viewHolder.btnFavorite = (Button) convertView.findViewById(R.id.btnFavorite);
			viewHolder.tvFavoriteCount = (TextView) convertView.findViewById(R.id.tvFavoriteCount);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			// reset the image from the recycled view
			viewHolder.ivProfileImage.setImageResource(0);
			viewHolder.tvUserName.setText("");
			viewHolder.tvUserHandle.setText("");
			viewHolder.tvCreationTime.setText("");
			viewHolder.tvBody.setText("");
			viewHolder.tvRetweetCount.setText("");
			viewHolder.tvFavoriteCount.setText("");
			
		}
			
		if (profileActivityListener) {
			viewHolder.ivProfileImage.setTag(tweet.getUser());
			viewHolder.ivProfileImage.setOnClickListener(new OnClickListener() {
				// Start the activity to show the clicked users profile.
				// TODO: Avoid recursive behavior: Don't go to ProfileActivity
				// from within ProfileActivity.
				@Override
				public void onClick(View v) {
					User user = (User) v.getTag();
					// Create an intent for profile activity
					Intent i = new Intent(getContext(), ProfileActivity.class);
					// Pass the user data
					i.putExtra(User.USER_KEY, user);
					getContext().startActivity(i);
				}
			});
		}
		
		viewHolder.btnReplyTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
				TweetReplyFragment tweetReplyFragment = new TweetReplyFragment(tweet);
				ft.replace(R.id.flTweetReply, tweetReplyFragment);
				ft.addToBackStack(null);
				ft.commit();		
			}
		});
		
		viewHolder.btnRetweet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.retweet(getContext(), tweet);
			};
		});
		
		viewHolder.btnFavorite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			};
		});
		
		if (Utils.isNetworkAvailable(getContext())) {
		    ImageLoader imageLoader = ImageLoader.getInstance();
		    imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);
		}
		viewHolder.tvUserName.setText(tweet.getUser().getName());
		viewHolder.tvUserHandle.setText("@" + tweet.getUser().getScreenName());
		viewHolder.tvCreationTime.setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));
		viewHolder.tvBody.setText(tweet.getBody());
		viewHolder.tvRetweetCount.setText(Integer.valueOf(tweet.getRetweetCount()).toString());
		viewHolder.tvFavoriteCount.setText(Integer.valueOf(tweet.getFavoriteCount()).toString());
		
		return convertView;
	}

}
