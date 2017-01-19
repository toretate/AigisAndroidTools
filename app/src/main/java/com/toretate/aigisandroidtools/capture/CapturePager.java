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
		
		button = (Button)root.findViewById(R.id.ocr_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap bitmap = ((BitmapDrawable)m_imageView.getDrawable()).getBitmap();
				
				// OCRの実験本体
				// * スクリーンショットをロード (ARGB_8888に変換する)
				// * OCRを行う TessBaseAPIを初期化
				// * traineddataを設定
				// * 画像を指定
				// * 文字を検出する範囲を指定
				TessBaseAPI baseApi = new TessBaseAPI();
				baseApi.init( EXSTORAGE_PATH + "/", "jpn");
				baseApi.setImage( bitmap );
				baseApi.setRectangle( 56, 81, 230,  22 );
				String recognizedtext = baseApi.getUTF8Text();
				baseApi.end();
				
				Log.d(TAG, "Recognized : " + recognizedtext );
			}
		});
		
		// Tessデータをアセットから外部ストレージに配置。TessBaseAPIがアセットを扱えないため。
		button = (Button)root.findViewById(R.id.setting_tess_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					prepareTrainedFileIfNotExist( context );
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
		AssetManager assetManager = context.getAssets();
		try {
			InputStream is = assetManager.open( "sample.jpg" );
			Bitmap bitmap = BitmapFactory.decodeStream( is );
			bitmap = bitmap.copy( Bitmap.Config.ARGB_8888, true );
			m_imageView.setImageBitmap( bitmap );
		} catch( IOException e ) {
			
		}

//		File file = new File( context.getFilesDir(), "captured.png" );
//		if( file.exists() ) {
//			Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath() );
//			m_imageView.setImageBitmap( bitmap );
//		}
	}

	// ScreenshotPermissionListener
	public void screenshotPermissionCompleted() {
		Intent intent = new Intent( this.getContext(), ScreenshotService.class ).setAction( ScreenshotService.Companion.getACTION_START() );
		this.getContext().startService( intent );
	}
	
	/**
	 * traineddataを assetsから外部ストレージにコピーする。TessBaseAPIクラスがアセットを直接扱えないため。
	 * @throws Exception アセットから外部ストレージへのコピーに失敗
	 */
	private void prepareTrainedFileIfNotExist( Context ctx ) throws Exception {
		
		// MEMO : Manifestの android.permission.WRITE_EXTERNAL_STORAGEを忘れずに
		
		String paths[] = {EXSTORAGE_PATH, EXSTORAGE_PATH + "/tessdata"};
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					throw new Exception("ディレクトリ生成に失敗");
				}
			}
		}
		
		String traineddata_path = String.format("%s/%s", TESSDATA_PATH, TRAINEDDATA);
		
		if ( (new File(traineddata_path).exists()))
			return;
		
		try {
			InputStream in = ctx.getAssets().open(TRAINEDDATA);
			OutputStream out = new FileOutputStream(traineddata_path);
			
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			throw new Exception("アセットのコピーに失敗");
		}
	}

}
