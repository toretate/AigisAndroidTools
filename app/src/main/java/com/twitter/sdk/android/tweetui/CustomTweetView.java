package com.twitter.sdk.android.tweetui;

import android.content.Context;
import android.util.AttributeSet;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetLinkClickListener;
import com.twitter.sdk.android.tweetui.TweetMediaClickListener;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * 添付された画像をすべて表示する TweetView
 * Created by toretate on 2016/01/19.
 */
public class CustomTweetView extends TweetView {

	private boolean m_useLaunchPermalink;	//!< true:TweetView タップで元の Tweet に飛ばす（ブラウザで）false:反応無し

	public CustomTweetView(Context context, Tweet tweet) {
		super(context, tweet);
		initialize();
	}

	public CustomTweetView(Context context, Tweet tweet, int styleResId) {
		super(context, tweet, styleResId);
		initialize();
	}

	public CustomTweetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public CustomTweetView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		this.setOnActionCallback( null );		// favorite, unfavorite 処理はしない
		this.setTweetActionsEnabled( false );	// tweet action ってなんぞ

		// tweet上のURLタップリスナ。setすると元々の処理ができなくなるのでコメントアウト
//		this.setTweetLinkClickListener(new TweetLinkClickListener() {
//			@Override
//			public void onLinkClick(Tweet tweet, String url) {
//				// tweet上のURLタップ
//			}
//		});

		// tweet上の画像？をタップした時の処理
//		this.setTweetMediaClickListener(new TweetMediaClickListener() {
//			@Override
//			public void onMediaEntityClick(Tweet tweet, MediaEntity entity) {
//				// 画像とかタップ？
//				System.out.println( "media url:" +entity.mediaUrl );
//			}
//		});

		// Viewタップして元画面に飛ばす処理をキャンセル
		m_useLaunchPermalink = false;
	}


	@Override
	protected int getLayout() {
		return super.getLayout();
	}

	@Override
	void launchPermalink() {
		if( m_useLaunchPermalink ) {
			super.launchPermalink();
		}
	}

}
