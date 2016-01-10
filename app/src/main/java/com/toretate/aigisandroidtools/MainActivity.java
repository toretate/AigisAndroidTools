package com.toretate.aigisandroidtools;

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

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import layout.TabFragment;


public class MainActivity extends AppCompatActivity implements TabFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener, MoPubView.BannerAdListener {
	private MoPubView moPubView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TwitterDefInterface twitterDef = new TwitterDefImpl();
		TwitterAuthConfig authConfig = twitterDef.createTwitterAuthConfig();
		Fabric.with(this, new Twitter(authConfig));

		setContentView(R.layout.activity_main);

		moPubView = (MoPubView) findViewById(R.id.adview);
		// TODO: Replace this test id with your personal ad unit id
		moPubView.setAdUnitId("df8b7addafda4f788212d2a8dead7a57");
		moPubView.loadAd();
		moPubView.setBannerAdListener(this);

		TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
		ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

		FragmentPagerAdapter adapter = new FragmentPagerAdapter( getSupportFragmentManager() ) {
			@Override
			public Fragment getItem(int position) {
				return TabFragment.newInstance( position +1 );
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch( position ) {
				case 0:
					return "運営";
				default:
				case 1:
					return "#千年戦争アイギス";
				case 2:
					return "運営A";
				case 3:
					return "作者";
				}
			}

			@Override
			public int getCount() {
				return 3;
			}
		};

		viewPager.setAdapter( adapter );
		viewPager.addOnPageChangeListener(this);
		tabLayout.setupWithViewPager( viewPager );

		// TODO: Use a more specific parent
		final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();

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
	protected void onDestroy() {
		moPubView.destroy();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
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

	// #tabFlagment

	@Override
	public void onFragmentInteraction(Uri uri) {

	}

	// #ViewPager
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	// #MoPubView

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
