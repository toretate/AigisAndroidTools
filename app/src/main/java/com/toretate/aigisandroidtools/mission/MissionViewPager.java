package com.toretate.aigisandroidtools.mission;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.pager.ViewPagerPageDefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toretatenee on 16/06/06.
 */
public class MissionViewPager extends ViewPagerPageDefs.CommonViewPagerPageDef {

	private List<Mission> m_mission;

	public MissionViewPager( String title, int itemId, String key, boolean defVisible) {
		super( title, itemId, key, defVisible, R.layout.tool_mission );
	}

	@Override
	public void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
		super.afterCreateView(root, inflater);
		if( root == null ) return;

		View view = root.findViewById( R.id.MissionTable );

		if( view instanceof ExpandableListView == false ) return;
		ExpandableListView table = (ExpandableListView)view;

		if( m_mission == null ) {
			JSONObject json = loadJSON( inflater.getContext() );

			try {
				JSONArray missionArray = json.getJSONArray( "missions" );
				m_mission = loadMissions( missionArray, inflater, table );
			} catch( JSONException ex ) {
				ex.printStackTrace();
			}
		}

		table.setAdapter( new MissionExpandableListAdapter(inflater.getContext(), m_mission));
	}

	private List<Mission> loadMissions(JSONArray missionArray, LayoutInflater inflater, ExpandableListView table ) throws JSONException {
		List<Mission> missions = new ArrayList<>();
		for( int i= 0; i<missionArray.length(); i++ ) {
			Mission special = Mission.loadFromJSON( missionArray.getJSONObject(i) );
			missions.add( special );
		}
		return missions;
	}

	void loadMissions(List<Mission> specials, final LayoutInflater inflater, ExpandableListView table ) throws JSONException {
		int index = 0;
		for( Mission special : specials ) {
			MissionItemView view = new MissionItemView( special, index );
			view.onCreate( inflater, table );
			view.update();
			index ++;
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
