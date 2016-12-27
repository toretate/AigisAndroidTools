package com.toretate.aigisandroidtools.pager;

/**
 * Created by Kenji on 2016/12/27.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.toretate.aigisandroidtools.R;

/** WebViewが張られているPager */
public class WebViewPagerPage extends AbstractViewPagerPage {
    private int m_layoutId;
    private String m_htmlFile;

    public WebViewPagerPage( String title, int itemId, String key, boolean defVisible, int layoutId, String htmlFile ) {
        super( title, itemId, key, defVisible );
        m_layoutId = layoutId;
        m_htmlFile = htmlFile;
    }

    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container ) {
        View view = m_layoutId != -1 ? inflater.inflate( m_layoutId, container, false ) : null;
        afterCreateView( view, inflater );
        return view;
    }

    public void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
        WebView view = (WebView)root.findViewById( R.id.webviewpager_webview );
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("file:///android_asset/compose.html");
    }
}
