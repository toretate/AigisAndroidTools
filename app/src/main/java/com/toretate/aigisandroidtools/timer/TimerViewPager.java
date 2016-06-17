package com.toretate.aigisandroidtools.timer;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.pager.ViewPagerPageDefs;

/**
 * Created by toretate on 16/06/16.
 * カリスマ/スタミナ 管理ページ
 */
public class TimerViewPager extends ViewPagerPageDefs.CommonViewPagerPageDef {
	public TimerViewPager(String title, int itemId, String key, boolean defVisible) {
		super(title, itemId, key, defVisible, R.layout.tool_timer);
	}
}
