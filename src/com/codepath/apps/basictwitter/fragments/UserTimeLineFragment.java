package com.codepath.apps.basictwitter.fragments;

import com.codepath.apps.basictwitter.utils.PopulateTimeLine;
import com.codepath.apps.basictwitter.utils.PopulateUserTimeLine;

public class UserTimeLineFragment extends TweetsListFragment {
	
	@Override
	protected PopulateTimeLine getTimelinePopulator() {
		return new PopulateUserTimeLine(getUser(), getActivity(), tweets, aTweets);
	}
}
