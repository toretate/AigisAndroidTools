package com.toretate.aigisandroidtools.dbview;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.toretate.aigisandroidtools.R;
import com.toretate.aigisandroidtools.pager.AbstractViewPagerPage;
import com.toretate.aigisandroidtools.pager.CommonViewPagerPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by toretate on 2016/12/27.
 */

public class DBViewPagerPage extends CommonViewPagerPage {

    private AigisDBHelper m_helper;
    private SQLiteDatabase m_db;

    public DBViewPagerPage(@NonNull String title, int itemId, @NonNull String key, boolean defVisible, int layoutID ) {
        super(title, itemId, key, defVisible, layoutID);
    }

    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        View result = super.createView(context, inflater, container);

        m_helper = new AigisDBHelper( context );
        try {
            m_helper.createEmptyDB();
            m_db = m_helper.openDataBase();
        } catch ( IOException e ) {
            throw new Error( "Unable to create DB");
        } catch( SQLException e ) {
            throw e;
        }

        return result;
    }

    @Override
    protected void destroyView() {
        m_db.close();
        super.destroyView();
    }

    @Override
    protected void afterCreateView(@Nullable View root, LayoutInflater inflater) {
        super.afterCreateView(root, inflater);

        TableLayout table = (TableLayout)root.findViewById(R.id.dbTable);
        Context ctx = root.getContext();

        Cursor c = findData();
        c.moveToFirst();
        final int total = c.getCount();
        for( int i=0; i<total; i++ ) {
            TableRow row = new TableRow( ctx );

            TextView c1 = new TextView( ctx );
            c1.setText( c.getString(0) );
            row.addView( c1 );

            TextView c2 = new TextView( ctx );
            c2.setText( c.getString(1) );
            row.addView( c2 );

            table.addView( row );

            c.moveToNext();
        }

    }

    private String[] Columns = {"RowIndex", "Message"};
    private String TABLE_NAME = "NameText";
    public Cursor findData() {
        Cursor cursor = m_db.query( TABLE_NAME, Columns, null, null, null, null, null );
        return cursor;
    }

}
