package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.twitter.TwitterPageBuilder;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toretatenee on 16/05/20.
 */
public class ViewPagerPageDefs {

	abstract class ViewPagerPageDef {
		String title;					//!< タイトル
		int itemId;						//!< アイテムID
		boolean visible;				//!< 表示状態
		String prefKey;					//!< visible の設定値を引き出すためのKey
		boolean defaultVisible;		//!< デフォルト表示状態

		ViewPagerPageDef( String title, int itemId, String key, boolean defVisible ) {
			this.title = title;
			this.itemId = itemId;
			this.visible = defVisible;
			this.prefKey = key;
			this.defaultVisible = defVisible;
		}

		abstract View createView(Context context, LayoutInflater inflater, ViewGroup container);
	}

	class CommonViewPagerPageDef extends ViewPagerPageDef {
		private int m_layoutId;
		CommonViewPagerPageDef( String title, int itemId, String key, boolean defVisible, int layoutId ) {
			super( title, itemId, key, defVisible );
			m_layoutId = layoutId;
		}
		View createView(Context context, LayoutInflater inflater, ViewGroup container ) {
			return m_layoutId != -1 ? inflater.inflate( m_layoutId, container ) : null;
		}
	}

	class TwitterUserPagerDef extends ViewPagerPageDef {
		String userName;
		TwitterUserPagerDef( String title, int itemId, String key, boolean defVisible, String userName ) {
			super( title, itemId, key, defVisible );
			this.userName = userName;
		}

		View createView(Context context, LayoutInflater inflater, ViewGroup container) {
			Timeline<Tweet> timeline = TwitterPageBuilder.createUserTimelinePage( this.userName ).build();
			return TwitterPageBuilder.createView( timeline, context, inflater, container );
		}
	}

	class TwitterSearchPagerDef extends ViewPagerPageDef {
		String query;
		TwitterSearchPagerDef( String title, int itemId, String key, boolean defVisible, String query ) {
			super( title, itemId, key, defVisible );
			this.query = query;
		}

		View createView(Context context, LayoutInflater inflater, ViewGroup container) {
			Timeline<Tweet> timeline = TwitterPageBuilder.createSearchTimelinePage( this.query ).build();
			return TwitterPageBuilder.createView( timeline, context, inflater, container );
		}
	}

	private List<ViewPagerPageDef> m_pages;
	private List<ViewPagerPageDef> m_pageVisibles;

	private static ViewPagerPageDefs instance;
	public static ViewPagerPageDefs getInstance( Context ctx ) {
		if( instance == null ) {
			instance = new ViewPagerPageDefs( ctx );
		}
		return instance;
	}

	private ViewPagerPageDefs( final Context context ) {
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences( context.getApplicationContext() );

		ArrayList<ViewPagerPageDef> pageDefs = new ArrayList<>();
		pageDefs.add( new TwitterUserPagerDef( 		"@aigis1000", 		R.id.tw_aigis1000, 	"tw_aigis1000",		true,	"Aigis1000" ) );
		pageDefs.add( new TwitterUserPagerDef( 		"@aigis1000_A", 		R.id.tw_aigis1000A, 	"tw_aigis1000A",		false,	"Aigis1000_A" ) );
		pageDefs.add( new TwitterSearchPagerDef(	"#千年戦争アイギス", 	R.id.tw_aigis_hash, 	"tw_aigis_hash",		true, 	"#千年戦争アイギス" ) );
		pageDefs.add( new CommonViewPagerPageDef(	"合成表", 				R.id.tool_compose,	"tool_compose",		true,	R.layout.compose_fragment ) );
		pageDefs.add( new TwitterUserPagerDef( 		"作者", 				R.id.tw_toretatenee,	"tw_toretatenee",		false,	"toretatenee" ) );
		pageDefs.add( new CommonViewPagerPageDef(	"管理", 				R.id.action_settings,	"action_settings",	false,	-1 ) );
		m_pages = pageDefs;

		update( context );
	}



	// Visible系
	public String getTitle( int index ) { return m_pageVisibles.get(index).title; }
	public int findItemIndex( int itemId ) {
		int index = 0;
		for ( ViewPagerPageDef page : m_pageVisibles ) {
			if( page.itemId == itemId ) return index;
			index ++;
		}
		return -1;
	}

	/** 設定情報を更新します */
	public void update( Context ctx ) {
		m_pageVisibles = new ArrayList<>();

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( ctx.getApplicationContext() );
		for( int i=0; i<m_pages.size(); i++ ) {
			ViewPagerPageDef page = m_pages.get(i);
			page.visible = sp.getBoolean( page.prefKey, page.defaultVisible );

			if( page.visible ) m_pageVisibles.add( page );
		}
	}


	public int getPageCount() { return m_pageVisibles.size(); }

	public View createView(int index,Context context, LayoutInflater inflater, ViewGroup container ) {
		return m_pageVisibles.get( index ).createView( context, inflater, container );
	}

	public static boolean isSettings( int id ) { return id == R.id.action_settings; }

}
