package com.toretate.aigisandroidtools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.toretate.aigisandroidtools.capture.ScreenshotService;
import com.toretate.aigisandroidtools.pager.PagerAdapter;
import com.toretate.aigisandroidtools.pager.ViewPagerContentFragment;
import com.toretate.aigisandroidtools.pager.ViewPagerPageDefs;
import com.toretate.aigisandroidtools.twitter.TwitterSettings;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * ルートとなるNavigationDrawer
 */
public class MainNavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, ViewPagerContentFragment.OnFragmentInteractionListener {

	public interface ScreenshotPermissionListener { void screenshotPermissionCompleted(); }
	private static ScreenshotPermissionListener s_screenshotPermisionListener;
	public static void setScreenshotPermissionListener( ScreenshotPermissionListener l ) { s_screenshotPermisionListener = l; }

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "66gsxBA0dccvtroeHnmUJNfhQ";
    private static final String TWITTER_SECRET = "8tbn8YFHabqNJuQ0QTBetKgmfaJJfGuljIuubq3slRLCej1YYh";

	private static final String TAG = MainNavDrawer.class.getCanonicalName();

	private  boolean m_enabled = true;

	private MoPubSettings m_moPub;
	private ViewPager m_pager;

	public void setTitle( String title ) {
		Toolbar toolbar = (Toolbar)this.findViewById( R.id.toolbar );
		if( toolbar != null ) toolbar.setTitle( title );
	}

	private static final int CALL_SETTINGS_ACTIVITY = 100;
	private static final int REQUEST_OVERLAY_PERMISSION = 101;

	public static boolean hasOverlayPermissison( Context context ) {
		if( Build.VERSION.SDK_INT >= 23 ) return Settings.canDrawOverlays( context );
		return true;
	}

	public static void requestOverlayPermission( final Context context ) {
		requestOverlayPermisison( REQUEST_OVERLAY_PERMISSION, context );
	}
	private static void requestOverlayPermisison( int requesstCode, final Context context ) {
		if( context == null || ( context instanceof Activity ) == false ) return;
		Intent intent = new Intent( Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:com.toretate.aigisandroidtools") );
		((Activity) context).startActivityForResult( intent, requesstCode );
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig));

		// Twitter関係
		TwitterSettings.init( this );

		setContentView(R.layout.activity_main_nav_drawer);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar( toolbar );

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
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
		drawer.addDrawerListener(toggle);
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
	protected void onStart() {
		super.onStart();
//		if( hasOverlayPermissison( this ) ) {
//			Intent intent = new Intent( this, ScreenshotService.class ).setAction( ScreenshotService.Companion.getACTION_START() );
//			startService( intent );
//		} else {
//			requestOverlayPermisison( REQUEST_OVERLAY_PERMISSION, this );
//		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		m_moPub.destroy();
		
		if( m_enabled && hasOverlayPermissison( this ) ) {
			Intent intent = new Intent( this, ScreenshotService.class );
			intent.setAction( ScreenshotService.Companion.getACTION_STOP() );
			startService( intent );
		}
		
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
			ViewPagerPageDefs.getInstance( this ).updateVisibility( this );
			m_pager.getAdapter().notifyDataSetChanged();
			break;
		case REQUEST_OVERLAY_PERMISSION:
			Log.d( TAG, "enable overlay permision" );
			break;
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		Integer itemIndex = ViewPagerPageDefs.getInstance( this ).findItemIndex( item.getItemId() );
		if( itemIndex == null ) {
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
		Toolbar toolbar = (Toolbar)this.findViewById( R.id.toolbar );
		if( toolbar != null ) toolbar.setTitle( ViewPagerPageDefs.getInstance( this ).getTitle( position ) );
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		m_enabled = false;
		return super.onTouchEvent(event);
	}

	// ViewPagerContentFragment.OnFragmentInteractionListener,

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
