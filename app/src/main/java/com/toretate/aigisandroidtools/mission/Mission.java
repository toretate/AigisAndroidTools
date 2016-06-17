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
	public String title;		//!< 緊急ミッションタイトル
	public List<String> items;	//!< 緊急ミッションの各ステージ名
	public boolean visible;

	public static Mission loadFromJSON(JSONObject mission ) throws JSONException {
		String title = mission.getString("title");
		boolean visible = mission.optBoolean("visible", true );
		JSONArray items = mission.getJSONArray("items");

		Mission result = new Mission();
		result.title = title;
		result.items = new ArrayList<>();
		result.visible = visible;

		for( int i=0; i<items.length(); i++ ) {
			String name = items.getString(i);
			result.items.add( name );
		}

		return result;
	}
}
