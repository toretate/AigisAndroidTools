package com.toretate.aigisandroidtools.pager;

/**
 * Created by toretate on 2016/12/27.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.toretate.aigisandroidtools.R;

/** WebViewが張られているPager */
public class WebViewPagerPage extends CommonViewPagerPage {
    private String m_htmlFile;

    public class LoadWebViewParams {

        private WebViewPagerPage m_pager;
        private int m_current;
        private int m_target;
        private int m_max;

        private SharedPreferences m_sp;

        LoadWebViewParams( final Context ctx ) {
            m_sp = PreferenceManager.getDefaultSharedPreferences( ctx.getApplicationContext() );

            m_current = m_sp.getInt( "CollectionCurrent", 500 );
            m_target = m_sp.getInt( "CollectionTarget", 1500 );
            m_max = m_sp.getInt( "CollectionMax", 1800 );
        }


        @JavascriptInterface
        public int loadCurrent() { return m_current; }

        @JavascriptInterface
        public int loadTarget() { return m_target; }

        @JavascriptInterface
        public int loadMax() { return m_max; }

        @JavascriptInterface
        public void save( String current, String target, String max ) {
            m_current = Integer.parseInt( current );
            m_target = Integer.parseInt( target );
            m_max = Integer.parseInt( max );

            SharedPreferences.Editor editor = m_sp.edit();
            editor.putInt( "CollectionCurrent", m_current );
            editor.putInt( "CollectionTarget", m_target );
            editor.putInt( "CollectionMax", m_target );
            editor.apply(); // 書き込み非同期
        }

    }

    public WebViewPagerPage( String title, int itemId, String key, boolean defVisible, int layoutId, String htmlFile ) {
        super( title, itemId, key, defVisible, layoutId );
        m_htmlFile = htmlFile;
    }

    @Override
    protected void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
        WebView view = (WebView)root.findViewById( R.id.webviewpager_webview );
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.addJavascriptInterface( new LoadWebViewParams( this.getContext() ), "wbAPI");
        view.loadUrl("file:///android_asset/" +m_htmlFile);

        view.setWebViewClient( new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:$().setParameters( /*current*/ 500, /*target*/ 1500, /*max*/ 1800 );");
                //view.loadUrl("javascript:(function(){ $().setParameters( /*current*/ 500, /*target*/ 1500, /*max*/ 1800 ) } )()");
            }
        });
    }
}
