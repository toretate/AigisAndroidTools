package com.toretate.aigisandroidtools.mission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.toretate.aigisandroidtools.R;

/**
 * Created by toretate on 16/06/15.
 */
public class MissionItemView {
	private TextView m_title;
	private ImageButton m_hensei;
	private ImageButton m_niconico;
	private ImageButton m_youtube;

	private Context m_context;
	private Mission m_special;
	private int m_index;

	public MissionItemView(Mission special, int index ) {
		m_special = special;
		m_index = index;
	}

	public View onCreate(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate( R.layout.tool_mission_item, null );
		m_context = inflater.getContext();

		m_title = (TextView)view.findViewById(R.id.title);
		m_hensei = (ImageButton)view.findViewById(R.id.hensei);
		m_niconico = (ImageButton)view.findViewById(R.id.niconico);
		m_youtube = (ImageButton)view.findViewById(R.id.youtube);

		return view;
	}

	public void update() {
		final String title = m_special.items.get( m_index );
		m_title.setText( title );

		ImageButton button;
		button = m_hensei;	// 編成用
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		button = m_niconico;	// ニコ動
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse( "http://sp.nicovideo.jp/search/" +title );
				Intent intent = new Intent( Intent.ACTION_VIEW, uri );
				m_context.startActivity( intent );
			}
		});

		button = m_youtube;	// Youtube
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent( Intent.ACTION_SEARCH );
				intent.setPackage( "com.google.android.youtube" );
				intent.putExtra( "query", title );
				intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
				m_context.startActivity( intent );
			}
		});
	}

}
