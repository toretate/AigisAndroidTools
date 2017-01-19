package com.toretate.aigisandroidtools.capture

import android.annotation.TargetApi
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CaptureService : Service() {

    companion object {
        private val TAG = CaptureService::class.qualifiedName
        val ACTION_CAPTURE = "capture"
    }

    private val capture = Capture(this)

    override fun onBind(intent: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                ACTION_CAPTURE -> doCapture()
            }
        }
        return Service.START_STICKY
    }

    private fun doCapture() {
        CaptureActivity.projection?.run {
            capture?.run( this ) {
                capture.stop()
            }
        }
    }

    private fun disableCapture() {
        capture.stop()
        CaptureActivity.projection = null
    }

    override fun onDestroy() {
        super.onDestroy()
        disableCapture()
    }
}