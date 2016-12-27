package com.toretate.aigisandroidtools;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CheckBox;

import java.io.BufferedReader;

/**
 * 設定画面
 * Created by toretatenee on 16/05/30.
 */
public class SettingsActivity extends AppCompatActivity {

	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setPreferenceScreen( createList() );
//
//			addPreferencesFromResource( R.xml.settings );
		}

		private Preference createCheckboxPref(
				@NonNull final String preferenceKey
				, @NonNull final String title
				, @Nullable  final String summary
		) {
			CheckBoxPreference pref = new CheckBoxPreference( getActivity() );
			pref.setKey( preferenceKey );
			pref.setTitle( title );
			if( summary != null ) pref.setSummary( summary );
			return pref;
		}


		private PreferenceScreen createList() {
			final Context c = getActivity();
			PreferenceScreen root = getPreferenceManager().createPreferenceScreen(c);

			PreferenceCategory category = new PreferenceCategory(c);
			root.addPreference( category );

			CheckBoxPreference pref;

			// twitter(@aigis1000)
			category.addPreference( createCheckboxPref("tw_aigis1000", "@aigis1000", "twitter") );

			// twitter(@aigis1000A)
			category.addPreference( createCheckboxPref("tw_aigis1000A", "@aigis1000A", "twitter") );

			// twitter(#千年戦争アイギス)
			category.addPreference( createCheckboxPref("tw_aigis_hash", "#千年戦争アイギス", "twitter") );

			// タイマー
			category.addPreference( createCheckboxPref("tool_timer", "カリ/スタ管理", null) );

			// 合成表
			category.addPreference( createCheckboxPref("tool_compose", "合成表", null) );

			// ミッション表
			category.addPreference( createCheckboxPref("tool_mission", "ミッション", null) );

			// twitter(@tw_toretatenee)
			category.addPreference( createCheckboxPref("tw_toretatenee", "お知らせ", "twitter") );

			return root;
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
