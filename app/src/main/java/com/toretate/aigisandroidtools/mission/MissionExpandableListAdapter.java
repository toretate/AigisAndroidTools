package com.toretate.aigisandroidtools.mission;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by toretate on 16/06/15.
 */
public class MissionExpandableListAdapter extends BaseExpandableListAdapter {
	private List<Mission> m_specials;
	private Context m_context;

	public MissionExpandableListAdapter( Context context, List<Mission> specials ) {
		m_context = context;
		m_specials = specials;
	}

	@Override
	public int getGroupCount() {
		return m_specials.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return getGroup(groupPosition).items.size();
	}

	@Override
	public Mission getGroup(int groupPosition) {
		return m_specials.get(groupPosition);
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition).items.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		AbsListView.LayoutParams param = new AbsListView.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 64 );
		TextView textView = new TextView( m_context );
		textView.setLayoutParams( param );
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding( 64, 0, 0, 0);

		textView.setText( getGroup(groupPosition).title );
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Mission special = getGroup( groupPosition );
		MissionItemView specialItem = new MissionItemView( special, childPosition );
		View view = specialItem.onCreate(LayoutInflater.from(m_context), parent);
		specialItem.update();
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
