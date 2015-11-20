package com.toretate.aigistwclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.*;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	private static final String TWITTER_KEY = "66gsxBA0dccvtroeHnmUJNfhQ";
	private static final String TWITTER_SECRET = "8tbn8YFHabqNJuQ0QTBetKgmfaJJfGuljIuubq3slRLCej1YYh";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig));

		setContentView(R.layout.activity_main);

		// TODO: Use a more specific parent
		final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();

		UserTimeline userTimeline = new UserTimeline.Builder()
				.includeReplies(false)
				.includeRetweets(false)
				.maxItemsPerRequest(5)
				.screenName("Aigis1000")
				.build();
		final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
				.setTimeline(userTimeline)
				.build();

		ListView twitter = (ListView)this.findViewById( R.id.twitter );
		twitter.setAdapter( adapter );

		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)this.findViewById( R.id.swipeRefresh );
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

//		// TODO: Base this Tweet ID on some data from elsewhere in your app
//		long tweetId = 631879971628183552L;
//		TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
//			@Override
//			public void success(Result<Tweet> result) {
//				TweetView tweetView = new TweetView(MainActivity.this, result.data);
//				parentView.addView(tweetView);
//			}
//
//			@Override
//			public void failure(TwitterException exception) {
//				Log.d("TwitterKit", "Load Tweet failure", exception);
//			}
//		});


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
