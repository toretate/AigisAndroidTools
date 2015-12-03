package com.toretate.aigistwclient;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetBuilder;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.internal.TimelineDelegate;

/**
 * Created by toretatenee on 2015/11/26.
 */
public class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

	public CustomTweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
		super(context, timeline);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent ) {
		View view = super.getView( position, convertView, parent );

		// 子Viewのイベントをキャンセル
		if( view instanceof ViewGroup ) {
			disableViewAndSubViews( (ViewGroup) view );
		}

		// ルートビューに対してリスナを設定する
		view.setEnabled( true );
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Activity activity = (Activity)context;
				final RelativeLayout view = (RelativeLayout)activity.findViewById( R.id.overlayTweetUI );

				TweetUtils.loadTweet(getItemId(position), new Callback<Tweet>() {
					@Override
					public void success(Result<Tweet> result) {
						view.setVisibility( View.VISIBLE );

						CompactTweetView tweetView = new CompactTweetView( context, result.data );

						view.addView( tweetView );
					}

					@Override
					public void failure(TwitterException e) {
					}
				});
			}
		});
		return view;
	}

	private void disableViewAndSubViews( ViewGroup layout ) {
		layout.setEnabled( false );

		for( int i=0; i<layout.getChildCount(); i++ ) {
			View child = layout.getChildAt( i );
			if( child instanceof ViewGroup ) {
				disableViewAndSubViews( (ViewGroup)child );
			} else {
				child.setEnabled( false );
				child.setClickable( false );
				child.setLongClickable( false );
			}
		}
	}


	public static class Builder {
		private Context context;
		private Timeline<Tweet> timeline;
		private Callback<Tweet> actionCallback;
		private int styleResId;

		public Builder(Context context) {
			this.styleResId = com.twitter.sdk.android.tweetui.R.style.tw__TweetLightStyle;
			this.context = context;
		}

		public Builder setTimeline(Timeline<Tweet> timeline) {
			this.timeline = timeline;
			return this;
		}

		public Builder setViewStyle(int styleResId) {
			this.styleResId = styleResId;
			return this;
		}

		public Builder setOnActionCallback(Callback<Tweet> actionCallback) {
			this.actionCallback = actionCallback;
			return this;
		}

		public CustomTweetTimelineListAdapter build() {
			return new CustomTweetTimelineListAdapter(this.context, this.timeline);
		}
	}
}
