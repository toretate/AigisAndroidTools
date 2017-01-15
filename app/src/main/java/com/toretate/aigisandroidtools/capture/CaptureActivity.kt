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

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CaptureActivity : Activity() {

    companion object {
        private const val REQUEST_CAPTURE = 1000

        var projection: MediaProjection? = null
    }

    private lateinit var mediaProjectionManager: MediaProjectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaProjectionManager = getSystemService(Service.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK) {
                projection = mediaProjectionManager.getMediaProjection(resultCode, data)
                val intent = Intent(this, CaptureService::class.java).setAction(CaptureService.ACTION_ENABLE_CAPTURE)
                startService(intent)
            } else {
                projection = null
                Toast.makeText( this, "Capture Error!", Toast.LENGTH_SHORT ).show()
//                Toast.makeText(this, R.string.capture_error, Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}