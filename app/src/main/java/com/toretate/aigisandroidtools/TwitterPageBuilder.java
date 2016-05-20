package com.toretate.aigisandroidtools;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by toretatenee on 16/05/20.
 */
public class TwitterPageBuilder {

	private UserTimeline.Builder m_userTimelineBuidler;
	private SearchTimeline.Builder m_searchTimelineBuilder;

	private String m_name;

	private TwitterPageBuilder() {
	}

	public static TwitterPageBuilder createUserTimelinePage( String name ) {
		TwitterPageBuilder builder = new TwitterPageBuilder();
		builder.m_userTimelineBuidler = new UserTimeline.Builder()
														.includeReplies(false)
														.includeRetweets(false)
														.maxItemsPerRequest(5);
		if( name != null ) builder = builder.screenName( name );
		return builder;
	}

	public static TwitterPageBuilder createSearchTimelinePage( String query ) {
		TwitterPageBuilder builder = new TwitterPageBuilder();
		builder.m_searchTimelineBuilder = new SearchTimeline.Builder()
															.maxItemsPerRequest(5)
															.languageCode("ja");
		if( query != null ) builder = builder.query( query );
		return builder;
	}

	public static View createView( final Timeline<Tweet> timeline, final Context context, final LayoutInflater inflater, final ViewGroup container ) {
		View view = inflater.inflate(R.layout.view_pager_content_fragment, container, false);
		TwitterPageBuilder.setupTwitterListView( timeline, context, view );
		return view;
	}

	TwitterPageBuilder screenName( String name ) {
		if( m_userTimelineBuidler != null ) m_userTimelineBuidler = m_userTimelineBuidler.screenName( name );
		return this;
	}

	TwitterPageBuilder query( String query ) {
		if( m_searchTimelineBuilder != null ) m_searchTimelineBuilder = m_searchTimelineBuilder.query( query );
		return this;
	}

	Timeline<Tweet> build() {
		return m_userTimelineBuidler != null ? m_userTimelineBuidler.build() : m_searchTimelineBuilder.build();
	}

	private static void setupTwitterListView(final Timeline<Tweet> timeline, final Context context, final View container ) {
		if( timeline == null ) return;

		final CustomTweetTimelineListAdapter adapter = new CustomTweetTimelineListAdapter.Builder( context )
				.setTimeline( timeline )
				.build();

		ListView twitter = (ListView)container.findViewById( R.id.twitter );
		twitter.setAdapter( adapter );

		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)container.findViewById( R.id.swipeRefresh );
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeRefreshLayout.setRefreshing( true );
				adapter.refresh(new Callback<TimelineResult<Tweet>>() {
					@Override
					public void success(Result<TimelineResult<Tweet>> result) {
						swipeRefreshLayout.setRefreshing( false );
					}

					@Override
					public void failure(TwitterException e) {
						// 失敗通知(Toastとかで？)
					}
				});
			}
		});
	}
}
