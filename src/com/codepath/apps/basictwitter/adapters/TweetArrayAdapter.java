package com.codepath.apps.basictwitter.adapters;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> objects) {
		super(context, R.layout.tweet_item, objects);
	}
	
	// View lookup cache
	private static class ViewHolder {
		ImageView ivProfileImage;
		TextView tvUserName;
		TextView tvUserHandle;
		TextView tvCreationTime;
		TextView tvBody;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Tweet tweet = getItem(position);
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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			// reset the image from the recycled view
			viewHolder.ivProfileImage.setImageResource(0);
			viewHolder.tvUserName.setText("");
			viewHolder.tvUserHandle.setText("");
			viewHolder.tvCreationTime.setText("");
			viewHolder.tvBody.setText("");
		}
		
		if (Utils.isNetworkAvailable(getContext())) {
		    ImageLoader imageLoader = ImageLoader.getInstance();
		    imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);
		}
		viewHolder.tvUserName.setText(tweet.getUser().getName());
		viewHolder.tvUserHandle.setText("@" + tweet.getUser().getScreenName());
		viewHolder.tvCreationTime.setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));
		viewHolder.tvBody.setText(tweet.getBody());
		
		return convertView;
	}
	
	

}
