package com.toretate.aigisandroidtools;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

/**
 * 設定画面
 * Created by toretatenee on 16/05/30.
 */
public class SettingsActivity extends AppCompatActivity {

	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource( R.xml.settings );
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );

		// 戻るボタン
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled( true );

		// SettingsFragmentを表示
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace( android.R.id.content, new SettingsFragment() );
		fragmentTransaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected( item );
	}
}
