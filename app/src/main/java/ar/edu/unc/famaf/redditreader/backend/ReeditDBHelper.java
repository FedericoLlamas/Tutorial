package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by federico on 01/11/16.
 */

public class ReeditDBHelper extends SQLiteOpenHelper{

    private String sqlCreate = "CREATE TABLE post_table (title TEXT, author TEXT, " +
            "date TEXT, comments TEXT, url TEXT)";

    public ReeditDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS post_table");
        db.execSQL(sqlCreate);
    }
}
