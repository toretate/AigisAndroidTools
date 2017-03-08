package com.toretate.aigisandroidtools.capture

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.system.Os.connect
import android.util.Log
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

/**
 * Tesserect OCR の辞書データをダウンロードする
 * Created by toretate on 2017/03/08.
 */
class TesserectDataDownloader( context : Context ) {
    val m_context = context;

    class AsyncFileDownload( context : Context, url : String, outFile : File, complete : ( Boolean ) -> Unit ) : AsyncTask<String, Void, Boolean>() {
        private val TAG = "AasyncFileDownload"
        private val TIMEOUT_READ = 5000;
        private val TIMEOUT_CONNECT = 30000;

        private val m_context = context;
        private val m_url = url;
        private val m_outFile = outFile;

        private var m_buff : BufferedInputStream? = null;
        private var m_fout : FileOutputStream? = null;

        private var m_complete : ( Boolean ) -> Unit = complete;

        override fun doInBackground( vararg urls: String? ) : Boolean {

            try {
                connect();
            } catch( e : IOException) {
                Log.d( TAG, "ConnectError:" + e.toString() );
                cancel( true );
            }

            if( isCancelled() ) return false;

            m_buff?.run {
                var input : BufferedInputStream = this

                FileOutputStream( m_outFile ).use {
                    var buf = ByteArray( 1024 )
                    var len : Int = 0
                    while( true ) {
                        len = input.read( buf )
                        if( len <= 0 ) break

                        it.write( buf, 0, len )
                        if( isCancelled ) break;
                    }
                }
            }

            try {
                close();
            } catch ( e : IOException ) {
                Log.d(TAG, "CloseError:" + e.toString());
            }

            return true;
        }

        override fun onPostExecute( result: Boolean? ) {
            super.onPostExecute(result)
            m_complete( result ?: false )
        }

        private fun connect() {
            var url : URL = URL( m_url );
            val conn = url.openConnection();
            conn.readTimeout = TIMEOUT_READ;
            conn.connectTimeout = TIMEOUT_CONNECT;
            val input = conn.getInputStream();
            m_buff = BufferedInputStream( input );
            m_fout = FileOutputStream( m_outFile );
        }

        private fun close() {
            m_fout?.flush();
            m_fout?.close();
            m_buff?.close();
        }
    }

    public fun download( outFile : String, complete : ( Boolean ) -> Unit ) {
        val downloader = AsyncFileDownload(
                            m_context,
                            "https://github.com/tesseract-ocr/tessdata/raw/master/jpn.traineddata",
                            File( outFile ),
                            complete
                        )
        downloader.execute()
    }

}