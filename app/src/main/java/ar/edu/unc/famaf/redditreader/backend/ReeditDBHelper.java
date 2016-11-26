package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 11/11/16.
 */

public class ReeditDBHelper {
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private int offset;
    private  int totalItemsCount;

    public ReeditDBHelper(Context ctx, int totalItemsCount) {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(context);
        this.totalItemsCount= totalItemsCount;

    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "redditPost.db";

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE " + RedditEntryApi.Entry.TABLE_NAME + " ("
                        + RedditEntryApi.Entry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + RedditEntryApi.Entry.AUTHOR + " TEXT,"
                        + RedditEntryApi.Entry.TITLE + " TEXT,"
                        + RedditEntryApi.Entry.SUBREDDIT + " TEXT,"
                        + RedditEntryApi.Entry.COMMENTS + " TEXT,"
                        + RedditEntryApi.Entry.CREATED + " TEXT,"
                        + RedditEntryApi.Entry.URL + " TEXT,"
                        + RedditEntryApi.Entry.THUMBNAIL + " TEXT,"
                        + RedditEntryApi.Entry.ICON + " BLOB,"
                        + "UNIQUE (" + RedditEntryApi.Entry.ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("upgrade table");
            db.execSQL("DROP TABLE IF EXISTS " + RedditEntryApi.Entry.TABLE_NAME);
            onCreate(db);
        }
    }

    public ReeditDBHelper open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public void upgrade(){
        DBHelper.onUpgrade(db,1,2);
    }

    public ReeditDBHelper savePostModel(List<PostModel> list) {
        db = DBHelper.getWritableDatabase();
        for(int i=0; i<list.size(); i++){
            db.insert(RedditEntryApi.Entry.TABLE_NAME, null,  list.get(i).toContentValues());
            Cursor c = db.query(RedditEntryApi.Entry.TABLE_NAME, null, RedditEntryApi.Entry.AUTHOR + " LIKE ?",
                    new String[]{list.get(i).getAuthor()}, null, null, null);
            if (c != null){
                c.moveToFirst();
                list.get(i).setId(c.getInt(c.getColumnIndex(RedditEntryApi.Entry.ID)));
                c.close();
            }
        }
        return this;
    }

    public  void updateImage(PostModel postModel) {
        if(!db.isOpen()){open();}
        System.out.println("UpdateImage");
        ContentValues values = new ContentValues();
        values.put(RedditEntryApi.Entry.ICON, postModel.getIcon());
        db.update(RedditEntryApi.Entry.TABLE_NAME, values, RedditEntryApi.Entry.ID + "=" + postModel.getId(),null);
        close();
    }

    public List<PostModel> getAllDb(){
        List<PostModel> list = new ArrayList<PostModel>();
        offset=totalItemsCount;

        String selectQuery2 = "SELECT  * FROM " + RedditEntryApi.Entry.TABLE_NAME + " LIMIT 5 OFFSET "+ String.valueOf(this.offset);
        try {
            Cursor cursor = db.rawQuery(selectQuery2, null);
            if(cursor.moveToFirst()) {
                do {
                    PostModel postModel = new PostModel();
                    postModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.ID))));
                    postModel.setAuthor(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.AUTHOR)));
                    postModel.setTitle(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.TITLE)));
                    postModel.setSubreddit(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.SUBREDDIT)));
                    postModel.setComments(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.COMMENTS))));
                    postModel.setCreated(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.CREATED))));
                    postModel.setUrl(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.URL)));
                    postModel.setThumbnail(cursor.getString(cursor.getColumnIndex(RedditEntryApi.Entry.THUMBNAIL)));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(RedditEntryApi.Entry.ICON));
                    if (image != null) {
                        postModel.setIcon(image);
                    }
                    list.add(postModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        return list;
    }
}
