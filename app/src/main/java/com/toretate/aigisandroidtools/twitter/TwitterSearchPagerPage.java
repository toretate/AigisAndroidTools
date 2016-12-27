package com.toretate.aigisandroidtools.twitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toretate.aigisandroidtools.pager.AbstractViewPagerPage;
import com.toretate.aigisandroidtools.twitter.TwitterPageBuilder;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;

/**
 * Created by toretate on 2016/12/27.
 */

public class TwitterSearchPagerPage extends AbstractViewPagerPage {

    /** 検索クエリ */
    private String query;

    public TwitterSearchPagerPage( String title, int itemId, String key, boolean defVisible, String query ) {
        super( title, itemId, key, defVisible );
        this.query = query;
    }

    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        Timeline<Tweet> timeline = TwitterPageBuilder.createSearchTimelinePage( this.query ).build();
        return TwitterPageBuilder.createView( timeline, context, inflater, container );
    }
}
