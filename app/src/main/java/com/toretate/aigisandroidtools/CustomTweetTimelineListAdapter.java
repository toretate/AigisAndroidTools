package com.toretate.aigisandroidtools;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * Twitterタイムライン用のアダプタ
 * Created by toretatenee on 2015/11/26.
 */
public class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

	TweetView m_tweetView = null;

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

		// そのままだと、タップ時にTwitterクライアントが立ち上がるのでカスタム
		// タップ時には Tweet内容を Overlay 表示する
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

						if( m_tweetView != null && m_tweetView.getParent() != null ) {
							view.removeView( m_tweetView );
						}
						m_tweetView = new TweetView( context, result.data );
						m_tweetView.setFocusable( true );
						m_tweetView.setFocusableInTouchMode( true );
						m_tweetView.setOnKeyListener(new View.OnKeyListener() {
							@Override
							public boolean onKey(View v, int keyCode, KeyEvent event) {
								if( event.getAction() != KeyEvent.ACTION_DOWN ) return false;

								if( keyCode == KeyEvent.KEYCODE_BACK ) {
									view.removeView( m_tweetView );
									m_tweetView.setOnKeyListener( null );
									m_tweetView = null;

									view.setVisibility( View.GONE );
									return true;
								}
								return false;
							}
						});

						view.addView( m_tweetView );
						m_tweetView.requestFocus();
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
