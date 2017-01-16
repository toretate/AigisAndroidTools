package com.toretate.aigisandroidtools.capture

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Capture(val context: Context) : ImageReader.OnImageAvailableListener {

    companion object {
        private val TAG = Capture::class.qualifiedName
    }

    private var display: VirtualDisplay? = null
    private var onCaptureListener: ((Bitmap) -> Unit)? = null

    fun run(mediaProjection: MediaProjection, onCaptureListener: (Bitmap) -> Unit) {
        this.onCaptureListener = onCaptureListener
        if (display == null) {
            display = createDisplay(mediaProjection)
        }
    }

    private fun createDisplay(mediaProjection: MediaProjection): VirtualDisplay {
        context.resources.displayMetrics.run {
            val maxImages = 2
            val reader = ImageReader.newInstance(
                    widthPixels, heightPixels, PixelFormat.RGBA_8888, maxImages)
            reader.setOnImageAvailableListener(this@Capture, null)
            val display = mediaProjection.createVirtualDisplay(
                    "Capture Display", widthPixels, heightPixels, densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    reader.surface, null, null)
            Log.d(TAG, "createVirtualDisplay")
            return display
        }
    }

    override fun onImageAvailable(reader: ImageReader) {
        if (display != null ) {

            // Bitmapの保存
            var bitmap = captureImage( reader )

            var timeStamp = SimpleDateFormat( "yyyy_MM_dd_hh_mm_ss" ).format( Calendar.getInstance().time )
            var imageFile = File( context.getFilesDir(), "captured.png" );

            FileOutputStream( imageFile ).use {
                bitmap.compress( Bitmap.CompressFormat.PNG, 0, it );
            }

            // captureを終了
            this.stop()

            // CaptureServiceの終了
            var intent = Intent( this.context, CaptureService::class.java );
            context.stopService( intent )

            // ScreenshotServiceの終了
            intent = Intent( this.context, ScreenshotService::class.java );
            context.stopService( intent )
        }
    }

    private fun captureImage(reader: ImageReader): Bitmap {
        Log.d(TAG, "captureImage")
        val image = reader.acquireLatestImage()
        context.resources.displayMetrics.run {
            image.planes[0].run {
                val bitmap = Bitmap.createBitmap(
                        rowStride / pixelStride, heightPixels, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(buffer)
                image.close()
                return bitmap
            }
        }
    }

    fun stop() {
        display?.release()
        display = null
        onCaptureListener = null
    }
}