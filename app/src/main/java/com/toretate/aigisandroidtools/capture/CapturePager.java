package com.toretate.aigisandroidtools.capture;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.leptonica.android.Scale;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.toretate.aigisandroidtools.MainNavDrawer;
import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.pager.CommonViewPagerPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by toretate on 2017/01/14.
 */

public class CapturePager extends CommonViewPagerPage implements MainNavDrawer.ScreenshotPermissionListener {
	
	private static String TAG = CapturePager.class.getSimpleName();
	private static String EXSTORAGE_PATH = String.format("%s/%s", Environment.getExternalStorageDirectory().toString(), "OCRTest");
	private static String TESSDATA_PATH = String.format("%s/%s", EXSTORAGE_PATH, "tessdata");
	private static String TRAIN_LANG = "jpn";
	private static String TRAINEDDATA = String.format("%s.traineddata", TRAIN_LANG);
	
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
	private Rect m_rect;

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

		button = (Button)root.findViewById(R.id.reload_sample_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AssetManager assetManager = context.getAssets();
				try {
					InputStream is = assetManager.open( "sample.jpg" );
					Bitmap bitmap = BitmapFactory.decodeStream( is );
					bitmap = bitmap.copy( Bitmap.Config.ARGB_8888, true );

					Canvas canvas = new Canvas( bitmap );

					RectF rect = new RectF( 56, 81, 56 + 230, 81 + 22 );
					Paint paint = new Paint();
					paint.setStyle( Paint.Style.STROKE );
					paint.setStrokeWidth( 10.0f );
					paint.setColor(Color.RED);
					canvas.drawRect( rect, paint );


					m_imageView.setImageBitmap( bitmap );
				} catch( IOException e ) {

				}
			}
		});
		
		button = (Button)root.findViewById(R.id.ocr_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap bitmap = ((BitmapDrawable)m_imageView.getDrawable()).getBitmap();

				String text = TessUtilKt.doOCR( bitmap, m_rect );

				Log.d(TAG, "Recognized : " + text );
			}
		});
		
		// Tessデータをアセットから外部ストレージに配置。TessBaseAPIがアセットを扱えないため。
		button = (Button)root.findViewById(R.id.setting_tess_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					TessUtilKt.doTessSetting( context );
				} catch( Exception e ) {
				}
			}
		});
	}

	/**
	 * スクリーンショットを再ロードする
	 * @param context
	 */
	private void reloadScreenshot( Context context ) {
		File file = new File( context.getFilesDir(), "captured.png" );
		if( file.exists() ) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inMutable = true;
			Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath(), options );

			Canvas canvas = new Canvas( bitmap );

			m_rect = new Rect( 490, 25, 650, 55 );	// left:550でちょうどカリスマの右横
			Paint paint = new Paint();
			paint.setStyle( Paint.Style.STROKE );
			paint.setStrokeWidth( 1.0f );
			paint.setColor(Color.RED);
			canvas.drawRect( m_rect, paint );

			m_imageView.setImageBitmap( bitmap );
			m_imageView.setScaleType(ImageView.ScaleType.CENTER);
		}
	}

	// ScreenshotPermissionListener
	public void screenshotPermissionCompleted() {
		Intent intent = new Intent( this.getContext(), ScreenshotService.class ).setAction( ScreenshotService.Companion.getACTION_START() );
		this.getContext().startService( intent );
	}
	
}
