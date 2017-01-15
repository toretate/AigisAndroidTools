package com.toretate.aigisandroidtools.capture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by toretate on 2017/01/14.
 */

public class CapturePager extends CommonViewPagerPage {
	
	private Reciever m_reciever;
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
	
	public static class Reciever extends BroadcastReceiver {
		
		private CapturePager m_pager;
		public Reciever( CapturePager pager ) {
			m_pager = pager;
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString( "message" );
			
			Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
			
			Message msg = new Message();
			msg.setData( new Bundle( bundle ) );
			m_pager.m_handler.handleMessage( msg );
			
		}
	}
	
	
	public CapturePager( String title, int itemId, String key, boolean defVisible, int layoutId ) {
		super( title, itemId, key, defVisible, layoutId );
	}
	
	
	@Override
	protected void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
		m_imageView = (ImageView)root.findViewById( R.id.capturedImageView );
		
		Button button = (Button)root.findViewById( R.id.button );
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Context context = root.getContext();
				
				// recieverを登録しとく
				if( m_reciever == null ) {
					m_reciever = new Reciever( CapturePager.this );
					IntentFilter filter = new IntentFilter();
					filter.addAction( "ACTION_CAPTURE_RESULT" );
					context.registerReceiver( m_reciever, filter );
				}
				
				if(MainNavDrawer.hasOverlayPermissison( context ) ) {
					Intent intent = new Intent( context, ScreenshotService.class ).setAction( ScreenshotService.Companion.getACTION_START() );
					context.startService( intent );
				} else {
					MainNavDrawer.requestOverlayPermission( context );
				}
			}
		});
		
	}
	
	
}
