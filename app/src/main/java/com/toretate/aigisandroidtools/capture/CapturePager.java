package com.toretate.aigisandroidtools.capture;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.toretate.aigisandroidtools.MainNavDrawer;
import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.pager.CommonViewPagerPage;

import java.io.File;

/**
 * Created by toretate on 2017/01/14.
 */

public class CapturePager extends CommonViewPagerPage implements MainNavDrawer.ScreenshotPermissionListener {
	
	private ImageView m_imageView;
	
	private Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String filePath = bundle.getString( "file" );
			
			Bitmap bitmap = BitmapFactory.decodeFile( filePath );
			m_imageView.setImageBitmap( bitmap );
		}
	};
	
	public CapturePager( String title, int itemId, String key, boolean defVisible, int layoutId ) {
		super( title, itemId, key, defVisible, layoutId );
	}

	private MediaProjectionManager m_mediaProjectionManager;
	
	@Override
	protected void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
		final Context context = root.getContext();

		m_imageView = (ImageView)root.findViewById( R.id.capturedImageView );
		reloadScreenshot( context );

		Button button;

		button = (Button)root.findViewById( R.id.capture_button );
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(MainNavDrawer.hasOverlayPermissison( context ) ) {
					// CaptureActivity を起動して、スクリーンショットの許可をユーザからもらう
					Intent intent = new Intent( context, CaptureActivity.class );
					context.startActivity( intent );
				} else {
					MainNavDrawer.requestOverlayPermission( context );
				}
			}
		});

		button = (Button)root.findViewById(R.id.reload_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				reloadScreenshot( context );
			}
		});
	}

	private void reloadScreenshot( Context context ) {
		File file = new File( context.getFilesDir(), "captured.png" );
		if( file.exists() ) {
			Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath() );
			m_imageView.setImageBitmap( bitmap );
		}
	}

	// ScreenshotPermissionListener
	public void screenshotPermissionCompleted() {
		Intent intent = new Intent( this.getContext(), ScreenshotService.class ).setAction( ScreenshotService.Companion.getACTION_START() );
		this.getContext().startService( intent );
	}
}
