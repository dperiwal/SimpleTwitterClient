package com.codepath.apps.basictwitter.fragments;

import com.codepath.apps.basictwitter.utils.PopulateMentionsTimeLine;
import com.codepath.apps.basictwitter.utils.PopulateTimeLine;

public class MentionsTimeLineFragment extends TweetsListFragment {

	public MentionsTimeLineFragment() {
		super();
	}

	@Override
	protected PopulateTimeLine getTimelinePopulator() {
		return new PopulateMentionsTimeLine(getUser(), getActivity(), tweets, aTweets);
	}

}
