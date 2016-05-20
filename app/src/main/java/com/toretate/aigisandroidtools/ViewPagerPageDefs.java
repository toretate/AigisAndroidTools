package com.toretate.aigisandroidtools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toretatenee on 16/05/20.
 */
public class ViewPagerPageDefs {

	abstract class ViewPagerPageDef {
		String title;
		int itemId;
		boolean visible;

		ViewPagerPageDef( String title, int itemId, boolean visibility ) {
			this.title = title;
			this.itemId = itemId;
			this.visible = visibility;
		}

		abstract View createView(Context context, LayoutInflater inflater, ViewGroup container);
	}

	class CommonViewPagerPageDef extends ViewPagerPageDef {
		CommonViewPagerPageDef( String title, int itemId, boolean visibility ) {
			super( title, itemId, visibility );
		}
		View createView(Context context, LayoutInflater inflater, ViewGroup container ) { return null; }
	}

	class TwitterUserPagerDef extends ViewPagerPageDef {
		String userName;
		TwitterUserPagerDef( String title, int itemId, String userName, boolean visibility ) {
			super( title, itemId, visibility );
			this.userName = userName;
		}

		View createView(Context context, LayoutInflater inflater, ViewGroup container) {
			Timeline<Tweet> timeline = TwitterPageBuilder.createUserTimelinePage( this.userName ).build();
			return TwitterPageBuilder.createView( timeline, context, inflater, container );
		}
	}

	class TwitterSearchPagerDef extends ViewPagerPageDef {
		String query;
		TwitterSearchPagerDef( String title, int itemId, String query, boolean visibility ) {
			super( title, itemId, visibility );
			this.query = query;
		}

		View createView(Context context, LayoutInflater inflater, ViewGroup container) {
			Timeline<Tweet> timeline = TwitterPageBuilder.createSearchTimelinePage( this.query ).build();
			return TwitterPageBuilder.createView( timeline, context, inflater, container );
		}
	}

	private List<ViewPagerPageDef> m_pages;
	private List<ViewPagerPageDef> m_pageVisibles;

	public static ViewPagerPageDefs instance;
	static {
		instance = new ViewPagerPageDefs();
	}

	private ViewPagerPageDefs() {
		ArrayList<ViewPagerPageDef> pageDefs = new ArrayList<>();
		pageDefs.add( new TwitterUserPagerDef( 		"@aigis1000", 		R.id.tw_aigis1000, 	"Aigis1000", 		true ) );
		pageDefs.add( new TwitterUserPagerDef( 		"@aigis1000_A", 	R.id.tw_aigis1000A, "Aigis1000_A", 		false ) );
		pageDefs.add( new TwitterSearchPagerDef(	"#千年戦争アイギス", 	R.id.tw_aigis_hash, "#千年戦争アイギス", 	true ) );
		pageDefs.add( new TwitterUserPagerDef( 		"作者", 				R.id.tw_toretatenee,"toretatenee", 		false ) );
		pageDefs.add( new CommonViewPagerPageDef(	"管理", 				R.id.nav_manage, false ) );
		m_pages = pageDefs;

		m_pageVisibles = new ArrayList<>();
		for( int i=0; i<pageDefs.size(); i++ ) {
			ViewPagerPageDef page = m_pages.get(i);
			if( page.visible ) m_pageVisibles.add( page );
		}
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

	public int getPageCount() { return m_pageVisibles.size(); }

	public View createView(int index,Context context, LayoutInflater inflater, ViewGroup container ) {
		return m_pageVisibles.get( index ).createView( context, inflater, container );
	}

}
