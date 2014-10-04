package com.codepath.apps.basictwitter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

public class Utils {
	
	static public final String NETWORK_UNAVAILABLE_MSG = "Network not available...";
	
	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
	public static Boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public static String getRelativeTimeAgoOld(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL)
					.toString();
			
/*			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();*/
			
			
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return relativeDate;
	}
	
	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public static String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		long dateMillis;
		String relativeDate = "";
		try {
			dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = getElapsedDisplayTime(dateMillis);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return relativeDate;
	}
	
	public static String getElapsedDisplayTime(long createdTime) {
        String display = "";

        Date date = new Date();
        date.setTime(createdTime);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date nowDate = calendar1.getTime();

        long diff = nowDate.getTime() - date.getTime();

        long dayDiff = TimeUnit.MILLISECONDS.toDays(diff);

        if(dayDiff > 0){
            display = String.format("%dd", dayDiff);
        }
        else {
            long hrDiff = TimeUnit.MILLISECONDS.toHours(diff);
            long minDiff = TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff));
            if (hrDiff > 0) {
                display = String.format("%dh", hrDiff);
            } else {
                if (minDiff < 0) {
                    minDiff = 0;
                }

                display = String.format("%dm", minDiff);
            }
        }

        return display;
    }
}
