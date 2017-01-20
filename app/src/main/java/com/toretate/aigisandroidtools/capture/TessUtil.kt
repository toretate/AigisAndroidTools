package com.toretate.aigisandroidtools.capture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.os.Environment
import android.util.Log
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by toretate on 2017/01/20.
 */

private val EXSTORAGE_PATH = String.format("%s/%s", Environment.getExternalStorageDirectory().toString(), "OCRTest")
private val TESSDATA_PATH = String.format("%s/%s", EXSTORAGE_PATH, "tessdata")
private val TRAIN_LANG = "jpn"
private val TRAINEDDATA = String.format("%s.traineddata", TRAIN_LANG)

/**
 * traineddataを assetsから外部ストレージにコピーする。TessBaseAPIクラスがアセットを直接扱えないため。
 * @throws Exception アセットから外部ストレージへのコピーに失敗
 */
fun doTessSetting( ctx : Context) {
    // MEMO : Manifestの android.permission.WRITE_EXTERNAL_STORAGEを忘れずに

    // 外部ストレージに Tesseract データを配置するためのディレクトリを作成
    var paths = arrayOf( EXSTORAGE_PATH, EXSTORAGE_PATH + "/tessdata" );
    for( path in paths ) {
        var dir = File( path )
        if( dir.exists() ) continue;
        if( dir.mkdirs() ) continue;

        throw Exception( "Tesseractデータ ディレクトリ作成に失敗" )
    }

    // 学習済みデータの配置
    var traineddata_path = String.format( "%s/%s", TESSDATA_PATH, TRAINEDDATA );
    if( File( traineddata_path).exists() ) return;  // 既に学習済みデータが配置されている

    try {
        ctx.assets.open( TRAINEDDATA ).use {
            var input = it
            FileOutputStream( traineddata_path ).use {
                var buf = ByteArray( 1024 )
                var len : Int = 0
                while( true ) {
                    len = input.read( buf )
                    if( len <= 0 ) break

                    it.write( buf, 0, len )
                }
            }
        }
    } catch ( e : IOException) {
        Log.e( "Tess", e.toString() )
        throw Exception( "TesseractデータをAssetからコピーするのに失敗")
    }
}

/**
 * OCR処理を行う
 */
fun doOCR( bitmap : Bitmap, rect : Rect ) : String {

    // OCRの実験本体
    // * スクリーンショットをロード (ARGB_8888に変換する)
    // * OCRを行う TessBaseAPIを初期化
    // * traineddataを設定
    // * 画像を指定
    // * 文字を検出する範囲を指定
    rect.left += 1
    rect.top += 1
    rect.right -= 1
    rect.bottom -= 1
    val baseApi = TessBaseAPI()
    baseApi.init(EXSTORAGE_PATH + "/", "jpn")
    baseApi.setImage(bitmap)
    baseApi.setRectangle( rect )
    val recognizedtext = baseApi.utF8Text
    baseApi.end()

    return recognizedtext
}