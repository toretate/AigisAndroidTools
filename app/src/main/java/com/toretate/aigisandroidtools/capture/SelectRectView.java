package com.toretate.aigisandroidtools.capture;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by toretate on 2017/01/20.
 */

public class SelectRectView extends View {

    private Rect m_rect = new Rect( 100, 100, 200, 200 );
    private Paint m_paint = null;

    public void setSelectRect( Rect rect ) {m_rect = rect; }
    public Rect getSelectRect() { return m_rect; }

    public SelectRectView(Context context) {
        super(context);
    }

    public SelectRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if( this.isEnabled() == false ) return;

        if( m_paint == null ) {
            m_paint = new Paint();
            m_paint.setStyle(Paint.Style.FILL_AND_STROKE );
            m_paint.setStrokeWidth( 2.0f );
            m_paint.setColor( Color.argb( 0x88, 0xff, 0x00, 0x00 ) );
        }
        canvas.drawRect( m_rect,  m_paint );
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if( visibility == VISIBLE ) {
            this.setBackgroundColor( Color.argb( 128, 0, 255, 9 ) );
            this.getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            this.getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( this.isEnabled() ) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            int x = (int)Math.floor( event.getX() );
            int y = (int)Math.floor( event.getY() );
            if( x < m_rect.left ) {
                m_rect.left = x;
            } else if( m_rect.right < x ) {
                m_rect.right = x;
            } else {
                // l < x < r なので近い方を合わせる
                int dl = x - m_rect.left;
                int dr = m_rect.right - x;
                if( dl < dr ) m_rect.left = x;
                else m_rect.right = x;
            }

            if( y < m_rect.top ) {
                m_rect.top = y;
            } else if( m_rect.bottom < y ) {
                m_rect.bottom = y;
            } else {
                // t < y < b なので近い方を合わせる
                int dt = y - m_rect.top;
                int db = m_rect.bottom - y;
                if( dt < db ) m_rect.top = y;
                else m_rect.bottom = y;
            }
            this.invalidate();
            return true;
        } else {
            int x = (int)Math.floor( event.getX() );
            int y = (int)Math.floor( event.getY() );
            return true;
        }
    }
}
