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
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

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

        m_helper = new AigisDBHelper( context );
        try {
            m_helper.createEmptyDB();
            m_db = m_helper.openDataBase();
        } catch ( IOException e ) {
            throw new Error( "Unable to create DB");
        } catch( SQLException e ) {
            throw e;
        }

        View result = super.createView(context, inflater, container);
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

        TableView<String[]> table = (TableView<String[]>)root.findViewById(R.id.dbTable);
        Context ctx = root.getContext();

        Cursor c = findData();
        c.moveToFirst();

        final int colCount = c.getColumnCount();
        table.setColumnCount( colCount );

        List<String[]> data = new ArrayList<>();

        final int total = c.getCount();
        for( int i=0; i<total; i++ ) {

            String[] values = new String[ colCount ];
            for( int colIndex = 0; colIndex < colCount; colIndex++ ) {
                String value = c.getString( colIndex );
                values[ colIndex ] = value;
            }
            data.add( values );

            c.moveToNext();
        }

        table.setDataAdapter( new SimpleTableDataAdapter( ctx, data.toArray( new String[][]{} ) ));
        table.setHeaderAdapter( new SimpleTableHeaderAdapter( ctx, Columns ) );
    }

    private String[] Columns = {"NameText.Message", "ClassData.Name"};
    private String TABLE_NAME = "NameText, ClassData, CardDataTable";
    private String SELECTION = "CardDataTable.CardID = NameText.RowIndex and CardDataTable.InitClassID = ClassID";

    /*
select cd.Name, cd2.Name, cd3.Name, cd3.JobChange
from ClassData as cd
         left join ClassData as cd2 on cd.JobChange = cd2.ClassID
         left join ClassData as cd3 on cd2.JobChange = cd3.ClassID
;
     */

    public Cursor findData() {
        Cursor cursor = m_db.query(
                /* distinct */          false
                , /* table */           TABLE_NAME
                , /* columns */         Columns
                , /* selection */       SELECTION
                , /* selectionArgs */   null
                , /* groupBy */         null
                , /* having */          null
                , /* orderBy */         null
                , /* limit */           null
        );
        return cursor;
    }

}
