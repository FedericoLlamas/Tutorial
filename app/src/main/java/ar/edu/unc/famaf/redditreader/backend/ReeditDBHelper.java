package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by federico on 01/11/16.
 */

public class ReeditDBHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "post_table";
    public static final int DATABASE_VERSION = 1;
    public static final String DATA_BASE_NAME = "POST_DATABASE";

    private String sqlCreate = "CREATE TABLE post_table (title TEXT, author TEXT, date TEXT, comments TEXT, url TEXT)";

    public ReeditDBHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    public void removeData() {
     final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS post_table");
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS post_table");
        onCreate(db);
    }

}
