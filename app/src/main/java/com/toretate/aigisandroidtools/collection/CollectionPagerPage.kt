package com.toretate.aigisandroidtools.collection

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.toretate.aigisandroidtools.R
import com.toretate.aigisandroidtools.pager.WebViewPagerPage

/**
 * Created by toretate on 2017/03/22.
 */
class CollectionPagerPage( title: String, itemId : Int, key : String, defVisible : Boolean, layoutId : Int, htmlFile : String ) : WebViewPagerPage( title, itemId, key, defVisible, layoutId, htmlFile ) {

    inner class LoadWebViewParams internal constructor(ctx: Context) {

        private val m_pager: WebViewPagerPage? = null
        private var m_current: Int = 0
        private var m_target: Int = 0
        private var m_max: Int = 0

        private val m_sp: SharedPreferences

        init {
            m_sp = PreferenceManager.getDefaultSharedPreferences(ctx.applicationContext)

            m_current = m_sp.getInt("CollectionCurrent", 500)
            m_target = m_sp.getInt("CollectionTarget", 1500)
            m_max = m_sp.getInt("CollectionMax", 1800)
        }


        @JavascriptInterface
        fun loadCurrent(): Int {
            return m_current
        }

        @JavascriptInterface
        fun loadTarget(): Int {
            return m_target
        }

        @JavascriptInterface
        fun loadMax(): Int {
            return m_max
        }

        @JavascriptInterface
        fun save(current: String, target: String, max: String) {
            m_current = Integer.parseInt(current)
            m_target = Integer.parseInt(target)
            m_max = Integer.parseInt(max)

            val editor = m_sp.edit()
            editor.putInt("CollectionCurrent", m_current)
            editor.putInt("CollectionTarget", m_target)
            editor.putInt("CollectionMax", m_target)
            editor.apply() // 書き込み非同期
        }
    }

    override fun afterCreateView(root: View?, inflater: LayoutInflater) {
        super.afterCreateView(root, inflater )

        val view = webView;

        WebView.setWebContentsDebuggingEnabled( true )

        view.settings.allowFileAccess = true
        view.settings.allowFileAccessFromFileURLs = true
        view.addJavascriptInterface(LoadWebViewParams(this.context), "wbAPI")
        view.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:$().setParameters( /*current*/ 500, /*target*/ 1500, /*max*/ 1800 );")
            }
        })
    }
}