package com.toretate.aigisandroidtools.capture

import android.annotation.TargetApi
import android.app.Activity
import android.app.Service
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast

/**
 * MediaProjectionManager の権限取得を行う Activity
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CaptureActivity : Activity() {

    companion object {
        private const val REQUEST_CAPTURE = 1
        val ACTION_FINISH = "finish"

        var projection: MediaProjection? = null
    }

    private lateinit var mediaProjectionManager: MediaProjectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // スクリーンキャプチャ 許可を問い合わせる
        mediaProjectionManager = getSystemService(Service.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CAPTURE)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.run {
            when( intent.action ) {
                ACTION_FINISH -> {
                    finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK) {
                projection = mediaProjectionManager.getMediaProjection(resultCode, data)

                // ユーザから許可がもらえたので、ScreenshotServiceを立ち上げてスクショ撮るためのUI作成
                val intent = Intent( this, ScreenshotService::class.java)
                intent.action = ScreenshotService.ACTION_START
                this.startService( intent )
            } else {
                projection = null
                Toast.makeText( this, "Capture Error!", Toast.LENGTH_SHORT ).show()
            }
        }
        finish()
    }
}