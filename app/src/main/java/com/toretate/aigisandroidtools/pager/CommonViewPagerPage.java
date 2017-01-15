package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by toretate on 2016/12/27.
 */
public class CommonViewPagerPage extends AbstractViewPagerPage {
    private int m_layoutId;

    public CommonViewPagerPage(String title, int itemId, String key, boolean defVisible, int layoutId) {
        super(title, itemId, key, defVisible);
        m_layoutId = layoutId;
    }

    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        View view = m_layoutId != -1 ? inflater.inflate(m_layoutId, container, false) : null;
        afterCreateView(view, inflater);
        return view;
    }

    protected void afterCreateView(final @Nullable View root, final LayoutInflater inflater) {
    }
    
}
