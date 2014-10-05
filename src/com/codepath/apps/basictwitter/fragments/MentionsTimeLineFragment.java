package com.codepath.apps.basictwitter.fragments;

import com.codepath.apps.basictwitter.utils.PopulateMentionsTimeLine;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine;

public class MentionsTimeLineFragment extends TweetsListFragment {

	public MentionsTimeLineFragment() {
	}

	@Override
	protected PopulateTimeLine getTimelinePopulator() {
		return new PopulateMentionsTimeLine(getActivity(), tweets, aTweets);
	}

}
