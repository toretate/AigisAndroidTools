package com.toretate.aigisandroidtools.twitter;

import android.content.Context;
import android.util.AttributeSet;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * 添付された画像をすべて表示する TweetView
 * Created by toretate on 2016/01/19.
 */
public class CustomTweetView extends TweetView {
	public CustomTweetView(Context context, Tweet tweet) {
		super(context, tweet);
	}

	public CustomTweetView(Context context, Tweet tweet, int styleResId) {
		super(context, tweet, styleResId);
	}

	public CustomTweetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomTweetView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getLayout() {
		return 0 ;
	}
}
