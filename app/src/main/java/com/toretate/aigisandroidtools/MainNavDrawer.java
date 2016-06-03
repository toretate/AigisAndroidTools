package com.toretate.aigisandroidtools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.toretate.aigisandroidtools.pager.PagerAdapter;
import com.toretate.aigisandroidtools.pager.ViewPagerContentFragment;
import com.toretate.aigisandroidtools.pager.ViewPagerPageDefs;
import com.toretate.aigisandroidtools.twitter.TwitterSettings;

/**
 * ルートとなるNavigationDrawer
 */
public class MainNavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, ViewPagerContentFragment.OnFragmentInteractionListener {

	private TwitterSettings m_tw;
	private MoPubSettings m_moPub;
	private ViewPager m_pager;
	private static Toolbar m_toolbar;
	public static void setTitle( String title ) { if( m_toolbar != null ) m_toolbar.setTitle( title ); }

	private static final int CALL_SETTINGS_ACTIVITY = 100;

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
			PagerAdapter pagerAdapter = new PagerAdapter( getSupportFragmentManager(), this );

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
			Intent intent = new Intent( this, SettingsActivity.class );
			startActivityForResult( intent, CALL_SETTINGS_ACTIVITY );

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch( requestCode ) {
		case CALL_SETTINGS_ACTIVITY:
			ViewPagerPageDefs.getInstance( this ).update( this );
			m_pager.getAdapter().notifyDataSetChanged();
			break;
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int itemIndex = ViewPagerPageDefs.getInstance( this ).findItemIndex( item.getItemId() );
		if( itemIndex == -1 ) {
			if( ViewPagerPageDefs.isSettings( item.getItemId() ) ) {
				// 設定アクティビティに飛ばす
				Intent intent = new Intent( this, SettingsActivity.class );
				this.startActivity( intent );
			}
		} else {
			m_pager.setCurrentItem( itemIndex );
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
	public void onPageSelected( final int position) {
		m_toolbar.setTitle( ViewPagerPageDefs.getInstance( this ).getTitle( position ) );
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	// ViewPagerContentFragment.OnFragmentInteractionListener,

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
