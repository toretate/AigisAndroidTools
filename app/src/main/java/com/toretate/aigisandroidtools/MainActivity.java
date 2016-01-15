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
import layout.ViewPagerContentFragment;


public class MainActivity extends AppCompatActivity implements ViewPagerContentFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {
	private MoPubSettings m_moPub;
	private TwitterSettings m_tw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_tw = new TwitterSettings( this );

		setContentView(R.layout.activity_main);

		m_moPub = new MoPubSettings( this );

		{	// Tab + ViewPager
			TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
			ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

			PagerAdapter adapter = new PagerAdapter( getSupportFragmentManager() );
			viewPager.setAdapter( adapter );
			viewPager.addOnPageChangeListener(this);
			tabLayout.setupWithViewPager( viewPager );
		}

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
		m_moPub.destroy();
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
}
