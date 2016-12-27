package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by toretate on 2016/12/27.
 */

public abstract class AbstractViewPagerPage {
    /** タイトル */
    private @NonNull String m_title;
    public @NonNull String getTitle() { return m_title; }

    /** アイテムID */
    private int m_itemId;
    public int getItemID() { return m_itemId; }

    /** true:表示する */
    private boolean m_visible;
    public void setVisibility( boolean visible ) { m_visible = visible; }
    public boolean isVisible() { return m_visible; }

    /** visible の key */
    private @NonNull  String m_prefKey;
    public @NonNull String getPreferenceKey() { return m_prefKey; }

    /** デフォルトの表示状態 */
    private boolean m_defaultVisible;
    public boolean isDefaultVisible() { return m_defaultVisible; }

    protected AbstractViewPagerPage( @NonNull final String title, final int itemId, @NonNull final String key, final boolean defVisible ) {
        this.m_title = title;
        this.m_itemId = itemId;
        this.m_visible = defVisible;
        this.m_prefKey = key;
        this.m_defaultVisible = defVisible;
    }

    protected abstract View createView(Context context, LayoutInflater inflater, ViewGroup container);

    protected void destroyView() {}
}
