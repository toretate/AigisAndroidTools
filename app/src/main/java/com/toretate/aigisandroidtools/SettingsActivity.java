package com.toretate.aigisandroidtools;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

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

		private Preference createCheckboxPref( final @NonNull XmlPullParser parser) {
			String key = parser.getAttributeValue( null, "key" );
			String title = parser.getAttributeValue( null, "title" );
			String summary = parser.getAttributeValue( null, "summary" );
			return createCheckboxPref( key, title, summary );
		}

		private PreferenceScreen createList() {
			final Context c = getActivity();
			PreferenceScreen root = getPreferenceManager().createPreferenceScreen(c);

			PreferenceCategory category = new PreferenceCategory(c);
			root.addPreference( category );

			// /pager-defs/* から @key, @title, @summary を取得してチェックボックス項目を作成
			XmlResourceParser parser = c.getResources().getXml( R.xml.settings );
			try {
				int event;
				boolean isPagerDefsChildren = false;
				while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
					switch( event ) {
						case XmlResourceParser.START_DOCUMENT:
							break;
						case XmlResourceParser.START_TAG: {
							String tagName = parser.getName();
							switch( tagName ) {
								case "pager-defs":
									isPagerDefsChildren = true;
									break;
								default:
									if( isPagerDefsChildren ) category.addPreference( createCheckboxPref( parser ) );
									break;
							}
							break;
						}
						case XmlResourceParser.END_TAG: {
							String tagName = parser.getName();
							switch( tagName ) {
								case "pager-defs":
									isPagerDefsChildren = false;
									break;
								default:
									break;
							}
							break;
						}
						case XmlResourceParser.TEXT:
							break;
					}
				}
			} catch( XmlPullParserException e ) {
			} catch( IOException e ) {
			}

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
