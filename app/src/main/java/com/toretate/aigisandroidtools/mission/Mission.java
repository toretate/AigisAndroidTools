package com.toretate.aigisandroidtools.mission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toretate on 16/06/15.
 */
public class Mission {
	public String title;        	//!< 緊急ミッションタイトル
	public int cha;
	public int sta;
	public int exp;
	public int gold;
	public List<String> rewards = new ArrayList<>();

	private List<String> stringItems = new ArrayList<>();    //!< 緊急ミッションの各ステージ名
	private List<Mission> subMissions = new ArrayList<>();

	public int getSubmissionCount() { return subMissions.size(); }
	public String getSubTitle( int index ) {
		return subMissions.get( index ).title;
	}

	public boolean visible;

	public static Mission loadFromJSON(JSONObject mission) throws JSONException {
		String title = mission.getString("title");
		int cha = mission.optInt( "cha", 0 );
		int sta = mission.optInt( "sta", 0 );
		int exp = mission.optInt( "exp", 0 );
		int gold = mission.optInt( "gold", 0 );
		JSONArray rewards = mission.optJSONArray( "rewards" );
		if( rewards == null ) rewards = new JSONArray();

		boolean visible = mission.optBoolean("visible", true);
		if( visible == false ) return null;

		JSONArray items = mission.getJSONArray("items");

		Mission result = new Mission();
		result.title = title;
		result.cha = cha;
		result.sta = sta;
		result.exp = exp;
		result.gold = gold;
		result.visible = visible;
		for( int i=0; i<rewards.length(); i++ ) {
			result.rewards.add( rewards.getString(i) );
		}

		for (int i = 0; i < items.length(); i++) {
			try {
				JSONObject obj = items.getJSONObject(i);
				Mission sub = Mission.loadFromJSON( obj );
				if( sub != null ) result.subMissions.add( sub );
			} catch( JSONException ex ) {
				// Stringだった
				String name = items.getString(i);
				result.stringItems.add(name);
			}
		}

		return result;
	}
}
