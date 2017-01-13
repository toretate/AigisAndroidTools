package com.toretate.aigisandroidtools.capture

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Build
import android.util.Log

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
        if (display != null) {
            var bitmap = captureImage( reader )
            onCaptureListener?.invoke( bitmap )
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