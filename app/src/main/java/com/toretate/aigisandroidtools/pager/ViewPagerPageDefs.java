package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.mission.MissionViewPager;
import com.toretate.aigisandroidtools.timer.TimerViewPager;
import com.toretate.aigisandroidtools.twitter.TwitterSearchPagerPage;
import com.toretate.aigisandroidtools.twitter.TwitterUserPagerPage;

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
		pageDefs.add( new TwitterUserPagerPage( 	"@aigis1000", 		R.id.tw_aigis1000, 	"tw_aigis1000",		true,	"Aigis1000" ) );
		pageDefs.add( new TwitterUserPagerPage( 	"@aigis1000_A", 		R.id.tw_aigis1000A, 	"tw_aigis1000A",		false,	"Aigis1000_A" ) );
		pageDefs.add( new TwitterSearchPagerPage(	"#千年戦争アイギス", 	R.id.tw_aigis_hash, 	"tw_aigis_hash",		true, 	"#千年戦争アイギス" ) );
		pageDefs.add( new MissionViewPager(			"ミッション",			R.id.tool_mission,	"tool_mission",		true	 ) );
		pageDefs.add( new TimerViewPager(			"カリ/スタ管理",		R.id.tool_timer,		"tool_timer",			false ) );
		pageDefs.add( new WebViewPagerPage(			"合成表", 				R.id.tool_compose,	"tool_compose",		false,	R.layout.compose_fragment, "compose.html" ) );
		pageDefs.add( new TwitterUserPagerPage( 	"作者", 				R.id.tw_toretatenee,	"tw_toretatenee",		false,	"toretatenee" ) );
		pageDefs.add( new CommonViewPagerPage(		"管理", 				R.id.action_settings,	"action_settings",	false,	-1 ) );
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

	/** 指定したIDが「設定」のものかどうか */
	public static boolean isSettings( int id ) { return id == R.id.action_settings; }

}
