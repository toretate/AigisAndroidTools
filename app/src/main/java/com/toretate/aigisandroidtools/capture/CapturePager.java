package com.toretate.aigisandroidtools.capture;

import android.animation.Animator;
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
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toretate on 2017/01/14.
 */

public class CapturePager extends CommonViewPagerPage implements ChaStaInfoView.ChaStaInfoViewListener {
	
	private static String TAG = CapturePager.class.getSimpleName();

	private View m_root;
	private ImageView m_imageView;
	
	public CapturePager( String title, int itemId, String key, boolean defVisible, int layoutId ) {
		super( title, itemId, key, defVisible, layoutId );
	}

	/** カリスマ／スタミナ 表示部分 */
	private ChaStaInfoView m_chaStaInfoView;

	/** 矩形選択View */
    private SelectRectView m_selectRectView;

    /** OCR選択範囲 */
    private List<Rect> m_rects = new ArrayList<>();
	private Rect m_currentRect = null;				//!< 現在選択中の範囲

    /** 画像のスクローラ */
    private HorizontalScrollView m_scrollH;
    /** 画像のスクローラ */
    private ScrollView m_scrollV;

    @Override
	protected void afterCreateView(final @Nullable View root, final LayoutInflater inflater ) {
		m_root = root;
		final Context context = root.getContext();

        // Tessデータをアセットから外部ストレージに配置。TessBaseAPIがアセットを扱えないため。
        try {
            TessUtilKt.doTessSetting( context );
        } catch( Exception e ) {
        }

        m_chaStaInfoView = (ChaStaInfoView)root.findViewById( R.id.chaStaInfoView );
		m_chaStaInfoView.setListener( this );

        m_imageView = (ImageView)root.findViewById( R.id.capturedImageView );
		reloadScreenshot( context );

		Button button;

        // スクリーンキャプチャボタン
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

        // 更新ボタン
		button = (Button)root.findViewById(R.id.reload_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				reloadScreenshot( context );
			}
		});

        // OCR実行ボタン
		button = (Button)root.findViewById(R.id.ocr_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick( final View view) {

				// まずボタンを disable にする
				view.setEnabled( false );
				final Drawable background = view.getBackground();
				view.setBackgroundColor( Color.argb( 0x88, 0xff, 0x00, 0x00 ) );

				// OCR対象の画像
				Bitmap bitmap = ((BitmapDrawable)m_imageView.getDrawable()).getBitmap();

				// OCR処理実行するタスク
				AsyncTask<Bitmap, Void, List<String>> task = new AsyncTask<Bitmap, Void, List<String>>() {

					@Override
					protected List<String> doInBackground(Bitmap... bmps) {
						List<String> results = new ArrayList<String>();
						String text;

						if( bmps == null || bmps.length <= 0 ) return results;
						Bitmap bmp = bmps[0];
						if( bmp == null ) return results;

						for( Rect rect : m_rects ) {
							text = TessUtilKt.doOCR( bmp, rect );
							results.add( text );
						}
						return results;
					}

					@Override
					protected void onPostExecute(List<String> results) {
						// ボタンの disable を戻す
						view.setEnabled( true );
						view.setBackground( background );

						for( String result : results ) {
							Toast.makeText( context, result, Toast.LENGTH_LONG ).show();
							Log.d(TAG, "Recognized : " + result );
						}
					}
				};
				task.execute( bitmap );

			}
		});

        // 画像表示領域の設定
		m_scrollH = (HorizontalScrollView)root.findViewById( R.id.image_h_scroll );
        m_scrollV = (ScrollView)root.findViewById(R.id.image_v_scroll);
        m_selectRectView = (SelectRectView)root.findViewById( R.id.select_rect );
		m_selectRectView.setVisibility( View.GONE );

        // 矩形選択UIの設定
		setupSelectRectUI( root );
	}

	/** select_rect_ui の表示を出したり引っ込めたり */
	private void switchOcrSelectAreaUI( final View root ) {
        View ui = root.findViewById( R.id.select_rect_ui );
        View rootUI = root.findViewById( R.id.root_ui_ocr );

        if( ui.getVisibility() == View.VISIBLE ) {
            // 閉じる
            ui.setVisibility( View.GONE );
            rootUI.setVisibility( View.VISIBLE );

            m_selectRectView.setVisibility( View.GONE );

            // ScrollViewのイベント再開
            m_scrollH.setOnTouchListener( null );
            m_scrollV.setOnTouchListener( null );

            // 設定した矩形を反映
            m_currentRect = m_selectRectView.getSelectRect();
            reloadScreenshot( CapturePager.this.getContext() );
        } else {
            // 開く
            ui.setVisibility( View.VISIBLE );
            rootUI.setVisibility( View.GONE );

            m_selectRectView.setVisibility( View.VISIBLE );
            m_selectRectView.setSelectRect( m_currentRect );

            // ScrollViewを無反応に
            m_scrollH.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            m_scrollV.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }


	private void setupSelectRectUI( final @Nullable View root ) {
		ToggleButton button;

		CompoundButton.OnCheckedChangeListener l = new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				int id;
				switch( compoundButton.getId() ) {
					case R.id.fix_left_button: m_selectRectView.setFixL( b ); break;
					case R.id.fix_top_button: m_selectRectView.setFixT( b ); break;
					case R.id.fix_right_button: m_selectRectView.setFixR( b ); break;
					case R.id.fix_bottom_button: m_selectRectView.setFixB( b ); break;
				}
			}
		};

		int[] buttonIds = { R.id.fix_left_button, R.id.fix_top_button, R.id.fix_right_button, R.id.fix_bottom_button };
        for( int buttonId : buttonIds ) {
            button = (ToggleButton)root.findViewById( buttonId );
			button.setOnCheckedChangeListener( l );
        }

		RadioGroup areaButtons = (RadioGroup)root.findViewById( R.id.areaButtons );
		areaButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
				OnAreaChanged( i );
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
			if( bitmap == null ) return;

			Canvas canvas = new Canvas( bitmap );

			if( m_rects.size() <= 0 ) {
				m_rects.add( new Rect( 100, 25, 200, 55 ) );	// Rasnk
				m_rects.add( new Rect( 100, 125, 200, 155 ) );	// Sta left:550でちょうどカリスマの右横
				m_rects.add( new Rect( 490, 25, 650, 55 ) );	// Cha left:550でちょうどカリスマの右横
				m_rects.add( new Rect( 490, 75, 650, 105 ) );	// ChaSub left:550でちょうどカリスマの右横

				m_currentRect = m_rects.get(2);	// Chaを初期選択とする
			}

			Paint paint = new Paint();
			paint.setStyle( Paint.Style.STROKE );
			paint.setStrokeWidth( 1.0f );
			for( Rect rect : m_rects ) {
				paint.setColor(Color.RED);
				canvas.drawRect( rect, paint );
			}

			BitmapDrawable drawable = (BitmapDrawable)m_imageView.getDrawable();
			Bitmap oldBMP = drawable.getBitmap();

			m_imageView.setImageBitmap( bitmap );

			oldBMP.recycle();
		}
	}

	/** 選択範囲の変更を行う */
	public void OnAreaChanged( @IdRes int buttonId ) {
		if( m_selectRectView != null && m_selectRectView.getVisibility() == View.VISIBLE ) {
			m_currentRect = m_selectRectView.getSelectRect();
		}
		switch ( buttonId ) {
			case R.id.rankAreaButton: 	m_currentRect = m_rects.get(0); break;
			case R.id.staAreaButton: 		m_currentRect = m_rects.get(1); break;
			case R.id.chaAreaButton: 		m_currentRect = m_rects.get(2); break;
			case R.id.chaSubAreaButton: 	m_currentRect = m_rects.get(3); break;
		}
		if( m_selectRectView != null && m_selectRectView.getVisibility() == View.VISIBLE ) {
			m_selectRectView.setSelectRect( m_currentRect );
			m_selectRectView.invalidate();
		}
	}

	/************************************************************
	 * ChaStaInfoViewListener
	 ***********************************************************/
	@Override
	public void OnAreaSelectButtonChanged() {
		switchOcrSelectAreaUI( m_root );
	}

}
