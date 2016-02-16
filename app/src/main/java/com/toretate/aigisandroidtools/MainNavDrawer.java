package com.toretate.aigisandroidtools;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

import io.fabric.sdk.android.Fabric;
import layout.ViewPagerContentFragment;

/**
 * ルートとなるNavigationDrawer
 */
public class MainNavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, ViewPagerContentFragment.OnFragmentInteractionListener {

	private TwitterSettings m_tw;
	private MoPubSettings m_moPub;
	private ViewPager m_pager;
	private static Toolbar m_toolbar;
	public static void setTitle( String title ) { if( m_toolbar != null ) m_toolbar.setTitle( title ); }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Twitter関係
		m_tw = new TwitterSettings( this );

		setContentView(R.layout.activity_main_nav_drawer);
		m_toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar( m_toolbar );

		// MoPUB
		m_moPub = new MoPubSettings( this );

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
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, m_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		// NavigationDrawer の選択リスナを張っておく
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// ViewPagerの設定
		{
			m_pager = (ViewPager)findViewById(R.id.pager);
			PagerAdapter pagerAdapter = new PagerAdapter( getSupportFragmentManager() );

			m_pager.setAdapter( pagerAdapter );
			m_pager.addOnPageChangeListener(this);
		}

		// 初期設定
		m_pager.setCurrentItem(0);
	}

	@Override
	protected void onDestroy() {
		m_moPub.destroy();
		super.onDestroy();
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

		if(id == R.id.tw_aigis1000) {
			m_pager.setCurrentItem(0);
		} else if(id == R.id.tw_aigis1000A) {
			m_pager.setCurrentItem(1);
		} else if(id == R.id.tw_aigis_hash) {
			m_pager.setCurrentItem(2);
		} else if(id == R.id.tw_toretatenee) {
			m_pager.setCurrentItem(3);
		} else if(id == R.id.nav_manage) {
			m_pager.setCurrentItem(4);
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	// #ViewPager.OnPageChangeListener
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	// ViewPagerContentFragment.OnFragmentInteractionListener,

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
