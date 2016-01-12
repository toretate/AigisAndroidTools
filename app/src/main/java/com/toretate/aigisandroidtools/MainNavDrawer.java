package com.toretate.aigisandroidtools;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;
import layout.TabFragment;

/**
 * ルートとなるNavigationDrawer
 */
public class MainNavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MoPubView.BannerAdListener {
	private MoPubView moPubView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fabric関係
		TwitterDefInterface twitterDef = new TwitterDefImpl();
		TwitterAuthConfig authConfig = twitterDef.createTwitterAuthConfig();
		Fabric.with(this, new Twitter(authConfig));

		setContentView(R.layout.activity_main_nav_drawer);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// MoPUB
		moPubView = (MoPubView) findViewById(R.id.adview);
		// TODO: Replace this test id with your personal ad unit id
		moPubView.setAdUnitId("df8b7addafda4f788212d2a8dead7a57");
		moPubView.loadAd();
		moPubView.setBannerAdListener(this);

		/*
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
		*/

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		setupTwitterLayout( 0 );
	}

	private void setupTwitterLayout( final int position ) {
		Timeline<Tweet> timeline;
		switch( position ) {
		case 0:
			timeline = new UserTimeline.Builder()
					.includeReplies(false)
					.includeRetweets(false)
					.maxItemsPerRequest(5)
					.screenName("Aigis1000")
					.build();
			break;
		case 1:
			timeline = new UserTimeline.Builder()
					.includeReplies(false)
					.includeRetweets(false)
					.maxItemsPerRequest(5)
					.screenName("Aigis1000_A")
					.build();
			break;
		case 2:
			timeline = new SearchTimeline.Builder()
					.query("#千年戦争アイギス")
					.maxItemsPerRequest(5)
					.languageCode("ja")
					.build();
			break;
		case 3:
		default:
			timeline = new UserTimeline.Builder()
					.includeReplies(false)
					.includeRetweets(false)
					.maxItemsPerRequest(5)
					.screenName("toretatenee")
					.build();
			break;
		}

		final CustomTweetTimelineListAdapter adapter = new CustomTweetTimelineListAdapter.Builder( this )
				.setTimeline( timeline )
				.build();

		ListView twitter = (ListView)findViewById( R.id.twitter );
		twitter.setAdapter( adapter );

		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById( R.id.swipeRefresh );
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		moPubView.destroy();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_nav_drawer, menu);
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

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.tw_aigis1000) {
			setupTwitterLayout(0);
		} else if (id == R.id.tw_aigis1000A) {
			setupTwitterLayout(1);
		} else if (id == R.id.tw_aigis_hash) {
			setupTwitterLayout(2);
		} else if (id == R.id.nav_manage) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	// #MOPUB View

	@Override
	public void onBannerLoaded(MoPubView moPubView) {

	}

	@Override
	public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {

	}

	@Override
	public void onBannerClicked(MoPubView moPubView) {

	}

	@Override
	public void onBannerExpanded(MoPubView moPubView) {

	}

	@Override
	public void onBannerCollapsed(MoPubView moPubView) {

	}
}
