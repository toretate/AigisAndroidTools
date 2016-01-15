package com.toretate.aigisandroidtools;

import android.app.Activity;
import android.view.View;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

/**
 * MoPUB用の設定や処理を行うクラス
 * Created by toretate on 2016/01/16.
 */
public class MoPubSettings implements MoPubView.BannerAdListener {
	private MoPubView m_moPubView;

	public MoPubSettings( final Activity activity ) {
		// MoPUB
		m_moPubView = (MoPubView) activity.findViewById(R.id.adview);
		// TODO: Replace this test id with your personal ad unit id
		m_moPubView.setAdUnitId("df8b7addafda4f788212d2a8dead7a57");
		m_moPubView.loadAd();
		m_moPubView.setBannerAdListener(this);
	}

	public void destroy() {
		m_moPubView.destroy();
	}

	@Override
	public void onBannerLoaded(MoPubView moPubView) {

	}

	@Override
	public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {

	}

	@Override
	public void onBannerClicked(MoPubView moPubView) {

	}

	@Override
	public void onBannerExpanded(MoPubView moPubView) {

	}

	@Override
	public void onBannerCollapsed(MoPubView moPubView) {

	}
}
