package com.codepath.apps.basictwitter.rest;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.basictwitter.utils.PopulateTimeLine.FetchDirection;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "pJ9qokgedQoOeBdu0mtxLsWy3";       // Change this
	public static final String REST_CONSUMER_SECRET = "DYf8B8odJEXjswgZQcTM1SMt0FvcL1hx8CccNE6uEPohzkknUq"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
/*	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}*/

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
	
	/**
	 * Fetches more tweets. One of idHigherThan and idLowerThan has to be positive. 
	 * 
	 * @param idHigherThan Provides since_id value (tweets with higher ids should be fetched)
	 * @param idLowerThan Provides max_id value (tweets with lower ids should be fetched)
	 * @param handler Callback
	 */
	public void getHomeTimeline(FetchDirection direction, int count, long idHigherThan, long idLowerThan, 
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (direction == FetchDirection.FORWARD) {
			params.put("since_id", Long.valueOf(idHigherThan).toString());
		} else {
			params.put("max_id", Long.valueOf(idLowerThan - 1).toString());
		}
		params.put("count",  Integer.valueOf(count).toString());
		client.get(apiUrl, params, handler);	
	}
	
	/**
	 * Fetches more tweets which mentions the user. One of idHigherThan and idLowerThan has to be positive. 
	 * 
	 * @param idHigherThan Provides since_id value (tweets with higher ids should be fetched)
	 * @param idLowerThan Provides max_id value (tweets with lower ids should be fetched)
	 * @param handler Callback
	 */
	public void getMentionsTimeline(FetchDirection direction, int count, long idHigherThan, long idLowerThan, 
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		if (direction == FetchDirection.FORWARD) {
			params.put("since_id", Long.valueOf(idHigherThan).toString());
		} else {
			params.put("max_id", Long.valueOf(idLowerThan - 1).toString());
		}
		params.put("count",  Integer.valueOf(count).toString());
		client.get(apiUrl, params, handler);	
	}
		
   public void postTweet(String tweet, long inReplyToStatusId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		if (inReplyToStatusId != 0) {
			params.put("in_reply_to_status_id", Long.valueOf(inReplyToStatusId).toString());
		}
		client.post(apiUrl, params, handler);	
	}
   
   public void getUserInfo(AsyncHttpResponseHandler handler) {
	   String apiUrl = getApiUrl("account/verify_credentials.json");
	   RequestParams params = new RequestParams();
	   params.put("skip_status", "1");
	   client.get(apiUrl, params, handler);
   }
}