package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.capture.CapturePager;
import com.toretate.aigisandroidtools.dbview.DBViewPagerPage;
import com.toretate.aigisandroidtools.mission.MissionViewPager;
import com.toretate.aigisandroidtools.timer.TimerViewPager;
import com.toretate.aigisandroidtools.twitter.TwitterSearchPagerPage;
import com.toretate.aigisandroidtools.twitter.TwitterUserPagerPage;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toretatenee on 16/05/20.
 */
public class ViewPagerPageDefs {

	/** ページ郡全体 */
	private List<AbstractViewPagerPage> m_pages;

	/** m_pagesの中の、表示中ページを集めたもの */
	private List<AbstractViewPagerPage> m_pageVisibles;

	/** シングルトン・・・だけどAndroidの場合この実装だとマズイ(破棄される可能性がある) */
	private static ViewPagerPageDefs s_instance;
	public static ViewPagerPageDefs getInstance( Context ctx ) {
		if( s_instance == null ) {
			s_instance = new ViewPagerPageDefs( ctx );
		}
		return s_instance;
	}

	private ViewPagerPageDefs( @NonNull  final Context context ) {
		ArrayList<AbstractViewPagerPage> pageDefs = new ArrayList<>();

		// /pager-defs/* から @key, @title, @summary を取得してチェックボックス項目を作成
		XmlResourceParser parser = context.getResources().getXml( R.xml.settings );
		try {
			int event;
			boolean isPagerDefsChildren = false;
			while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
				switch( event ) {
					case XmlResourceParser.START_DOCUMENT:
					case XmlResourceParser.END_DOCUMENT:
						break;
					case XmlResourceParser.START_TAG: {
						String tagName = parser.getName();

						String title = parser.getAttributeValue( null, "title" );
						int itemID = parser.getAttributeResourceValue( null, "itemID", -1 );
						String key = parser.getAttributeValue( null, "key" );
						boolean defVisible = parser.getAttributeBooleanValue( null, "defVisible", true );

						if( tagName.equals("pager-defs") ) {
							isPagerDefsChildren = true;
						} else if( isPagerDefsChildren ) {
							switch( tagName ) {
								case "twitter":
									if( isPagerDefsChildren ) {
										String user = parser.getAttributeValue( null, "user" );
										String search = parser.getAttributeValue( null, "search" );
										if( user != null ) {
											pageDefs.add( new TwitterUserPagerPage( title, itemID, key, defVisible,	user ) );
										} else {
											pageDefs.add( new TwitterSearchPagerPage( title, itemID, key, defVisible, search ) );
										}
									}
									break;
								case "mission":
									pageDefs.add( new MissionViewPager(	 title, itemID,	key, defVisible ) );
									break;
								case "timer":
									pageDefs.add( new TimerViewPager( title, itemID, key, defVisible ) );
									break;
								case "webview": {
									int fragment = parser.getAttributeResourceValue( null, "fragment", -1 );
									String html = parser.getAttributeValue( null, "html" );
									pageDefs.add( new WebViewPagerPage( title, itemID, key, defVisible,	fragment, html ) );
									break;
								}
								case "capture": {
									int layout = parser.getAttributeResourceValue( null, "layout", -1 );
									pageDefs.add( new CapturePager( title, itemID,  key, defVisible, layout ) );
									break;
								}
								case "db": {
									int layout = parser.getAttributeResourceValue( null, "layout", -1 );
									pageDefs.add( new DBViewPagerPage( title, itemID, key, defVisible,	layout ) );
									break;
								}
								case "common": {
									int layout = parser.getAttributeResourceValue( null, "layout", -1 );
									pageDefs.add( new CommonViewPagerPage( title, itemID, key, defVisible,	layout ) );
									break;
								}
							}
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

		m_pages = pageDefs;

		updateVisibility( context );
	}

	// Visible系

	/** 現在Visibleになっているページ数を取得する*/
	public int getPageCount() { return m_pageVisibles.size(); }

	/**
	 *  現在VisibleになっているPagerPageのリストからタイトルを取得する
	 *  @param index タイトルを取得する、現在Visibleになっているリスト中のインデックス
	 */
	public @NonNull  String getTitle( final int index ) { return m_pageVisibles.get(index).getTitle(); }

	/**
	 * ItemIDから表示中のPagerPageのIndexを取得する
	 * @param itemId ItemID
	 * @return Index ( 表示中PagerPageに存在しない場合は null )
	 */
	public @Nullable Integer findItemIndex( final int itemId ) {
		int index = 0;
		for ( AbstractViewPagerPage page : m_pageVisibles ) {
			if( page.getItemID() == itemId ) return index;
			index ++;
		}
		return null;
	}

	/** PagerPageeの表示／非表示状態を更新します */
	public void updateVisibility( @NonNull final Context ctx ) {
		m_pageVisibles = new ArrayList<>();

		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( ctx.getApplicationContext() );
		for( int i=0; i<m_pages.size(); i++ ) {
			final AbstractViewPagerPage page = m_pages.get(i);
			page.setVisibility( sp.getBoolean( page.getPreferenceKey(), page.isDefaultVisible() ) );

			if( page.isVisible() ) m_pageVisibles.add( page );
		}
	}

	/** 設定情報からViewを作成します */
	public @Nullable View createView( final int index, final Context context, final LayoutInflater inflater, final ViewGroup container ) {
		if( m_pageVisibles.size() <= index ) return null;
		final AbstractViewPagerPage viewPager = m_pageVisibles.get( index );
		View view = null;
		if( viewPager != null ) view = viewPager.createView( context, inflater, container );
		return view;
	}

	public void destroyView( final int index ) {
		if( m_pageVisibles.size() <= index ) return;
		final AbstractViewPagerPage viewPager = m_pageVisibles.get( index );
		if( viewPager != null ) viewPager.destroyView();
	}

	/** 指定したIDが「設定」のものかどうか */
	public static boolean isSettings( int id ) { return id == R.id.action_settings; }

}
