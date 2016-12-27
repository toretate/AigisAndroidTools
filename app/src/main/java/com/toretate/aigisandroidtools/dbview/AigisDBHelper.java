package com.toretate.aigisandroidtools.dbview;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by toretate on 2016/12/27.
 */

class AigisDBHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "aigis";
    private static String DB_NAME_ASSET = "aigis.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase m_db;
    private final Context m_context;
    private final File m_dbPath;

    AigisDBHelper( final Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
        m_context = context;
        m_dbPath = m_context.getDatabasePath( DB_NAME );
    }

    public void createEmptyDB() throws IOException {
        boolean dbExist = checkDBExists();

        if( dbExist ) {
            // すでにDB作成済み
        } else {
            // このメソッドを呼ぶことで、空のDBがアプリのデフォルトシステムパスに作成される
            getReadableDatabase();

            try {
                // assetにあるDBファイルをコピー
                copyDBFromAsset();

                String dbPath = m_dbPath.getAbsolutePath();
                SQLiteDatabase checkDB = null;
                try {
                    checkDB = SQLiteDatabase.openDatabase( dbPath, null, SQLiteDatabase.OPEN_READWRITE );
                } catch ( SQLiteException e ) {
                }

                if( checkDB != null ) {
                    checkDB.setVersion( DB_VERSION );
                    checkDB.close();
                }
            } catch ( IOException e ) {
                throw new Error( "Error copying db");
            }
        }
    }

    /**
     * 再コピーを防止するために、すでにDBがあるかどうかチェック
     * @return {@code true}:存在する
     */
    private boolean checkDBExists() {
        String dbPath = m_dbPath.getAbsolutePath();

        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase( dbPath, null, SQLiteDatabase.OPEN_READONLY );
        } catch ( Exception e ) {
            // 存在していない
        }

        if( checkDB == null ) {
            // DBが存在していない
            return false;
        }

        int versionOld = checkDB.getVersion();
        int versionNew = DB_VERSION;

        if( versionOld == versionNew ) {
            // DBが最新
            checkDB.close();
            return true;
        }

        // DBが存在してて古い
        File f = new File( dbPath );
        f.delete();
        return false;
    }

    /**
     * assetにあるDBをデフォルトのDBパスにコピー
     * @throws IOException
     */
    private void copyDBFromAsset() throws IOException {
        // asset内のDBファイルにアクセス
        InputStream in = m_context.getAssets().open( DB_NAME_ASSET );

        // デフォルトのDBパスに作成した空のDB
        OutputStream out = new FileOutputStream( m_dbPath );

        // コピー
        byte[] buffer = new byte[1024];
        int size;
        while( ( size = in.read( buffer ) ) > 0 ) {
            out.write( buffer, 0, size );
        }

        out.flush();
        out.close();
        in.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        return getReadableDatabase();
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        if( m_db != null ) {
            m_db.close();
        }
        super.close();
    }
}
