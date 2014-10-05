package com.codepath.apps.basictwitter.fragments;

import com.codepath.apps.basictwitter.utils.PopulateHomeTimeLine;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine;

public class HomeTimeLineFragment extends TweetsListFragment {
	
	public HomeTimeLineFragment() {
		super();
	}

	@Override
	protected PopulateTimeLine getTimelinePopulator() {
		return new PopulateHomeTimeLine(getActivity(), tweets, aTweets);
	}
}
