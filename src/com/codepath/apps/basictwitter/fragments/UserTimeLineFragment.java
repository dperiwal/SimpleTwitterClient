package com.codepath.apps.basictwitter.fragments;

import com.codepath.apps.basictwitter.utils.PopulateTimeLine;
import com.codepath.apps.basictwitter.utils.PopulateUserTimeLine;

public class UserTimeLineFragment extends TweetsListFragment {
	
	public UserTimeLineFragment() {
		super();
		setProfileActivityListener(false);
	}

	@Override
	protected PopulateTimeLine getTimelinePopulator() {
		return new PopulateUserTimeLine(getUser(), getActivity(), tweets, aTweets);
	}
	
}
