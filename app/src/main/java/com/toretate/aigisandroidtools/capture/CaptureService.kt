package com.toretate.aigisandroidtools.capture


import android.annotation.TargetApi
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.util.*

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CaptureService : Service() {

    companion object {
        private val TAG = CaptureService::class.qualifiedName
        val ACTION_ENABLE_CAPTURE = "enable_capture"
        val ACTION_DO_CAPTURE = "do_capture"
    }

    private val notificationId = Random().nextInt()

    private val capture = Capture(this)

    // ... snip ...

    override fun onBind(intent: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                ACTION_ENABLE_CAPTURE -> onEnableCapture()
                ACTION_DO_CAPTURE -> enableCapture()
            }
        }
        return Service.START_STICKY
    }

    private fun enableCapture() {
        if (CaptureActivity.projection == null) {
            Log.d(TAG, "startActivity(CaptureActivity)")
            val intent = Intent(this, CaptureActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            onEnableCapture()
        }
    }

    private fun onEnableCapture() {
        CaptureActivity.projection?.run {
            capture.run(this){
                capture.stop()
                // save bitmap

                // it に Bitmap が入ってるらしー
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