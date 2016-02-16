package com.toretate.aigisandroidtools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;

import java.lang.ref.WeakReference;

import layout.ViewPagerContentFragment;

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
		switch( position ) {
		default:
		case 0:
			return "@aigis1000";
		case 1:
			return "@aigis1000A";
		case 2:
			return "#千年戦争アイギス";
		case 3:
			return "@toretatenee";
		case 4:
			return "Tools";
		}
	}

	@Override
	public int getCount() {
		return 5;
	}
}
