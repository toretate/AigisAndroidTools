package com.toretate.aigisandroidtools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ViewPager用のアダプタ
 * Created by toretate on 2016/01/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

	public PagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {
		return ViewPagerContentFragment.newInstance(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return ViewPagerPageDefs.instance.getTitle( position );
	}

	@Override
	public int getCount() {
		return ViewPagerPageDefs.instance.getPageCount();
	}
}
