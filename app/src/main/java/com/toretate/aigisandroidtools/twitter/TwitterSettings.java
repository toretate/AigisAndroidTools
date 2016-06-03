package com.toretate.aigisandroidtools.twitter;

import android.app.Activity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by toretate on 2016/01/16.
 */
public class TwitterSettings {
	public TwitterSettings( final Activity activity ) {
		TwitterDefInterface twitterDef = new TwitterDefImpl();
		TwitterAuthConfig authConfig = twitterDef.createTwitterAuthConfig();
		Fabric.with( activity, new Twitter(authConfig));
	}
}
