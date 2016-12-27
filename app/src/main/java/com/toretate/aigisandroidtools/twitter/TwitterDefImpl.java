package com.toretate.aigisandroidtools.twitter;

import com.twitter.sdk.android.core.TwitterAuthConfig;

/**
 * Twitter用の固定値
 * Created by toretate on 2015/11/21.
 */
public class TwitterDefImpl implements TwitterDefInterface {
	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	private static final String TWITTER_KEY = "66gsxBA0dccvtroeHnmUJNfhQ";
	private static final String TWITTER_SECRET = "8tbn8YFHabqNJuQ0QTBetKgmfaJJfGuljIuubq3slRLCej1YYh";

	public TwitterAuthConfig createTwitterAuthConfig() {
		return new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
	}

}
