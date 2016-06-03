package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ViewPager用のアダプタ
 * Created by toretate on 2016/01/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

	private Context m_appContext;
	private boolean m_nowDataSetChanging;

	public PagerAdapter(FragmentManager fragmentManager, Context context ) {
		super(fragmentManager);
		m_appContext = context.getApplicationContext();
		m_nowDataSetChanging = false;
	}

	@Override
	public Fragment getItem(int position) {
		return ViewPagerContentFragment.newInstance(position);
	}

	@Override
	public void notifyDataSetChanged() {
		try {
			m_nowDataSetChanging = true;
			super.notifyDataSetChanged();
		} finally {
			m_nowDataSetChanging = false;
		}
	}

	@Override
	public int getItemPosition(Object object) {
		if( m_nowDataSetChanging ) {
			return POSITION_NONE;	// 再作成される
		} else {
			return super.getItemPosition(object);
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return ViewPagerPageDefs.getInstance( m_appContext ).getTitle( position );
	}

	@Override
	public int getCount() {
		return ViewPagerPageDefs.getInstance( m_appContext ).getPageCount();
	}
}
