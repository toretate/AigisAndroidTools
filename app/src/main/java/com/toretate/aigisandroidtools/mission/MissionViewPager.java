package com.toretate.aigisandroidtools.mission;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.pager.ViewPagerPageDefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * Created by kenji_watatani on 16/06/06.
 */
public class MissionViewPager extends ViewPagerPageDefs.CommonViewPagerPageDef {

	public MissionViewPager( String title, int itemId, String key, boolean defVisible) {
		super( title, itemId, key, defVisible, R.layout.tool_mission );
	}

	@Override
	public void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
		super.afterCreateView(root, inflater);
		if( root == null ) return;

		View view = root.findViewById( R.id.MissionTable );

		if( view instanceof TableLayout == false ) return;
		TableLayout table = (TableLayout)view;

		JSONObject json = loadJSON( inflater.getContext() );

		try {
			JSONArray specials = json.getJSONArray( "specials" );
			loadSpecials( specials, inflater, table );
		} catch( JSONException ex ) {
			ex.printStackTrace();
		}
	}

	void loadSpecials(JSONArray specials, LayoutInflater inflater, TableLayout table ) throws JSONException {
		for( int i= 0; i<specials.length(); i++ ) {
			loadSpecial( specials.getJSONObject(i), inflater, table );
		}
	}

	void loadSpecial( JSONObject special, LayoutInflater inflater, TableLayout table ) throws JSONException {
		String title = special.getString("title");
		JSONArray missions = special.getJSONArray("missions");
		loadMissions( missions, inflater, table );
	}

	void loadMissions( JSONArray missions, LayoutInflater inflater, TableLayout table ) throws JSONException {
		for( int i= 0; i<missions.length(); i++ ) {
			inflater.inflate( R.layout.tool_mission_item, table );	// ここで table に追加

			String mission = missions.getString( i );
			TableRow tr = (TableRow)table.getChildAt( i );
			TextView tv = (TextView)tr.getChildAt(0);
			tv.setText( mission );
		}
	}

	JSONObject loadJSON( final Context context ) {
		try {
			InputStream in = context.getResources().openRawResource( R.raw.mission_defs );
			BufferedReader br = new BufferedReader( new InputStreamReader( in ) );

			StringBuilder sb = new StringBuilder();
			String line;
			while( ( line = br.readLine() ) != null ) {
				sb.append( line );
			}

			return new JSONObject( sb.toString() );

		} catch( IOException ex ) {
			ex.printStackTrace();
		} catch( JSONException ex ) {
			ex.printStackTrace();
		}
		return null;
	}

	void createTableRow() {}

}
