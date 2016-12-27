package com.toretate.aigisandroidtools.pager;

/**
 * Created by toretate on 2016/12/27.
 */

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.toretate.aigisandroidtools.R;

/** WebViewが張られているPager */
public class WebViewPagerPage extends CommonViewPagerPage {
    private String m_htmlFile;

    public WebViewPagerPage( String title, int itemId, String key, boolean defVisible, int layoutId, String htmlFile ) {
        super( title, itemId, key, defVisible, layoutId );
        m_htmlFile = htmlFile;
    }

    @Override
    protected void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
        WebView view = (WebView)root.findViewById( R.id.webviewpager_webview );
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("file:///android_asset/" +m_htmlFile);
    }
}
