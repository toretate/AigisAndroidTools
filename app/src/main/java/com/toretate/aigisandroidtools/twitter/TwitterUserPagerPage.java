package com.toretate.aigisandroidtools.twitter;

/**
 * Created by toretate on 2016/12/27.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toretate.aigisandroidtools.pager.AbstractViewPagerPage;
import com.toretate.aigisandroidtools.twitter.TwitterPageBuilder;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

/**
 * TwitterUserを表示するPagerPage
 */
public class TwitterUserPagerPage extends AbstractViewPagerPage {

    /** ユーザ名(@付き) */
    private String m_userName;

    public TwitterUserPagerPage( String title, int itemId, String key, boolean defVisible, String userName ) {
        super( title, itemId, key, defVisible );
        m_userName = userName;
    }

    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        Timeline<Tweet> timeline = TwitterPageBuilder.createUserTimelinePage( m_userName ).build();
        return TwitterPageBuilder.createView( timeline, context, inflater, container );
    }
}

