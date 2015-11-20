package com.toretate.aigistwclient;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import layout.TabFragment;


public class MainActivity extends AppCompatActivity implements TabFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TwitterDefInterface twitterDef = new TwitterDefImpl();
		TwitterAuthConfig authConfig = twitterDef.createTwitterAuthConfig();
		Fabric.with(this, new Twitter(authConfig));

		setContentView(R.layout.activity_main);

		TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
		ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

		FragmentPagerAdapter adapter = new FragmentPagerAdapter( getSupportFragmentManager() ) {
			@Override
			public Fragment getItem(int position) {
				return TabFragment.newInstance( position +1 );
			}

			@Override
			public CharSequence getPageTitle(int position) {
				if( position == 0 ) {
					return "政務官アンナ";
				} else {
					return "#千年戦争アイギス";
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};

		viewPager.setAdapter( adapter );
		viewPager.addOnPageChangeListener(this);
		tabLayout.setupWithViewPager( viewPager );

		// TODO: Use a more specific parent
		final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();

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

//		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});
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

	@Override
	public void onFragmentInteraction(Uri uri) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
