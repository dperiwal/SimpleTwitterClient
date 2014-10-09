package com.codepath.apps.basictwitter.fragments;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.activities.ComposeTweetActivity;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.codepath.apps.basictwitter.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetReplyFragment extends Fragment {
	private Tweet currTweet;
	private ViewGroup rlTweetReplyLayout;
	private EditText etTweetReply;
	private Button btnPostReply;
	private Button btnCancelReply;
	private TextView tvRemainingCharsCount;
	private TextView tvRemainingCharsLabel; 
	
	public TweetReplyFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TweetReplyFragment(Tweet currTweet) {
		this.currTweet = currTweet;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweet_reply, container, false);
		rlTweetReplyLayout = (ViewGroup) v.findViewById(R.id.rlTweetReplyLayout);
		etTweetReply = (EditText) v.findViewById(R.id.etTweetReply);
		etTweetReply.setMovementMethod(new ScrollingMovementMethod());
		etTweetReply.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				int currCharCount = s.length();
				int remainingChars = ComposeTweetActivity.MAX_TWEET_SIZE
						- currCharCount;
				tvRemainingCharsCount.setText(Integer.valueOf(remainingChars)
						.toString());
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
		
		tvRemainingCharsCount = (TextView) v.findViewById(R.id.tvRemainingCharsCount);
		tvRemainingCharsLabel = (TextView) v.findViewById(R.id.tvRemainingCharsLabel);
		
		btnPostReply = (Button) v.findViewById(R.id.btnPostReply);
		btnPostReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postReply(null);
				
			}
		});
		btnCancelReply = (Button) v.findViewById(R.id.btnCancelReply);
		
		btnCancelReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancelReply(null);
				
			}
		});
		
		String startWith = "@" + currTweet.getUser().getScreenName() + " ";	
	    etTweetReply.setText(startWith);
	    etTweetReply.setSelection(startWith.length());
	    etTweetReply.requestFocus();
		return v;	
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try  {
		    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		} catch (RuntimeException e) {		
			e.printStackTrace();
		}
	}
		
	// On Send button press
	public void postReply(View view) {
		String reply = etTweetReply.getText().toString();
		if (Utils.isNullOrEmpty(reply)) {
			Toast.makeText(getActivity(), "Can't post an empty reply",
					Toast.LENGTH_SHORT).show();
			return;
		}

		TwitterClient client = TwitterApplication.getRestClient();

		client.postTweet(reply, currTweet.getTweetId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Toast.makeText(getActivity(), "Reply sent.",
								Toast.LENGTH_SHORT).show();
						//setReplyVisibility(View.GONE);
						onDestroy();
					}

					@Override
					public void onFailure(Throwable e, String error) {
						Log.d("Debug", "In postTweet:onFailure");
						Log.d("Debug", error);
						Toast.makeText(getActivity(), "Error: " +  error,
								Toast.LENGTH_SHORT).show();
						//setReplyVisibility(View.GONE);
						onDestroy();
					}
				});
	}

	// On Cancel button press
	public void cancelReply(View view) {
		onDestroy();
	}

}
